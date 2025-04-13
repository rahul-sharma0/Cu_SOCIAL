package com.example.cusocial;

public class Events_Model {
    String title, body, event_from,event_till,sendername,currentDateandTime;

    public Events_Model() {
    }

    public Events_Model(String title, String body, String event_from, String event_till, String sendername, String currentDateandTime) {
        this.title = title;
        this.body = body;
        this.event_from = event_from;
        this.event_till = event_till;
        this.sendername = sendername;
        this.currentDateandTime = currentDateandTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEvent_from() {
        return event_from;
    }

    public void setEvent_from(String event_from) {
        this.event_from = event_from;
    }

    public String getEvent_till() {
        return event_till;
    }

    public void setEvent_till(String event_till) {
        this.event_till = event_till;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getCurrentDateandTime() {
        return currentDateandTime;
    }

    public void setCurrentDateandTime(String currentDateandTime) {
        this.currentDateandTime = currentDateandTime;
    }
}
