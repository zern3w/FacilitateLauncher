package android.facilitatelauncher.model;

/**
 * Created by puttipongtadang on 1/31/18.
 */

public class Contact {

    private int contactId;
    private String source;
    private String phoneNumber;
    private String name;

    public Contact(int contactId, String name, String source, String phoneNumber) {
        this.contactId = contactId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.source = source;
    }

    public Contact() {
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
