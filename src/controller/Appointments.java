package controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import DAO.CustomerDAO;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * This class is the controller for appointments.fxml.
 */
public class Appointments implements Initializable {

    @FXML
    private Label currentDate;
    @FXML
    public Label currentUser;
    public ToggleGroup apptViewToggle;
    public RadioButton viewByMonth;
    public RadioButton viewByWeek;
    public RadioButton viewAllAppts;
    @FXML
    private TableView<Appointment> mainApptTable;
    @FXML
    private TableColumn<Appointment,String> apptTimes;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptLocationCol;
    @FXML
    private TableColumn<Appointment, String> apptDate;
    @FXML
    private TableColumn<Appointment, String> apptCustCol;
    @FXML
    private TableColumn<Appointment, String> apptUserCol;

    /**
     * This method launches the home screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Main Menu initialized!");

        // Display user's first name in welcome tagline
        String firstName = UserDAO.getUserLogin().getUserFirstName();
        currentUser.setText(firstName + "!");

        // Display current date
        LocalDate current = LocalDate.now();
        DateTimeFormatter formatCurrentDate = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String formattedDate = current.format(formatCurrentDate);
        currentDate.setText("Today," + " " + formattedDate);

        // Set appointment table columns
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptCustCol.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            int customerId = appointment.getCustomerID();
            Customer customer;
            try {
                customer = CustomerDAO.allCustomers().get(customerId - 1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String customerName = (customer != null) ? customer.getCustomerName() : "";
            return new SimpleStringProperty(customerName);
        });
        apptUserCol.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            int userId = appointment.getUserID();
            User user;
            try {
                user = UserDAO.allUsers().get(userId - 1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String userFirstLastName = (user != null) ? user.getUserFirstName() + " " + user.getUserLastName(): "";
            return new SimpleStringProperty(userFirstLastName);
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        apptDate.setCellValueFactory(cellData -> {
            String dateTimeValue = cellData.getValue().getStart().format(dateFormatter);
            return new SimpleStringProperty(dateTimeValue);
        });

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        apptTimes.setCellValueFactory(cellData -> {
            LocalTime startTime = cellData.getValue().getStart().toLocalTime();
            LocalTime endTime = cellData.getValue().getEnd().toLocalTime();
            String timeRange = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
            return new SimpleStringProperty(timeRange);
        });

        try {
            loadApptTable();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method takes the user to the appointments screen.
     *
     * @param actionEvent Appointments label is clicked (located on the left panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public static void toAppointments(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(Appointments.class.getResource("/view/appointments.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 538);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method takes the user to the customers screen.
     *
     * @param actionEvent Customers label is clicked (located on the left panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toCustomers(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/customers.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 538);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method takes the user to the reports screen.
     *
     * @param actionEvent Reports button is clicked (located on the right panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toReports(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/reports.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 538);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method loads the data from the appointments table in the database to the appointments' tableview.
     *
     * @throws SQLException The exception to throw if there is an issue with the sql query.
     */
    public void loadApptTable() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        mainApptTable.setItems(allAppointments);
        mainApptTable.getSelectionModel().clearSelection();
    }

    /**
     * This method filters the appointments to display all appointments.
     *
     * @throws SQLException The exception to throw if there is an issue with the sql query.
     */
    public void changeToAllAppts() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        for (Appointment ignored : allAppointments) {
            mainApptTable.setItems(allAppointments);
            mainApptTable.setPlaceholder(new Label("No appointments available"));

        }
    }

    /**
     * This method filters the appointments to display only appointments for the current month.
     *
     * @throws SQLException The exception to throw if there is an issue with the sql query.
     */    public void changeToMonth() throws SQLException {
        ObservableList<Appointment> currentMonth = AppointmentDAO.currentMonth();
        for (Appointment ignored : currentMonth) {
            mainApptTable.setItems(currentMonth);
            mainApptTable.setPlaceholder(new Label("No appointments for this month"));
        }
    }

    /**
     * This method filters the appointments to display only appointments for the current week.
     *
     * @throws SQLException The exception to throw if there is an issue with the sql query.
     */
    public void changeToWeek() throws SQLException {
        ObservableList<Appointment> currentWeek = AppointmentDAO.currentWeek();
        for (Appointment ignored : currentWeek) {
            mainApptTable.setItems(currentWeek);

            mainApptTable.setPlaceholder(new Label("No appointments for this week"));
        }
    }

    /**
     * This method will take the user to the Add Appointment screen where a new appointment can be added.
     *
     * @param actionEvent Add button is clicked under the appointments' tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void addAppt(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-appointment.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 560, 625);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method will take the user to view details of the selected appointment. The data from the selected row will
     * auto-populate in the input fields and the user can then make modifications.
     *
     * @param actionEvent View Details button is clicked under the appointments' tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void viewApptDetails (ActionEvent actionEvent) throws IOException {

        // error message if user does not make a selection
        if (mainApptTable.getSelectionModel().isEmpty()) {
            Alert modApptSelect = new Alert(Alert.AlertType.WARNING, "Please select an appointment to view the " +
                    "details.");
            modApptSelect.setTitle("Appointment Booking System");
            modApptSelect.setHeaderText("Appointment Details");
            Optional<ButtonType> results = modApptSelect.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                modApptSelect.setOnCloseRequest(Event::consume);
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/modify-appointment.fxml"));
        loader.load();

        // send selected data to modify appointment screen
        ModifyAppointment MainMenu = loader.getController();
        MainMenu.sendAppointment(mainApptTable.getSelectionModel().getSelectedItem());

        Parent scene = loader.getRoot();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));

        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method will cancel the selected appointment by deleting the appointment from the database.
     * The deleted row will no longer be displayed on the appointment tableview. A message will also appear for deleted
     * appointment stating the cancelled appointment ID and type.
     */
    public void cancelAppointment() {
        if (mainApptTable.getSelectionModel().isEmpty()) {
            Alert deleteAppt = new Alert(Alert.AlertType.WARNING, "Please select an appointment to be cancelled.");
            deleteAppt.setTitle("Appointment Booking System");
            deleteAppt.setHeaderText("Cancel Appointment");
            Optional<ButtonType> result = deleteAppt.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
                mainApptTable.getSelectionModel().clearSelection();
        } else {
            Alert deleteApptConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this " +
                    "appointment?", ButtonType.YES, ButtonType.NO);
            deleteApptConfirm.setTitle("Appointment Booking System");
            deleteApptConfirm.setHeaderText("Confirm Cancellation");
            Optional<ButtonType> result = deleteApptConfirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    Appointment selectedAppt = mainApptTable.getSelectionModel().getSelectedItem();
                    AppointmentDAO.deleteAppt(selectedAppt);
                    loadApptTable();

                    Alert apptInfo = new Alert(Alert.AlertType.INFORMATION, "You have cancelled the following " +
                            "appointment:" + '\n' + "Appointment ID: " + selectedAppt.getAppointmentID() + '\n' +
                            "Appointment Title: " + selectedAppt.getTitle(), ButtonType.OK);
                    apptInfo.setTitle("Appointment Booking System");
                    apptInfo.setHeaderText("Cancel Appointment");
                    apptInfo.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            mainApptTable.getSelectionModel().clearSelection();
        }
    }

    /**
     * This method closes the application and an alert will ask the user to confirm close.
     */
    public void toClose() {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the " +
                "application?", ButtonType.YES, ButtonType.NO);
        closeConfirm.setTitle("Appointment Booking System");
        closeConfirm.setHeaderText("Exit Application");
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Platform.exit();
            System.out.println("Application Closed");
        }
    }
}