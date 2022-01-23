package Model;

public class FirstLvlDivision {
    private String divisionName;
    private int divisionID;

    public FirstLvlDivision(String divisionName, int divisionID) {
        this.divisionName = divisionName;
        this.divisionID = divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public int getDivisionID() {
        return divisionID;
    }
}
