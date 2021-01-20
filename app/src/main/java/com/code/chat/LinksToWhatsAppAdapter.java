package com.code.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.code.R;
import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class LinksToWhatsAppAdapter extends ArrayAdapter<LinksToWhatsApp> {

    private Context context;
    private List<LinksToWhatsApp> listLink;
    private View view;




    public LinksToWhatsAppAdapter(Context context, int resource, int textViewResourceId, List<LinksToWhatsApp> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.listLink = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.link_list, parent, false);

        TextView textViewName = (TextView) view.findViewById(R.id.textView_name_group);

        if(!listLink.get(position).getNumOfMembers().isEmpty()){


        long d=getTimestamp(listLink.get(position).getDateUpdateNumOfMembers());
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ssZ");
        String dateString = format.format(new Date(d));
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date2 = "";
        String time2 = "";
        try {
            Date value = format.parse(dateString);

            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd/MM");
            SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");
            simpleDateFormatDate.setTimeZone(TimeZone.getDefault());
            simpleDateFormatTime.setTimeZone(TimeZone.getDefault());

            date2 = simpleDateFormatDate.format(value);
            time2 = simpleDateFormatTime.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

            textViewName.setText(listLink.get(position).getNameGroup()+"  מספר משתתפים:"+
                    listLink.get(position).getNumOfMembers()+" נכון ל- "+time2+" "+date2 );

        }
        else
        textViewName.setText(listLink.get(position).getNameGroup());



        TextView textViewLink = (TextView) view.findViewById(R.id.textView_link);
       textViewLink.setText(listLink.get(position).getLink());
        return view;
    }
    @Exclude
    public long getTimestamp(Object timestamp) {
        return (long) timestamp;
    }


}