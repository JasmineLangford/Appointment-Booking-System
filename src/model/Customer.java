package model;

/**
 * This class represents a customer.
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
    private int totalCustomers;
    private String customerType;


    // override format for combo box selection display
    @Override
    public String toString() {
        return (getCustomerName());
    }

    /**
     * This constructor represents all customer data fields.
     *
     * @param customerId      Auto-incremented ID from the database.
     * @param customerName    The customer's first and last name.
     * @param customerAddress The customer's address.
     * @param customerPhone   The customer's phone number.
     * @param customerCountry The customer's country name.
     * @param countryId       The int related to the customer country.
     * @param divisionId      The int related to the customer state or province.
     * @param division        The customer's state or province name.
     * @param customerPostal  The customer's postal code.
     */
    public Customer(int customerId, String customerName, String customerAddress, String customerPhone, String
            customerCountry, int countryId, int divisionId, String division, String customerPostal) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerCountry = customerCountry;
        this.countryId = countryId;
        this.divisionId = divisionId;
        this.division = division;
        this.customerPostal = customerPostal;
    }

    /**
     * This is the constructor representing customer's state or province and the total customers calculated.
     * This is used for reporting.
     *
     * @param divisionId     The int related to the customer's state or province.
     * @param division       The customer's state or province name.
     * @param countryId      The int related to the customer country.
     * @param totalCustomers The sum of customers in the same state or province.
     */
    public Customer(int divisionId, String division, int countryId, int totalCustomers) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.totalCustomers = totalCustomers;
    }

    /**
     * This is the constructor representing the customer's country.
     * Used for country combo box selection.
     *
     * @param countryId       The int related to the customer country.
     * @param customerCountry The customer's country name.
     */
    public Customer(int countryId, String customerCountry) {
        this.countryId = countryId;
        this.customerCountry = customerCountry;
    }

    /**
     * This is the constructor representing customer's state or province.
     * This is used for the state/province combo box selection.
     *
     * @param divisionId The int related to the customer's state or province.
     * @param division   The customer's state or province name.
     * @param countryId  The int related to the customer country.
     */
    public Customer(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * Null method for customer.
     */
    public Customer() {
    }


    public static class RegularCustomer extends Customer {
        private int loyaltyPoints;
        public RegularCustomer(int customerId, String customerName, String customerAddress, String customerPhone, String
            customerCountry, int countryId, int divisionId, String division, String customerPostal, int loyaltyPoints) {
                super(customerId, customerName, customerAddress, customerPhone, customerCountry, countryId, divisionId,
                        division, customerPostal);
                this.loyaltyPoints = loyaltyPoints;
        }

        public RegularCustomer() {

        }

        public int getLoyaltyPoints() {
            return loyaltyPoints;
        }

        public void setLoyaltyPoints(int loyaltyPoints) {
            this.loyaltyPoints = loyaltyPoints;
        }
    }

    public static class CorporateAccount extends Customer {
        private String company;
        public CorporateAccount(int customerId, String customerName, String customerAddress, String customerPhone, String
                customerCountry, int countryId, int divisionId, String division, String customerPostal) {
            super(customerId, customerName, customerAddress, customerPhone, customerCountry, countryId, divisionId,
                    division, customerPostal);
        }

        public CorporateAccount() {
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }
    /**
     * Gets the customer type.
     *
     * @return A string representing the customer type.
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the customer type.
     *
     * @param customerType A string containing the customer type.
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    /**
     * Gets the customer ID.
     *
     * @return An integer representing the customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId An integer containing the customer ID.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the customer name.
     *
     * @return A string representing the customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     *
     * @param customerName A string containing the customer name.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the customer address.
     *
     * @return A string representing the customer address.
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Sets the customer address.
     *
     * @param customerAddress A string containing the customer address.
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Gets the customer phone number.
     *
     * @return A string representing customer phone number.
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * Sets the customer phone number.
     *
     * @param customerPhone A string containing the customer phone number.
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * Gets the customer country.
     *
     * @return A string representing the customer country name.
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     * Sets the customer country.
     *
     * @param customerCountry A string containing customer country name.
     */
    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    /**
     * Gets the country ID.
     *
     * @return An integer representing the customer country.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country ID.
     *
     * @param countryId An integer containing the customer country.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Gets the division ID.
     *
     * @return An integer representing the customer state/province.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division ID.
     *
     * @param divisionId An integer containing the customer state/province.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets the customer division name.
     *
     * @return A string representing the customer state/province name.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the customer division name.
     *
     * @param division A string containing the customer state/province name.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the customer postal code.
     *
     * @return A string representing the customer postal code.
     */
    public String getCustomerPostal() {
        return customerPostal;
    }

    /**
     * Sets the customer postal code.
     *
     * @param customerPostal A string containing the customer postal code.
     */
    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }

    /**
     * Gets the sum of customers.
     *
     * @return An integer representing the sum of customers.
     */
    public int getTotalCustomers() {
        return totalCustomers;
    }

    /**
     * Sets the sum of customers.
     *
     * @param totalCustomers An integer containing the sum of customers.
     */
    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;

    }
}
