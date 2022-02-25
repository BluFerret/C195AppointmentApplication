package Model;
//TODO:javadoc
public class LoginAttempt {
    private final String userName;
    private final String timestampUTC;
    private final String loginSucessful;
    //TODO:javadoc
    public LoginAttempt(String userName, String timestampUTC, String loginSucessful) {
        this.userName = userName;
        this.timestampUTC = timestampUTC;
        this.loginSucessful = loginSucessful;
    }
    //TODO:javadoc
    public String getUserName() {
        return userName;
    }
    //TODO:javadoc
    public String getTimestampUTC() {
        return timestampUTC;
    }
    //TODO:javadoc
    public String isLoginSucessful() {
        return loginSucessful;
    }
}
