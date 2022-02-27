package Model;

/**
 * This is a class that holds information on an attempted login.
 */
public class LoginAttempt {
    private final String userName;
    private final String timestampUTC;
    private final String loginSuccessful;

    /**
     * Constructor for a login attempt
     * @param userName - the username provided by the user
     * @param timestampUTC - the time the login attempt was made as displayed in UTC
     * @param loginSuccessful - a boolean indicating if the login was successful
     */
    public LoginAttempt(String userName, String timestampUTC, String loginSuccessful) {
        this.userName = userName;
        this.timestampUTC = timestampUTC;
        this.loginSuccessful = loginSuccessful;
    }
    /**
     * This method is the getter for the username from the login attempt
     * @return - username from the login attempt
     */
    public String getUserName() {
        return userName;
    }
    /**
     * This method is the getter for the timestamp from the login attempt.
     * @return - the timestamp from the login attempt in UTC
     */
    public String getTimestampUTC() {
        return timestampUTC;
    }
    /**
     * This method is the getter for the boolean that indicates if the login attempt was successful or not.
     * @return - boolean that indicates if the login attempt was successful or not
     */
    public String isLoginSucessful() {
        return loginSuccessful;
    }
}
