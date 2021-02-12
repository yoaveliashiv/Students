package com.code.game;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Player {
    private String uid = "";
    private String name = "";
    private LocationMy location=new LocationMy();

    public Player() {
    }

    public Player(String uid, String name, LocationMy location) {
        this.uid = uid;
        this.name = name;
        this.location = location;
    }

    public Player(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationMy getLocation() {
        return location;
    }

    public void setLocation(LocationMy location) {
        this.location = location;
    }
}
