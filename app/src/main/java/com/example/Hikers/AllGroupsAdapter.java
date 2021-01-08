package com.example.Hikers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.students.CardTutor;
import com.example.students.ExpertiseAdapter;
import com.example.students.MainActivityCardView;
import com.example.students.MainActivityRegisterTutor;
import com.example.R;
import com.example.students.RegisterInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class AllGroupsAdapter extends ArrayAdapter<String> {

    private ImageView ivProduct;
    private Context context;
    private List<String> objects;
    private CardTutor temp;
    private boolean go = false;
    private View view;
    private boolean flagSee = false;
    String email = "";


    public AllGroupsAdapter(Context context, int resource, int textViewResourceId, List<String> objects, String email) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = false;
        this.context = context;
        this.objects = objects;
        this.email = email;

    }

    public AllGroupsAdapter(Context context, int resource, int textViewResourceId, List<String> objects, boolean flagSee) {
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
        final Button buttonJoin = (Button) view.findViewById(R.id.buttonJoinGrop);
         Button buttonIn = (Button) view.findViewById(R.id.buttonInChat);
buttonIn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(email.length()==0){

    textView.setVisibility(View.VISIBLE);
    buttonJoin.setError("Enter valid code");
    buttonJoin.requestFocus();
    return;
}
                DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");

                cardRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key = "";
                        RegisterInformation2 registerInformation = new RegisterInformation2();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            registerInformation = child.getValue(RegisterInformation2.class);
                            key = child.getKey();
                        }
                        DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
                        cardRef.child(key).setValue(registerInformation);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        ListView lv1 = (ListView) view.findViewById(R.id.listViewChat);

        return view;
    }


}