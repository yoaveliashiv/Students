package com.example.chat;


import java.util.Comparator;

public class ComperatorContact implements Comparator<Contact> {
    public ComperatorContact() {

    }

    public int compare(Contact o1, Contact o2) {
        if(o1.getMessage().getTime().isEmpty())
            return 1;
        if(o2.getMessage().getTime().isEmpty())
            return -1;
        return dateSearch(o1.getMessage().getDate(), o1.getMessage().getTime()
                , o2.getMessage().getDate(), o2.getMessage().getTime());
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

}
