package com.code.Manage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.code.Hikers.RegisterInformation2;
import com.code.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Lottery extends AppCompatActivity {
private ArrayList<RegisterInformation2> arrayListUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        TextView textViewNumUesrs=findViewById(R.id.textNumUesrs);
        TextView textViewName=findViewById(R.id.textName);
        TextView textViewPohne=findViewById(R.id.textPohne);

        TextView textViewNumUesrWin=findViewById(R.id.textNumUser);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListUser=new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                   arrayListUser.add(child.getValue(RegisterInformation2.class));
                }
                textViewNumUesrs.setText("מספר משתמשים: "+ arrayListUser.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Button button=findViewById(R.id.buttonLottery);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numUsers=arrayListUser.size();
                int win = (int)(Math.random()*numUsers);
                textViewName.setText("הזוכה: "+arrayListUser.get(win).getName());
                textViewPohne.setText(arrayListUser.get(win).getEmail());
                win++;
                textViewNumUesrWin.setText("מספר משתמש של זוכה: "+win);

            }
        });


    }
}