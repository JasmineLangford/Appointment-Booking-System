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
 * This class is the controller for login.fxml. The user will sign in with their credentials. The operating system
 * being used by the user will also determine the timezone, which is displayed below the login button.
 */
public class Login implements Initializable {
    @FXML
    private Label validationIcon;
    @FXML
    private Label validationLabel;
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
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login initialized!");

        usernameLogin.isFocused();

        // Set language
        ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
        usernameLabel.setText(rb.getString("usernameLabel"));
        passwordLabel.setText(rb.getString("passwordLabel"));
        timeZoneLabel.setText(rb.getString("timezone"));
        loginButtonLabel.setText(rb.getString("loginButtonLabel"));

        // Input validation display set to hide on launch
        validationIcon.setVisible(false);
        validationLabel.setVisible(false);

        // Display user timezone
        zoneID.setText(ZoneId.systemDefault().toString());
    }

    /**
     * This method navigates the user to the appointments screen upon successful login.
     * Error message is displayed if login is unsuccessful.
     *
     * @param actionEvent Login button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void loginButton(ActionEvent actionEvent) throws IOException {

        // User input text fields
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();

        // Check database for valid user
        boolean isValidUser = UserDAO.validateUser(username, password);

        if (isValidUser) {
            ObservableList<Appointment> checkAppointments;
            try {
                checkAppointments = AppointmentDAO.allAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            LocalDateTime currentDT = LocalDateTime.now();
            LocalDateTime currentDTFifteen = LocalDateTime.now().plusMinutes(15);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            boolean fifteenMinAppt = false;

            for (Appointment a : checkAppointments) {
                if ((a.getStart().isAfter(currentDT) || a.getStart().isEqual(currentDT)) &&
                        ((a.getStart().isBefore(currentDTFifteen) || a.getStart().isEqual(currentDTFifteen)))) {
                    Alert fifteenAlertTrue = new Alert(Alert.AlertType.INFORMATION, "You have an upcoming " +
                            "appointment: " + '\n' + '\n' + "Appointment: " + a.getTitle()
                            + '\n' + "Date and Time: " +
                            a.getStart().format(formatter) + '\n' + "Shop Location: " + a.getLocation(), ButtonType.OK);
                    fifteenAlertTrue.setTitle("Appointment Booking System");
                    fifteenAlertTrue.setHeaderText("Appointment Alert");
                    fifteenAlertTrue.showAndWait();
                    fifteenMinAppt = true;
                    break;
                }
            }
            if (!fifteenMinAppt) {
                Alert fifteenAlertFalse = new Alert(Alert.AlertType.INFORMATION, "There are no upcoming " +
                        "appointments.", ButtonType.OK);
                fifteenAlertFalse.setTitle("Appointment Booking System");
                fifteenAlertFalse.setHeaderText("Appointment Alert");
                fifteenAlertFalse.showAndWait();

                Appointments.backToAppointments(actionEvent);
            }
        } else if (usernameLogin.getText().isBlank()) {
            validationIcon.setVisible(true);
            validationLabel.setVisible(true);
            validationLabel.setText("Please enter valid username.");
        } else if (passwordLogin.getText().isBlank()) {
            validationIcon.setVisible(true);
            validationLabel.setVisible(true);
            validationLabel.setText("Please enter valid password.");
        } else {
            validationIcon.setVisible(true);
            validationLabel.setVisible(true);
            validationLabel.setText("Username and/or password is incorrect.");
        }
    }
}