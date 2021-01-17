package com.example.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.example.R;
import com.example.students.MainActivity1;
import com.example.students.MainActivityCardView;
import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MessageAdapter extends ArrayAdapter<Message> {

    private ImageView ivProduct;
    private Context context;
    private List<Message> listMessage;
    private boolean go = false;
    private View view;
    private boolean flagSee = false;
    private String uidVist;


    public MessageAdapter(Context context, int resource, int textViewResourceId, List<Message> messageList
            , String uidVist) {
        super(context, resource, textViewResourceId, messageList);
        this.flagSee = false;
        this.context = context;
        this.listMessage = messageList;
        this.uidVist = uidVist;

    }

    public MessageAdapter(Context context, int resource, int textViewResourceId, List<Message> objects, boolean flagSee) {
        super(context, resource, textViewResourceId);
        this.flagSee = flagSee;
        this.context = context;
        this.listMessage = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        if(listMessage.get(position).getMessage().isEmpty()&& listMessage.get(position).getTime().isEmpty()) {
            view = layoutInflater.inflate(R.layout.message_dont_reed_num, parent, false);
        }
        else{
            if (!listMessage.get(position).getUid().equals(uidVist))
                view = layoutInflater.inflate(R.layout.message, parent, false);
            else
                view = layoutInflater.inflate(R.layout.message_i_am, parent, false);


            TextView textViewName = (TextView) view.findViewById(R.id.textView_name);
            TextView textViewMessage = (TextView) view.findViewById(R.id.textView_message);

            if (!listMessage.get(position).getImageMessage().isEmpty()) {
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                Glide.with(view)
                        .load(listMessage.get(position).getImageMessage())
                        .into(imageView);
                textViewMessage.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            long d=getTimestamp(listMessage.get(position).getDateTimeZone());
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



            String name = listMessage.get(position).getName();
            String message = listMessage.get(position).getMessage();
            // String phone = listMessage.get(position).getPhone();
            String time = listMessage.get(position).getTime();
            String date = listMessage.get(position).getDate().substring(0, 5);
            textViewName.setText(name + "   " + time2 + " " + date2 + " ");

            textViewMessage.setText(message + " ");
            return view;
        }
        TextView textViewName = (TextView) view.findViewById(R.id.textView_num_dont_see);
        textViewName.setText(""+listMessage.get(position).getId()+" הודעות חדשות");


        return view;
    }
    @Exclude
    public long getTimestamp(Object timestamp) {
        return (long) timestamp;
    }

}