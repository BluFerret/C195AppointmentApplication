package Main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is an appointment scheduling application for a fictional global consulting company. This project is for
 * WGU Course C195 created by Lindsey Hill, student ID: #001994073.
 *
 * @author  Lindsey Hill
 */
public class Main extends Application {
    /**
     * This is the method that starts the Login View.
     * @param stage - The view for the program.
     */
    @Override
    public void start(Stage stage){
        if(Locale.getDefault().getLanguage().equals("fr")) {
            Locale.setDefault(new Locale("fr", "CA"));
        }
        else{
            Locale.setDefault(new Locale("en", "US"));
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources/Login");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            loader.setResources(resourceBundle);
            Parent main = loader.load();
            Scene scene = new Scene(main);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * This is the main method that launches the application as well as opens the connection to the database.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
    }
}
