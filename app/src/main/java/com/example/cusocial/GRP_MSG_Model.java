package com.example.cusocial;

public class GRP_MSG_Model {
    String MESSAGE, SENDER_UID,Photo,TimeStamp,SENDER_USERNAME,Message_id;

    public GRP_MSG_Model() {
    }

    public String getMessage_id() {
        return Message_id;
    }

    public void setMessage_id(String message_id) {
        Message_id = message_id;
    }

    public String getSENDER_USERNAME() {
        return SENDER_USERNAME;
    }

    public void setSENDER_USERNAME(String SENDER_USERNAME) {
        this.SENDER_USERNAME = SENDER_USERNAME;
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

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public GRP_MSG_Model(String MESSAGE, String SENDER_UID, String photo, String timeStamp,String SENDER_USERNAME,String Message_id) {
        this.MESSAGE = MESSAGE;
        this.SENDER_UID = SENDER_UID;
        Photo = photo;
        TimeStamp = timeStamp;
        this.SENDER_USERNAME = SENDER_USERNAME;
        this.Message_id = Message_id;
    }
}
