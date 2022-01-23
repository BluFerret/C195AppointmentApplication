package Controller;

import DAO.JDBC;
import Model.Appointment;
import Model.Customer;
import Model.SessionData;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static DAO.JDBC.listOfAppointments;

public class ApplicationMain implements Initializable {
    @FXML private Label errorMessage;
    @FXML private Label dashUsername;
    @FXML private ToggleGroup AppointmentTab;
    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment,Integer> cAppID;
    @FXML private TableColumn<Appointment,String> cAppTitle;
    @FXML private TableColumn<Appointment,String> cAppDesc;
    @FXML private TableColumn<Appointment,String> cAppLoc;
    @FXML private TableColumn<Appointment,String> cAppContact;
    @FXML private TableColumn<Appointment,String> cAppType;
    @FXML private TableColumn<Appointment,String> cAppStartDT;
    @FXML private TableColumn<Appointment,String> cAppEndDT;
    @FXML private TableColumn<Appointment,Integer> cAppCustomerID;
    @FXML private TableColumn<Appointment,Integer> cAppUserID;
    public ObservableList<Appointment> appointmentList;
    @FXML private TableView<Appointment> dashAppointmentTableView;
    @FXML private TableColumn<Appointment,Integer> dAppID;
    @FXML private TableColumn<Appointment,String> dAppTitle;
    @FXML private TableColumn<Appointment,String> dAppDesc;
    @FXML private TableColumn<Appointment,String> dAppLoc;
    @FXML private TableColumn<Appointment,String> dAppContact;
    @FXML private TableColumn<Appointment,String> dAppType;
    @FXML private TableColumn<Appointment,String> dAppStartDT;
    @FXML private TableColumn<Appointment,String> dAppEndDT;
    @FXML private TableColumn<Appointment,Integer> dAppCustomerID;
    @FXML private TableColumn<Appointment,Integer> dAppUserID;
    public ObservableList<Appointment> dashAppointmentList;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer,Integer> custID;
    @FXML private TableColumn<Customer,String> custName;
    @FXML private TableColumn<Customer,String> custPhone;
    @FXML private TableColumn<Customer,String> custAddress;
    @FXML private TableColumn<Customer,String> custZip;
    @FXML private TableColumn<Customer,String> custDiv;
    @FXML private TableColumn<Customer,Integer> custDivID;
    @FXML private TableColumn<Customer,String> custCountry;
    @FXML private TableColumn<Customer,Integer> custCountryID;
    public ObservableList<Customer> customerList;

    // TODO: Customer records and appointments can be added, updated, and deleted. When deleting a customer record,
    //  all of the customer’s appointments must be deleted first, due to foreign key constraints.
    // TODO: When adding and updating a customer, text fields are used to collect the following data: customer name,
    //  address, postal code, and phone number. Customer IDs are auto-generated, and first-level division
    //  (i.e., states, provinces) and country data are collected using separate combo boxes.
    // TODO: Country and first-level division data is pre-populated in separate combo boxes or lists in the user
    //  interface for the user to choose. The first-level list should be filtered by the user’s selection of a
    //  country (e.g., when choosing U.S., filter so it only shows states).
    //  TODO: When a customer record is deleted, a custom message should display in the user interface.
    //  TODO: Write code that enables the user to add, update, and delete appointments. A contact name is assigned to
    //   an appointment using a drop-down menu or combo box. A custom message is displayed in the user interface with
    //   the Appointment_ID and type of appointment canceled. The Appointment_ID is auto-generated and disabled
    //   throughout the application. When adding and updating an appointment, record the following data:
    //   Appointment_ID, title, description, location, contact, type, start date and time, end date and time,
    //   Customer_ID, and User_ID. All of the original appointment information is displayed on the update form in
    //   local time zone. All of the appointment fields can be updated except Appointment_ID, which must be disabled.
    //   TODO: Write code that enables the user to adjust appointment times. While the appointment times should be
    //    stored in Coordinated Universal Time (UTC), they should be automatically and consistently updated according
    //    to the local time zone set on the user’s computer wherever appointments are displayed in the application.
    //    Note: There are up to three time zones in effect. Coordinated Universal Time (UTC) is used for storing the
    //    time in the database, the user’s local time is used for display purposes, and Eastern Standard Time (EST) is
    //    used for the company’s office hours. Local time will be checked against EST business hours before they are
    //    stored in the database as UTC.
    //    TODO: Write code to implement input validation and logical error checks to prevent each of the following
    //     changes when adding or updating information; display a custom message specific for each error check in the
    //     user interface: scheduling an appointment outside of business hours defined as 8:00 a.m. to 10:00 p.m. EST,
    //     including weekends. scheduling overlapping appointments for customers. entering an incorrect username and
    //     password.
    //    TODO: Write code to provide an alert when there is an appointment within 15 minutes of the user’s log-in.
    //     A custom message should be displayed in the user interface and include the appointment ID, date, and time.
    //     If the user does not have any appointments within 15 minutes of logging in, display a custom message in the
    //     user interface indicating there are no upcoming appointments. Note: Since evaluation may be testing your
    //     application outside of business hours, your alerts must be robust enough to trigger an appointment within
    //     15 minutes of the local time set on the user’s computer, which may or may not be EST.
    //    TODO: Write code that generates accurate information in each of the following reports and will display the
    //     reports in the user interface: Note: You do not need to save and print the reports to a file or provide
    //     a screenshot.the total number of customer appointments by type and month. a schedule for each contact in
    //     your organization that includes appointment ID, title, type and description, start date and time, end date
    //     and time, and customer ID. an additional report of your choice that is different from the two other required
    //     reports in this prompt and from the user log-in date and time stamp that will be tracked in part C.
    //    TODO: Write at least two different lambda expressions to improve your code.
    //    TODO: track login attempts on txt file saved in root folder

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            updateAppointmentLists();
            updateCustomerList();
        } catch (SQLException e) {
            e.printStackTrace();
            displayError("SQL error");
        }
        dashUsername.setText("Appointments for user: "+ SessionData.getUsername());
        dAppID.setCellValueFactory(new PropertyValueFactory<>("appID")); // dash appointment table for user
        dAppTitle.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        dAppDesc.setCellValueFactory(new PropertyValueFactory<>("appDesc"));
        dAppLoc.setCellValueFactory(new PropertyValueFactory<>("appLocation"));
        dAppContact.setCellValueFactory(new PropertyValueFactory<>("appContact"));
        dAppType.setCellValueFactory(new PropertyValueFactory<>("appType"));
        dAppStartDT.setCellValueFactory(new PropertyValueFactory<>("appStartDateTime"));
        dAppEndDT.setCellValueFactory(new PropertyValueFactory<>("appEndDateTime"));
        dAppCustomerID.setCellValueFactory(new PropertyValueFactory<>("appCustomerID"));
        dAppUserID.setCellValueFactory(new PropertyValueFactory<>("appUserID"));
        dashAppointmentTableView.setItems(dashAppointmentList);
        cAppID.setCellValueFactory(new PropertyValueFactory<>("appID")); // appointment table
        cAppTitle.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        cAppDesc.setCellValueFactory(new PropertyValueFactory<>("appDesc"));
        cAppLoc.setCellValueFactory(new PropertyValueFactory<>("appLocation"));
        cAppContact.setCellValueFactory(new PropertyValueFactory<>("appContact"));
        cAppType.setCellValueFactory(new PropertyValueFactory<>("appType"));
        cAppStartDT.setCellValueFactory(new PropertyValueFactory<>("appStartDateTime"));
        cAppEndDT.setCellValueFactory(new PropertyValueFactory<>("appEndDateTime"));
        cAppCustomerID.setCellValueFactory(new PropertyValueFactory<>("appCustomerID"));
        cAppUserID.setCellValueFactory(new PropertyValueFactory<>("appUserID"));
        if(appointmentList.isEmpty()){displayError("No appointments to display");}
        appointmentTableView.setItems(appointmentList);
        custID.setCellValueFactory(new PropertyValueFactory<>("cusID")); // customer table
        custName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        custPhone.setCellValueFactory(new PropertyValueFactory<>("cusPhone"));
        custAddress.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        custZip.setCellValueFactory(new PropertyValueFactory<>("cusPostalCode"));
        custDiv.setCellValueFactory(new PropertyValueFactory<>("cusDivision"));
        custDivID.setCellValueFactory(new PropertyValueFactory<>("cusDivisionCode"));
        custCountry.setCellValueFactory(new PropertyValueFactory<>("cusCountry"));
        custCountryID.setCellValueFactory(new PropertyValueFactory<>("cusCountryCode"));
        if(customerList.isEmpty()){displayError("No customers to display");}
        customerTableView.setItems(customerList);

    }

    private void updateAppointmentLists() throws SQLException {
        appointmentList = listOfAppointments();
        dashAppointmentList = listOfAppointments(SessionData.getUsername());
    }

    /**
     * updates the list of customers
     * @throws SQLException - from the SQL executeQuery of the statement via database connection
     */
    private void updateCustomerList() throws SQLException{
        customerList = JDBC.listOfCustomers();
    }

    public void updateAppointmentAction(ActionEvent e) {
        try {
            AppointmentView.setUpdateAppointment(appointmentTableView.getSelectionModel().getSelectedItem());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/AppointmentView.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException x) {
            displayError("Something went wrong: Please select an appointment to modify and try again.");
        }
    }

    public void newAppointmentAction(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/AppointmentView.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioException) {
            displayError("Something went wrong displaying the application form.");
        }
    }

    /**
     * Method to display error message to user. Error message initially set to be invisible and becomes visible once
     * an error is shown to the user for the first time on the screen.
     * @param s- String containing error message to be displayed to the user.
     */
    private void displayError(String s){
        errorMessage.setText(s);
        errorMessage.setVisible(true);
    }
    /**
     * This method is called when the exit button is clicked. This closes the connection to the database and exits the program.
     */
    public void exitButtonAction() {
        JDBC.closeConnection();
        System.exit(0);
    }
}
