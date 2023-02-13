package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for adding a new appointment.
 *
 * End-user will be able to input data into text fields and combo boxes.
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
    private ComboBox<ContactDAO> contactCombo;
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

    // list of contacts
    ObservableList<ContactDAO> contacts = ContactDAO.allContacts();

    Appointment newAppointment = new Appointment();

    public AddAppointment() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add appointment is initialized!");

        // generates unique appointment id for new appointment
        try {
            apptID.setText(String.valueOf(AppointmentDAO.allAppointments().size()+1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // combo box for time selection
        LocalTime start = LocalTime.of(4,0);
        LocalTime end = LocalTime.of(23,0);

        while(start.isBefore(end.plusSeconds(1))){
            startCombo.getItems().add(start);
            endCombo.getItems().add(start);
            start = start.plusMinutes(30);
        }

        startCombo.setPromptText("Select a time.");
        endCombo.setPromptText("Select a time.");

        // contact combo box
        contactCombo.setItems(contacts);
        contactCombo.setPromptText("Select contact.");
    }

    /**
     * This will save data entered in form fields and add the new appointment to the database.
     *
     * @param actionEvent save button clicked
     */
    public void onSaveAppt(ActionEvent actionEvent) {

        // input validation messages
        try{
            if(startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                startCombo.getValue() == null || endCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select start date/time and end " +
                        "date/time.");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
                }
        }catch(NullPointerException e) {
            e.printStackTrace();
        }

        try {
            if (contactCombo.getValue() == null || typeTextfield.getText().isEmpty() || titleTextfield.getText().isEmpty() ||
                    descTextfield.getText().isEmpty() || locationTextfield.getText().isEmpty()) {
                Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
                Optional<ButtonType> results = emptyField.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            Integer.parseInt(custIdTextfield.getText());
        } catch (NumberFormatException e) {
            Alert invalidDataType = new Alert(Alert.AlertType.ERROR, "Customer ID should be an integer.");
            Optional<ButtonType> results = invalidDataType.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

        try {
            Integer.parseInt(userIdTextfield.getText());
        } catch (NumberFormatException e) {
            Alert invalidDataType = new Alert(Alert.AlertType.ERROR, "User ID should be an integer.");
            Optional<ButtonType> results = invalidDataType.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

        try{
            if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End date cannot be before start date.");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch (Exception e){
            return;
        }

        try{
            if (endCombo.getValue().isBefore(startCombo.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End time cannot be before start time.");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch (Exception e){
            return;
        }

        LocalDate addStartDate = startDatePicker.getValue();
        LocalTime addStartTime = startCombo.getValue();
        LocalDate addEndDate = endDatePicker.getValue();
        LocalTime addEndTime = endCombo.getValue();
        int apptId = Integer.parseInt(apptID.getText());
        int addContact = contactCombo.getSelectionModel().getSelectedItem().getContactId();
        String addType = typeTextfield.getText();
        String addTitle = titleTextfield.getText();
        String addDescription = descTextfield.getText();
        String addLocation = locationTextfield.getText();
        int addCustID = Integer.parseInt(custIdTextfield.getText());
        int addUserID = Integer.parseInt(userIdTextfield.getText());



        try {

            newAppointment.setStart(LocalDateTime.of(addStartDate,addStartTime));
            newAppointment.setEnd(LocalDateTime.of(addEndDate,addEndTime));
            newAppointment.setAppointmentID(apptId);
            newAppointment.setContactID(addContact);
            newAppointment.setType(addType);
            newAppointment.setTitle(addTitle);
            newAppointment.setDescription(addDescription);
            newAppointment.setLocation(addLocation);
            newAppointment.setCustomerID(addCustID);
            newAppointment.setUserID(addUserID);

            Alert addAppointmentConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save " +
                    "this appointment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = addAppointmentConfirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                AppointmentDAO.addAppointment(addStartDate,addStartTime,addEndDate,addEndTime,apptId, String.valueOf(addContact),
                        addType,addTitle,addDescription,addLocation,addCustID,addUserID);
                toMainMenu(actionEvent);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This navigates the user back to the Main Menu.
     *
     * @param actionEvent cancel button clicked
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