package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.time.DayOfWeek.*;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

/**
 * This class holds relevant session data as well as being the point of conversion of time for the user's session and
 * the parsing of strings formatted to show additional information during the application session.
 */
public abstract class SessionData {
    private static String userName;
    private static ObservableList<LoginAttempt> loginAttempts;

    // ========== Login and Username ==========
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
    /**
     * This method records a login attempt to a text file.
     * @param userName - the username of the login attempt
     * @param successfulLogin - the boolean indicating if the login attempt was successful
     */
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
    /**
     * This method is the getter for the login attempt list.
     * @return - login attempt list in an Observable list
     */
    public static ObservableList<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }
    /**
     * This method is the setter for the login attempt list as pulled from the login_logs text document
     */
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
    // ========== Time Conversion ==========
    /**
     * This method coverts UTC date and time as a string to local date and time as a string.
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
     * This method coverts local date and time as a string to UTC date and time as a string.
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
    /**
     * This method coverts EST date and time as a string to local date and time as a string.
     * Local time is the local time of the JVM.
     * @param dateAndTimeEST - a string containing the date and time in EST timezone
     * @return a string containing the date and time provided but converted to local timezone
     */
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
    // ========== Time - Other ==========
    /**
     * This method provides the current time and date in UTC timezone.
     * @return - current time and date in UTC timezone as a string
     */
    public static String currentTimeUTC(){
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String localTimeNow = formatter.format(LocalDateTime.now());
        return convertLocaltoUTC(localTimeNow);
    }
    /**
     * This method provides the time and date 15 minutes from the current time and date in UTC timezone.
     * @return - time and date 15 minutes from now in UTC timezone as a string
     */
    public static String fifteenMinFromNowUTC(){
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String localTimeNow = formatter.format(LocalDateTime.now().plusMinutes(15));
        return convertLocaltoUTC(localTimeNow);
    }
    /**
     * This method provides the starting date and time of the week as a string.
     * @return the starting date and time of the week as a string
     */
    public static String currentWeekStart(){
        LocalDate today = LocalDate.now();
        LocalDate sunday = today.with(previousOrSame(SUNDAY));
        String sundayString = sunday + " 00:00:00";
        return convertLocaltoUTC(sundayString);
    }
    /**
     * This method provides the ending date and time of the week as a string.
     * @return the ending date and time of the week as a string
     */
    public static String currentWeekEnd(){
        LocalDate today = LocalDate.now();
        LocalDate sunday = today.with(nextOrSame(SUNDAY));
        String sundayString = sunday + " 00:00:00";
        return convertLocaltoUTC(sundayString);
    }
    /**
     * This method provides the current month as an integer.
     * @return - the current month as an integer
     */
    public static int currentMonth(){
        LocalDate today = LocalDate.now();
        return today.getMonthValue();
    }
    // ========== Parsing ==========
    /**
     * This method parses a string from the beginning to a comma delimiter.
     * @param string - the string to be parsed up until the comma
     * @return - the first portion of the provided string until but not including the comma
     */
    public static String parseStringComma(String string){
        int i = string.indexOf(",");
        String parsedString = null;
        if (i!=-1) {parsedString= string.substring(0 , i);}
        return parsedString;
    }
    /**
     * This method parses a string from a comma delimiter till the end of the string, not including a space after the comma.
     * @param string - the string to be parsed after the comma and space delimiter
     * @return - the latter portion of the provided string after a comma and space delimiter
     */
    public static String parseCommaString(String string){
        int i = string.indexOf(",");
        String parsedString = null;
        if (i!=-1) {parsedString= string.substring(i+2);}
        return parsedString;
    }
    /**
     * This method parses the date from a string containing a date and time.
     * @param dateTime - the string containing the date and time
     * @return - the date from the data and time provided, as a string
     */
    public static String parseDateFromDateTime(String dateTime){
        return dateTime.substring(0,10);
    }
}
