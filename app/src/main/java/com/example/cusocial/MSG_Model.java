package com.example.cusocial;

public class MSG_Model {
    String MESSAGE, SENDER_UID;
    String TimeStamp, MESSAGE_ID;



    public MSG_Model() {

    }

    public MSG_Model(String MESSAGE, String SENDER_UID, String timeStamp, String MESSAGE_ID) {
        this.MESSAGE = MESSAGE;
        this.SENDER_UID = SENDER_UID;
        this.TimeStamp = timeStamp;
        this.MESSAGE_ID=MESSAGE_ID;
    }

    public String getMESSAGE_ID() {
        return MESSAGE_ID;
    }

    public void setMESSAGE_ID(String MESSAGE_ID) {
        this.MESSAGE_ID = MESSAGE_ID;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getSENDER_UID() {
        return SENDER_UID;
    }

    public void setSENDER_UID(String SENDER_UID) {
        this.SENDER_UID = SENDER_UID;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }
}
