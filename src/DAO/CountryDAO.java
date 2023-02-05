package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO extends Customer {

    @Override
    public String toString(){
        return (getCustomerCountry());
    }
    public CountryDAO (int countryId, String customerCountry) {
        super(countryId,customerCountry);
    }

    /**
     * Method to query country selections for combo box.
     *
     * @return all country options
     */


    public static ObservableList<CountryDAO> allCountries() throws SQLException {
        ObservableList<CountryDAO> allCountries = FXCollections.observableArrayList();
        String countriesQuery = "SELECT Country_ID, Country FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(countriesQuery);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String customerCountry = rs.getString("Country");

            CountryDAO country = new CountryDAO(countryId, customerCountry);
            allCountries.add(country);
        }
        return allCountries;
    }

}
