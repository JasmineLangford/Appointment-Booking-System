package controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.DateTimeUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

/**
 * This class is the controller for login.fxml.
 * The end-user will sign in with their username and password.
 *
 * The language setting on the end-user's operating system will determine the language translation presented on the UI.
 * Input validation message for empty text fields (username and password text fields) and error message for
 * invalid username and/or password are also translated. For the scope of this project, there are French and
 * English translations available.
 *
 * The end-user's operating system will also determine a timezone, which will be displayed on a label in the lower
 * right-hand corner of the login screen.
 */
public class Login implements Initializable {

    // login screen labels
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
    @FXML
    private Label zoneID;

    // end-user username and password input
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;

    // error control message for language translations
    @FXML
    private String blankUserInput;
    @FXML
    private String invalidLoginHeader;
    @FXML
    private String invalidLoginContent;

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
        blankUserInput = rb.getString("blankUserInput");
        invalidLoginHeader = rb.getString("invalidLoginHeader");
        invalidLoginContent = rb.getString("invalidLoginContent");

        // set end-user timezone
        zoneID.setText(ZoneId.systemDefault().toString());
    }

    /**
     * This method navigates the end-user to the Main Menu upon successful login.
     * Error message is displayed if login is unsuccessful.
     *
     * @param actionEvent Login button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void loginButton(ActionEvent actionEvent) throws IOException, SQLException {

        // end-user input
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();

        // check database for valid user
        boolean validUser = UserDAO.validateUser(username, password);

        if (validUser) {
            // change from login screen to main menu
            MainMenu.toMainMenu(actionEvent);

            // check for appointments within 15 minutes on login
            Appointment appointmentAlert = AppointmentDAO.appointmentAlert();

            if (appointmentAlert != null) {
                Alert fifteenAlertTrue = new Alert(Alert.AlertType.INFORMATION, "You have an upcoming " +
                        "appointment: " + '\n' + '\n' + "Appointment ID: " + appointmentAlert.getAppointmentID()
                        + '\n' + "Date and Time: " +
                        DateTimeUtil.toLocalDT(Timestamp.valueOf(appointmentAlert.getStart())) + '\n' + "User ID: " +
                        appointmentAlert.getUserID(), ButtonType.OK);
                fifteenAlertTrue.showAndWait();
            } else {
                Alert fifteenAlertFalse = new Alert(Alert.AlertType.INFORMATION, "You do not have any upcoming appointments.", ButtonType.OK);
                fifteenAlertFalse.showAndWait();
            }

        // error control message for blank username/password fields
        } else if (usernameLogin.getText().isEmpty() || passwordLogin.getText().isEmpty()) {
            Alert blankUser = new Alert(Alert.AlertType.ERROR, blankUserInput);
            blankUser.setTitle(" ");
            blankUser.setHeaderText(invalidLoginHeader);
            blankUser.showAndWait();

        } else {
                // error control message - end-user did not enter valid login credentials
                Alert invalidUser = new Alert(Alert.AlertType.ERROR, invalidLoginContent);
                invalidUser.setTitle(" ");
                invalidUser.setHeaderText(invalidLoginHeader);
                invalidUser.showAndWait();
            }
        }
    }