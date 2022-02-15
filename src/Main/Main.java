package Main;

import DAO.JDBC;
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
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Login.fxml")));
        stage.setTitle("Fictional Global Consulting Company");
        stage.setScene(new Scene(root));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * @param args arguments from command line
     */

    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
    }
}
