package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains the database queries for customers.
 */
public class CustomerDAO {
    /**
     * Method to show all customers queried from database - customer table
     */
    public static ObservableList<Customer> allCustomers() throws SQLException {
        ObservableList<Customer> listOfCustomers = FXCollections.observableArrayList();
        String customerQuery = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, countries.Country, first_level_divisions.Country_ID, customers.Division_ID, " +
                     "Division FROM customers INNER JOIN first_level_divisions ON customers.Division_ID=" +
                     "first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID=" +
                     "countries.Country_ID;";
        PreparedStatement ps = JDBC.connection.prepareStatement(customerQuery);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPhone = rs.getString("Phone");
            String customerCountry = rs.getString("Country");
            int countryId = rs.getInt("Country_ID");
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            String customerPostal = rs.getString("Postal_Code");
            Customer customer = new Customer(customerId,customerName,customerAddress,customerPhone, customerCountry,
                                countryId, divisionId,division,customerPostal);
            listOfCustomers.add(customer);
        }
        return listOfCustomers;
    }

    public static void deleteCustomer (Customer customer) throws SQLException {

        String deleteCustomerQuery = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteCustomerQuery);
        ps.setInt(1, customer.getCustomerId());
        ps.executeUpdate();
    }
}
