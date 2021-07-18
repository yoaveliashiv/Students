package com.code.Chat;


import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class ComperatorContact implements Comparator<Contact> {
    public ComperatorContact() {

    }

    public int compare(Contact o1, Contact o2) {
        if(o1.getMessage().getTime().isEmpty())
            return 1;
        if(o2.getMessage().getTime().isEmpty())
            return -1;
        long d=getTimestamp(o1.getMessage().getDateTimeZone());
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateString = format.format(new Date(d));
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date2 = "";
        String time2 = "";
        try {
            Date value = format.parse(dateString);

            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss.SSS");
            simpleDateFormatDate.setTimeZone(TimeZone.getDefault());
            simpleDateFormatTime.setTimeZone(TimeZone.getDefault());

            date2 = simpleDateFormatDate.format(value);
            time2 = simpleDateFormatTime.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
         d=getTimestamp(o2.getMessage().getDateTimeZone());
         format = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSZ");
         dateString = format.format(new Date(d));
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date3 = "";
        String time3 = "";
        try {
            Date value = format.parse(dateString);

            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss.SSS");
            simpleDateFormatDate.setTimeZone(TimeZone.getDefault());
            simpleDateFormatTime.setTimeZone(TimeZone.getDefault());

            date3 = simpleDateFormatDate.format(value);
            time3 = simpleDateFormatTime.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateSearch(date2, time2
                , date3, time3);
    }

    public int dateSearch(String o1_date, String o1_time, String o2_date, String o2_time) {
//        Toast.makeText(MainActivity1.this, "" + dateStartUser + " " + dateEndUser + ";" + dateStart + " " + dateEnd, Toast.LENGTH_LONG).show();
//        final Button buttonData = findViewById(R.id.buttonDate2);
//        buttonData.setTextSize(8);

        int yearStartUser = Integer.valueOf(o1_date.substring(6));
        int monthStartUser = Integer.valueOf(o1_date.substring(3, 5));
        int dayStartUser = Integer.valueOf(o1_date.substring(0, 2));

        int yearEnd = Integer.valueOf(o2_date.substring(6));
        int monthEnd = Integer.valueOf(o2_date.substring(3, 5));
        int dayEnd = Integer.valueOf(o2_date.substring(0, 2));

        if (yearEnd > yearStartUser)
            return 1;
        if (yearEnd == yearStartUser && monthEnd > monthStartUser)
            return 1;
        if (yearEnd == yearStartUser && monthEnd == monthStartUser && dayEnd > dayStartUser)
            return 1;
        if (yearEnd == yearStartUser && monthEnd == monthStartUser && dayEnd == dayStartUser){
            if (o2_time.compareTo(o1_time)>0)return 1;
            if (o2_time.compareTo(o1_time)==0)return 0;

        }



        return -1;
    }


    @Exclude
    public long getTimestamp(Object timestamp) {
        return (long) timestamp;
    }
}
