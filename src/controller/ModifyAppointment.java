package controller;

import DAO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
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
    private ComboBox<String> locationCombo;
    @FXML
    private ComboBox<String> typeCombo;
    @FXML
    private TextField titleText;
    @FXML
    private TextArea descText;
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private ComboBox<UserDAO> userCombo;


    // observable lists for combo boxes
    ObservableList<ContactDAO> contacts = ContactDAO.allContacts();
    ObservableList <UserDAO> users = UserDAO.allUsers();
    ObservableList <Customer> customers = CustomerDAO.allCustomers();

    Appointment modAppointment = new Appointment();

    public ModifyAppointment() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Appointment initialized.");

        // combo box for time selection
        LocalTime start = LocalTime.of(8,0);
        LocalTime end = LocalTime.of(17,0);

        while(start.isBefore(end.plusSeconds(1))){
            startCombo.getItems().add(start);
            endCombo.getItems().add(start);
            start = start.plusMinutes(15);
        }

        // contact combo box
        contactModCombo.setItems(contacts);

        // user combo box
        userCombo.setItems(users);

        // customer combo box
        customerCombo.setItems(customers);

        // Set end date same as start date
        startDatePicker.setOnAction(event -> {
            LocalDate selectedDate = startDatePicker.getValue();
            endDatePicker.setValue(selectedDate);
        });

        // appointment type combo box
        ObservableList<String> types = FXCollections.observableArrayList(
                "In-Person","Virtual"
        );
        typeCombo.setItems(types);

        // location combo box
        ObservableList<String> locations = FXCollections.observableArrayList(
                "Manhattan", "Newark"
        );
        locationCombo.setItems(locations);
    }

    /**
     * This method populates the selected appointment row of data from the Main Menu.
     *
     * @param appointment The appointment from the appointment model.
     */
    public void sendAppointment(Appointment appointment) {

        // input validation messages: Dates
        try{
            modAppointment = appointment;
            for (ContactDAO a:contactModCombo.getItems()) {
                if(a.getContactID() == appointment.getContactID()){
                    contactModCombo.getSelectionModel().select(a);
                    break;
                }
            }

            for (Customer c : customerCombo.getItems()) {
                if(c.getCustomerId() == modAppointment.getCustomerID()) {
                    customerCombo.getSelectionModel().select(c);
                    break;
                }
            }

            for (UserDAO u : userCombo.getItems()) {
                if(u.getUserID() == modAppointment.getUserID()) {
                    userCombo.getSelectionModel().select(u);
                    break;
                }
            }

            apptId.setText(String.valueOf(modAppointment.getAppointmentID()));
            startDatePicker.setValue(modAppointment.getStart().toLocalDate());
            startCombo.setValue(modAppointment.getStart().toLocalTime());
            endDatePicker.setValue(modAppointment.getEnd().toLocalDate());
            endCombo.setValue(modAppointment.getEnd().toLocalTime());
            locationCombo.setValue(modAppointment.getLocation());
            typeCombo.setValue(modAppointment.getType());
            titleText.setText(modAppointment.getTitle());
            descText.setText(modAppointment.getDescription());

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method will save the modified appointment data.
     *
     * @param actionEvent The save button is clicked.
     */
    public void onSaveModAppointment(ActionEvent actionEvent) throws SQLException {

        try {
            if (startDatePicker == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select appointment date.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException");
        }

        try {
            if (startDatePicker.getValue().isBefore(LocalDate.now())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "Selected date has already passed. Please " +
                        "select another date.");
                invalidDate.setTitle("Appointment Booking System");
                invalidDate.setHeaderText("Add Appointment");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidDate.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
        }

        try {
            if (startDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                    startDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "Appointment date must be a weekday " +
                        "(Monday-Friday).");
                invalidDate.setTitle("Appointment Booking System");
                invalidDate.setHeaderText("Add Appointment");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidDate.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
        }

        // input validation messages: start and end time combo boxes
        try {
            if (startCombo.getSelectionModel().isEmpty() || endCombo.getSelectionModel().isEmpty()) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select start and end times.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException");
        }

        LocalDateTime localStart = LocalDateTime.of(startDatePicker.getValue(), startCombo.getValue());
        LocalDateTime localEnd = LocalDateTime.of(endDatePicker.getValue(), endCombo.getValue());
        try {
            if (localEnd.isBefore(localStart)) {
                Alert invalidTime = new Alert(Alert.AlertType.ERROR, "End date/time cannot be before start " +
                        "date/time.");
                invalidTime.setTitle("Appointment Booking System");
                invalidTime.setHeaderText("Add Appointment");
                Optional<ButtonType> results = invalidTime.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidTime.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception TimeException) {
            System.out.println("Caught TimeException");
        }

        try {
            if (startCombo.getValue().equals(endCombo.getValue())) {
                Alert invalidTime = new Alert(Alert.AlertType.ERROR, "End time cannot be the same as start " +
                        "time. Please select another end time.");
                invalidTime.setTitle("Appointment Booking System");
                invalidTime.setHeaderText("Add Appointment");
                invalidTime.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                Optional<ButtonType> results = invalidTime.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidTime.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception TimeException) {
            System.out.println("Caught TimeException");
        }

        // input validation: empty selection for contact combo
        try {
            if (contactModCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a contact.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty text fields
        try {
            if (contactModCombo.getValue() == null || typeCombo.getValue() == null|| titleText.getText().isEmpty() ||
                    descText.getText().isEmpty() || locationCombo.getValue() == null) {
                Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
                emptyField.setTitle("Appointment Booking System");
                emptyField.setHeaderText("Add Appointment");
                Optional<ButtonType> results = emptyField.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    emptyField.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty customer ID combo
        try {
            if (customerCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a customer ID.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty user ID combo
        try {
            if (userCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a user ID.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        int modApptID = Integer.parseInt(apptId.getText());
        LocalDate modStartDate = startDatePicker.getValue();
        LocalTime modStartTime = startCombo.getValue();
        LocalDate modEndDate = endDatePicker.getValue();
        LocalTime modEndTime = endCombo.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDatePicker.getValue(),startCombo.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(),endCombo.getValue());
        int modContact = contactModCombo.getSelectionModel().getSelectedItem().getContactID();
        String modType = typeCombo.getSelectionModel().getSelectedItem();
        String modTitle = titleText.getText();
        String modDescription = descText.getText();
        String modLocation = locationCombo.getSelectionModel().getSelectedItem();
        int modCustID = customerCombo.getSelectionModel().getSelectedItem().getCustomerId();
        int modUserID = userCombo.getSelectionModel().getSelectedItem().getUserID();

        LocalDateTime dateTimeStart = LocalDateTime.of(modStartDate,modStartTime);
        LocalDateTime dateTimeEnd = LocalDateTime.of(modEndDate,modEndTime);

        ZonedDateTime userStart = ZonedDateTime.of(dateTimeStart,ZoneId.systemDefault());
        ZonedDateTime userEnd = ZonedDateTime.of(dateTimeEnd,ZoneId.systemDefault());
        ZonedDateTime businessStart = ZonedDateTime.of(modStartDate, LocalTime.of(8,0),ZoneId.of("US/Eastern"));
        ZonedDateTime businessEnd = ZonedDateTime.of(modEndDate, LocalTime.of(22,0), ZoneId.of("US/Eastern"));

        // checking for selected appointment dates/times are out of business hours
        if(userStart.isBefore(businessStart) || userStart.isAfter(businessEnd) || userEnd.isBefore(businessStart)
                || userEnd.isAfter(businessEnd)) {
            Alert businessHourConflict = new Alert(Alert.AlertType.ERROR, "Time is outside of normal business hours (8am-10pm EST).");
            Optional<ButtonType> results = businessHourConflict.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

        // input validation messages for conflicting appointments
        if(modStartDate != modAppointment.getStart().toLocalDate() ||
                modEndDate != modAppointment.getEnd().toLocalDate() ||
                modStartTime != modAppointment.getStart().toLocalTime() ||
                modEndTime != modAppointment.getEnd().toLocalTime())  {
            ObservableList<Appointment> getAllAppointments = AppointmentDAO.allAppointments();
            try {
                for (Appointment apptConflicts : getAllAppointments) {

                    if (modCustID == apptConflicts.getCustomerID() && (dateTimeStart.isEqual(apptConflicts.getStart()) &&
                            dateTimeEnd.isEqual(apptConflicts.getEnd()))) {

                        Alert apptConflict = new Alert(Alert.AlertType.ERROR, "This customer already has an existing " +
                                "appointment for this date and time.");
                        apptConflict.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        Optional<ButtonType> results = apptConflict.showAndWait();
                        if (results.isPresent() && results.get() == ButtonType.OK)
                            return;
                    }

                    if (modCustID == apptConflicts.getCustomerID() && ((dateTimeStart.isBefore(apptConflicts.getStart()) ||
                            dateTimeStart.isEqual(apptConflicts.getStart()))) && ((dateTimeEnd.isAfter(apptConflicts.getEnd()) ||
                            dateTimeEnd.isEqual(apptConflicts.getEnd())))) {

                        Alert apptConflict = new Alert(Alert.AlertType.ERROR, "This meeting overlaps with the " +
                                "customer's existing appointment.");
                        apptConflict.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        Optional<ButtonType> results = apptConflict.showAndWait();
                        if (results.isPresent() && results.get() == ButtonType.OK)
                            return;
                    }

                    if (modCustID == apptConflicts.getCustomerID() && ((dateTimeStart.isAfter(apptConflicts.getStart()) &&
                            dateTimeStart.isBefore(apptConflicts.getEnd())))) {

                        Alert apptConflict = new Alert(Alert.AlertType.ERROR, "Start time overlaps with existing " +
                                "appointment.");
                        Optional<ButtonType> results = apptConflict.showAndWait();
                        if (results.isPresent() && results.get() == ButtonType.OK)
                            return;
                    }

                    if (modCustID == apptConflicts.getCustomerID() && ((dateTimeEnd.isAfter(apptConflicts.getStart()) &&
                            ((dateTimeEnd.isBefore(apptConflicts.getEnd()) || dateTimeEnd.isEqual(apptConflicts.getEnd())))))) {

                        Alert apptConflict = new Alert(Alert.AlertType.ERROR, "End time overlaps with existing " +
                                "appointment.");
                        Optional<ButtonType> results = apptConflict.showAndWait();
                        if (results.isPresent() && results.get() == ButtonType.OK)
                            return;
                    }
                }
            } catch (Exception AppointmentConflicts) {
                System.out.println("Caught AppointmentConflicts");
            }
        }
        else{
            System.out.println("No changes to dates and times.");
        }
        try {
            modAppointment.setAppointmentID(modApptID);
            modAppointment.setTitle(modTitle);
            modAppointment.setDescription(modDescription);
            modAppointment.setLocation(modLocation);
            modAppointment.setType(modType);
            modAppointment.setStart(startDateTime);
            modAppointment.setEnd(endDateTime);
            modAppointment.setCustomerID(modCustID);
            modAppointment.setUserID(modUserID);
            modAppointment.setContactID(modContact);

            Alert modAppointment = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " +
                    "appointment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = modAppointment.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                AppointmentDAO.modifyAppointment(modApptID,modTitle, modDescription, modLocation, modType,
                        startDateTime,endDateTime,modCustID, modUserID, String.valueOf(modContact));

                Appointments.backToAppointments(actionEvent);
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
     * This method takes the end-user back to the Main Menu.
     *
     * @param actionEvent The cancel button is clicked.
     * @throws IOException The exception thrown if there is an I/O error for the alert.
     */
    public void toAppointments(ActionEvent actionEvent) throws IOException {
        Alert noChange = new Alert(Alert.AlertType.INFORMATION, "There were no changes made to this " +
                "appointment.", ButtonType.OK);
        noChange.showAndWait();
        Appointments.backToAppointments(actionEvent);
    }
}
