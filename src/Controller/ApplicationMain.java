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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the controller class for the ApplicationMain.fxml view. This controls the main application including
 * the various tabs displaying the user's dashboard, the appointment tab, the customer tab, tabs for reports 1, 2, and
 * 3, and the logs tab. The user's dashboard displays the upcoming appointments associated with the user who is
 * currently logged in per the login screen. The appointments tab displays all the appointments for all users and
 * offers ways for the user to create, update, and delete appointments as well as view appointments by month and
 * week or by all time. The Customer tab displays all the customers and allows the user to create, update, and
 * delete customers. The report 1 tab displays the count of appointments groups by type and by month. The report 2 tab
 * displays a schedule of appointments for each contact. The report 3 tab displays _________________________________.
 * The logs tab displays all login attempts, when they took place, and if they were successful.
 */
public class ApplicationMain implements Initializable {
    private static boolean imminentApp;
    @FXML private ImageView logoImage;
    @FXML private Label errorMessage;
    // user dashboard
    @FXML private Label dash15MinLabel;
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
    @FXML private RadioButton allTimeAppointments;
    @FXML private RadioButton currentWeekAppointments;
    @FXML private RadioButton currentMonthAppointments;
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
    private ObservableList<Appointment> groupAppList;
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
    // report 3
    @FXML private TableView<Customer> groupCustTable;
    @FXML private TableColumn<Customer, String> groupCustCountry;
    @FXML private TableColumn<Customer, String> groupCustDivision;
    @FXML private TableColumn<Customer, Integer> groupCustCount;
    private ObservableList<Customer> groupCustList;
    // logs
    @FXML private TableView<LoginAttempt> logTableView;
    @FXML private TableColumn<LoginAttempt,String> logUsername;
    @FXML private TableColumn<LoginAttempt,String> logTimestampUTC;
    @FXML private TableColumn<LoginAttempt,String> logAttemptSucessful;

    // ========== Initialization and data refresh ==========
    /**
     * This method initializes the main screen of the application by populating tables, labels, and comboboxes to a default setting.
     * @param url - possible url location for root object if provided, null if not needed.
     * @param resourceBundle - possible resources for root object, null if not needed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Image logo = new Image(new FileInputStream("src\\Resources\\FGCLogo.jpg"));
            logoImage.setImage(logo);
        } catch (FileNotFoundException e) {
            System.out.println("logo image in resource file has been moved or renamed");
        }
        try{appointmentList = JDBC.listOfAppointmentsAll();}
        catch (SQLException throwables) {
            displayError("SQL error: no appointments found, check database connection.");
        }
        try{customerList = JDBC.listOfCustomers();} catch (SQLException throwables) {
            displayError("SQL error: no customers found, check database connection.");
            throwables.printStackTrace();
        }
        // user dashboard setup
        dashUsername.setText("Future Appointments for User: "+ SessionData.getUsername());
        dAppID.setCellValueFactory(new PropertyValueFactory<>("appID"));
        dAppTitle.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        dAppDesc.setCellValueFactory(new PropertyValueFactory<>("appDesc"));
        dAppLoc.setCellValueFactory(new PropertyValueFactory<>("appLocation"));
        dAppContact.setCellValueFactory(new PropertyValueFactory<>("appContact"));
        dAppType.setCellValueFactory(new PropertyValueFactory<>("appType"));
        dAppStartDT.setCellValueFactory(new PropertyValueFactory<>("appStartDateTime"));
        dAppEndDT.setCellValueFactory(new PropertyValueFactory<>("appEndDateTime"));
        dAppCustomerID.setCellValueFactory(new PropertyValueFactory<>("appCustomerID"));
        userDashUpcomingUpdate();
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
        appointmentTabTableUpdate();
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
        // report 3
        groupCustCountry.setCellValueFactory(new PropertyValueFactory<>("cusCountry"));
        groupCustDivision.setCellValueFactory(new PropertyValueFactory<>("cusDivision"));
        groupCustCount.setCellValueFactory(new PropertyValueFactory<>("cusCount"));
        groupCustList = JDBC.report3CountCustomersByCountryAndDivision();
        groupCustTable.setItems(groupCustList);
        // logs table setup
        ObservableList<LoginAttempt> loginList = SessionData.getLoginAttempts();
        logUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        logTimestampUTC.setCellValueFactory(new PropertyValueFactory<>("timestampUTC"));
        logAttemptSucessful.setCellValueFactory(new PropertyValueFactory<>("loginSucessful"));
        logTableView.setItems(loginList);
        fifteenMinAlert();
    }
    /**
     * This method refreshes the data in the various tables and labels across the ApplicationMain View.
     */
    public void refreshData(){
        userDashUpcomingUpdate();
        try{
            customerList = JDBC.listOfCustomers();
            groupAppList = JDBC.report1AppointmentsByMonthAndType();
            groupCustList = JDBC.report3CountCustomersByCountryAndDivision();
        }
        catch (SQLException throwables) {
            displayError("SQL error: no appointments found, check database connection.");
        }
        dashAppointmentTableView.setItems(dashAppointmentList);
        dashAppointmentTableView.refresh();
        appointmentTabTableUpdate();
        customerTableView.setItems(customerList);
        customerTableView.refresh();
        groupAppTable.setItems(groupAppList);
        groupAppTable.refresh();
        contactSelectAction();
        groupCustTable.setItems(groupCustList);
        groupCustTable.refresh();
    }
    /**
     * This method updates the report 2 schedule list based on which contact has been selected from the contact drop down list.
     */
    public void contactSelectAction(){
        conAppointmentList=JDBC.report2ScheduleByContact(SessionData.parseStringComma(contactSelection.getValue()));
        contactTableView.setItems(conAppointmentList);
        contactTableView.refresh();
        report2Label.setText("Appointments for "+contactSelection.getValue());
    }
    /**
     * This method updated the user's dashboard tab to display any future appointments associated with the user and give
     * notice in the UI whether an appointment is coming up within 15 minutes.
     */
    private void userDashUpcomingUpdate(){
        if(JDBC.appointmentsExistWithin15MinTimeFrameForUser()){
            dash15MinLabel.setText("User has an appointment within next 15 minutes");
        }
        else{dash15MinLabel.setText("User has no appointments within next 15 minutes");}
        try{dashAppointmentList = JDBC.listOfAppointments(SessionData.getUsername());}
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dashAppointmentTableView.setItems(dashAppointmentList);
    }
    /**
     * This method populates the appointment table based on the current selection of the radio buttons on the appointment tab.
     */
    public void appointmentTabTableUpdate(){
        if(allTimeAppointments.isSelected()){
            try{
                appointmentList = JDBC.listOfAppointmentsAll();
            } catch (SQLException throwables) {
                displayError("SQL error: no appointments found, check database connection.");
            }
        }
        if(currentWeekAppointments.isSelected()){
            try{
                appointmentList = JDBC.listOfAppointmentsWeek();
            } catch (SQLException throwables) {
                displayError("SQL error: no appointments found, check database connection.");
            }
        }
        if(currentMonthAppointments.isSelected()){
            try{
                appointmentList = JDBC.listOfAppointmentsMonth();
            } catch (SQLException throwables) {
                displayError("SQL error: no appointments found, check database connection.");
            }
        }
        appointmentTableView.setItems(appointmentList);
        appointmentTableView.refresh();
    }
    /**
     * This method is called when successfully logging in to check if the user has an appointment within 15 minutes so an alert can be generated.
     */
    public static void setImminentApp(){
        imminentApp= JDBC.appointmentsExistWithin15MinTimeFrameForUser();
    }
    /**
     * This method generates an alert when a user is associated with an appointment within 15 minutes of logging in to the application.
     */
    private void fifteenMinAlert(){
        if(imminentApp){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, JDBC.appointmentWithin15MinTimeFrameForUser(), ButtonType.OK);
            alert.setTitle("Imminent Appointment");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                imminentApp = false;
            }
        }
    }

    // ========== appointment actions ==========
    /**
     * This method brings the user to the AppointmentView after saving the currently selected appointment as the
     * appointment to be updated with the AppointmentView form.
     * @param e - event of pressing the modify button on the appointment tab
     */
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
    /**
     * This method brings the user to the AppointmentView
     * @param e - event of pressing the add button on the appointment tab
     */
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
    /**
     * This method gives the user an option to delete an appointment via a pop-up message. A lambda expression is used
     * for the filter method to both make code more concise as it filters the Observable List collection of customerList.
     */
    public void deleteAppointment(){
        if(appointmentTableView.getSelectionModel().isEmpty()){
            displayError("Please select an appointment to delete and try again.");
        }
        else {
            Appointment appToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
            String associatedCustomer = Objects.requireNonNull(customerList.stream().filter(customer -> customer.getCusID() == appToDelete.getAppCustomerID())
                    .findFirst().orElse(null)).getCusName();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the following appointment?: " +
                    "\nAppointment " + appToDelete.getAppID() +
                    ", " + appToDelete.getAppType() +
                    "\nCustomer " + appToDelete.getAppCustomerID() +
                    ", " + associatedCustomer, ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Delete Appointment");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.YES) {
                    if (JDBC.deleteAppointment(appToDelete.getAppID())) {
                        displayError("Appointment "+appToDelete.getAppID()+", "+appToDelete.getAppType()+" deleted.");
                    }
                }
            }
            refreshData();
        }
    }

    // ========== customer actions ==========
    /**
     * This method brings the user to the CustomerView after saving the currently selected customer as the
     * customer to be updated with the CustomerView form.
     * @param e - event of pressing the modify button on the customer tab
     */
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
    /**
     * This method brings the user to the CustomerView
     * @param e - event of pressing the add button on the customer tab
     */
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
    /**
     * This method gives the user an option to delete a customer via a pop-up message, provided the customer is valid
     * to delete.
     */
    public void deleteCustomer(){
        if(customerTableView.getSelectionModel().isEmpty()){
            displayError("Please select a customer to delete and try again.");
        }
        else if(JDBC.customerHasAppointmentsCheck(customerTableView.getSelectionModel().getSelectedItem().getCusID())){
            displayError("Please delete associated appointments before deleting customer "
                    +customerTableView.getSelectionModel().getSelectedItem().getCusID()+".");
        }
        else {
            Customer custToDelete = customerTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the following customer?: " +
                    "\nCustomer " + custToDelete.getCusID() +
                    ", " + custToDelete.getCusName(), ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Delete Customer");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.YES) {
                    if (JDBC.deleteCustomer(custToDelete.getCusID())) {
                        displayError("Customer "+custToDelete.getCusID()+", "+custToDelete.getCusName()+" deleted.");
                    }
                }
            }
            refreshData();
        }
    }

    // ========== application exit and error display ==========
    /**
     * Method to display error message to user. Lambda used for setOnFinished of PauseTransition to return error text
     * to be invisible after 10 seconds. The use of this lambda on the setOnFinished method is to keep the code
     * readable and concise.
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
