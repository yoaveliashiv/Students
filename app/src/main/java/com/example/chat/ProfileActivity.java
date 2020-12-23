package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.example.students.RegisterInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String uidProfile = getIntent().getExtras().getString("profile_user_id");
        final String uidVisit = getIntent().getExtras().getString("visit_user_id");


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uidProfile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CircleImageView circleImageView = findViewById(R.id.image_profile);
                TextView textViewName = findViewById(R.id.text_view_name_profile);
                TextView textViewPhone = findViewById(R.id.text_view_phone_profile);
                RegisterInformation2 registerInformation = new RegisterInformation2();
                registerInformation = snapshot.getValue(RegisterInformation2.class);
//                if (registerInformation.getName().isEmpty()) textViewName.setVisibility(View.GONE);
//                else textViewName.setText(registerInformation.getName());
                textViewPhone.setText(registerInformation.getEmail());
              if(!registerInformation.getImageUrl().isEmpty())  Glide.with(ProfileActivity.this)
                        .load(registerInformation.getImageUrl())
                        .into(circleImageView);
              textViewName.setText(uidProfile);
              textViewPhone.setText(uidVisit);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}