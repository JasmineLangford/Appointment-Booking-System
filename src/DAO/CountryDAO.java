package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains the database queries for countries.
 */
public class CountryDAO extends Customer {
    // override format for combo box selection display
    @Override
    public String toString(){
        return (getCustomerCountry());
    }

    /**
     * This constructor represents the customer countries.
     *
     * @param countryId The country ID.
     * @param customerCountry The country name.
     */
    public CountryDAO (int countryId, String customerCountry) {
        super(countryId,customerCountry);
    }

    /**
     * This method queries country selections for the combo box.
     *
     * @throws SQLException The exception to throw if there is an error with database connection or with the query.
     * @return The list of all countries.
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
