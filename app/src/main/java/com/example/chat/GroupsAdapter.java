package com.example.chat;

import android.app.Activity;
import android.content.Context;
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


        String name = contact.getMessage().getName();
        TextView textViewMessage = (TextView) view.findViewById(R.id.textView_message_list);
        textViewMessage.setText(name + ": " + contact.getMessage().getMessage());

//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyGroups");
//            databaseReference.child(contact.getUid()).child().addListenerForSingleValueEvent(new);

        TextView textViewTime = (TextView) view.findViewById(R.id.textView_time);
        textViewTime.setText(contact.getMessage().getTime());
        return view;
    }


}