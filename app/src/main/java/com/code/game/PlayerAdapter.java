package com.code.game;

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
import com.code.chat.Contact;
import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerAdapter extends ArrayAdapter<Player> {

    private ImageView ivProduct;
    private Context context;
    private List<Player> listContact;
    private boolean go = false;
    private View view;
    private boolean flagSee = false;
    private LocationMy locationMy;
    private Player player;


    public PlayerAdapter(Context context, int resource, int textViewResourceId, List<Player> listContact
            , LocationMy locationMy) {
        super(context, resource, textViewResourceId, listContact);
        this.context = context;
        this.listContact = listContact;
        this.locationMy=locationMy;

    }

    public PlayerAdapter(Context context, int resource, int textViewResourceId, List<Player> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.listContact = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.list_player, parent, false);
        player = listContact.get(position);
        TextView textView = (TextView)view.findViewById(R.id.textView_name_player);
        if(player.getLocation().getLongitude()!=0&&locationMy.getLongitude()!=0) {
            int distnsce=locationMy.distance(player.getLocation())/1000;
            if (distnsce<1)
                distnsce=1;
            textView.setText("שם: " + player.getName() + "  מרחק ממך:" +distnsce );
        }
        else
            textView.setText(""+player.getName());



        return view;
    }

    @Exclude
    public long getTimestamp(Object timestamp) {
        return (long) timestamp;
    }

}