package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.Hikers.RegisterLoginActivity;
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
    private Dialog d;
    private String nameGroup;
    private RegisterInformation2 registerInformation;
    private String uid;
    private DatabaseReference databaseReference;
    private ArrayList<Message> arrayListMessage;
    private MessageAdapter arrayAdapter;
    EditText editText;
    ListView listView;

    // private TextView textViewDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_chat);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nameGroup = getIntent().getExtras().getString("nameGroup");
        Boolean flag = getIntent().getExtras().getBoolean("flagAllGroup");


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

        arrayAdapter = new MessageAdapter(GroupChatActivity.this, 0, 0, arrayListMessage, uid);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getSupportActionBar().setTitle("חיים");

                dialogSendPrivateMessage(i);
            }
        });
        final Button button = findViewById(R.id.button_goin);
        if (!flag) button.setVisibility(View.GONE);
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
        onStart1();
        super.onCreate(savedInstanceState);

    }

    private void dialogSendPrivateMessage(final int i) {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_massge);
        d.setTitle("Manage");

        final TextView textViewSendMassge = d.findViewById(R.id.textView_send_massage);
        TextView textViewOpenProfile = d.findViewById(R.id.textView_propile_open);
        TextView textViewSendWhatappsMassge = d.findViewById(R.id.textView_send_whatapps);
        TextView textViewCopyPhone = d.findViewById(R.id.textView_copy_phone);
        TextView textViewCopyMessage = d.findViewById(R.id.textView_copy_massage);
        TextView textViewFeed = d.findViewById(R.id.textView_feed);

        textViewOpenProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, ProfileActivity.class);
                intent.putExtra("profile_user_id", arrayListMessage.get(i).getUid());
                intent.putExtra("visit_user_id", uid);
                startActivity(intent);

            }
        });
        textViewSendMassge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid.equals(arrayListMessage.get(i).getUid())) {
                    Toast.makeText(GroupChatActivity.this, "לא ניתן לשלוח הודעה לעצמך", Toast.LENGTH_SHORT).show();
                    d.dismiss();
                    return;
                }
                chakIfBlockAndSend(i, textViewSendMassge);


            }
        });
        textViewSendWhatappsMassge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String num = arrayListMessage.get(i).getPhone();
                String url = "https://api.whatsapp.com/send?phone=" + num;
                intentWhatsapp.setData(Uri.parse(url));
                intentWhatsapp.setPackage("com.whatsapp");
                startActivity(intentWhatsapp);
            }
        });
        textViewCopyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListMessage.get(i).getPhone());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(GroupChatActivity.this, "המספר הועתק", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewCopyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListMessage.get(i).getMessage());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(GroupChatActivity.this, "ההודעה הועתקה", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, ActivityFeedbackChat.class);
                intent.putExtra("message", arrayListMessage.get(i));
                intent.putExtra("flag", true);
                startActivity(intent);
            }
        });
        d.show();
    }

    private void chakIfBlockAndSend(final int i, final TextView textViewSendMassge) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocked")
                .child(uid).child(arrayListMessage.get(i).getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    textViewSendMassge.setText("משתמש זה חסום");
                    textViewSendMassge.setTextColor(Color.parseColor("#EF2B2B"));
                    textViewSendMassge.setClickable(false);
                } else {
                    Intent intent = new Intent(GroupChatActivity.this, ChatActivity.class);
                    intent.putExtra("send_message_user_id", uid);
                    intent.putExtra("to_message_user_id", arrayListMessage.get(i).getUid());
                    startActivityForResult(intent, 0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveDatabase(String sms, final String nameGroup) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());

        final Message message = new Message();
        message.setDate(date);
        message.setMessage(sms);
        message.setName(registerInformation.getName());
        message.setPhone(registerInformation.getEmail());
        message.setUid(uid);
        message.setTime(time);
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("NamesGroups");
        databaseReference2.child(nameGroup).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int id = 0;
                if (snapshot.exists()) id = (int) snapshot.getChildrenCount();


                message.setId(id);
                databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups").child(nameGroup).push();

                databaseReference.setValue(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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


    protected void onStart1() {//TODO:


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
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("NotificationsIdSeeLast").child(uid).child(nameGroup);//set Notifications
        databaseReference3.setValue(message.getId());
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

    private void saveNotifications() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyGroups");
        databaseReference.child(uid).child(nameGroup).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int num = snapshot.getValue(Integer.class);
                    num++;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NotificationsGroups");
                    databaseReference.child(uid).child(nameGroup).setValue(num);
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NotificationsGroups");
                    databaseReference.child(uid).child(nameGroup).setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                intent2 = new Intent(GroupChatActivity.this, MainActivity3.class);
                startActivity(intent2);
                return true;

            case R.id.settingsMenu:
                intent2 = new Intent(GroupChatActivity.this, ActivitySettings.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                intent2 = new Intent(GroupChatActivity.this, RegisterLoginActivity.class);
                startActivity(intent2);
                return true;
            case R.id.refresh:


                intent2 = new Intent(GroupChatActivity.this, MainActivity3.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}