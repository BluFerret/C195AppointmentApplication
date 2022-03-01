package Controller;

import DAO.JDBC;
import Model.Appointment;
import Model.SessionData;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import static java.time.LocalDate.now;

/**
 * This is the controller class for AppointmentView.fxml. This provides the fields to update or add a new appointment.
 */
public class AppointmentView implements Initializable {
    @FXML private Label errorMessage;
    @FXML private TextField appIDText;
    @FXML private TextField titleText;
    @FXML private TextField descText;
    @FXML private TextField locationText;
    @FXML private TextField typeText;
    @FXML private ComboBox<String> customerBox;
    @FXML private ComboBox<String> contactBox;
    @FXML private ComboBox<String> userBox;
    @FXML private DatePicker startDateBox;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private ComboBox<String> endTimeComboBox;
    private static Appointment currentAppointment;
    private static boolean modifyAppointment = false;

    /**
     * This method initializes the AppointmentView of the application by populating textviews and comboboxes
     * accordingly depending on if the appointment is new or being updated. Lambdas for the forEach method are used to
     * keep code concise and easier to read. These for each and lambda combinations are used for updating most of the
     * fields from their respective collection lists when an appointment is being modified instead of creating a
     * new appointment.
     * @param url - possible url location for root object if provided, null if not needed.
     * @param resourceBundle - possible resources for root object, null if not needed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customerList = JDBC.listOfCustomerNames();
        customerBox.setItems(customerList);
        ObservableList<String> contactList = JDBC.listOfContactNames();
        contactBox.setItems(contactList);
        ObservableList<String> userList = JDBC.listOfUserNames();
        userBox.setItems(userList);
        try {
            startDateBox.setValue(now());
        } catch (Exception e) {
            displayError("Start date is invalid, try again");
        }
        userList.forEach((String userName) -> {
            if (SessionData.getUsername().equals(SessionData.parseStringComma(userName))) {
                userBox.setValue(userName);
            }
        });
        if(modifyAppointment){
            appIDText.setText(String.valueOf(currentAppointment.getAppID()));
            titleText.setText(currentAppointment.getAppTitle());
            descText.setText(currentAppointment.getAppDesc());
            locationText.setText(currentAppointment.getAppLocation());
            typeText.setText(currentAppointment.getAppType());
            customerList.forEach((String customerInfo) -> {
                if (String.valueOf(currentAppointment.getAppCustomerID()).equals(SessionData.parseCommaString(customerInfo))) {
                    customerBox.setValue(customerInfo);
                }
            });
            contactList.forEach((String contactInfo) -> {
                if (currentAppointment.getAppContact().equals(SessionData.parseStringComma(contactInfo))) {
                    contactBox.setValue(contactInfo);
                }
            });
            userList.forEach((String userInfo) -> {
                if (String.valueOf(currentAppointment.getAppUserID()).equals(SessionData.parseCommaString(userInfo))) {
                    userBox.setValue(userInfo);
                }
            });
            try {
                startDateBox.setValue(LocalDate.parse(SessionData.parseDateFromDateTime(currentAppointment.getAppStartDateTime())));
            } catch (Exception e) {
                displayError("Start date is invalid, try again");
            }
            populateDateTimeComboBoxes();
            startTimeComboBox.setValue(currentAppointment.getAppStartDateTime());
            endTimeComboBox.setValue(currentAppointment.getAppEndDateTime());
        }
        else{
            populateDateTimeComboBoxes();}
    }
    /**
     * This method populates the list of start and end date and times by provided a list of times within business hours
     * in EST with 5 minute intervals and then converting those times and dates to local time to prevent confusion
     * for the user.
     * @param chosenDate - date provided to generate list of business hours time for.
     * @return - Observable list of dates and times for scheduling in the local timezone
     */
    private ObservableList<String> listOfAppointmentTimesInLocal(String chosenDate){
        ObservableList<String> list = FXCollections.observableArrayList();
        int h = 8;
        while(h<=22){
            int m = 0;
            while(m<60) {
                String preConvertedDateTime = chosenDate+" "+String.format("%02d", h)+":"+String.format("%02d", m)+":00";
                String convertedDateTime = SessionData.convertESTtoLocal(preConvertedDateTime);
                list.add(convertedDateTime);
                if(h==22){break;}
                m= 5+m;
            }
            h++;
        }
        return list;
    }
    /**
     * This method populates the startTimeComboBox and end TimeComboBox with the dates and times provided in the
     * listOfAppointmentTimesInLocal method.
     */
    public void populateDateTimeComboBoxes(){
        ObservableList<String> dateTimeList = listOfAppointmentTimesInLocal(startDateBox.getValue().toString());
        startTimeComboBox.setItems(dateTimeList);
        endTimeComboBox.setItems(dateTimeList);
    }
    /**
     * This method indicates that an appointment is being updated instead of a new appointment being made.
     * @param appointment - appointment being updated that is passed from ApplicationMain
     */
    public static void setUpdateAppointment(Appointment appointment){
        currentAppointment = appointment;
        modifyAppointment = true;
    }
    /**
     * This method validates that the appointment fields are all provided, the end date and time is not before the
     * start date and time, and the appointment does not conflict with another appointment for the same customer.
     * @return - a boolean indicating if the appointment values provided are valid or not.
     */
    private boolean validateAppointment(){
        if(titleText.getText().isEmpty()||descText.getText().isEmpty()||locationText.getText().isEmpty()||typeText.getText().isEmpty()||
                customerBox.getValue().isEmpty()||contactBox.getValue().isEmpty()||userBox.getValue().isEmpty()||
                startTimeComboBox.getValue().isEmpty()||endTimeComboBox.getValue().isEmpty()){
            displayError("Appointment not saved: Please fill in all fields");
            return false;
        }
        if(titleText.getText().length()>50){
            displayError("Appointment not saved: Appointment title is too long");
            return false;
        }
        if(descText.getText().length()>50){
            displayError("Appointment not saved: Appointment description is too long");
            return false;
        }
        if(locationText.getText().length()>50){
            displayError("Appointment not saved: Appointment location is too long");
            return false;
        }
        if(typeText.getText().length()>50){
            displayError("Appointment not saved: Appointment type is too long");
            return false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTimeComboBox.getValue(), formatter);
        LocalDateTime end = LocalDateTime.parse(endTimeComboBox.getValue(), formatter);
        if(start.isAfter(end)){
            displayError("Appointment not saved: Appointment end time is before the start time");
            return false;
        }

            String startTimeUTC = SessionData.convertLocaltoUTC(startTimeComboBox.getValue());
            String endTimeUTC = SessionData.convertLocaltoUTC(endTimeComboBox.getValue());
            boolean overlap = JDBC.appointmentsExistWithinTimeFrameForCustomer(startTimeUTC,
                    endTimeUTC,SessionData.parseCommaString(customerBox.getValue()));
            if(modifyAppointment){overlap = JDBC.appointmentsExistWithinTimeFrameForCustomer(startTimeUTC,
                    endTimeUTC,SessionData.parseCommaString(customerBox.getValue()),currentAppointment.getAppID());}
            if(overlap){
                displayError("Appointment not saved: Customer already has appointment during timeframe");
                return false;
            }
        return true;
    }
    /**
     * This method saves a valid appointment and returns the user to the ApplicationMain view or generates an error
     * message if the appointment is not valid.
     * @param e - event of clicking the save button.
     */
    public void saveAppointment(ActionEvent e){
        if(validateAppointment()){
            boolean updated;
            if(modifyAppointment){
                updated = JDBC.updateAppointment(currentAppointment.getAppID(),titleText.getText(),descText.getText(),locationText.getText(),typeText.getText(),
                        SessionData.convertLocaltoUTC(startTimeComboBox.getValue()),SessionData.convertLocaltoUTC(endTimeComboBox.getValue()),
                        SessionData.parseCommaString(customerBox.getValue()),SessionData.parseCommaString(userBox.getValue()),
                        SessionData.parseCommaString(contactBox.getValue()));
            }
            else{
                updated = JDBC.addNewAppointment(titleText.getText(),descText.getText(),locationText.getText(),typeText.getText(),
                        SessionData.convertLocaltoUTC(startTimeComboBox.getValue()),SessionData.convertLocaltoUTC(endTimeComboBox.getValue()),
                        SessionData.parseCommaString(customerBox.getValue()),SessionData.parseCommaString(userBox.getValue()),
                        SessionData.parseCommaString(contactBox.getValue()));
            }
            if (updated){
                modifyAppointment=false;
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ApplicationMain.fxml")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
                assert root != null;
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.centerOnScreen();
            }
            else{displayError("SQL error saving appointment, please try again.");}
        }
    }
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
     * This method returns the user to the ApplicationMain view.
     * @param e - event of clicking the cancel button.
     */
    public void cancelButtonAction(ActionEvent e) {
        modifyAppointment=false;
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ApplicationMain.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
        assert root != null;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}
