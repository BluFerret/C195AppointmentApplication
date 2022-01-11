package Model;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * Coverts UTC date and time as a string to local date and time as a string.
     * Local time is the local time of the JVM.
     * @param dateAndTimeUTC - a string containing the date and time in UTC timezone
     * @return a string containing the date and time provided but converted to the local timezone
     */
    public static String convertUTCtoLocal(String dateAndTimeUTC){
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZoneId toTimeZone = ZoneId.systemDefault();
        ZoneId fromTimeZone = ZoneId.of("UTC");
        LocalDateTime date = LocalDateTime.parse(dateAndTimeUTC, formatter);
        ZonedDateTime originalTime = date.atZone(fromTimeZone);
        ZonedDateTime convertedTime = originalTime.withZoneSameInstant(toTimeZone);
        return convertedTime.format(formatter);
    }

    /**
     * Coverts local date and time as a string to UTC date and time as a string.
     * Local time is the local time of the JVM.
     * @param dateAndTimeUTC - a string containing the date and time in local timezone
     * @return a string containing the date and time provided but converted to UTC timezone
     */
    public static String convertLocaltoUTC(String dateAndTimeUTC){
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZoneId fromTimeZone = ZoneId.systemDefault();
        ZoneId toTimeZone = ZoneId.of("UTC");
        LocalDateTime date = LocalDateTime.parse(dateAndTimeUTC, formatter);
        ZonedDateTime originalTime = date.atZone(fromTimeZone);
        ZonedDateTime convertedTime = originalTime.withZoneSameInstant(toTimeZone);
        return convertedTime.format(formatter);
    }
//    public static boolean validateTime(String utcTime){
//        String format = "yyyy-MM-dd HH:mm:ss";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
//        ZoneId fromTimeZone = ZoneId.of("UTC");
//        ZoneId toTimeZone = ZoneId.of("America/New_York");
//        LocalDateTime date = LocalDateTime.parse(utcTime, formatter);
//        ZonedDateTime originalTime = date.atZone(fromTimeZone);
//        ZonedDateTime convertedTime = originalTime.withZoneSameInstant(toTimeZone);
//        String hour = new SimpleDateFormat("H:mm").format(convertedTime);
//
//    }
}
