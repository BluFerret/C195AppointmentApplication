package Model;

public class LoginAttempt {
    private final String userName;
    private final String timestampUTC;
    private final String loginSucessful;

    public LoginAttempt(String userName, String timestampUTC, String loginSucessful) {
        this.userName = userName;
        this.timestampUTC = timestampUTC;
        this.loginSucessful = loginSucessful;
    }

    public String getUserName() {
        return userName;
    }

    public String getTimestampUTC() {
        return timestampUTC;
    }

    public String isLoginSucessful() {
        return loginSucessful;
    }
}
