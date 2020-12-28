package com.example.chat;

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
import com.example.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

        textViewName.setText(listLink.get(position).getNameGroup());



        TextView textViewLink = (TextView) view.findViewById(R.id.textView_link);
       textViewLink.setText(listLink.get(position).getLink());
        return view;
    }


}