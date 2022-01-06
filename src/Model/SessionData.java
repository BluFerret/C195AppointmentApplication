package Model;

import java.util.Date;

public abstract class SessionData {
    private static String userName;

    /**
     * Records the username of the user for the current session. This is only called when username and password have
     * been validated.
     * @param username is the username successfully used in the login screen.
     */

    public static void setUsername (String username){
        userName = username;
    }

    /**
     * Returns username of this session.
     * @return username
     */
    public static String getUsername(){
        return userName;
    }
    public static void recordLoginAttempt(String userName, boolean successfulLogin){
        System.out.println(userName + ", " + successfulLogin);
    }


}
