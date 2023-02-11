package controller;

import DAO.AppointmentDAO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for the add-appointment.fxml view.
 *
 * End-user will be able to input data into text fields and choice boxes in order to add a new appointment.
 */
public class AddAppointment implements Initializable {


    // Form fields
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField apptID;
    @FXML
    private ComboBox contactCombo;
    @FXML
    private TextField typeTextfield;
    @FXML
    private TextField titleTextfield;
    @FXML
    private TextField descTextfield;
    @FXML
    private TextField locationTextfield;
    @FXML
    private TextField custIdTextfield;
    @FXML
    private TextField userIdTextfield;

    Appointment newAppointment = new Appointment();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add appointment is initialized!");

        // generates unique appointment id for new appointment
        try {
            apptID.setText(String.valueOf(AppointmentDAO.allAppointments().size()+1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalTime start = LocalTime.of(8,0);
        LocalTime end = LocalTime.of(22,0);


        while(start.isBefore(end.plusSeconds(1))){
            startCombo.getItems().add(start);
            endCombo.getItems().add(start);
            start = start.plusMinutes(30);
        }

        startCombo.setPromptText("Select a time.");
        endCombo.setPromptText("Select a time.");
    }

    /**
     * This will add the data from form fields as a new appointment to database.
     *
     @param actionEvent Save button is clicked.
     */
    public void onSaveAppt(ActionEvent actionEvent) {

        // end-user form fields
        LocalDate addStartDate = startDatePicker.getValue();
        LocalTime addStartTime = startCombo.getValue();
        LocalDate addEndDate = endDatePicker.getValue();
        LocalTime addEndTime = endCombo.getValue();
        int apptId = Integer.parseInt(apptID.getText());
        String addContact = contactCombo.getValue().toString();
        String addType = typeTextfield.getText();
        String addTitle = titleTextfield.getText();
        String addDescription = descTextfield.getText();
        String addLocation = locationTextfield.getText();
        int addCustID = Integer.parseInt(custIdTextfield.getText());
        int addUserID = Integer.parseInt(userIdTextfield.getText());

        // Input Validation Messages
        try {
            if (addContact.isEmpty() || addType.isEmpty() || addTitle.isEmpty() || addDescription.isEmpty() ||
                    addLocation.isEmpty()) {
                Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
                Optional<ButtonType> results = emptyField.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception e) {
            return;
        }

        try {
            Integer.parseInt(String.valueOf(addCustID));
        } catch (NumberFormatException e) {
            Alert invalidDataType = new Alert(Alert.AlertType.ERROR, "Customer ID should be an integer.");
            Optional<ButtonType> results = invalidDataType.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

        try {
            Integer.parseInt(String.valueOf(addUserID));
        } catch (NumberFormatException e) {
            Alert invalidDataType = new Alert(Alert.AlertType.ERROR, "User ID should be an integer.");
            Optional<ButtonType> results = invalidDataType.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

        try {

            newAppointment.setStart(LocalDateTime.from(addStartDate));
            newAppointment.setStart(LocalDateTime.from(addStartTime));
            newAppointment.setStart(LocalDateTime.from(addEndDate));
            newAppointment.setStart(LocalDateTime.from(addEndTime));
            newAppointment.setAppointmentID(apptId);
            newAppointment.setContactID(Integer.parseInt(addContact));
            newAppointment.setType(addType);
            newAppointment.setTitle(addTitle);
            newAppointment.setDescription(addDescription);
            newAppointment.setLocation(addLocation);
            newAppointment.setCustomerID(addCustID);
            newAppointment.setUserID(addUserID);

            Alert addAppointmentConfirm = new Alert(Alert.AlertType.CONFIRMATION, "" +
                    "appointment?");
            Optional<ButtonType> result = addAppointmentConfirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                AppointmentDAO.addAppointment(addStartDate,addStartTime,addEndDate,addEndTime,apptId,addContact,
                        addType,addTitle,addDescription,addLocation,addCustID,addUserID);
                toMainMenu(actionEvent);

            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This navigates the user back to the Main Menu.
     * @param actionEvent Cancel button is clicked.
     * */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/main-menu.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }


}