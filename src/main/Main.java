package main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Objects;

/**
 * This class creates the appointment booking application.
 */

public class Main extends Application {

    /**
     * This method is called to run the application.
     */
    public static void main(String[] args) {

        // database connection
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }

    /**
     * This method opens the Login screen which will allow users to access the appointment booking application upon
     * successful login.
     *
     * @param primaryStage The top-level container for all scenes.
     * @throws Exception The exception to throw.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Set launch icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources" +
                "/thecakeshop-launch.png"))));

        // Set login scene
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        Scene scene = new Scene(root, 600, 380);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Appointment Booking System");
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-color: transparent;");
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");

        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}