package com.code.Chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.code.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyGroupsAdapter extends ArrayAdapter<Contact> {

    private ImageView ivProduct;
    private Context context;
    private List<Contact> listContact;
    private boolean go = false;
    private View view;
    private boolean flagSee = false;
    private String uidVist;
    private Contact contact;


    public MyGroupsAdapter(Context context, int resource, int textViewResourceId, List<Contact> listContact
            , String uidVist) {
        super(context, resource, textViewResourceId, listContact);
        this.flagSee = false;
        this.context = context;
        this.listContact = listContact;
        this.uidVist = uidVist;

    }

    public MyGroupsAdapter(Context context, int resource, int textViewResourceId, List<Contact> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.listContact = objects;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.list_contacts_and, parent, false);
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

        TextView textViewTime = (TextView) view.findViewById(R.id.textView_time);

        if (contact.getNotifications() > 0) {
            textViewTime.setText(contact.getMessage().getTime() + "\n" + contact.getNotifications() + " הודעות חדשות");
            textViewTime.setTextSize(15);
            textViewTime.setTextColor(Color.BLUE);
            return view;

        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups")
                .child(contact.getName());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        textViewTime.setText(contact.getMessage().getTime());
        return view;
    }


}