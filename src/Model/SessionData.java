package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public abstract class SessionData {
    private static String userName;
    private static ObservableList<LoginAttempt> loginAttempts;

    /**
     * Records the username of the user for the current session. This is only called when username and password have
     * been validated.
     * @param username is the username successfully used in the login screen.
     */
    public static void setUsernameAndLogs (String username){
        userName = username;
        setLoginAttempts();
    }
    /**
     * Returns username of this session.
     * @return username
     */
    public static String getUsername(){
        return userName;
    }
    //TODO: record login javadoc
    public static void recordLoginAttempt(String userName, boolean successfulLogin){
        try{
            String format = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String timestamp = LocalDateTime.now(ZoneId.of("UTC")).format(formatter);
            FileWriter writer = new FileWriter("login_logs.txt",true);
            writer.write(userName+","+timestamp+" UTC,"+successfulLogin+",");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ObservableList<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }
    private static void setLoginAttempts(){
        loginAttempts = FXCollections.observableArrayList();
        try {
            Scanner read = new Scanner(new File("login_logs.txt"));
            read.useDelimiter(",");
            while (read.hasNext()) {
                String userName = read.next();
                String timestampUTC = read.next();
                String loginSucessful = read.next();
                LoginAttempt loginAttempt= new LoginAttempt(userName,timestampUTC,loginSucessful);
                loginAttempts.add(loginAttempt);
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found to read");
        }
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
     * @param dateAndTimeLocal - a string containing the date and time in local timezone
     * @return a string containing the date and time provided but converted to UTC timezone
     */
    public static String convertLocaltoUTC(String dateAndTimeLocal){
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZoneId fromTimeZone = ZoneId.systemDefault();
        ZoneId toTimeZone = ZoneId.of("UTC");
        LocalDateTime date = LocalDateTime.parse(dateAndTimeLocal, formatter);
        ZonedDateTime originalTime = date.atZone(fromTimeZone);
        ZonedDateTime convertedTime = originalTime.withZoneSameInstant(toTimeZone);
        return convertedTime.format(formatter);
    }
    public static String convertESTtoLocal(String dateAndTimeEST){
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZoneId toTimeZone = ZoneId.systemDefault();
        ZoneId fromTimeZone = ZoneId.of("US/Eastern");
        LocalDateTime date = LocalDateTime.parse(dateAndTimeEST, formatter);
        ZonedDateTime originalTime = date.atZone(fromTimeZone);
        ZonedDateTime convertedTime = originalTime.withZoneSameInstant(toTimeZone);
        return convertedTime.format(formatter);
    }
    public static String currentTimeUTC(){
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String localTimeNow = formatter.format(LocalDateTime.now());
        return convertLocaltoUTC(localTimeNow);
    }
    public static String parseStringComma(String string){
        int i = string.indexOf(",");
        String parsedString = null;
        if (i!=-1) {parsedString= string.substring(0 , i);}
        return parsedString;
    }
    public static String parseCommaString(String string){
        int i = string.indexOf(",");
        String parsedString = null;
        if (i!=-1) {parsedString= string.substring(i+2);}
        return parsedString;
    }
    public static String parseDateFromDateTime(String dateTime){
        return dateTime.substring(0,10);
    }
    public static String parseTimeFromDateTime(String dateTime){
        return dateTime.substring(11);
    }
}
