package com.code.game;


import com.code.chat.Contact;
import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class ComperatorLocation implements Comparator<Player> {
    private Player my;

    public ComperatorLocation(Player my) {
        this.my = my;
    }
    public ComperatorLocation() {

    }

    public int compare(Player o1, Player o2) {
        if (o2.getLocation().getLongitude() == 0)
            return -1;
        if (o1.getLocation().getLongitude() == 0)
            return 1;
        if (o1.getLocation().distance(my.getLocation())<o2.getLocation().distance(my.getLocation()))
            return  -1;

            return 1;

    }




}
