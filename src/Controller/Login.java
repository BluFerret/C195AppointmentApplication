package Controller;

import DAO.JDBC;
import Model.SessionData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML private Label errorMessage;
    @FXML private TextField userNameText;
    @FXML private TextField passwordText;
    @FXML private Label zoneIDLabel;
    // TODO: Login displays the log-in form in English or French based on the user’s computer language setting
    //  to translate all the text, labels, buttons, and errors on the form. Note: Some operating systems
    //  require a reboot when changing the language settings.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneIDLabel.setText(ZoneId.systemDefault().toString());
    }
    /**
     * This method attempts a login based on the information provided from the user. Error messages are
     * displayed depending on if the user information provided is incomplete or does not match the login information
     * in the database.
     * @return = a boolean that shows if the provided login information matches the login information in the database
     * @throws SQLException - from the SQL executeQuery of the statement via database connection
     */
    private boolean loginValidation() throws SQLException {
        boolean loginAttempt = true;
        if(userNameText.getText().isEmpty() || passwordText.getText().isEmpty()){
            displayError("Please enter a user name and password."); // if username or password are empty
            loginAttempt = false;}
        else if(!JDBC.validateUsernameAndPassword(userNameText.getText(),passwordText.getText())){
            displayError("Username and/or password not valid, please try again."); // if username/password don't match database
            loginAttempt = false;
            }
        SessionData.recordLoginAttempt(userNameText.getText(),loginAttempt);
        return loginAttempt;
    }

    public void loginButtonAction(ActionEvent e) throws IOException, SQLException {
        if (loginValidation()){
            SessionData.setUsernameAndLogs(userNameText.getText());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ApplicationMain.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
    }

    /**
     * Method to display error message to user. Error message initially set to be invisible and becomes visible once
     * an error is shown to the user for the first time on the screen.
     * @param s- String containing error message to be displayed to the user.
     */
    private void displayError(String s){
        errorMessage.setText(s);
        errorMessage.setVisible(true);
    }

    /**
     * This method is called when the exit button is clicked. This closes the connection to the database and exits the program.
     */
    public void exitButtonAction() {
        JDBC.closeConnection();
        System.exit(0);
    }
}
