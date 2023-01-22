package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is the controller for the add-appointment.fxml view.
 * User will be able to input data into text fields and choice boxes in order to add a new appointment.
 */
public class AddAppointment {

    // Form fields
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ChoiceBox startTimeCombo;
    @FXML
    private ChoiceBox endTimeCombo;
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



    /**
     * This will add the data from form fields as a new appointment to database.
     *
     @param actionEvent Save button is clicked.
     */
    public void onSaveAppt(ActionEvent actionEvent) {

        // Exception dialog messages
        try {
            if (typeTextfield.getText().isEmpty() || titleTextfield.getText().isEmpty() ||
                    descTextfield.getText().isEmpty() || locationTextfield.getText().isEmpty()) {
                Alert emptyField = new Alert(Alert.AlertType.ERROR, "Invalid data type. Please enter value in each "
                                   + "form field.");
                Optional<ButtonType> results = emptyField.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception e) {
            return;
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

         String addStartDate = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd MMMM yyyy zzzz"));
         int addStartTime = startTimeCombo.getSelectionModel().getSelectedIndex();
         String addEndDate = endDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd MMMM yyyy zzzz"));
         int addEndTime = endTimeCombo.getSelectionModel().getSelectedIndex();
         int id = Integer.parseInt(apptID.getText());
         String addContact = contactCombo.getValue().toString();
         String addType = typeTextfield.getText();
         String addTitle = titleTextfield.getText();
         String addDescription = descTextfield.getText();
         String addLocation = locationTextfield.getText();
         int addCustID = Integer.parseInt(custIdTextfield.getText());
         int addUserID = Integer.parseInt(userIdTextfield.getText());

    }

    /**
     * This navigates the user back to the Main Menu.
     * @param actionEvent Cancel button is clicked.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/main-menu.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}