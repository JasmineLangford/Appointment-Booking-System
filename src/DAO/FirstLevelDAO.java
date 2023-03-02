package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains the database queries for divisions.
 */
public class FirstLevelDAO extends Customer {

    // override format for combo box selection display
    @Override
    public String toString(){
        return (getDivision());
    }

    /**
     * This constructor represents the customer divisions.
     *
     * @param division The division name.
     * @param countryId The country ID.
     * @param divisionId The division ID.
     */
    public FirstLevelDAO(int divisionId, String division, int countryId) {
        super(divisionId, division, countryId);
    }

    /**
     * This method queries all division selections for combo box.
     *
     * @throws SQLException The exception to throw if there is an error with database connection or with the query.
     * @return The list of all divisions.
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
