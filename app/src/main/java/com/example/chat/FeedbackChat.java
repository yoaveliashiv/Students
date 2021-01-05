package com.example.chat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FeedbackChat {

    private String feedback="";
    private String uidReportingOn="";
    private String uidReportingSend="";
    private int id=-1;
    private String key="";
    private String type="";
    private Message message=null;




    public FeedbackChat() {
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUidReportingOn() {
        return uidReportingOn;
    }

    public void setUidReportingOn(String uidReportingOn) {
        this.uidReportingOn = uidReportingOn;
    }

    public String getUidReportingSend() {
        return uidReportingSend;
    }

    public void setUidReportingSend(String uidReportingSend) {
        this.uidReportingSend = uidReportingSend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
