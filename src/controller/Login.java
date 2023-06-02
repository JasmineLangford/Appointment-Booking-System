package controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class is the controller for login.fxml.
 * The end-user will sign in with their username and password.*
 * The language setting on the end-user's operating system will determine the language translation presented on the UI.
 * Input validation message for empty text fields (username and password text fields) and error message for
 * invalid username and/or password are also translated. For the scope of this project, there are French and
 * English translations available.
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
        loginButtonLabel.setText(rb.getString("loginButtonLabel"));
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
        boolean isValidUser = UserDAO.validateUser(username, password);

        if (isValidUser) {

            // check for appointments within 15 minutes on login
            ObservableList<Appointment> checkAppointments = AppointmentDAO.allAppointments();
            LocalDateTime currentDT = LocalDateTime.now();
            LocalDateTime currentDTFifteen = LocalDateTime.now().plusMinutes(15);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            boolean fifteenMinAppt = false;

            for(Appointment a : checkAppointments) {
                if ((a.getStart().isAfter(currentDT) || a.getStart().isEqual(currentDT)) &&
                        ((a.getStart().isBefore(currentDTFifteen) || a.getStart().isEqual(currentDTFifteen)))) {
                    Alert fifteenAlertTrue = new Alert(Alert.AlertType.INFORMATION, "You have an upcoming " +
                            "appointment: " + '\n' + '\n' + "Appointment ID: " + a.getAppointmentID()
                            + '\n' + "Date and Time: " +
                            a.getStart().format(formatter) + '\n' + "User ID: " + a.getUserID(), ButtonType.OK);
                    fifteenAlertTrue.showAndWait();
                    fifteenMinAppt = true;
                    break;
                }
            }
            if(!fifteenMinAppt){
                Alert fifteenAlertTrue = new Alert(Alert.AlertType.INFORMATION, "There are no upcoming " +
                        "appointments.", ButtonType.OK);
                fifteenAlertTrue.showAndWait();
            }
        } else {
            // error control message - end-user did not enter valid login credentials
            Alert invalidUser = new Alert(Alert.AlertType.ERROR, invalidLoginContent);
            invalidUser.setTitle(" ");
            invalidUser.setHeaderText(invalidLoginHeader);
            invalidUser.showAndWait();
            return;
        }
        // change from login screen to main menu
        MainMenu.toMainMenu(actionEvent);
    }
}