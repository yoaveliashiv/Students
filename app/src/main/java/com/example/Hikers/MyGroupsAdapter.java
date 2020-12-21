package com.example.Hikers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.R;
import com.example.students.CardTutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class MyGroupsAdapter extends ArrayAdapter<String> {

    private ImageView ivProduct;
    private Context context;
    private List<String> objects;
    private CardTutor temp;
    private boolean go = false;
    private View view;
    private boolean flagSee = false;
    String email = "";


    public MyGroupsAdapter(Context context, int resource, int textViewResourceId, List<String> objects, String email) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = false;
        this.context = context;
        this.objects = objects;
        this.email = email;

    }

    public MyGroupsAdapter(Context context, int resource, int textViewResourceId, List<String> objects, boolean flagSee) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = flagSee;
        this.context = context;
        this.objects = objects;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.list_view_groups, parent, false);

        final TextView textView = (TextView) view.findViewById(R.id.textViewNoJoin);
        TextView textViewGrope = (TextView) view.findViewById(R.id.textViewNameGroup);
        textViewGrope.setText(objects.get(position));
         Button buttonJoin = (Button) view.findViewById(R.id.buttonJoinGrop);
        buttonJoin.setVisibility(View.GONE);
        Button buttonIn = (Button) view.findViewById(R.id.buttonInChat);
        buttonIn.setText("כניסה");
        buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ListView lv1 = (ListView) view.findViewById(R.id.listViewChat);

        return view;
    }


}