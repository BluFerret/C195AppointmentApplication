package Model;

//TODO:javadoc
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
    //TODO:javadoc
    public Customer(int cusID, String cusName, String cusAddress, String cusPostalCode, String cusDivision, int cusDivisionCode, String cusCountry, int cusCountryCode, String cusPhone) {
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
    //TODO:javadoc
    public Customer(String country, String division, int count) {
        this.cusDivision = division;
        this.cusCountry = country;
        this.cusCount = count;
    }
    //TODO:javadoc
    public int getCusID() {
        return cusID;
    }
    //TODO:javadoc
    public String getCusName() {
        return cusName;
    }
    //TODO:javadoc
    public String getCusAddress() {
        return cusAddress;
    }
    //TODO:javadoc
    public String getCusPostalCode() {
        return cusPostalCode;
    }
    //TODO:javadoc
    public String getCusDivision() {
        return cusDivision;
    }
    //TODO:javadoc
    public int getCusDivisionCode() {
        return cusDivisionCode;
    }
    //TODO:javadoc
    public String getCusCountry() {
        return cusCountry;
    }
    //TODO:javadoc
    public int getCusCountryCode() {
        return cusCountryCode;
    }
    //TODO:javadoc
    public String getCusPhone() {
        return cusPhone;
    }
    //TODO:javadoc
    public int getCusCount() {
        return cusCount;
    }

}
