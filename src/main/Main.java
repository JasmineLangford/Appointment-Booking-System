package main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.text.html.ImageView;
import java.awt.*;
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
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root, 600, 380);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        root.setStyle("-fx-background-radius: 20px 20px 20px 20px;");
        primaryStage.show();
        primaryStage.setResizable(false);

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