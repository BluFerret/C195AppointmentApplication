package Main;

import Helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * Appointment Application for WGU Course C195 created by Lindsey Hill. Student ID: #001994073
 */

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Login.fxml")));
        primaryStage.setTitle("Fictional Global Consulting Company");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * @param args arguments from command line
     */

    public static void main(String[] args) {
        JDBC.openConnection();
        JDBC.closeConnection();
        launch(args);
    }
}
