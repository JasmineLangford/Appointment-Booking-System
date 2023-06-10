package controller;

import DAO.CountryDAO;
import DAO.FirstLevelDAO;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ModifyCorpAcct {
    public TextField customerIdField;
    public TextField customerNameText;
    public TextField customerPhoneText;
    public TextField customerAddressText;
    public TextField customerPostalText;
    public ComboBox<CountryDAO> countryModCombo;
    public ComboBox<FirstLevelDAO> firstLevelModCombo;

    public void onSaveModCustomer(ActionEvent actionEvent) {
    }

    public void toMainMenu(ActionEvent actionEvent) {
    }

    public void onModCountry(ActionEvent actionEvent) {
    }
}
