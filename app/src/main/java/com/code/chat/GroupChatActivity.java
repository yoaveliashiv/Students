package com.code.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.code.Hikers.RegisterInformation2;
import com.code.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class GroupChatActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private Dialog d;
    private String nameGroup;
    private RegisterInformation2 registerInformation;
    private String uid;
    private DatabaseReference databaseReference;
    private ArrayList<Message> arrayListMessage;
    private MessageAdapter arrayAdapter;
    private EditText editText;
    private ListView listView;
    private Boolean flag;
    private TextView textViewGroupDetails;
    private int numMembers;
    private int numNotifications = -1;
    private Boolean flagNewMessage = false;
    private String nameCologeEnglish = "";
    ChildEventListener childEventListener=null;

    // private TextView textViewDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setContentView(R.layout.activity_group_chat);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nameGroup = getIntent().getExtras().getString("nameGroup");
        flag = getIntent().getExtras().getBoolean("flagAllGroup");
        if (getIntent().hasExtra("num_notifications"))
            numNotifications = getIntent().getExtras().getInt("num_notifications");
        if (getIntent().hasExtra("flagNameCologeEnglish"))
            nameCologeEnglish = getIntent().getExtras().getString("flagNameCologeEnglish");
        // textViewDisplay=findViewById(R.id.text_group_display);
        ImageButton imageButton = findViewById(R.id.imageButtonSendMassege);
        scrollView = findViewById(R.id.scrollView);
        editText = findViewById(R.id.editTextMssege);
        getSupportActionBar().setTitle(nameGroup);


        getUserInfo();

        databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups")
                .child(nameCologeEnglish).child(nameGroup);

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
                databaseReference1.setValue(nameCologeEnglish);
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups details")
                        .child(nameGroup).child(uid);
                databaseReference2.setValue(nameCologeEnglish);
                Intent intent = new Intent(GroupChatActivity.this, MainActivity3.class);
                intent.putExtra("flagPage", 1);
                startActivity(intent);
                finish();
                // setGroupDetails();
                // Toast.makeText(GroupChatActivity.this, "הצטרפת לקבוצה בהצלחה", Toast.LENGTH_SHORT).show();

            }
        });
        textViewGroupDetails = findViewById(R.id.textView_group_details);
        textViewGroupDetails.setVisibility(View.GONE);
        setGroupDetails();

        onStart1();
        showMessage();
        super.onCreate(savedInstanceState);

    }

    private void showMessage() {
        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("NamesGroups")
                .child(nameCologeEnglish).child(nameGroup);
        reference.limitToLast(1000).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Message message;

                for (DataSnapshot child : snapshot.getChildren()) {
                    message = child.getValue(Message.class);
                    arrayListMessage.add(message);

                }
                if (arrayListMessage.size() == 0){
                    flagNewMessage = true;

                    return;
                }
                if (numNotifications > 0) {
                    Message message1 = new Message();
                    message1.setId(numNotifications);
                    arrayListMessage.add(arrayListMessage.size() - numNotifications, message1);
                }

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                        .getReference("NotificationsIdSeeLast").child(uid).child(nameGroup);//set Notifications
                databaseReference3.setValue(arrayListMessage.get(arrayListMessage.size() - 1).getId());
                arrayAdapter.notifyDataSetChanged();

                listView.setSelection(listView.getAdapter().getCount() - 1);
                flagNewMessage = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setGroupDetails() {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups details")
                .child(nameGroup);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numMembers = 0;
                if (snapshot.exists()) {
                    numMembers = (int) snapshot.getChildrenCount();

                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("GroupsPrice")
                            .child(nameGroup);
                    databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String price = snapshot.getValue(String.class);
                                textViewGroupDetails.setText(" מספר המשתתפים " + numMembers + ". " + "מחיר נסיעה " +
                                        price + " ");
                                textViewGroupDetails.setVisibility(View.VISIBLE);


                            } else {
                                if (!flag) {
                                    TextView textViewPrice = findViewById(R.id.button_price);
                                    textViewPrice.setVisibility(View.VISIBLE);
                                    textViewPrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogPriceGroup();
                                        }
                                    });


                                }
                                textViewGroupDetails.setText(" מספר המשתתפים " + numMembers + ". " + "מחיר נסיעה ? ");
                                textViewGroupDetails.setVisibility(View.VISIBLE);

                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else                                 textViewGroupDetails.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void dialogSendPrivateMessage(final int i) {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_massge);
        d.setTitle("Manage");

        TextView textViewSendMassge = d.findViewById(R.id.feed_delete);
        TextView textViewOpenProfile = d.findViewById(R.id.profile_blocked);
        TextView textViewSendWhatappsMassge = d.findViewById(R.id.delete_blocked);
        TextView textViewCopyPhone = d.findViewById(R.id.feed_delete_link);
        TextView textViewCopyMessage = d.findViewById(R.id.feed_block_he);
        TextView textViewFeed = d.findViewById(R.id.feed_block_i);

        textViewOpenProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, ProfileActivity.class);
                intent.putExtra("profile_user_id", arrayListMessage.get(i).getUid());
                intent.putExtra("visit_user_id", uid);
                startActivity(intent);
                finish();

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
                chakIfExistAndBlokeAndSend(i);


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
                finish();
            }
        });
        d.show();
    }

    private void chakIfBlockAndSend(final int i) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocked")
                .child(uid).child(arrayListMessage.get(i).getPhone());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dialogIsBlocking();
                    d.dismiss();
                } else {
                    Intent intent = new Intent(GroupChatActivity.this, ChatActivity.class);
                    intent.putExtra("send_message_user_id", uid);
                    intent.putExtra("to_message_user_id", arrayListMessage.get(i).getUid());
                    startActivityForResult(intent, 0);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialogIsBlocking() {
        final Dialog dialog = new Dialog(GroupChatActivity.this);
        dialog.setContentView(R.layout.dialog_is_blocking);
        dialog.setTitle("Manage");

        dialog.setCancelable(true);
        Button buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
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
        if (!registerInformation.getName().isEmpty())
            message.setName(registerInformation.getName());
        else
            message.setName(registerInformation.getEmail());
        message.setDateTimeZone(ServerValue.TIMESTAMP);

        message.setPhone(registerInformation.getEmail());
        message.setUid(uid);
        message.setTime(time);
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("NamesGroups")
                .child(nameCologeEnglish);
        databaseReference2.child(nameGroup).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int id = 0;
                if (snapshot.exists()) id = (int) snapshot.getChildrenCount();


                message.setId(id+1);
                databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups")
                        .child(nameCologeEnglish).child(nameGroup).push();
                message.setKey(databaseReference.getKey());
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
      //  databaseReference.removeEventListener();

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && flagNewMessage) {
                    displayMessage(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    if (snapshot.exists()) {
//                        displayMessage(snapshot);
//                    }
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
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    private void displayMessage(DataSnapshot snapshot) {

        Message message;

        message = snapshot.getValue(Message.class);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("NotificationsIdSeeLast").child(uid).child(nameGroup);//set Notifications
        databaseReference3.setValue(message.getId());

        arrayListMessage.add(message);

        arrayAdapter.notifyDataSetChanged();

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

        menuInflater.inflate(R.menu.menu__group_chat, menu);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (flag) {
            MenuItem item = menu.findItem(R.id.delete_group_Menu);
            item.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;


        switch (item.getItemId()) {

            case R.id.mainIconMenu:
                intent2 = new Intent(GroupChatActivity.this, MainActivity3.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.settingsMenu:
                intent2 = new Intent(GroupChatActivity.this, ActivitySettings.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                finish();
                return true;
            case R.id.delete_group_Menu:
                dialogDelete();
                return true;
            case R.id.feedbackMenu:
                intent2 = new Intent(GroupChatActivity.this, ActivityFeedbackChat.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                finish();
                return true;
            case R.id.returnMainMenu:

                intent2 = new Intent(GroupChatActivity.this, MainActivity3.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dialogDelete() {
        final Dialog d = new Dialog(GroupChatActivity.this);
        d.setContentView(R.layout.dialog_delete_my_group);
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
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyGroups")
                        .child(uid).child(nameGroup);
                databaseReference.removeValue();
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups details")
                        .child(nameGroup).child(uid);
                databaseReference2.removeValue();

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                        .getReference("NotificationsIdSeeLast").child(uid).child(nameGroup);//set Notifications
                databaseReference3.removeValue();

                Toast.makeText(GroupChatActivity.this, "יצאת מהקבוצה בהצלחה", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GroupChatActivity.this, MainActivity3.class);

                startActivityForResult(intent, 0);
                finish();
            }

        });

        d.show();
    }

    private void chakIfExistAndBlokeAndSend(final int i) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInformation2")
                .child(arrayListMessage.get(i).getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chakIfBlockAndSend(i);
                } else {
                    dialogIsBlocking();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialogPriceGroup() {
        final Dialog d = new Dialog(GroupChatActivity.this);
        d.setContentView(R.layout.dialog_new_price_drive);
        d.setTitle("Manage");

        d.setCancelable(true);
        final EditText editTextNameGroup = d.findViewById(R.id.editText_name_link);

        Button buttonSend = d.findViewById(R.id.button_send_new_link_dialog);

        Button buttonClose = d.findViewById(R.id.button_close_window_new);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String price = editTextNameGroup.getText().toString();

                if (price.isEmpty() || price.length() > 4) {
                    editTextNameGroup.setError("הכנס מספר תקין בלי רווחים");
                    editTextNameGroup.requestFocus();
                    return;
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupsPriceOffer")
                        .child(registerInformation.getNameCollegeEnglish()).child(nameGroup).push();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("price", price);
                hashMap.put("key", databaseReference.getKey());
                hashMap.put("uid", uid);
                hashMap.put("nameGroup", nameGroup);
                databaseReference.setValue(hashMap);
                Toast.makeText(GroupChatActivity.this, "המחיר נשלח בהצלחה ומחכה לאישור המערכת", Toast.LENGTH_LONG).show();
                d.dismiss();

            }
        });
        d.show();

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
    }
}