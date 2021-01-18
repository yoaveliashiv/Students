package com.code.chat;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Message implements Serializable {

    private String message = "";
    private String imageMessage = "";
    private String name = "";
    private String phone = "";
    private String date = "";
    private String time = "";
    private Object dateTimeZone=null;
    private String uid = "";
    private int id = 0;
    private String deviceToken = "";

    public Message() {
    }

    public Message(Message message) {
        this.message = message.message;
        this.name = message.name;
        this.date = message.date;
        this.time = message.time;
        this.phone = message.phone;
        this.uid = message.uid;
        this.id = message.id;
        this.dateTimeZone=message.dateTimeZone;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }

    public Object getDateTimeZone() {
        return dateTimeZone;
    }

    public void setDateTimeZone(Object dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }
}
