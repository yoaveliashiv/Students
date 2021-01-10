package com.example.chat;

public class Contact {

    private Message message;
    private String name = "";
    private String image = "";
    private String uidContacts = "";
    private String uidI = "";
    private String phoneContacts = "";
    private int notifications = 0;

    public Contact() {
    }

    public Message getMessage() {
        return message;
    }



    public void setMessage(Message message) {
        this.message=new Message(message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public String getUidContacts() {
        return uidContacts;
    }

    public void setUidContacts(String uidContacts) {
        this.uidContacts = uidContacts;
    }

    public String getUidI() {
        return uidI;
    }

    public void setUidI(String uidI) {
        this.uidI = uidI;
    }

    public String getPhoneContacts() {
        return phoneContacts;
    }

    public void setPhoneContacts(String phoneContacts) {
        this.phoneContacts = phoneContacts;
    }
}
