package Controller;

import DAO.JDBC;
import Model.*;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import static DAO.JDBC.listOfAppointments;

public class ApplicationMain implements Initializable {
    @FXML private Label errorMessage;
    // user dashboard
    @FXML private Label dashUsername;
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
    private ObservableList<Appointment> dashAppointmentList;
    // appointments
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
    private ObservableList<Appointment> appointmentList;
    // customer
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
    private ObservableList<Customer> customerList;
    // report 1
    @FXML private TableView<Appointment> groupAppTable;
    @FXML private TableColumn<Appointment,String> groupAppType;
    @FXML private TableColumn<Appointment,String> groupAppMonth;
    @FXML private TableColumn<Appointment,Integer> groupAppCount;
    // report 2
    @FXML private Label report2Label;
    @FXML private ComboBox<String> contactSelection;
    @FXML private TableView<Appointment> contactTableView;
    @FXML private TableColumn<Appointment,Integer> conAppID;
    @FXML private TableColumn<Appointment,String> conAppTitle;
    @FXML private TableColumn<Appointment,String> conAppDesc;
    @FXML private TableColumn<Appointment,String> conAppLoc;
    @FXML private TableColumn<Appointment,String> conAppContact;
    @FXML private TableColumn<Appointment,String> conAppType;
    @FXML private TableColumn<Appointment,String> conAppStartDT;
    @FXML private TableColumn<Appointment,String> conAppEndDT;
    @FXML private TableColumn<Appointment,Integer> conAppCustomerID;
    @FXML private TableColumn<Appointment,Integer> conAppUserID;
    private ObservableList<Appointment> conAppointmentList;
    // logs
    @FXML private TableView<LoginAttempt> logTableView;
    @FXML private TableColumn<LoginAttempt,String> logUsername;
    @FXML private TableColumn<LoginAttempt,String> logTimestampUTC;
    @FXML private TableColumn<LoginAttempt,String> logAttemptSucessful;
    public ObservableList<LoginAttempt> loginList;

    // TODO: Customers & appointments can be added, updated, and deleted. When deleting a customer, all of the customer’s
    //  appointments must be deleted first.
    //  TODO: When a customer or appointment record is deleted, a custom message should display in the user interface that includes
    //   appointment type. id, and customer name where applicable.
    //  TODO: Add user ID to appointments adding/modification.
    //    TODO: Write code to implement input validation and logical error checks to prevent each of the following
    //     changes when adding or updating information; display a custom message specific for each error check in the
    //     user interface: scheduling overlapping appointments for customers.
    //    TODO: Write code to provide an alert when there is an appointment within 15 minutes of the user’s log-in.
    //     A custom message should be displayed in the user interface and include the appointment ID, date, and time.
    //     If the user does not have any appointments within 15 minutes of logging in, display a custom message in the
    //     user interface indicating there are no upcoming appointments. Note: Since evaluation may be testing your
    //     application outside of business hours, your alerts must be robust enough to trigger an appointment within
    //     15 minutes of the local time set on the user’s computer, which may or may not be EST.
    //    TODO: Write code that generates accurate information in each of the following reports and will display the
    //     reports in the user interface: an additional report of your choice
    //    TODO: Write at least two different lambda expressions to improve your code.
    // TODO:Javadoc note
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateAppointmentLists();
        updateCustomerList();
        // user dashboard setup
        dashUsername.setText("Upcoming Appointments for User: "+ SessionData.getUsername());
        dAppID.setCellValueFactory(new PropertyValueFactory<>("appID"));
        dAppTitle.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        dAppDesc.setCellValueFactory(new PropertyValueFactory<>("appDesc"));
        dAppLoc.setCellValueFactory(new PropertyValueFactory<>("appLocation"));
        dAppContact.setCellValueFactory(new PropertyValueFactory<>("appContact"));
        dAppType.setCellValueFactory(new PropertyValueFactory<>("appType"));
        dAppStartDT.setCellValueFactory(new PropertyValueFactory<>("appStartDateTime"));
        dAppEndDT.setCellValueFactory(new PropertyValueFactory<>("appEndDateTime"));
        dAppCustomerID.setCellValueFactory(new PropertyValueFactory<>("appCustomerID"));
        dashAppointmentTableView.setItems(dashAppointmentList);
        // appointment tab setup
        cAppID.setCellValueFactory(new PropertyValueFactory<>("appID"));
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
        // customer tab setup
        custID.setCellValueFactory(new PropertyValueFactory<>("cusID"));
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
        // report 1
        groupAppType.setCellValueFactory(new PropertyValueFactory<>("appType"));
        groupAppMonth.setCellValueFactory(new PropertyValueFactory<>("appMonth"));
        groupAppCount.setCellValueFactory(new PropertyValueFactory<>("appCount"));
        ObservableList<Appointment> groupAppList = JDBC.report1AppointmentsByMonthAndType();
        groupAppTable.setItems(groupAppList);
        // report 2
        ObservableList<String> contactTestSelectionList = JDBC.listOfContactNames();
        contactSelection.setItems(contactTestSelectionList);
        contactSelection.getSelectionModel().selectFirst();
        conAppID.setCellValueFactory(new PropertyValueFactory<>("appID"));
        conAppTitle.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        conAppDesc.setCellValueFactory(new PropertyValueFactory<>("appDesc"));
        conAppLoc.setCellValueFactory(new PropertyValueFactory<>("appLocation"));
        conAppContact.setCellValueFactory(new PropertyValueFactory<>("appContact"));
        conAppType.setCellValueFactory(new PropertyValueFactory<>("appType"));
        conAppStartDT.setCellValueFactory(new PropertyValueFactory<>("appStartDateTime"));
        conAppEndDT.setCellValueFactory(new PropertyValueFactory<>("appEndDateTime"));
        conAppCustomerID.setCellValueFactory(new PropertyValueFactory<>("appCustomerID"));
        conAppUserID.setCellValueFactory(new PropertyValueFactory<>("appUserID"));
        conAppointmentList=JDBC.report2ScheduleByContact(SessionData.parseStringComma(contactSelection.getValue()));
        contactTableView.setItems(conAppointmentList);
        report2Label.setText("Appointments for "+contactSelection.getValue());
        // logs table setup
        loginList = SessionData.getLoginAttempts();
        logUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        logTimestampUTC.setCellValueFactory(new PropertyValueFactory<>("timestampUTC"));
        logAttemptSucessful.setCellValueFactory(new PropertyValueFactory<>("loginSucessful"));
        logTableView.setItems(loginList);
    }
    // TODO:Javadoc note
    private void updateAppointmentLists() {
        try{appointmentList = listOfAppointments();}
        catch (SQLException throwables) {
            displayError("There was an error and no appointments could be found, please try again later.");
        }
        try{dashAppointmentList = listOfAppointments(SessionData.getUsername());}
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * updates the list of customers
     */
    private void updateCustomerList(){
        try{customerList = JDBC.listOfCustomers();} catch (SQLException throwables) {
            displayError("There was an error pulling the customers from the database");
            throwables.printStackTrace();
        }
    }
    // TODO:Javadoc note
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
    // TODO:Javadoc note
    public void newAppointmentAction(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/AppointmentView.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException x) {
            displayError("Something went wrong displaying the appointment form.");
        }
    }
    public void deleteAppointment(){}
    // TODO:Javadoc note
    public void updateCustomerAction(ActionEvent e) {
        try {
            CustomerView.setUpdateCustomer(customerTableView.getSelectionModel().getSelectedItem());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/CustomerView.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException x) {
            displayError("Something went wrong: Please select a customer to modify and try again.");
        }
    }
    // TODO:Javadoc note **** possibly replace all new windows with lambdas
    public void newCustomerAction(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/CustomerView.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException x) {
            displayError("Something went wrong displaying the customer form.");
        }
    }
    // TODO:Javadoc note
    public void contactSelectAction(){
        conAppointmentList=JDBC.report2ScheduleByContact(SessionData.parseStringComma(contactSelection.getValue()));
        contactTableView.setItems(conAppointmentList);
        contactTableView.refresh();
        if(conAppointmentList.isEmpty()){displayError("Report 2- No appointments associated with that contact");}
        report2Label.setText("Appointments for "+contactSelection.getValue());
    }
    /**
     * Method to display error message to user. Lambda used for setOnFinished of PauseTransition to return error text
     * to be invisible after 10 seconds.
     * @param s- String containing error message to be displayed to the user.
     */
    private void displayError(String s){
        errorMessage.setText(s);
        errorMessage.setVisible(true);
        PauseTransition visibleErrorText = new PauseTransition(Duration.seconds(10));
        visibleErrorText.setOnFinished(actionEvent -> errorMessage.setVisible(false));
        visibleErrorText.play();
    }
    /**
     * This method is called when the exit button is clicked. This closes the connection to the database and exits the program.
     */
    public void exitButtonAction() {
        JDBC.closeConnection();
        System.exit(0);
    }
}
