package Main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * This is an appointment scheduling application for a fictional global consulting company. This project is for
 * WGU Course C195 created by Lindsey Hill, student ID: #001994073.
 *
 * @author  Lindsey Hill
 */
//TODO: Write README file
public class Main extends Application {
    /**
     * This is the method that starts the Login View.
     * @param stage - The view for the program.
     * @throws Exception - from FXMLLoader.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Login.fxml")));
        stage.setTitle("Fictional Global Consulting Company");
        stage.setScene(new Scene(root));
        stage.show();
        stage.centerOnScreen();
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
