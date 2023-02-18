package model;
import java.time.LocalDateTime;

/**
 * This class is the model class for appointments.
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;
    private int totalType;

    /**
     * Constructor
     */
    public Appointment(int appointmentID, String title, String description, String location, int contactID, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    // Null appointment
    public Appointment() {
    }

    public Appointment(int appointment_id, LocalDateTime start) {
        this.appointmentID = appointment_id;
        this.start = start;
    }

    public Appointment(int appointmentID, LocalDateTime start, LocalDateTime end) {
        this.appointmentID = appointmentID;
        this.start = start;
        this.end = end;

    }

    public Appointment(int appointmentID, String title, String description, String type, LocalDateTime start,
                       LocalDateTime end, int customerID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
    }

    public Appointment(LocalDateTime start, String type, int totalType) {
        this.start = start;
        this.type = type;
        this.totalType = totalType;
    }

    /**
     * @return the Appointment ID
     */
    public int getAppointmentID(){
        return appointmentID;
    }

    /**
     * @param appointmentID to set
     */
    public void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID to set
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

   /**
    * @return the type
    */
    public String getType() {
        return type;
    }

    /**
     * @param type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return start date and time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start date and time to set
     */
    public void setStart(LocalDateTime start) {
        this.start = start;

    }

    /**
     * @return end date and time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param end date and time to set
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * @return the customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTotalType() {
        return totalType;
    }

    public void setTotalType(int totalType) {
        this.totalType = totalType;
    }
}
