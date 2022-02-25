package Model;

import java.time.Month;

/**
 * This is a class for holding information about an appointment.
 */
public class Appointment {
    private int appID; // appointment ID - Appointment_ID INT(10) (PK)
    private String appTitle; // appointment title - Title VARCHAR(50)
    private String appDesc; // appointment description - Description VARCHAR(50)
    private String appLocation; // appointment location - Location VARCHAR(50)
    private final String appType; // appointment type - Type VARCHAR(50)
    private String appStartDateTime; // appointment start date and time - Start DATETIME
    private String appEndDateTime; // appointment end date and time - End DATETIME
    private String appMonth; // MONTH FROM appStartDateTime
    private int appCustomerID; // appointment customer ID - Customer_ID INT(10) (FK)
    private int appUserID; // appointment user ID - User_ID INT(10) (FK)
    private String appContact; // appointment contact name - CONTACTS TABLE Contact_Name VARCHAR(50) joined on Contact_ID INT(10)
    private int appCount; // Count of appointments for reports

    /**
     * This method is a constructor for an appointment.
     * @param appID - the appointment ID
     * @param appTitle - the appointment title
     * @param appDesc - the appointment description
     * @param appLocation - the appointment location
     * @param appType - the appointment type
     * @param appStartDateTime - the appointment start date and time in String format
     * @param appEndDateTime - the appointment end date and time in String format
     * @param appCustomerID - the Customer ID of the Customer associated with this appointment instance
     * @param appUserID - the User ID of the User associated with this appointment instance
     * @param appContact - the Contact associated with this appointment instance
     */
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
    /**
     * This method is an alternative, overloaded constructor for an appointment object, specifically this appointment
     * is the count of appointments, grouped by type and month, used in Report 1.
     * @param appType - the type of appointments
     * @param appMonth - The month of the appointments
     * @param appCount - the count of the appointments
     */
    public Appointment(String appType, int appMonth, int appCount) {
        this.appType = appType;
        this.appMonth = Month.of(appMonth).toString();
        this.appCount = appCount;
    }
    /**
     * This method is the getter for the appointment ID.
     * @return appID (the appointment ID)
     */
    public int getAppID() {
        return appID;
    }
    /**
     * This method is the getter for the appointment title.
     * @return appTitle (the appointment title)
     */
    public String getAppTitle() {
        return appTitle;
    }
    /**
     * This method is the getter for the appointment description.
     * @return appDesc (the appointment description)
     */
    public String getAppDesc() {
        return appDesc;
    }
    /**
     * This method is the getter for the appointment location.
     * @return appLocation (the appointment location)
     */
    public String getAppLocation() {
        return appLocation;
    }
    /**
     * This method is the getter for the appointment type.
     * @return appType (the appointment type)
     */
    public String getAppType() {
        return appType;
    }
    /**
     * This method is the getter for the appointment start date and time.
     * @return appStartDateTime (the appointment start date and time)
     */
    public String getAppStartDateTime() {
        return appStartDateTime;
    }
    /**
     * This method is the getter for the appointment end date and time.
     * @return appEndDateTime (the appointment end date and time)
     */
    public String getAppEndDateTime() {
        return appEndDateTime;
    }
    /**
     * This method is the getter for the appointment customer ID.
     * @return appCustomerID (the appointment customer ID)
     */
    public int getAppCustomerID() {
        return appCustomerID;
    }
    /**
     * This method is the getter for the appointment user ID.
     * @return appUserID (the appointment user ID)
     */
    public int getAppUserID() {
        return appUserID;
    }
    /**
     * This method is the getter for the appointment contact's name.
     * @return appContact (the appointment contact's name)
     */
    public String getAppContact() {
        return appContact;
    }
    /**
     * This method is the getter for the count of appointments when grouped by type and by month.
     * @return appCount (the count of appointments when grouped by type and by month)
     */
    public int getAppCount() {
        return appCount;
    }
    /**
     * This method is the getter for the month of the appointment.
     * @return appMonth (the month of the appointment)
     */
    public String getAppMonth() {
        return appMonth;
    }
}
