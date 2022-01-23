package DAO;
import Model.Appointment;
import Model.Contact;
import Model.Country;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
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

    // ========== Database Connection and App Login ==========
    /**
     * Opens connection to the database using attributes locally saved in this class.
     */
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
    /**
     * Closes the connection to the database.
     */
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
     * @throws SQLException from the SQL executeQuery of the statement via database connection
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

    // ========== Appointment SQL Queries ==========
    /**
     * This method queries a list of all appointments.
     * @return an observable list of appointments with the specifications of the query
     * @throws SQLException from the SQL executeQuery of the statement via database connection
     */
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
            list.add(appointment);
        }
        return list;
    }
    /**
     * This method queries a list of appointments for a specific username.
     * @param userName - the username being used in the query
     * @return an observable list of appointments with the specifications of the query
     * @throws SQLException from the SQL executeQuery of the statement via database connection
     */
    public static ObservableList<Appointment> listOfAppointments(String userName) throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        Statement statement = conn.createStatement() ;
        String q = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, users.User_ID, " +
                "Contact_Name FROM appointments JOIN contacts ON contacts.Contact_ID = appointments.Contact_ID JOIN " +
                "users ON users.User_ID = appointments.User_ID WHERE users.User_Name = \""+userName+"\";";
        ResultSet rs = statement.executeQuery(q);
        while(rs.next()){
            Appointment appointment =  new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"),rs.getString("Type"),
                    rs.getString("Start"),rs.getString("End"),rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),rs.getString("Contact_Name"));
            list.add(appointment);
        }
        return list;
    }
    /**
     * This method executes a query to add a new appointment. Appointments will need to be validated before this
     * method is called.
     * @param title - title of appointment
     * @param desc - description of appointment
     * @param location - location of appointment
     * @param type - type of appointment
     * @param start - start date and time of appointment
     * @param end - end date and time of appointment
     * @param customerID - customer ID associated with appointment
     * @param userID - user ID associated with appointment
     * @param contactID - contact ID associated with appointment
     * @return boolean indicating if new appointment was added correctly
     * @throws SQLException - from the SQL executeQuery of the statement via database connection
     */
    public static boolean addNewAppointment(String title, String desc, String location, String type, String start, String end, int customerID, int userID, int contactID) throws SQLException {
        try {
        Statement statement = conn.createStatement() ;
        String q = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, " +
                "Contact_ID) VALUES (\"" + title + "\", \"" + desc+"\", \"" + location + "\", \"" + type +
                "\", \"" + start + "\", " + "\"" + end + "\", NOW(), \"JDBC connection\", NOW(), \"JDBC connection\", " +
                customerID + ", " + userID + ", " + contactID+");";
        statement.executeQuery(q);}
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }
    /**
     * This method executes a query to remove an appointment.
     * @param appointmentID = The ID number of the appointment to be removed.
     * @return - a boolean indicating whether or not the appointment was removed.
     */
    public static boolean deleteAppointment(int appointmentID){
        try {
            Statement statement = conn.createStatement() ;
            String q = "DELETE FROM appointments WHERE Appointment_ID = "+appointmentID+";";
            statement.executeQuery(q);}
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    // ========== Customer SQL Queries ==========
    /**
     * This method executes a query to pull a list of customers and their associated information.
     * @return an ObservableList containing customers.
     * @throws SQLException - from the SQL executeQuery of the statement via database connection
     */
    public static ObservableList<Customer> listOfCustomers()throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        Statement statement = conn.createStatement() ;
        String q = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, first_level_divisions.Division, " +
                "first_level_divisions.Division_ID, countries.Country, countries.Country_ID, Phone FROM customers " +
                "JOIN first_level_divisions ON first_level_divisions.Division_ID = customers.Division_ID JOIN countries " +
                "ON first_level_divisions.Country_ID = countries.Country_ID;";
        ResultSet rs = statement.executeQuery(q);
        while(rs.next()){
            Customer customer =  new Customer(rs.getInt("Customer_ID"),rs.getString("Customer_Name"),
                    rs.getString("Address"),rs.getString("Postal_Code"),
                    rs.getString("Division"),rs.getInt("Division_ID"),
                    rs.getString("Country"),rs.getInt("Country_ID"),rs.getString("Phone"));
            list.add(customer);
        }
        return list;
    }
    /**
     * This method executes a query to pull a list of all contacts and their IDs/
     * @return an ObservableList containing all contacts and their IDs.
     * @throws SQLException - from the SQL executeQuery of the statement via database connection
     */
    public static ObservableList<Contact> listOfContacts()throws SQLException{
        ObservableList<Contact> list = FXCollections.observableArrayList();
        Statement statement = conn.createStatement() ;
        String q = "SELECT Contact_Name, Contact_ID FROM contacts;";
        ResultSet rs = statement.executeQuery(q);
        while(rs.next()){
            Contact contact =  new Contact(rs.getString("Contact_Name"),rs.getInt("Contact_ID"));
            list.add(contact);
        }
        return list;
    }
    public static ObservableList<Country> listOfCountries()throws SQLException{
        ObservableList<Country> list = FXCollections.observableArrayList();
        Statement statement = conn.createStatement() ;
        String q = "SELECT Country, Country_ID from countries;";
        ResultSet rs = statement.executeQuery(q);
        while(rs.next()){
            Country country =  new Country(rs.getString("Country"),rs.getInt("Country_ID"));
            list.add(country);
        }
        return list;
    }
    public static ObservableList<Country> listOfFirstLvlDivisions()throws SQLException{
        ObservableList<Country> list = FXCollections.observableArrayList();
        Statement statement = conn.createStatement() ;
        String q = "SELECT Country, Country_ID from countries;";
        ResultSet rs = statement.executeQuery(q);
        while(rs.next()){
            Country country =  new Country(rs.getString("Country"),rs.getInt("Country_ID"));
            list.add(country);
        }
        return list;
    }
}
