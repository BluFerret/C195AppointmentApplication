package Model;

import javafx.collections.ObservableList;

public class Country {
    private String countryName;
    private int countryID;

    public Country(String countryName, int countryID) {
        this.countryName = countryName;
        this.countryID = countryID;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }
}
