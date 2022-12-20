package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * This class contains a tableview for appointments and customers.
 * Users are able to add, modify and delete appointments and customers.
 * Users can also navigate to a report for this data.
 */
public class MainMenu {

    public TableView apptView;
    public TableView customerView;

    /**
     * This will take the user to the Add Appointment screen where they can add a new appointment.
     * @param actionEvent Add button is clicked under the Appointments tableview.
     * */
    public void addAppt(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Appointment initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-appointment.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 560, 625);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This will take the user to Modify Appointment screen where they can modify existing appointments.
     * @param actionEvent Modify button is clicked under the Appointments tableview.
     * */
    public void updateAppt(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify Appointment initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/modify-appointment.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 560, 625);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This will take the user to the Add Customer screen where they can add a new customer.
     * @param actionEvent Add button is clicked under the Customers tableview.
     * */
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Customer initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-customer.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 475);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This will take the user to Modify Customer screen where they can modify existing customers.
     * @param actionEvent Modify button is clicked under the Customer tableview.
     * */

    public void updateCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify Customer initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/modify-customer.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 475);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This will delete appointments from the Appointments tableview.
     * Appointments that involve customers cannot be deleted.
     * The customers associated with that appointment will need to be deleted prior to deleting the appointment.
     * @param actionEvent delete button below Appointment tableview clicked.
     */
    public void deleteApt(ActionEvent actionEvent) {
    }

    /**
     * This will delete a customer from the Customer tableview.
     * @param actionEvent delete button below Customer tableview is clicked.
     */
    public void deleteCustomer(ActionEvent actionEvent) {

    }

    /**
     * This takes the user to view reports on the Reports screen.
     * @param actionEvent Reports button is clicked (located on the right panel).
     */
    public void toReports(ActionEvent actionEvent) throws IOException {
        System.out.println("Reports initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/reports.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This closes the application.
     * An alert will ask the user to confirm close.
     * @param actionEvent Logout button is clicked (located on the right panel).
     */
    public void toClose(ActionEvent actionEvent) {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            System.out.println("Program Closed");
        }
    }
}