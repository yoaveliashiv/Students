package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RegisterInformation2 registerInformationSend;
    private RegisterInformation2 registerInformationRecive;
    private Menu menu2;
    private DatabaseReference databaseReference;
    private ArrayList<Message> arrayListMessage;
    private MessageAdapter arrayAdapter;
    private EditText editText;
    private ListView listView;
    private String uidSend;
    private String uidRecive;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        circleImageView = findViewById(R.id.profile_image);

        getSupportActionBar().hide();
        uidSend = getIntent().getExtras().getString("send_message_user_id");
        uidRecive = getIntent().getExtras().getString("to_message_user_id");

//<include
//        android:id="@+id/chat_toolbar"
//        layout="@layout/app_bar_layout"
//                ></include>
//        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
//        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View actionBarView = layoutInflater.inflate(R.layout.chat_bar, null);
//        actionBar.setCustomView(actionBarView);

//        CircleImageView circleImageView=findViewById(R.id.profile_image);
//        TextView textViewName=findViewById(R.id.textView_name_chat);
//        textViewName.setText(name);
//        if(!image.isEmpty())
//        Glide.with(ChatActivity.this)
//                .load(registerInformation.getImageUrl())
//                .into(circleImageView);
//        else
//            Glide.with(ChatActivity.this)
//                    .load(R.drawable.profile_image)
//                    .into(circleImageView);
        // getSupportActionBar().setTitle(name);
        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts").child(uidSend).child(uidRecive);

        ImageButton imageButton = findViewById(R.id.imageButtonSendMassege);
        editText = findViewById(R.id.editTextMssege);

        getUserReciveInfo();
        getUserSendInfo();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sms = editText.getText().toString();
                if (sms.isEmpty()) {
                    editText.setError("אנא הכנס הודעה");
                    editText.requestFocus();
                    return;
                }

                saveDatabase(sms);
                saveNotifications();
                editText.setText("");
            }
        });
        listView = findViewById(R.id.list_view_message);

        // String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        arrayListMessage = new ArrayList<>();

        arrayAdapter = new MessageAdapter(ChatActivity.this, 0, 0, arrayListMessage, uidSend);
        listView.setAdapter(arrayAdapter);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                intent.putExtra("profile_user_id", uidRecive);
                intent.putExtra("visit_user_id", uidSend);
                startActivity(intent);
            }
        });
    }


    private void getUserReciveInfo() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uidRecive).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerInformationRecive = new RegisterInformation2();
                registerInformationRecive = snapshot.getValue(RegisterInformation2.class);


                TextView textViewName = findViewById(R.id.textView_name_chat);
                textViewName.setText(registerInformationRecive.getName());
                if (!registerInformationRecive.getImageUrl().isEmpty()) {
                    Glide.with(ChatActivity.this)
                            .load(registerInformationRecive.getImageUrl())
                            .into(circleImageView);
                }
                // final MenuItem menuItem = menu2.findItem(R.id.mainIconMenu2);


//                Glide.with(ChatActivity.this).asDrawable()
//                        .load(registerInformationRecive.getImageUrl()).into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        menuItem.setIcon(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });

                //  Drawable myDrawable = imageView.getDrawable();

                //  menuItem.setIcon(R.drawable.profile_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveDatabase(String sms) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());

        Message message = new Message();
        message.setDate(date);
        message.setMessage(sms);
        message.setName(registerInformationSend.getName());
        message.setPhone(registerInformationSend.getEmail());
        message.setUid(uidSend);
        message.setTime(time);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Contacts")
                .child(uidSend).child(uidRecive).push();
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Contacts")
                .child(uidRecive).child(uidSend).push();

//databaseReference.getKey();
        databaseReference1.setValue(message);
        databaseReference2.setValue(message);


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

    private void getUserSendInfo() {

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uidSend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerInformationSend = new RegisterInformation2();
                registerInformationSend = snapshot.getValue(RegisterInformation2.class);


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
        listView.setSelection(listView.getAdapter().getCount() - 1);


    }

    //    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.manu_chat, menu);
//        menu2 = menu;
//        getUserReciveInfo();
////            if(!registerInformationRecive.getImageUrl().isEmpty())
////        Glide.with(this).asBitmap().load(registerInformationRecive.getImageUrl()).into(new CustomTarget<Bitmap>() {
////            @Override
////            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
////            }
////
////            @Override
////            public void onLoadCleared(@Nullable Drawable placeholder) {
////
////            }
////        });
//
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent intent;
//        switch (item.getItemId()) {
//            case R.id.mainIconMenu:
//                intent = new Intent(ChatActivity.this, ProfileActivity.class);
//                intent.putExtra("profile_user_id", uidSend);
//                intent.putExtra("visit_user_id", uidRecive);
//                startActivity(intent);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    private void saveNotifications() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NotificationsPrivate");
        databaseReference.child(uidRecive).child(uidSend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int num = snapshot.getValue(Integer.class);
                    num++;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");
                    databaseReference.child(uidRecive).child(uidSend).setValue(num);
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");
                    databaseReference.child(uidRecive).child(uidSend).setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
