package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class contains the database queries for customers.
 */
public abstract class CustomerDAO {

    /**
     * Method to show all customers queried from database - customer table
     */
    public static ObservableList<Customer> allCustomers() throws SQLException {
        ObservableList<Customer> listOfCustomers = FXCollections.observableArrayList();
        String customerQuery = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, countries.Country, " +
                "first_level_divisions.Country_ID, customers.Division_ID, " +
                     "Division FROM customers INNER JOIN first_level_divisions ON customers.Division_ID=" +
                     "first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID=" +
                     "countries.Country_ID;";
        try {
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
        } catch (SQLException e){
            e.printStackTrace();
        }
        return listOfCustomers;
    }

    /**
     * Method to add customer into customers table in the database.
     *
     */
    public static void addCustomer(int customerId, String customerName, String customerAddress, String customerPostal,
                                   String customerPhone, int divisionId) throws SQLException {

        String addCustomerQuery = "INSERT INTO customers (Customer_ID,Customer_Name,Address,Postal_Code,Phone," +
                "Division_ID) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(addCustomerQuery);
        ps.setInt(1, customerId);
        ps.setString(2, customerName);
        ps.setString(3, customerAddress);
        ps.setString(4,customerPostal);
        ps.setString(5,customerPhone);
        ps.setInt(10, divisionId);
        int results = ps.executeUpdate();
    }

    /**
     * Method to delete customer from database.
     */
    public static void deleteCustomer (Customer customer) throws SQLException {

        String deleteCustomerQuery = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteCustomerQuery);
        ps.setInt(1, customer.getCustomerId());
        ps.executeUpdate();
    }

    /**
     * Method to query appointments if the there is a matching customer ID.
     */
    public static ObservableList<Appointment> deleteAssociated(int customerId) throws SQLException {
        ObservableList<Appointment> associatedAppts = FXCollections.observableArrayList();
        String associatedApptQuery = "SELECT * FROM appointments WHERE EXISTS (SELECT Customer_ID FROM customers " +
                "WHERE appointments.Customer_ID = customers.Customer_ID AND Customer_ID = ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(associatedApptQuery);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contactID = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            Appointment deleteAppt = new Appointment(appointmentID, title, description, location, contactID, type,
                    start, end, customerID, userID);
            associatedAppts.add(deleteAppt);
        }
        return associatedAppts;
    }

}