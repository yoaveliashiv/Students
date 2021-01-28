package com.code.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.code.R;
import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsAdapter extends ArrayAdapter<Contact> {

    private ImageView ivProduct;
    private Context context;
    private List<Contact> listContact;
    private boolean go = false;
    private View view;
    private boolean flagSee = false;
    private String uidVist;
    private Contact contact;


    public GroupsAdapter(Context context, int resource, int textViewResourceId, List<Contact> listContact
            , String uidVist) {
        super(context, resource, textViewResourceId, listContact);
        this.flagSee = false;
        this.context = context;
        this.listContact = listContact;
        this.uidVist = uidVist;

    }

    public GroupsAdapter(Context context, int resource, int textViewResourceId, List<Contact> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.listContact = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.list_and_group, parent, false);
        contact = listContact.get(position);

        TextView textViewName = (TextView) view.findViewById(R.id.textView_name_list);

        textViewName.setText(contact.getName());
        if (!contact.getImage().isEmpty()) {
            CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.circleimageview_list);

            Glide.with(view)
                    .load(contact.getImage())
                    .into(circleImageView);
        }
        TextView textViewTime = (TextView) view.findViewById(R.id.textView_time);
        String date2 = "";
        String time2 = "";

        if(contact.getMessage().getDateTimeZone()!=null){
            long d=getTimestamp(contact.getMessage().getDateTimeZone());
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ssZ");
            String dateString = format.format(new Date(d));
            format.setTimeZone(TimeZone.getTimeZone("GMT"));

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

        }



            String name = contact.getMessage().getName();
        if(name.isEmpty())
            name=contact.getMessage().getPhone();
        TextView textViewMessage = (TextView) view.findViewById(R.id.textView_message_list);

        textViewMessage.setText(name + ": " + contact.getMessage().getMessage());
        if (name.isEmpty())
            textViewMessage.setText("");

        if (contact.getNotifications() > 0) {
            textViewTime.setText(""+time2+" "+date2 + "\n" + contact.getNotifications() + " הודעות חדשות");
            textViewTime.setTextSize(15);
            textViewTime.setTextColor(Color.BLUE);
            return view;

        }
        textViewTime.setText(""+time2+" "+date2 );
        return view;
    }
    @Exclude
    public long getTimestamp(Object timestamp) {
        return (long) timestamp;
    }

}