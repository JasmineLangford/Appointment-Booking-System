package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the controller for Login.fxml
 * The user will sign in with their username and password.
 * The user can also choose between English and French translations for the application.
 * Time Zone will automatically be determined by the user's location.
 * */
public class Login implements Initializable {

    public PasswordField userIDLogin;
    public PasswordField passwordLogin;

    @Override

    /**
     * This initializes the Login screen.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized.");
    }

    /**
     * This navigates the user to the Main Menu upon successful login.
     * @param actionEvent Login button is clicked.
     * */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("main-menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}

