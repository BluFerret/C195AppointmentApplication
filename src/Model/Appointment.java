package Model;

import java.text.SimpleDateFormat;

public class Appointment {
    private int appID; // appointment ID - Appointment_ID INT(10) (PK)
    private String appTitle; // appointment title - Title VARCHAR(50)
    private String appDesc; // appointment description - Description VARCHAR(50)
    private String appLocation; // appointment location - Location VARCHAR(50)
    private String appType; // appointment type - Type VARCHAR(50)
    private String appStartDateTime; // appointment start date and time - Start DATETIME
    private String appEndDateTime; // appointment end date and time - End DATETIME
    private String appMonth; // MONTH FROM appStartDateTime
    private int appCustomerID; // appointment customer ID - Customer_ID INT(10) (FK)
    private int appUserID; // appointment user ID - User_ID INT(10) (FK)
    private String appContact; // appointment contact name - CONTACTS TABLE Contact_Name VARCHAR(50) joined on Contact_ID INT(10)
    private int appCount; // Count of appointments for reports

    public Appointment(int appID, String appTitle, String appDesc, String appLocation, String appType,
                       String appStartDateTime, String appEndDateTime, int appCustomerID, int appUserID,
                       String appContact) {
        this.appID = appID;
        this.appTitle = appTitle;
        this.appDesc = appDesc;
        this.appLocation = appLocation;
        this.appType = appType;
        this.appStartDateTime = SessionData.convertUTCtoLocal(appStartDateTime);
        this.appEndDateTime = SessionData.convertUTCtoLocal(appEndDateTime);
        this.appCustomerID = appCustomerID;
        this.appUserID = appUserID;
        this.appContact = appContact;
    }

    public Appointment(String appType, int appMonth, int appCount) {
        this.appType = appType;
        this.appMonth = new SimpleDateFormat("MMMM").format(appMonth-1);
        this.appCount = appCount;
    }

    public int getAppID() {
        return appID;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppLocation() {
        return appLocation;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppStartDateTime() {
        return appStartDateTime;
    }

    public void setAppStartDateTime(String appStartDateTime) {
        this.appStartDateTime = appStartDateTime;
    }

    public String getAppEndDateTime() {
        return appEndDateTime;
    }

    public void setAppEndDateTime(String appEndDateTime) {
        this.appEndDateTime = appEndDateTime;
    }

    public String getAppMonth() {
        return appMonth;
    }

    public int getAppCustomerID() {
        return appCustomerID;
    }

    public void setAppCustomerID(int appCustomerID) {
        this.appCustomerID = appCustomerID;
    }

    public int getAppUserID() {
        return appUserID;
    }

    public void setAppUserID(int appUserID) {
        this.appUserID = appUserID;
    }

    public String getAppContact() {
        return appContact;
    }

    public void setAppContact(String appContact) {
        this.appContact = appContact;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }
}
