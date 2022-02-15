package Controller;

import DAO.JDBC;
import Model.Appointment;
import Model.Contact;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppointmentView implements Initializable {
    @FXML private Label errorMessage;
    @FXML private TextField appIDText;
    @FXML private TextField titleText;
    @FXML private TextField descText;
    @FXML private TextField locationText;
    @FXML private TextField typeText;
    @FXML private ComboBox<Contact> customerBox;
    @FXML private TextField custIDText;
    @FXML private ComboBox<String> contactBox;
    @FXML private TextField contactIDText;
    private static Appointment currentAppointment;
    private static boolean modifyAppointment = false;
    private ArrayList<Contact> contactsAndIDs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactsAndIDs = JDBC.listOfContactsAndIDs();
        contactBox.setItems(JDBC.listOfContactNames());

        if(modifyAppointment){
            appIDText.setText(String.valueOf(currentAppointment.getAppID()));
            titleText.setText(currentAppointment.getAppTitle());
            descText.setText(currentAppointment.getAppDesc());
            locationText.setText(currentAppointment.getAppLocation());
            typeText.setText(currentAppointment.getAppType());
            custIDText.setText(String.valueOf(currentAppointment.getAppCustomerID()));
            }
    }

    public static void setUpdateAppointment(Appointment appointment){
        currentAppointment = appointment;
        modifyAppointment = true;
    }

    private int indexOfContactFromAttribute(String contactName){
        int i = 0;
        while (i<contactsAndIDs.size()){
            if(Objects.equals(contactsAndIDs.get(i).getContactName(), contactName)){
                return i;
            }
            i++;
        }
        return -1;
    }
    private int indexOfContactFromAttribute(int contactID){
        int i = 0;
        while (i<contactsAndIDs.size()){
            if(Objects.equals(contactsAndIDs.get(i).getContactID(), contactID)){
                return i;
            }
            i++;
        }
        return -1;
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
