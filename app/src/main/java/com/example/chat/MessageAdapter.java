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

import com.bumptech.glide.load.engine.Resource;
import com.example.R;
import com.example.students.MainActivity1;
import com.example.students.MainActivityCardView;

import java.util.List;

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
        if (!listMessage.get(position).getUid().equals(uidVist))
            view = layoutInflater.inflate(R.layout.message, parent, false);
        else
            view = layoutInflater.inflate(R.layout.message_i_am, parent, false);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        TextView textViewName = (TextView) view.findViewById(R.id.textView_name);
        TextView textViewMessage = (TextView) view.findViewById(R.id.textView_message);
        TextView textViewTime = (TextView) view.findViewById(R.id.textView_time);
        TextView textViewPhone = (TextView) view.findViewById(R.id.textView_phone);
String name=listMessage.get(position).getName();
        String message=listMessage.get(position).getMessage();
        String phone=listMessage.get(position).getPhone();

        textViewTime.setText(listMessage.get(position).getTime());
        textViewName.setText(name );

        textViewMessage.setText(listMessage.get(position).getMessage());
        textViewPhone.setText(phone);

//if(listMessage.get(position).getUid().equals(uidVist))
//    relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorBackgroundMessageMe));

        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profile_user_id", listMessage.get(position).getUid());
                intent.putExtra("visit_user_id", uidVist);
                ((Activity) context).startActivityForResult(intent, 0);
            }
        });
        textViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profile_user_id", listMessage.get(position).getUid());
                intent.putExtra("visit_user_id", uidVist);
                ((Activity) context).startActivityForResult(intent, 0);
            }
        });


        return view;
    }


}