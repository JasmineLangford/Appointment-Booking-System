package main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class creates the scheduler application.
 */

public class Main extends Application {

    /**
     * This method opens the Login screen which will allow users to access the scheduler application upon successful
     * login.
     *
     * @param primaryStage The top-level container for all scenes.
     * @throws Exception The exception to throw.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        primaryStage.setScene(new Scene(root,600,380));
        primaryStage.setTitle("Appointment Scheduler");
        primaryStage.show();
        primaryStage.setResizable(false);

        // translation based on default language in user's language setting
        ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
        primaryStage.setTitle(rb.getString("loginScreenHeader"));
    }

    /**
     * This method is called to run the application.
     */
    public static void main(String[] args) {

        // database connection
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}