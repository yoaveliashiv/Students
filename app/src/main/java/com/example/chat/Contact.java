package com.example.chat;

public class Contact {

    private Message message;
    private String name = "";
    private String image = "";
    private String uid = "";


    public Contact() {
    }

    public Message getMessage() {
        return message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
