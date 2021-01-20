package com.code.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.URISyntaxException;

public class Blocked {
    private String date = "";
    private String Cause_of_blockage = "";
    private String phone = "";
    private String uidBloked = "";
    private Boolean IBloked = false;

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

    public String getUidBloked() {
        return uidBloked;
    }

    public void setUidBloked(String uidBloked) {
        this.uidBloked = uidBloked;
    }

    public Boolean getIBloked() {
        return IBloked;
    }

    public void setIBloked(Boolean IBloked) {
        this.IBloked = IBloked;
    }
}
