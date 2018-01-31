package android.facilitatelauncher.model;

/**
 * Created by puttipongtadang on 1/31/18.
 */

public class Contact {

    private int id;
    private String contactId;
    private String phoneId;
    private String name;
    private String number;
    private String fwNumber;
    private String fwId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFwNumber() {
        return fwNumber;
    }

    public void setFwNumber(String fwNumber) {
        this.fwNumber = fwNumber;
    }

    public String getFwId() {
        return fwId;
    }

    public void setFwId(String fwId) {
        this.fwId = fwId;
    }

    public String getFwName() {
        return fwName;
    }

    public void setFwName(String fwName) {
        this.fwName = fwName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCallCenterNumber() {
        return callCenterNumber;
    }

    public void setCallCenterNumber(String callCenterNumber) {
        this.callCenterNumber = callCenterNumber;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    private String fwName;
    private String userId;
    private String callCenterNumber;
    private boolean deleted;
    private boolean editable;
    private boolean inUse;
    private boolean linked;
}
