package Model;

public class Customer {
    private final int cusID; // Customer_ID INT(10) (PK)
    private String cusName; // Customer_Name VARCHAR(50)
    private String cusAddress; // Address VARCHAR(100)
    private String cusPostalCode; // Postal_Code VARCHAR(50)
    private String cusDivision; // (FIRST-LEVEL DIVISIONS table) Division VARCHAR(50)
    private int cusDivisionCode; // Division_ID INT(10) (FK)
    private String cusCountry; // (COUNTRIES) Country VARCHAR(50)
    private int cusCountryCode; // (FIRST-LEVEL DIVISIONS table) Country_ID INT(10) (FK)
    private String cusPhone; // Phone VARCHAR(50)

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

    public int getCusID() {
        return cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusPostalCode() {
        return cusPostalCode;
    }

    public void setCusPostalCode(String cusPostalCode) {
        this.cusPostalCode = cusPostalCode;
    }

    public String getCusDivision() {
        return cusDivision;
    }

    public void setCusDivision(String cusDivision) {
        this.cusDivision = cusDivision;
    }

    public int getCusDivisionCode() {
        return cusDivisionCode;
    }

    public void setCusDivisionCode(int cusDivisionCode) {
        this.cusDivisionCode = cusDivisionCode;
    }

    public String getCusCountry() {
        return cusCountry;
    }

    public void setCusCountry(String cusCountry) {
        this.cusCountry = cusCountry;
    }

    public int getCusCountryCode() {
        return cusCountryCode;
    }

    public void setCusCountryCode(int cusCountryCode) {
        this.cusCountryCode = cusCountryCode;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }
}
