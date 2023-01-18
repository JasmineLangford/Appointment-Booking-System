package controller;

import DAO.AppointmentDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * This class is the controller for Login.fxml
 * The end-user will sign in with their username and password.
 *
 * The user's computer language setting will determine this screen's language translation.
 * French or English translation for the scope of this project.
 *
 * End-user's computer setting will determine the timezone listed.
 */
public class Login implements Initializable {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private Label logoTagline;
    @FXML
    private Label signInLabel;
    @FXML
    private Button loginButtonLabel;

    // timezone
    @FXML
    private Label zoneID;

    // end-user username and password input
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login initialized!");

        // language translation
        ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
        usernameLabel.setText(rb.getString("usernameLabel"));
        passwordLabel.setText(rb.getString("passwordLabel"));
        timeZoneLabel.setText(rb.getString("timezone"));
        logoTagline.setText(rb.getString("tagline"));
        signInLabel.setText(rb.getString("signInLabel"));
        loginButtonLabel.setText(rb.getString("loginButtonLabel"));

        // timezone
        zoneID.setText(ZoneId.systemDefault().toString());
    }

    /**
     * This navigates the end-user to the Main Menu upon successful login.
     *
     * @param actionEvent Login button is clicked.
     */
    public void loginButton(ActionEvent actionEvent) throws IOException{
            // Change from login screen to main menu
            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/main-menu.fxml"))));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1108, 620);
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
            stage.setResizable(false);
        }

}

