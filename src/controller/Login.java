package controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
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
import model.DateTimeUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    // timezone
    @FXML
    private Label zoneID;

    // end-user username and password input
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;

    // error control message
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

        // timezone
        zoneID.setText(ZoneId.systemDefault().toString());
    }

    /**
     * This navigates the end-user to the Main Menu upon successful login.
     *
     * @param actionEvent Login button is clicked.
     */
    public void loginButton(ActionEvent actionEvent) throws IOException {

        String username = usernameLogin.getText();
        String password = passwordLogin.getText();

        boolean validUser = UserDAO.validateUser(username, password);

        if (validUser) {


            // change from login screen to main menu
            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/main-menu.fxml"))));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1108, 620);
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
            stage.setResizable(false);

            Appointment appointment = AppointmentDAO.appointmentAlert();

            //TEST
            System.out.println("This is the time converted from UTC: " + DateTimeUtil.toLocalDT(Timestamp.valueOf("2023-02-05 20:22:32")));

            /*LocalDateTime startUDT = DateTimeUtil.formattedLocal(appointment.getStart());
            ZonedDateTime zStartUDT = startUDT.atZone(ZoneId.of("UTC"));
            ZonedDateTime zStartLocal = zStartUDT.withZoneSameInstant(ZoneId.systemDefault());
            LocalDateTime startLocal = zStartLocal.toLocalDateTime();*/

            if (appointment != null){
                Alert fifteenAlertTrue = new Alert(Alert.AlertType.INFORMATION,"You have an upcoming appointment: " + '\n' + '\n' +
                        "Appointment ID: " + appointment.getAppointmentID() + '\n' + "Time: " + appointment.getStart(), ButtonType.OK);
                fifteenAlertTrue.showAndWait();
            } else {
                Alert fifteenAlertFalse = new Alert(Alert.AlertType.INFORMATION,"You do not have any upcoming appointments.", ButtonType.OK);
                fifteenAlertFalse.showAndWait();
            }


        } else if (usernameLogin.getText().isEmpty() || passwordLogin.getText().isEmpty()) {

            // error control message - end-user did not enter values in username/password fields
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
