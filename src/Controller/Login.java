package Controller;

import DAO.JDBC;
import Model.SessionData;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This is the controller class for the Login.fxml view. The login displays the user's zone and accepts a
 * username and password from the user. If the username and password match a valid username and password combination
 * stored in the connected database, the login will send the user to the ApplicationMain view. An error message will
 * be displayed for 10 seconds if errors are encountered. The login view will have all components translated to French
 * if the user's computer language setting is set to French. Otherwise, the login view is displayed in English.
 */
public class Login implements Initializable {
    @FXML private ImageView logoImage;
    @FXML private Label companyLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    @FXML private Label errorMessage;
    @FXML private TextField userNameText;
    @FXML private TextField passwordText;
    @FXML private Label zoneIDLabel;
    private String errorMissingField;
    private String errorWrongUsernamePassword;
    private String errorSQLException;

    /**
     * This method initializes the controller, populates the text to be in English or French based on the computer
     * language settings and populates the zoneID label.
     * @param url - possible url location for root object if provided, null if not needed.
     * @param resourceBundle - possible resources for root object, null if not needed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneIDLabel.setText(ZoneId.systemDefault().toString());
        try{
            Image logo = new Image(new FileInputStream("src\\Resources\\FGCLogo.jpg"));
            logoImage.setImage(logo);
        } catch (FileNotFoundException e) {
            System.out.println("logo image in resource file has been moved or renamed");
        }
        companyLabel.setText(resourceBundle.getString("companyName"));
        usernameLabel.setText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        loginButton.setText(resourceBundle.getString("loginButton"));
        exitButton.setText(resourceBundle.getString("exitButton"));
        errorMissingField = resourceBundle.getString("errorMissingField");
        errorWrongUsernamePassword = resourceBundle.getString("errorWrongUsernamePassword");
        errorSQLException = resourceBundle.getString("errorSQLException");
    }
    /**
     * This method validates a user's attempt at logging in to the application. An error message is displayed if a
     * username or password are not provided as well as if the username and password do not match a username and
     * password combination stored in the connected database. The result of the validation is recorded in the login_logs
     * text file if both username and password fields are filled in when making an attempt at logging in.
     * @return = a boolean that shows if the provided login information matches the login information in the database
     */
    private boolean loginValidation(){
        if(userNameText.getText().isEmpty() || passwordText.getText().isEmpty()){
            displayError(errorMissingField);
            return false;
        }
        try{
            if(JDBC.validateUsernameAndPassword(userNameText.getText(),passwordText.getText())){
                SessionData.recordLoginAttempt(userNameText.getText(),true);
                return true;
            }
            else{
                displayError(errorWrongUsernamePassword);
                SessionData.recordLoginAttempt(userNameText.getText(),false);
                return false;
            }
        } catch (SQLException throwables) {
            displayError(errorSQLException);
            throwables.printStackTrace();
            return false;
        }
    }
    /**
     * This method is called when the login button is pressed. If the login is validated, the ApplicationMain View will
     * be displayed.
     * @param e - event of clicking the login button
     */
    public void loginButtonAction(ActionEvent e) {
        if (loginValidation()){
            SessionData.setUsernameAndLogs(userNameText.getText());
            ApplicationMain.setImminentApp();
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ApplicationMain.fxml")));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            assert root != null;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        }
    }
    /**
     * Method to display error message to user. A Lambda was used for setOnFinished of PauseTransition to return error
     * text to be invisible after 10 seconds.
     * @param s- String containing error message to be displayed to the user.
     */
    private void displayError(String s){
        errorMessage.setText(s);
        errorMessage.setVisible(true);
        PauseTransition visibleErrorText = new PauseTransition(Duration.seconds(10));
        visibleErrorText.setOnFinished(actionEvent -> errorMessage.setVisible(false));
        visibleErrorText.play();
    }
    /**
     * This method is called when the exit button is clicked. This closes the connection to the database and exits the program.
     */
    public void exitButtonAction() {
        JDBC.closeConnection();
        System.exit(0);
    }
}
