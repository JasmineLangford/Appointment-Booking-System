package main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class creates the scheduler appointment.
 */

public class Main extends Application {
    /**
     * This opens the Login screen which will allow users to access the scheduler application upon successful sign in.
     * @param primaryStage top-level container for all scenes.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 380);
        primaryStage.setTitle("Scheduling System");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    /**
     * This is called to run the program.
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        JDBC.closeConnection();
        launch();
    }

}