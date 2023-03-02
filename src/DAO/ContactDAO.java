package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains the database queries for contacts.
 */
public class ContactDAO extends Appointment {
    // override data type for combo box selection display
    @Override
    public String toString(){
        return (getContactID() + " " + getContactName());
    }

    /**
     * This constructor represents the contact.
     *
     * @param contactId The contact ID.
     * @param contactName The contact name.
     */
    public ContactDAO (int contactId, String contactName){
        super(contactId, contactName);
    }

    /**
     * This method queries all contact selections for the combo box.
     *
     * @throws SQLException The exception to throw if there is an error with database connection or with the query.
     * @return The list of all contacts.
     */
    public static ObservableList<ContactDAO> allContacts() throws SQLException {
        ObservableList<ContactDAO> contactList = FXCollections.observableArrayList();
        String contactQuery = "SELECT Contact_ID, Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(contactQuery);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");

            ContactDAO contacts = new ContactDAO(contactId, contactName);
            contactList.add(contacts);
        }
        return contactList;
    }
}