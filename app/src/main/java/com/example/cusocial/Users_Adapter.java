package com.example.cusocial;

public class Users_Adapter {

    public Users_Adapter(String user_id, String EMAILID, String UID, String FULLNAME, String ROLE, String CONTACTNUM, String PASSWORD, String STATUS, String IMAGE) {
        this.user_id = user_id;
        this.EMAILID = EMAILID;
        this.UID = UID;
        this.FULLNAME = FULLNAME;
        this.ROLE = ROLE;
        this.CONTACTNUM = CONTACTNUM;
        this.PASSWORD = PASSWORD;
        this.LASTMESSAGE = LASTMESSAGE;
        this.STATUS = STATUS;
        this.IMAGE = IMAGE;
    }
    public Users_Adapter() {
    }

    String user_id;
    String EMAILID;
    String UID;
    String FULLNAME;
    String ROLE;
    String CONTACTNUM;
    String PASSWORD;
    String LASTMESSAGE;
    String STATUS;
    String IMAGE;
    String Batch;
    String Class;

//

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEMAILID() {
        return EMAILID;
    }

    public void setEMAILID(String EMAILID) {
        this.EMAILID = EMAILID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    public String getCONTACTNUM() {
        return CONTACTNUM;
    }

    public void setCONTACTNUM(String CONTACTNUM) {
        this.CONTACTNUM = CONTACTNUM;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getLASTMESSAGE() {
        return LASTMESSAGE;
    }

    public void setLASTMESSAGE(String LASTMESSAGE) {
        this.LASTMESSAGE = LASTMESSAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }


}
