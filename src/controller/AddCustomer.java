package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is the controller for add-customer.fxml view.
 * User will be able to input data in text fields and choice boxes in order to add a new customer.
 */
public class AddCustomer {

    /**
     * This will save data in the text fields and add the new customer to the Customer tableview on the Main Menu.
     @param actionEvent Save button is clicked.
     */
    public void onSave(ActionEvent actionEvent) {
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
