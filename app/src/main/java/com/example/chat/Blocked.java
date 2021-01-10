package com.example.chat;

import java.util.ArrayList;

public class Blocked {
    private String date="";
    private String Cause_of_blockage="";
private String phone="";
    public Blocked() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCause_of_blockage() {
        return Cause_of_blockage;
    }

    public void setCause_of_blockage(String cause_of_blockage) {
        Cause_of_blockage = cause_of_blockage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
