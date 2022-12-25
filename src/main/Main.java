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
 * This class creates the scheduler appointment.
 */

public class Main extends Application {
    /**
     * This opens the Login screen which will allow users to access the scheduler application upon successful sign in.
     * @param primaryStage top-level container for all scenes.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        primaryStage.setScene(new Scene(root,600,380));
        primaryStage.setTitle("Appointment Scheduler");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    /**
     * This is called to run the program.
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch();

        ResourceBundle rb = ResourceBundle.getBundle("Resource Bundle 'Nat'", Locale.getDefault());

        // checks default locale to see if it matches any locales in file
        if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
            System.out.println(rb.getString("user-id"));
        }
        JDBC.closeConnection();
    }

}