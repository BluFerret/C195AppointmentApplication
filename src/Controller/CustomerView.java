package Controller;

import DAO.JDBC;
import Model.Customer;
import Model.SessionData;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
/**
 * This is the controller class for CustomerView.fxml. This provides the fields to update or add a new customer.
 */
public class CustomerView implements Initializable {
    @FXML private Label errorMessage;
    @FXML private TextField custIDText;
    @FXML private TextField nameText;
    @FXML private TextField addressText;
    @FXML private ComboBox<String> countryBox;
    @FXML private ComboBox<String> firstLvlDivisionBox;
    @FXML private TextField postalCodeText;
    @FXML private TextField phoneText;
    private static Customer currentCustomer;
    private static boolean modifyCustomer = false;
    private ObservableList<String> firstLvlDivisionList;

    /**
     * This method initializes the CustomerView of the application by populating textviews and comboboxes
     * accordingly depending on if the customer is new or being updated.
     * @param url - possible url location for root object if provided, null if not needed.
     * @param resourceBundle - possible resources for root object, null if not needed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countryList = JDBC.listOfCountries();
        countryBox.setItems(countryList);
        if(modifyCustomer){
            custIDText.setText(String.valueOf(currentCustomer.getCusID()));
            nameText.setText(currentCustomer.getCusName());
            addressText.setText(currentCustomer.getCusAddress());
            int i = 0;
            while(i<countryList.size()){
                if(SessionData.parseStringComma(countryList.get(i)).equals(currentCustomer.getCusCountry())) {
                    countryBox.setValue(countryList.get(i));
                    break;
                }
                i++;
            }
            if(!countryBox.getValue().isEmpty()){
                i = 0;
                populateFirstLvlDivisionBox();
                while(i<firstLvlDivisionList.size()){
                    if(SessionData.parseStringComma(firstLvlDivisionList.get(i)).equals(currentCustomer.getCusDivision())) {
                        firstLvlDivisionBox.setValue(firstLvlDivisionList.get(i));
                        break;
                    }
                    i++;
                }
            }
            postalCodeText.setText(currentCustomer.getCusPostalCode());
            phoneText.setText(currentCustomer.getCusPhone());
        }
    }
    /**
     * This method indicates that a customer is being updated instead of a new customer being made.
     * @param customer - customer being updated that is passed from ApplicationMain
     */
    public static void setUpdateCustomer(Customer customer){
        currentCustomer = customer;
        modifyCustomer = true;
    }
    /**
     * This method populates the first level division combobox depending on the country choice provided.
     */
    public void populateFirstLvlDivisionBox(){
        if(!countryBox.getValue().isEmpty()) {
            firstLvlDivisionList = JDBC.listOfFirstLvlDivisions(SessionData.parseCommaString(countryBox.getValue()));
            firstLvlDivisionBox.setItems(firstLvlDivisionList);
        }
    }
    /**
     * This method validates that the customer values are valid and all fields are provided.
     * @return - a boolean indicating if the customer values provided are valid or not.
     */
    private boolean validateCustomer(){
        boolean valid = true;
        if(nameText.getText().isEmpty()||addressText.getText().isEmpty()||postalCodeText.getText().isEmpty()||
                phoneText.getText().isEmpty()||countryBox.getValue().isEmpty()||firstLvlDivisionBox.getValue().isEmpty()){
            valid = false;
            displayError("Customer not saved: Please fill in all fields");
        }
        if( nameText.getText().length()>50){
            displayError("Customer not saved: Customer name is too long");
            valid = false;
        }
        if(addressText.getText().length()>100){
            displayError("Customer not saved: Customer address is too long");
            valid = false;
        }
        if(postalCodeText.getText().length()>50){
            displayError("Customer not saved: Customer postal code is too long");
            valid = false;
        }
        if(phoneText.getText().length()>50){
            displayError("Customer not saved: Customer phone is too long");
            valid = false;
        }
        return valid;
    }
    /**
     * This method saves a valid customer and returns the user to the ApplicationMain view or generates an error
     * message if the customer is not valid.
     * @param e - event of clicking the save button.
     */
    public void saveCustomer(ActionEvent e){
        if(validateCustomer()){
            int division = Integer.parseInt(SessionData.parseCommaString(firstLvlDivisionBox.getValue()));
            boolean updated;
            if(modifyCustomer){
                updated = JDBC.updateCustomer(currentCustomer.getCusID(),nameText.getText(),addressText.getText(),postalCodeText.getText(),phoneText.getText(),division);
            }
            else{
                updated = JDBC.addNewCustomer(nameText.getText(),addressText.getText(),postalCodeText.getText(),phoneText.getText(),division);
            }
            if (updated){
                modifyCustomer=false;
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
            else{displayError("SQL error saving customer, please try again.");}
        }
    }
    /**
     * This Method displays an error message to user. A Lambda expression is used for setOnFinished of PauseTransition
     * to return error text to be invisible after 10 seconds. The use of this lambda on the setOnFinished method is
     * to keep the code readable and concise. This uses the abstract method setOnFinished and sets the textView
     * to become invisible once the previously entered PauseTransition is finished with the assigned duration of 10 seconds.
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
        modifyCustomer=false;
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
