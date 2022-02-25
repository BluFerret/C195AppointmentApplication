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
//TODO:javadoc
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

    //TODO:javadoc
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
    //TODO:javadoc
    public static void setUpdateCustomer(Customer customer){
        currentCustomer = customer;
        modifyCustomer = true;
    }
    //TODO:javadoc
    public void populateFirstLvlDivisionBox(){
        if(!countryBox.getValue().isEmpty()) {
            firstLvlDivisionList = JDBC.listOfFirstLvlDivisions(SessionData.parseCommaString(countryBox.getValue()));
            firstLvlDivisionBox.setItems(firstLvlDivisionList);
        }
    }
    //TODO:javadoc
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
    //TODO:javadoc
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
     * Method to display error message to user. A Lambda was used for setOnFinished of PauseTransition to return error
     * text to be invisible after 10 seconds.
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
