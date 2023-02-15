package model;

import DAO.ContactDAO;
import javafx.scene.control.SingleSelectionModel;

/**
 * This class is the model for contacts.
 */

public class Contact {

    private int contactId;
    private String contactName;

    /**
     * Constructor
     */

    public Contact (int contactId, String contactName){
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * @return contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @param  contactId to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @return contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    // override format for combo box selection display
    @Override
    public String toString(){
        return (getContactName());
    }
}
