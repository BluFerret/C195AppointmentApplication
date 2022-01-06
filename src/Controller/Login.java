package Controller;

import DAO.JDBC;
import Model.SessionData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Login {
    @FXML private Label errorMessage;
    @FXML private TextField userNameText;
    @FXML private TextField passwordText;
    // TODO: Login determines the user’s location (i.e., ZoneId) and displays it in a label on the log-in form
    // TODO: Login displays the log-in form in English or French based on the user’s computer language setting
    //  to translate all the text, labels, buttons, and errors on the form. Note: Some operating systems
    //  require a reboot when changing the language settings.
    // TODO: Login automatically translates error control messages into English or French based on the user’s
    //  computer language setting. Note: Some operating systems require a reboot when changing the language settings.

    private boolean loginValidation() throws SQLException {
        boolean loginAttempt = true;
        if(userNameText.getText().isEmpty() || passwordText.getText().isEmpty()){
            displayError("Please enter a user name and password.");
            loginAttempt = false;}
        else if(!JDBC.validateUsernameAndPassword(userNameText.getText(),passwordText.getText())){
            displayError("Username and/or password not valid, please try again.");
            loginAttempt = false;
            }
        SessionData.recordLoginAttempt(userNameText.getText(),loginAttempt);
        return loginAttempt;
    }

    public void loginButtonAction(ActionEvent e) throws IOException, SQLException {
        if (loginValidation()){
            SessionData.setUsername(userNameText.getText());
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
