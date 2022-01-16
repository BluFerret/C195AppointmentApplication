package Model;

public class Contact {
    private final String contactName;
    private final int contactID;

    public Contact(String contactName, int contactID) {
        this.contactName = contactName;
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public int getContactID() {
        return contactID;
    }
}
