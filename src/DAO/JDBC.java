package DAO;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

import static javax.swing.UIManager.getInt;


/**
 * This Java Database Connection class connects to the local database and sends queries to the database to
 * create, read, update, and delete data. The methods for opening and closing the database connection are paraphrased
 * from code snippets from WGU course C195 code repository.
 */
//TODO: Make sure result sets are all closed on competing any queries.
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String accessUserName = "sqlUser";
    public static Connection conn;

    // ========== Database Connection and App Login ==========
    /**
     * Opens connection to the database using attributes locally saved in this class.
     */
    public static void openConnection(){
        try {
            Class.forName(driver);
            String accessPassword = "Passw0rd!";
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
     * @throws SQLException from the SQL executeQuery of the statement via database connection - exception handled in Login
     */
    public static boolean validateUsernameAndPassword(String usernameAttempt, String passwordAttempt) throws SQLException {
            Statement statement = conn.createStatement();
            String q = "SELECT COUNT(*) FROM client_schedule.users WHERE User_Name = \"" + usernameAttempt + "\" " +
                    "AND Password = \"" + passwordAttempt + "\";";
            ResultSet rs = statement.executeQuery(q);
            if (rs.next() && rs.getInt("COUNT(*)")==1) {
                rs.close();
                return true;
            }
            rs.close();
            return false;
    }

    // ========== Appointment SQL Queries ==========
    /**
     * This method queries a list of all appointments.
     * @return an observable list of appointments with the specifications of the query
     * @throws SQLException from the SQL executeQuery of the statement via database connection - exception handled in ApplicationMain
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
     * @throws SQLException from the SQL executeQuery of the statement via database connection - exception handled in ApplicationMain
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
     * This method executes a query to see if an appointment already exists for a certain customer within the time frame provided.
     * @param start - the start date and time provided to be checked, date and time provided in UTC timezone.
     * @param end - the end date and time provided to be checked, date and time provided in UTC timezone.
     * @param customerID - the ID number of the customer provided, should be supplied stored as a string.
     * @return a boolean indicating if an appointment already exists for the customer ID provided that overlaps with the start and end dates provided.
     */
    public static boolean appointmentsExistWithinTimeFrameForCustomer(String start, String end, String customerID){
        boolean result = true;
        try{
            Statement statement = conn.createStatement() ;
            String q = "SELECT COUNT(*) FROM appointments WHERE Customer_ID = "+customerID+" AND (\""+start+"\" <= End) AND (Start <= \""+end+"\");";
            ResultSet rs = statement.executeQuery(q);
            while(rs.next()){
                result = rs.getInt("COUNT(*)")!=0;}
                rs.close();
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
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
     */
    public static boolean addNewAppointment(String title, String desc, String location, String type, String start, String end, String customerID,
                                            String userID, String contactID) {
        try {
        Statement statement = conn.createStatement() ;
        String currentTime = SessionData.currentTimeUTC();
        String q = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, " +
                "Contact_ID) VALUES (\"" + title + "\", \"" + desc+"\", \"" + location + "\", \"" + type +
                "\", \"" + start + "\", " + "\"" + end + "\", \""+currentTime+"\", \"JDBC\", \""+currentTime+"\", \"JDBC\", " +
                customerID + ", " + userID + ", " + contactID+");";
            statement.executeUpdate(q);}
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean updateAppointment(int appID, String title, String desc, String location, String type, String start, String end,
                                            String customerID, String userID, String contactID) {
        try {
            Statement statement = conn.createStatement();
            String currentTime = SessionData.currentTimeUTC();
            String q = "UPDATE appointments SET Title = \""+title+"\", Description = \""+desc+"\", " +
                    "Location = \""+location+"\", Type = \""+type+"\", Customer_ID = "+customerID+
                    ", Start = \""+start+"\", End = \""+end+"\", User_ID = "+userID+", Contact_ID = "+contactID+
                    ", Last_Update = \""+currentTime+"\", Last_Updated_By = \"JDBC\" " +
                    "WHERE Appointment_ID = "+appID+";";
            statement.executeUpdate(q);}
        catch(Exception e){
            e.printStackTrace();
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
            statement.executeUpdate(q);}
        catch(Exception e){
            return false;
        }
        return true;
    }

    // ========== Customer SQL Queries ==========
    /**
     * This method executes a query to pull a list of customers and their associated information.
     * @return an ObservableList containing customers.
     * @throws SQLException - from the SQL executeQuery of the statement via database connection - exception handled in ApplicationMain
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
    //TODO: Javadoc note
    public static ObservableList<String> listOfCustomerNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            Statement statement = conn.createStatement();
            String q = "SELECT Customer_Name, Customer_ID FROM client_schedule.customers;";
            ResultSet rs = statement.executeQuery(q);
            while (rs.next()) {
                String customer = rs.getString("Customer_Name")+", "+rs.getInt("Customer_ID");
                list.add(customer);
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    //TODO: finish this query - still appointments
    public static boolean addNewCustomer(String name, String address, String postalCode, String phone, int division) {
        try {
            Statement statement = conn.createStatement();
            String currentTime = SessionData.currentTimeUTC();
            String q = "INSERT into customers (Customer_Name, Address, Postal_Code, Phone, Division_ID, Create_Date, " +
                    "Created_By, Last_Update, Last_Updated_By)"+
                    "values(\""+name+"\",\""+address+"\",\""+postalCode+"\",\""+phone+"\","+division+",\""+currentTime+"\", " +
                    "\"JDBC connection\", \""+currentTime+"\", \"JDBC connection\");";
            statement.executeUpdate(q);}
        catch(Exception e){
            e.printStackTrace();
                return false;
        }
        return true;
    }
    public static boolean updateCustomer(int customerID, String name, String address, String postalCode, String phone, int division) {
        try {
            Statement statement = conn.createStatement();
            String currentTime = SessionData.currentTimeUTC();
            String q = "UPDATE customers SET Customer_Name = \""+name+"\", Address = \""+address+"\", " +
                    "Postal_Code = \""+postalCode+"\", Phone = \""+phone+"\", Division_ID = "+division+
                    ", Last_Update = \""+currentTime+"\", Last_Updated_By = \"JDBC connection\" " +
                    "WHERE Customer_ID = "+customerID+";";
            statement.executeUpdate(q);}
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //TODO: Javadoc note
    public static boolean deleteCustomer(int customerID){
        try{Statement statement = conn.createStatement() ;
        String q = "DELETE FROM customers WHERE Customer_ID ="+customerID+";";
        statement.executeUpdate(q); }
        catch (SQLException throwables) {
            return false;
        }
        return true;
    }
    //TODO: Javadoc note
    public static boolean customerHasAppointmentsCheck(int customerID) {
        try {
            Statement statement = conn.createStatement();
            String q = "SELECT Customer_ID FROM appointments WHERE Customer_ID = " + customerID + ";";
            ResultSet rs = statement.executeQuery(q);
            if (!rs.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    // ========== Location SQL Queries ==========
    /**
     * This method executes a query to pull a list of all countries and their IDs
     * @return an ObservableList containing all countries and their IDs.
     */
    public static ObservableList<String> listOfCountries(){
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            Statement statement = conn.createStatement();
            String q = "SELECT Country, Country_ID from client_schedule.countries;";
            ResultSet rs = statement.executeQuery(q);
            while (rs.next()) {
                String country = rs.getString("Country")+", "+rs.getInt("Country_ID");
                list.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    /**
     *This method executes a query to pull a list of all first level divisions and their IDs
     * @return an ObservableList containing all first level divisions and their IDs.
     */
    public static ObservableList<String> listOfFirstLvlDivisions(String countryID){
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            Statement statement = conn.createStatement();
            String q = "SELECT Division, Division_ID from client_schedule.first_level_divisions WHERE Country_ID = "+countryID+";";
            ResultSet rs = statement.executeQuery(q);
            while (rs.next()) {
                String division = rs.getString("Division")+", "+rs.getInt("Division_ID");
                list.add(division);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    // ========== Report SQL Queries ==========
    //TODO: Javadoc note
    public static ObservableList<Appointment> report1AppointmentsByMonthAndType() {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        try {
            Statement statement = conn.createStatement();
            String q = "SELECT Type, extract(MONTH FROM Start) AS Month ,count(*) AS Appointments FROM " +
                    "client_schedule.appointments group by Type, Month(Start);";
            ResultSet rs = statement.executeQuery(q);
            while (rs.next()) {
                Appointment appointment = new Appointment(rs.getString("Type"), rs.getInt("Month"),
                        rs.getInt("Appointments"));
                list.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    //TODO: Javadoc note
    public static ObservableList<Appointment> report2ScheduleByContact(String contactName){
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        try{
        Statement statement = conn.createStatement() ;
        String q = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, users.User_ID, " +
                    "Contact_Name FROM appointments JOIN contacts ON contacts.Contact_ID = appointments.Contact_ID JOIN " +
                    "users ON users.User_ID = appointments.User_ID WHERE Contact_Name = \""+contactName+"\";";
        ResultSet rs = statement.executeQuery(q);
            while(rs.next()){
                Appointment appointment =  new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getString("Description"), rs.getString("Location"),rs.getString("Type"),
                        rs.getString("Start"),rs.getString("End"),rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),rs.getString("Contact_Name"));
                list.add(appointment);
            }
        }
        catch (SQLException t) {
            t.printStackTrace();
        }
        return list;
    }

    // ========== Miscellaneous SQL Queries ==========
    /**
     * This method executes a query for a list of Contact names and IDs.
     * @return an ObservableList containing all contact names and IDs concatenated with a delimiter of a comma and a space.
     */
    public static ObservableList<String> listOfContactNames(){
        ObservableList<String> list = FXCollections.observableArrayList();
        try{
            Statement statement = conn.createStatement() ;
            String q = "SELECT Contact_Name, Contact_ID FROM contacts;";
            ResultSet rs = statement.executeQuery(q);
            while(rs.next()){
                String contact =  rs.getString("Contact_Name") + ", "+rs.getInt("Contact_ID");
                list.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    /**
     * This method executes a query for a list of Usernames and IDs.
     * @return an ObservableList containing all Usernames and IDs concatenated with a delimiter of a comma and a space.
     */
    public static ObservableList<String> listOfUserNames(){
        ObservableList<String> list = FXCollections.observableArrayList();
        try{
            Statement statement = conn.createStatement() ;
            String q = "SELECT User_Name, User_ID FROM users;";
            ResultSet rs = statement.executeQuery(q);
            while(rs.next()){
                String contact =  rs.getString("User_Name") + ", "+rs.getInt("User_ID");
                list.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
}
