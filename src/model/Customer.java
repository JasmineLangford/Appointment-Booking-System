package model;

/**
 * This class is the model class for customer.
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerCountry;
    private int countryId;
    private int divisionId;
    private String division;
    private String customerPostal;

    /**
     * Constructor
     */
    public Customer(int customerId, String customerName, String customerAddress, String customerPhone, String customerCountry, int countryId, int divisionId, String division, String customerPostal){
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerCountry = customerCountry;
        this.countryId= countryId;
        this.divisionId = divisionId;
        this.division = division;
        this.customerPostal = customerPostal;
    }

    /**
     * Constructor for query of first-level divisions for combo box selection.
     *
     * @param divisionId to get
     * @param division to get
     * @param countryId to get
     */
    public Customer(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * Constructor for query of country selections for combo box.
     *
     * @param countryId to get
     * @param customerCountry to get
     */
    public Customer(int countryId, String customerCountry) {
        this.countryId = countryId;
        this.customerCountry = customerCountry;
    }

    /**
     * @return the custID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @return the customer phone number
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param customerPhone to set
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     *@return the customer country
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     * @param customerCountry to set
     */
    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    /**
     @return the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     @param countryId to set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * @return the division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId to set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * @return the customer postal code
     */
    public String getCustomerPostal() {
        return customerPostal;
    }

    /**
     * @param customerPostal to set
     */
    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }
}
