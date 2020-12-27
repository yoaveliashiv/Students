package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Hikers.MainActivity2;
import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.example.students.MainActivityCardView;
import com.example.students.MainActivityRegisterTutor;
import com.example.students.RegisterInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String name = "";
    private String image = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final String uidProfile = getIntent().getExtras().getString("profile_user_id");
        final String uidVisit = getIntent().getExtras().getString("visit_user_id");
        Button buttonSendMessage = findViewById(R.id.button_send_message);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uidProfile.equals(uidVisit)) {
                    Toast.makeText(ProfileActivity.this, "לא ניתן לשלוח הודעה לעצמך", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                intent.putExtra("send_message_user_id", uidVisit);
                intent.putExtra("to_message_user_id", uidProfile);
                intent.putExtra("image", image);

                intent.putExtra("name", name);
                startActivityForResult(intent, 0);
            }
        });

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uidProfile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CircleImageView circleImageView = findViewById(R.id.image_profile);
                TextView textViewName = findViewById(R.id.text_view_name_profile);
                TextView textViewPhone = findViewById(R.id.text_view_phone_profile);
                RegisterInformation2 registerInformation = new RegisterInformation2();
                registerInformation = snapshot.getValue(RegisterInformation2.class);
                textViewName.setText(registerInformation.getName());
                name = registerInformation.getName();
                if (name.isEmpty())
                    name = registerInformation.getEmail();
                textViewPhone.setText(registerInformation.getEmail());
                if (!registerInformation.getImageUrl().isEmpty())
                    Glide.with(ProfileActivity.this)
                        .load(registerInformation.getImageUrl())
                        .into(circleImageView);
                image=registerInformation.getImageUrl();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}