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
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


// TODO: rewrite Javadocs

/**
 * This class is the controller for home.fxml.
 * End-user can also navigate to the reports screen.
 */
public class Home implements Initializable {
    @FXML
    public Label currentDate;
    @FXML
    public Label currentUser;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public ToggleGroup apptViewToggle;
    public RadioButton viewByMonth;
    public RadioButton viewByWeek;
    public RadioButton viewAllAppts;
    @FXML
    public TableColumn<Appointment,String> apptEndCol;
    @FXML
    private TableView<Appointment> mainApptTable;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptLocationCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, String> apptStartCol;
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

        LocalDate current = LocalDate.now();
        DateTimeFormatter formatCurrentDate = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String formattedDate = current.format(formatCurrentDate);
        currentDate.setText("Today," + " " + formattedDate);

        // appointment table columns
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptCustCol.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            int customerId = appointment.getCustomerID();
            Customer customer = null;
            try {
                customer = CustomerDAO.allCustomers().get(customerId);
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
                user = UserDAO.allUsers().get(userId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String userFirstLastName = (user != null) ? user.getUserFirstName() + " " + user.getUserLastName(): "";
            return new SimpleStringProperty(userFirstLastName);
        });


        // lambda
        apptStartCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatter)));
        apptEndCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatter)));

        try {
            loadApptTable();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method takes the end-user to the main menu from another screen.
     *
     * @param actionEvent When button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public static void toHomeScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(Home.class.getResource("/view/home.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void toAppointments(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/appointments.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void toCustomers(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/customers.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }
    /**
     * This method takes the end-user to view reports on the Reports screen.
     *
     * @param actionEvent Reports button is clicked (located on the right panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toReports(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/reports.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }


    // load appointment tableview
//    public void loadApptTable() throws SQLException {
//        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
//        mainApptTable.setItems(allAppointments);
//        mainApptTable.getSelectionModel().clearSelection();
//    }
    public void loadApptTable() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();

        for (Appointment appointment : allAppointments) {
            int customerId = appointment.getCustomerID();
            Customer customer = CustomerDAO.allCustomers().get(customerId);
            String customerName = (customer != null) ? customer.getCustomerName() : "";
            assert customer != null;
            customer.setCustomerName(customerName);
        }

        mainApptTable.setItems(allAppointments);
        mainApptTable.getSelectionModel().clearSelection();
    }
    // show all appointments if All Appointments radio button selected - default selection
    public void changeToAllAppts() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        for (Appointment ignored : allAppointments) {
            mainApptTable.setItems(allAppointments);
            mainApptTable.setPlaceholder(new Label("No appointments available"));

        }
    }

    // show appointments for current month if radio button selected
    public void changeToMonth() throws SQLException {
        ObservableList<Appointment> currentMonth = AppointmentDAO.currentMonth();
        for (Appointment ignored : currentMonth) {
            mainApptTable.setItems(currentMonth);
            mainApptTable.setPlaceholder(new Label("No appointments for this month"));
        }
    }

    // show appointments for current week if radio button selected
    public void changeToWeek() throws SQLException {
        ObservableList<Appointment> currentWeek = AppointmentDAO.currentWeek();
        for (Appointment ignored : currentWeek) {
            mainApptTable.setItems(currentWeek);

            mainApptTable.setPlaceholder(new Label("No appointments for this week"));
        }
    }

    /**
     * This method will take the end-user to the Add Appointment screen where a new appointment can be added.
     *
     * @param actionEvent Add button is clicked under the appointments tableview.
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
     * This method will take the end-user to Modify Appointment screen where they can modify existing appointments. The
     * data from the selected row will auto-populate to the modify form.
     *
     * @param actionEvent Modify button is clicked under the appointments tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void updateAppt(ActionEvent actionEvent) throws IOException {
        // error message if end-user does not make a selection
        if (mainApptTable.getSelectionModel().isEmpty()) {
            Alert modApptSelect = new Alert(Alert.AlertType.WARNING, "Please select an appointment to be modified.");
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
     * This method will delete appointments from appointments table in the database. The deleted row will no longer be
     * displayed on appointment tableview. A message will also appear for deleted appointments stating the cancelled
     * appointment ID and type.
     */
    public void deleteApptRow() {
        if (mainApptTable.getSelectionModel().isEmpty()) {
            Alert deleteAppt = new Alert(Alert.AlertType.WARNING, "Please select an appointment to be deleted.");
            Optional<ButtonType> result = deleteAppt.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
                mainApptTable.getSelectionModel().clearSelection();
        } else {
            Alert deleteApptConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this " +
                    "appointment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = deleteApptConfirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    Appointment selectedAppt = mainApptTable.getSelectionModel().getSelectedItem();
                    AppointmentDAO.deleteAppt(selectedAppt);
                    loadApptTable();

                    Alert apptInfo = new Alert(Alert.AlertType.INFORMATION, "You have deleted the following appointment: " +
                            '\n' + '\n' + "Appointment ID: " + selectedAppt.getAppointmentID() + '\n' + "Type: " +
                            selectedAppt.getType(), ButtonType.OK);
                    apptInfo.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            mainApptTable.getSelectionModel().clearSelection();
        }
    }
    /**
     * This method closes the application and an alert will ask the end-user to confirm close.
     */
    public void closeApp() {
        exitApp();
    }

    public void exitApp() {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Platform.exit();
            System.out.println("Program Closed");
        }
    }
}