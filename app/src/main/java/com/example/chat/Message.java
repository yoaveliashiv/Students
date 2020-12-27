package com.example.chat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {
    private String message="";
    private String name="";
    private String phone="";
    private String date="";
    private String time="";
    private String uid="";
    private int id=0;
    public Message() {
    }
    public Message(Message message) {
        this.message=message.message;
        this.name=message.name;
        this.date=message.date;
        this.time=message.time;
        this.phone=message.phone;
        this.uid=message.uid;
        this.id=message.id;

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
}
