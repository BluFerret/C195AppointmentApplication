package DAO;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String accessUserName = "sqlUser";
    private static String accessPassword = "Passw0rd!";
    public static Connection conn;

    public static void openConnection(){
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, accessUserName, accessPassword);
        }
        catch(Exception e)
        {
            System.out.println("Error connecting to database:" + e.getMessage());
        }
    }

    public static void closeConnection(){
        try {
            conn.close();
        }
        catch(Exception e)
        {
            System.out.println("Error disconnecting from database:" + e.getMessage());
        }
    }

    /**
     * This method checks the provided username and password verses the usernames and associated passwords in the database.
     * @param usernameAttempt - the username provided by the user on the login form
     * @param passwordAttempt - the password provided by the user on the login form
     * @return a boolean pertaining to the validity of the username and associated password provided by the user
     * compared to the database.
     * @throws SQLException from SQL executeQuery of Statement
     */
    public static boolean validateUsernameAndPassword(String usernameAttempt, String passwordAttempt) throws SQLException {
        Statement statement = conn.createStatement() ;
        String q = "SELECT User_Name, password FROM client_schedule.users WHERE User_Name = \""+usernameAttempt+"\";";
        ResultSet rs = statement.executeQuery(q);
        if(!rs.next()){
            rs.close();
            return false;}
        else {
            String temp = rs.getString("password");
            rs.close();
            return Objects.equals(temp, passwordAttempt);
        }
    }

    public static ObservableList<Appointment> listOfAppointments() throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        Statement statement = conn.createStatement() ;
        String q = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, " +
                "Contact_Name FROM appointments JOIN contacts ON contacts.Contact_ID = appointments.Contact_ID;";
        ResultSet rs = statement.executeQuery(q);
        while(rs.next()){
            Appointment appointment =  new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"),rs.getString("Type"),
                    rs.getString("Start"),rs.getString("End"),rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),rs.getString("Contact_Name"));
            System.out.println(appointment.getAppID());
            list.add(appointment);
        }
        return list;
    }

}
