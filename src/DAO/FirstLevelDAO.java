package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDAO extends Customer {

    public FirstLevelDAO (int divisionId, String division, int countryId) {
        super(divisionId, division, countryId);
    }

    @Override
    public String toString(){
        return (getDivision());
    }

    /**
     * Method to query all first-level division selections for combo box.
     *
     * @return all first level options
     */

    public static ObservableList<FirstLevelDAO> allFirstLevelDivision() throws SQLException {
        ObservableList<FirstLevelDAO> firstLevelDivision = FXCollections.observableArrayList();
        String firstLevelQuery = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(firstLevelQuery);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            FirstLevelDAO firstLevel = new FirstLevelDAO (divisionId, division, countryId);
            firstLevelDivision.add(firstLevel);
        }
        return firstLevelDivision;
    }
}
