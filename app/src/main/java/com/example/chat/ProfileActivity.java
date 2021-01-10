package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Hikers.RegisterInformation2;
import com.example.Hikers.RegisterLoginActivity;
import com.example.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String name = "";
    private String image = "";

    private String uidProfile;
    private String uidVisit;
private   RegisterInformation2 registerInformationProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        uidProfile = getIntent().getExtras().getString("profile_user_id");
        uidVisit = getIntent().getExtras().getString("visit_user_id");
        Button buttonSendMessage = findViewById(R.id.button_send_message);
        Button buttonBloke = findViewById(R.id.button_blok);
        Button buttonReport = findViewById(R.id.button_report_profile);
        if (uidProfile.equals(uidVisit)) {
            buttonBloke.setVisibility(View.GONE);
            buttonReport.setVisibility(View.GONE);
        } else {
            buttonReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this, ActivityFeedbackChat.class);
                    Message message = new Message();
                    message.setUid(uidProfile);
                    intent.putExtra("message", message);
                    intent.putExtra("flag", true);
                    startActivity(intent);
                }
            });
            buttonBloke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBloke();
                }
            });
        }
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uidProfile.equals(uidVisit)) {
                    Toast.makeText(ProfileActivity.this, "לא ניתן לשלוח הודעה לעצמך", Toast.LENGTH_SHORT).show();
                    return;
                }
                chakIfExistAndBlokeAndSend();




            }
        });

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uidProfile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CircleImageView circleImageView = findViewById(R.id.image_profile);
                TextView textViewName = findViewById(R.id.text_view_name_profile);
                TextView textViewPhone = findViewById(R.id.text_view_phone_profile);
                 registerInformationProfile = new RegisterInformation2();
                registerInformationProfile = snapshot.getValue(RegisterInformation2.class);
                textViewName.setText(registerInformationProfile.getName());
                name = registerInformationProfile.getName();
                if (name.isEmpty())
                    name = registerInformationProfile.getEmail();
                textViewPhone.setText(registerInformationProfile.getEmail());
                if (!registerInformationProfile.getImageUrl().isEmpty())
                    Glide.with(ProfileActivity.this)
                            .load(registerInformationProfile.getImageUrl())
                            .into(circleImageView);
                image = registerInformationProfile.getImageUrl();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void dialogBloke() {
        final Dialog d = new Dialog(ProfileActivity.this);
        d.setContentView(R.layout.dialog_bloke);
        d.setTitle("Manage");

        d.setCancelable(true);
        Button buttonBlokeDialog = d.findViewById(R.id.button_delete_dialog);
        Button buttonNoBloke = d.findViewById(R.id.button_exit_delete);
        buttonNoBloke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                return;
            }
        });
        buttonBlokeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Blocked")
                        .child(uidVisit).child(uidProfile);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(calendar.getTime());
                databaseReference.setValue(date+" "+uidVisit);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Blocked")
                        .child(uidProfile).child(registerInformationProfile.getEmail());
                databaseReference1.setValue(date+" "+uidVisit);
                Toast.makeText(ProfileActivity.this, "החשבון נחסם בהצלחה", Toast.LENGTH_LONG).show();

                d.dismiss();
            }

        });

        d.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_chat, menu);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.refresh);
        item.setTitle("לדף הראשי");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;


        switch (item.getItemId()) {

            case R.id.mainIconMenu:
                intent2 = new Intent(ProfileActivity.this, MainActivity3.class);
                startActivity(intent2);
                return true;

            case R.id.settingsMenu:
                intent2 = new Intent(ProfileActivity.this, ActivitySettings.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                intent2 = new Intent(ProfileActivity.this, RegisterLoginActivity.class);
                startActivity(intent2);
                return true;
            case R.id.refresh:


                intent2 = new Intent(ProfileActivity.this, MainActivity3.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void chakIfBlockAndSend() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocked")
                .child(uidVisit).child(registerInformationProfile.getEmail());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dialogIsBlocking();
                } else {
                    Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                    intent.putExtra("send_message_user_id", uidVisit);
                    intent.putExtra("to_message_user_id", uidProfile);
                    startActivityForResult(intent, 0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void dialogIsBlocking() {
        final Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.dialog_is_blocking);
        dialog.setTitle("Manage");

        dialog.setCancelable(true);
        Button buttonClose = dialog.findViewById(R.id.button_close_window);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    private void chakIfExistAndBlokeAndSend() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInformation2")
                .child(uidProfile);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   chakIfBlockAndSend();
                } else {
                  dialogIsBlocking();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}