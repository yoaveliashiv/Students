package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Hikers.MainActivityPageUser2;
import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.example.students.MainActivityRegisterTutor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//<TextView
//                android:id="@+id/text_group_display"
//                        android:layout_width="match_parent"
//                        android:layout_height="wrap_content"
//                        android:layout_marginStart="2dp"
//                        android:layout_marginEnd="2dp"
//                        android:layout_marginBottom="50dp"
//                        android:hint=""
//                        android:padding="10dp"
//                        android:textAllCaps="false"
//                        android:textColor="@color/cardview_dark_background"
//                        android:textSize="20sp" />
public class GroupChatActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private String nameGroup;
    private RegisterInformation2 registerInformation;
    private String uid ;
    private DatabaseReference databaseReference;
    private ArrayList<Message> arrayListMessage;
    private MessageAdapter arrayAdapter;
    EditText editText;
    ListView listView;

    // private TextView textViewDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nameGroup = getIntent().getExtras().getString("nameGroup");
        Boolean flag=getIntent().getExtras().getBoolean("flagAllGroup");

        databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups").child(nameGroup);

        // textViewDisplay=findViewById(R.id.text_group_display);
        ImageButton imageButton = findViewById(R.id.imageButtonSendMassege);
        scrollView = findViewById(R.id.scrollView);
        editText = findViewById(R.id.editTextMssege);
        getSupportActionBar().setTitle(nameGroup);


        getUserInfo();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sms = editText.getText().toString();
                if (sms.isEmpty()) {
                    editText.setError("אנא הכנס הודעה");
                    editText.requestFocus();
                    return;
                }

                saveDatabase(sms, nameGroup);
                editText.setText("");
            }
        });
        listView = findViewById(R.id.list_view_message);

        // String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        arrayListMessage = new ArrayList<>();

        arrayAdapter = new MessageAdapter(GroupChatActivity.this, 0,0, arrayListMessage,uid);
        listView.setAdapter(arrayAdapter);
        final Button button=findViewById(R.id.button_goin);
        if(!flag)button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("MyGroups")
                        .child(uid).child(nameGroup);
                databaseReference1.setValue("");
                button.setVisibility(View.GONE);
                Toast.makeText(GroupChatActivity.this, "הצטרפת לקבוצה בהצלחה", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void saveDatabase(String sms, String nameGroup) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());

        Message message = new Message();
        message.setDate(date);
        message.setMessage(sms);
        message.setName(registerInformation.getName());
        message.setPhone(registerInformation.getEmail());
        message.setUid(uid);
        message.setTime(time);
        databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups").child(nameGroup).push();

//databaseReference.getKey();
        databaseReference.setValue(message);

    }

//    private String getPhoneUser() {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String email = "";
//        if (firebaseUser != null) {
//            try {
//                if (!firebaseUser.getEmail().toString().isEmpty())
//                    email = firebaseUser.getEmail().toString();
//                else email = firebaseUser.getPhoneNumber().toString();
//            } catch (RuntimeException e) {
//                email = firebaseUser.getPhoneNumber().toString();
//            }
//
//        }
//        return email;
//    }

    private void getUserInfo() {

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerInformation = new RegisterInformation2();
                registerInformation = snapshot.getValue(RegisterInformation2.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    displayMessage(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    displayMessage(snapshot);
                }
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
    }

    private void displayMessage(DataSnapshot snapshot) {

        Message message;

        message = snapshot.getValue(Message.class);
//        String sms;
//        if (!message.getName().isEmpty())
//            sms = message.getName() + " :\n" + message.getMessage() + "\n" + message.getTime() + "\n";
//        else
//            sms = message.getPhone() + " :\n" + message.getMessage() + "\n" + message.getTime() + "\n";
        //   textViewDisplay.setHint(message.getUid());
        //  textViewDisplay.append(sms);
        arrayListMessage.add(message);

        arrayAdapter.notifyDataSetChanged();
// Force scroll to the top of the scroll view.
// Because, when the list view gets loaded it focuses the list view
// automatically at the bottom of this page.
        listView.setSelection(listView.getAdapter().getCount()-1);


    }
    private void saveNotifications() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NotificationsGroups");
        databaseReference.child(uid).child(nameGroup).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int num = snapshot.getValue(Integer.class);
                    num++;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");
                    databaseReference.child(uid).child(nameGroup).setValue(num);
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");
                    databaseReference.child(uid).child(nameGroup).setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}