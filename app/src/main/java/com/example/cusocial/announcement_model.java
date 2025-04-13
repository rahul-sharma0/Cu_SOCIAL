package com.example.cusocial;

public class announcement_model {
    String title, body, sendername,currentDateandTime;

    public announcement_model(String title, String body, String sendername, String currentDateandTime) {
        this.title = title;
        this.body = body;
        this.sendername = sendername;
        this.currentDateandTime = currentDateandTime;
    }

    public String getCurrentDateandTime() {
        return currentDateandTime;
    }

    public void setCurrentDateandTime(String currentDateandTime) {
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

   public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public announcement_model() {
    }
}
