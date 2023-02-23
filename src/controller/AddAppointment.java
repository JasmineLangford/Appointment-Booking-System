package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for add-appointment.fxml. The end-user can add a new appointment by inputting data into
 * text fields, combo boxes, and date pickers. The appointment ID is auto-incremented from the database and disabled.
 * The end-user will be able to save the data for a new appointment by clicking the save button at the bottom of the
 * screen or cancel if the end-user no longer wants to add an appointment.
 */
public class AddAppointment implements Initializable {

    // Form fields
    @FXML
    private TextField apptID;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private DatePicker endDatePicker;
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
    private ComboBox<CustomerDAO> custIDCombo;
    @FXML
    private ComboBox <UserDAO> userCombo;

    // observable lists for combo boxes
    ObservableList <ContactDAO> contactList = ContactDAO.allContacts();
    ObservableList <UserDAO> users = UserDAO.allUsers();
    ObservableList <CustomerDAO> customers = CustomerDAO.allCustomerSelections();

    Appointment newAppointment = new Appointment();

    public AddAppointment() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add appointment is initialized!");

        // combo box for time selection
        LocalTime start = LocalTime.of(3,0);
        LocalTime end = LocalTime.of(23,0);

        while(start.isBefore(end.plusSeconds(1))){
            startCombo.getItems().add(start);
            endCombo.getItems().add(start);
            start = start.plusMinutes(30);
        }

        startCombo.setPromptText("Select Start Time");
        endCombo.setPromptText("Select End Time");

        // contact combo box
        contactCombo.setItems(contactList);
        contactCombo.setPromptText("Select Contact");

        // user combo box
        userCombo.setItems(users);
        userCombo.setPromptText("Select User ID");

        // customer combo box
        custIDCombo.setItems(customers);
        custIDCombo.setPromptText("Select Customer ID");
    }

    /**
     * This method will save data entered in form fields and add the new appointment to the database.
     *
     * @param actionEvent The save button clicked.
     * @throws SQLException The exception to throw if unable to access the database or has errors.
     */
    public void onSaveAppt(ActionEvent actionEvent) throws SQLException {

        // input validation messages: start and end date pickers
        try {
            if (startDatePicker == null || endDatePicker.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select start and end dates.");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException");
        }

        try {
            if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End date cannot be before start date.");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
        }

        try {
            if (startDatePicker.getValue().isBefore(LocalDate.now())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "Start date has already passed. Please " +
                        "select another date.");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
            }

        try {
            if (startDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                    startDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                    endDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                    endDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "Start/End dates must be a weekday " +
                        "(Monday-Friday).");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
            }

        // input validation messages: start and end time combo boxes
        try {
            if (startCombo.getSelectionModel().isEmpty() || endCombo.getSelectionModel().isEmpty()) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select start and end times.");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch (NullPointerException e){
            System.out.println("Caught NullPointerException");
            }

        try {
            if (endCombo.getValue().isBefore(startCombo.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End time cannot be before start time.");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch (Exception TimeException){
            System.out.println("Caught TimeException");
            }

        try{
            if (startCombo.getValue().equals(endCombo.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End time cannot be the same as start " +
                        "time. Please select another end time.");
                invalidDate.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch (Exception TimeException){
            System.out.println("Caught TimeException");
        }

        // input validation: empty selection for contact combo
        try{
            if(contactCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a contact.");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch(NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty text fields
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

        // input validation: empty customer ID combo
        try{
            if(custIDCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a customer ID.");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch(NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty user ID combo
        try{
            if(custIDCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a user ID.");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        }catch(NullPointerException e) {
            e.printStackTrace();
        }

        LocalDate addStartDate = startDatePicker.getValue();
        LocalTime addStartTime = startCombo.getValue();
        LocalDate addEndDate = endDatePicker.getValue();
        LocalTime addEndTime = endCombo.getValue();
        int addContact = contactCombo.getSelectionModel().getSelectedItem().getContactID();
        String addType = typeTextfield.getText();
        String addTitle = titleTextfield.getText();
        String addDescription = descTextfield.getText();
        String addLocation = locationTextfield.getText();
        int addCustID = Integer.parseInt(String.valueOf(custIDCombo.getValue()));
        int addUserID = Integer.parseInt(String.valueOf(userCombo.getValue()));

        LocalDateTime dateTimeStart = LocalDateTime.of(addStartDate,addStartTime);
        LocalDateTime dateTimeEnd = LocalDateTime.of(addEndDate,addEndTime);

        ZonedDateTime userStart = ZonedDateTime.of(dateTimeStart,ZoneId.systemDefault());
        ZonedDateTime userEnd = ZonedDateTime.of(dateTimeEnd,ZoneId.systemDefault());
        ZonedDateTime businessStart = ZonedDateTime.of(addStartDate, LocalTime.of(8,0),ZoneId.of("US/Eastern"));
        ZonedDateTime businessEnd = ZonedDateTime.of(addEndDate, LocalTime.of(22,0), ZoneId.of("US/Eastern"));

        // checking for selected appointment dates/times are out of business hours
        if(userStart.isBefore(businessStart) || userStart.isAfter(businessEnd) || userEnd.isBefore(businessStart)
                || userEnd.isAfter(businessEnd)) {
            Alert businessHourConflict = new Alert(Alert.AlertType.ERROR, "Time is outside of normal business hours (8am-10pm EST).");
            Optional<ButtonType> results = businessHourConflict.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

        // input validation messages for conflicting appointments
        ObservableList<Appointment> getAllAppointments = AppointmentDAO.allAppointments();
        for(Appointment apptConflicts : getAllAppointments) {

            if(addCustID == apptConflicts.getCustomerID() && (dateTimeStart.isEqual(apptConflicts.getStart()) &&
                    dateTimeEnd.isEqual(apptConflicts.getEnd()) ) ){

            /////TEST/////////////
            System.out.println(dateTimeStart); //user input
            System.out.println(apptConflicts.getStart()); // database
            System.out.println(dateTimeEnd); // user input
            System.out.println(apptConflicts.getEnd()); //database

            Alert apptConflict = new Alert(Alert.AlertType.ERROR, "This customer already has an existing " +
                    "appointment for this date and time.");
            apptConflict.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> results = apptConflict.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK){
                apptConflict.close();
            }

        }

            if(addCustID == apptConflicts.getCustomerID() && ((dateTimeStart.isBefore(apptConflicts.getStart()) ||
                    dateTimeStart.isEqual(apptConflicts.getStart()))) && ((dateTimeEnd.isAfter(apptConflicts.getEnd()) ||
                    dateTimeEnd.isEqual(apptConflicts.getEnd())))) {
                Alert apptConflict = new Alert(Alert.AlertType.ERROR, "This meeting overlaps with the customer's existing appointment.");
                Optional<ButtonType> results = apptConflict.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }

            if(addCustID == apptConflicts.getCustomerID() && ((dateTimeStart.isAfter(apptConflicts.getStart()) &&
                    dateTimeStart.isBefore(apptConflicts.getEnd())))){
                Alert apptConflict = new Alert(Alert.AlertType.ERROR, "Start time overlaps with existing " +
                        "appointment.");
                Optional<ButtonType> results = apptConflict.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }

            if(addCustID == apptConflicts.getCustomerID() && ((dateTimeEnd.isAfter(apptConflicts.getStart()) &&
                    ((dateTimeEnd.isBefore(apptConflicts.getEnd()) || dateTimeEnd.isEqual(apptConflicts.getEnd())))))){
                Alert apptConflict = new Alert(Alert.AlertType.ERROR, "End time overlaps with existing " +
                        "appointment.");
                Optional<ButtonType> results = apptConflict.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
    }

        try {
            newAppointment.setStart(LocalDateTime.of(addStartDate,addStartTime));
            newAppointment.setEnd(LocalDateTime.of(addEndDate,addEndTime));
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
                AppointmentDAO.addAppointment(addStartDate,addStartTime,addEndDate,addEndTime,String.valueOf(addContact),
                     addType,addTitle,addDescription,addLocation,addCustID, addUserID);
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