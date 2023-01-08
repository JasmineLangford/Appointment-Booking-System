package model;

/**
 * This class is the model class for customer.
 */
public class Customer {
    private int custId;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerCountry;
    private String customerStateProv;
    private String customerPostal;

    /**
     * Constructor
     */
    public Customer(int custId,String customerName, String customerAddress, String customerPostal, String customerPhone, String customerCountry,String customerStateProv){
        this.custId = custId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerCountry = customerCountry;
        this.customerStateProv = customerStateProv;
        this.customerPostal = customerPostal;
    }

    /**
     * @return the custID
     */
    public int getCustId() {
        return custId;
    }

    /**
     * @param custId to set
     */
    public void setCustId(int custId) {
        this.custId = custId;
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
     * @param customerAddress to set
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
     * return the customer state and province
     */
    public String getCustomerStateProv() {
        return customerStateProv;
    }

    /**
     * @param customerStateProv to set
     */
    public void setCustomerStateProv(String customerStateProv) {
        this.customerStateProv = customerStateProv;
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
