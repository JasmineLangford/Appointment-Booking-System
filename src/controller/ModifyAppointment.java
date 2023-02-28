package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for modify-appointment.fxml. The end-user is able to modify text fields, combo boxes,
 * and date pickers. The appointment ID was auto-incremented from the database when the appointment was added and is
 * disabled. The end-user will be able to save the changes to this appointment by clicking the save button at the bottom
 * of the screen or cancel changes if the end-user no longer wants to modify the appointment.
 */
public class ModifyAppointment implements Initializable {

    // form fields
    @FXML
    private TextField apptId;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private ComboBox<ContactDAO> contactModCombo;
    @FXML
    private TextField locationText;
    @FXML
    private TextField typeText;
    @FXML
    private TextField titleText;
    @FXML
    private TextField descText;
    @FXML
    private ComboBox<CustomerDAO> customerCombo;
    @FXML
    private ComboBox<UserDAO> userCombo;


    // observable lists for combo boxes
    ObservableList<ContactDAO> contacts = ContactDAO.allContacts();
    ObservableList <UserDAO> users = UserDAO.allUsers();
    ObservableList <CustomerDAO> customers = CustomerDAO.allCustomerSelections();

    Appointment modAppointment = new Appointment();

    public ModifyAppointment() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Appointment initialized.");

        // combo box for time selection
        LocalTime start = LocalTime.of(4,0);
        LocalTime end = LocalTime.of(23,0);

        while(start.isBefore(end.plusSeconds(1))){
            startCombo.getItems().add(start);
            endCombo.getItems().add(start);
            start = start.plusMinutes(30);
        }

        // contact combo box
        contactModCombo.setItems(contacts);

        // user combo box
        userCombo.setItems(users);

        // customer combo box
        customerCombo.setItems(customers);
    }

    /**
     * This populates the date from the selected appointment tableview from Main Menu.
     */
    public void sendAppointment(Appointment appointment) {

        try{
        modAppointment = appointment;

        for (ContactDAO a:contactModCombo.getItems()) {
            if(a.getContactID() == appointment.getContactID()){
                contactModCombo.getSelectionModel().select(a);
                break;
            }
        }
        apptId.setText(String.valueOf(modAppointment.getAppointmentID()));
        startDatePicker.setValue(modAppointment.getStart().toLocalDate());
        startCombo.setValue(modAppointment.getStart().toLocalTime());
        endDatePicker.setValue(modAppointment.getEnd().toLocalDate());
        endCombo.setValue(modAppointment.getEnd().toLocalTime());
        locationText.setText(modAppointment.getLocation());
        typeText.setText(modAppointment.getType());
        titleText.setText(modAppointment.getTitle());
        descText.setText(modAppointment.getDescription());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
        /**
         * Modified fields will be updated in the database and reflect on appointment table on main menu.
         *
         * @param actionEvent Save button is clicked.
         */
    public void onSaveModAppointment(ActionEvent actionEvent) {
        // input validation messages
        try {
            if (startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                    startCombo.getValue() == null || endCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select start date/time and end " +
                        "date/time.");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            if (contactModCombo.getSelectionModel().isEmpty() || typeText.getText().isEmpty() || titleText.getText().isEmpty() ||
                    descText.getText().isEmpty() || locationText.getText().isEmpty()) {
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
            if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End date cannot be before start date.");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception e) {
            return;
        }

        try {
            if (endCombo.getValue().isBefore(startCombo.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End time cannot be before start time.");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception e) {
            return;
        }

        LocalDateTime modStartDateTime = LocalDateTime.of(startDatePicker.getValue(),startCombo.getValue());
        LocalDateTime modEndDateTime = LocalDateTime.of(endDatePicker.getValue(),endCombo.getValue());
        int modContact = contactModCombo.getSelectionModel().getSelectedItem().getContactID();
        String modType = typeText.getText();
        String modTitle = titleText.getText();
        String modDescription = descText.getText();
        String modLocation = locationText.getText();
        int modCustID = Integer.parseInt(String.valueOf(customerCombo.getValue()));
        int modUserID = Integer.parseInt(String.valueOf(userCombo.getValue()));

        try {
            modAppointment.setTitle(modTitle);
            modAppointment.setDescription(modDescription);
            modAppointment.setLocation(modLocation);
            modAppointment.setType(modType);
            modAppointment.setStart(modStartDateTime);
            modAppointment.setEnd(modEndDateTime);
            modAppointment.setCustomerID(modCustID);
            modAppointment.setUserID(modUserID);
            modAppointment.setContactID(modContact);

            Alert modAppointment = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " +
                    "appointment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = modAppointment.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                AppointmentDAO.modifyAppointment(modTitle, modDescription, modLocation, modType,
                        modStartDateTime,modEndDateTime,modCustID, modUserID, String.valueOf(modContact));

                MainMenu.toMainMenu(actionEvent);
            } else {
                Alert noChange = new Alert(Alert.AlertType.INFORMATION, "There were no changes made to this " +
                        "appointment.", ButtonType.OK);
                noChange.showAndWait();
            }
            } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This navigates the end-user back to the Main Menu.
     *
     * @param actionEvent Cancel button is clicked.
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Alert noChange = new Alert(Alert.AlertType.INFORMATION, "There were no changes made to this " +
                "appointment.", ButtonType.OK);
        noChange.showAndWait();
        MainMenu.toMainMenu(actionEvent);
    }
}
