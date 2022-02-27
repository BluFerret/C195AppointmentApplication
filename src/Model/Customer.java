package Model;

/**
 * This is a class for holding information about a customer.
 */
public class Customer {
    private int cusID; // Customer_ID INT(10) (PK)
    private String cusName; // Customer_Name VARCHAR(50)
    private String cusAddress; // Address VARCHAR(100)
    private String cusPostalCode; // Postal_Code VARCHAR(50)
    private final String cusDivision; // (FIRST-LEVEL DIVISIONS table) Division VARCHAR(50)
    private int cusDivisionCode; // Division_ID INT(10) (FK)
    private final String cusCountry; // (COUNTRIES) Country VARCHAR(50)
    private int cusCountryCode; // (FIRST-LEVEL DIVISIONS table) Country_ID INT(10) (FK)
    private String cusPhone; // Phone VARCHAR(50)
    private int cusCount;

    /**
     * This method is a constructor for a customer
     * @param cusID - the customer ID.
     * @param cusName - the customer name.
     * @param cusAddress - the customer address
     * @param cusPostalCode - the customer postal code
     * @param cusDivision - the customer's first level division also known as state or province
     * @param cusDivisionCode - the code for the customer's first level division
     * @param cusCountry - the country of the customer
     * @param cusCountryCode - the code for the country of the customer
     * @param cusPhone - the phone number for the customer
     */
    public Customer(int cusID, String cusName, String cusAddress, String cusPostalCode, String cusDivision,
                    int cusDivisionCode, String cusCountry, int cusCountryCode, String cusPhone) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.cusAddress = cusAddress;
        this.cusPostalCode = cusPostalCode;
        this.cusDivision = cusDivision;
        this.cusDivisionCode = cusDivisionCode;
        this.cusCountry = cusCountry;
        this.cusCountryCode = cusCountryCode;
        this.cusPhone = cusPhone;
    }
    /**
     * This is an overloaded constructor for a customer specifically used in report 3 when finding the count of
     * customers by country and by first level division.
     * @param country - the country in the count of customers
     * @param division - the division in the count of customers
     * @param count - the count of customers
     */
    public Customer(String country, String division, int count) {
        this.cusDivision = division;
        this.cusCountry = country;
        this.cusCount = count;
    }
    /**
     * This method is the getter for the customer ID
     * @return - the customer ID
     */
    public int getCusID() {
        return cusID;
    }
    /**
     * This method is the getter for the customer name.
     * @return - the customer name.
     */
    public String getCusName() {
        return cusName;
    }
    /**
     * This method is the getter for the customer address.
     * @return - customer address
     */
    public String getCusAddress() {
        return cusAddress;
    }
    /**
     * This method is the getter for the customer postal code.
     * @return - the customer postal code
     */
    public String getCusPostalCode() {
        return cusPostalCode;
    }
    /**
     * This method is the getter for the customer's first level division, also known as the state or province.
     * @return - the customer's first level division
     */
    public String getCusDivision() {
        return cusDivision;
    }
    /**
     * This method is the getter for the customer's division code.
     * @return - the customer's division code
     */
    public int getCusDivisionCode() {
        return cusDivisionCode;
    }
    /**
     * This method is the getter for the customer's country.
     * @return - the customer's country
     */
    public String getCusCountry() {
        return cusCountry;
    }
    /**
     * This method is the getter for the customer's country code.
     * @return - the customer's country code
     */
    public int getCusCountryCode() {
        return cusCountryCode;
    }
    /**
     * This method is the getter for the customer's phone.
     * @return - the customer's phone
     */
    public String getCusPhone() {
        return cusPhone;
    }
    /**
     * This method is the getter for the count of customers
     * @return - count of customers
     */
    public int getCusCount() {
        return cusCount;
    }
}
