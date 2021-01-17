package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Management2Feedbacks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management2_feedbacks);
        feedMake();

    }

    private void feedMake() {
        final ListView listView = findViewById(R.id.list_management2);
        final ArrayList<String> arrayListGroups = new ArrayList<>();
        final ArrayList<FeedbackChat> groups = new ArrayList<>();


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Management2Feedbacks.this,
                android.R.layout.simple_list_item_1, arrayListGroups);
        listView.setAdapter(arrayAdapter);

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("FeedbackChat");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child2 : snapshot.getChildren()) {
                    String nameGrop = child2.getKey();
                    groups.add(new FeedbackChat());
                    arrayListGroups.add("                                  " + nameGrop);
                    for (DataSnapshot child : child2.getChildren()) {
                        FeedbackChat feedbackChat = child.getValue(FeedbackChat.class);
                        String line = "התקלה: " + feedbackChat.getFeedback();
                        if (feedbackChat.getMessage() != null)
                            line += " ההודעה: " + feedbackChat.getMessage().getMessage();
                        if (feedbackChat.getLinksToWhatsApp() != null)
                            line += " לינק של: " + feedbackChat.getLinksToWhatsApp().getNameGroup();
                        arrayListGroups.add(line);
                        groups.add(feedbackChat);
                    }
                }
                arrayListGroups.add("                                           סוף");
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogFeed(i, groups);
            }
        });
    }

    private void dialogFeed(final int i, final ArrayList<FeedbackChat> arrayList) {
        final Dialog d = new Dialog(Management2Feedbacks.this);
        d.setContentView(R.layout.dialog_feedback_mange);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView textViewDelete = d.findViewById(R.id.feed_delete);
        TextView textViewSendMessage = d.findViewById(R.id.feed_send_message);
        TextView textView_chak_link = d.findViewById(R.id.feed_chak_link);
        TextView textView_delete_link = d.findViewById(R.id.feed_delete_link);
        TextView textView_block_he = d.findViewById(R.id.feed_block_he);
        TextView textView_block_i = d.findViewById(R.id.feed_block_i);

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFeed(arrayList, i, d);

            }
        });
        textViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSendMessageMenge(arrayList.get(i));

            }
        });
        if (arrayList.get(i).getLinksToWhatsApp() == null) {
            textView_chak_link.setVisibility(View.GONE);
            textView_delete_link.setVisibility(View.GONE);
        }
        textView_chak_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String link = arrayList.get(i).getLinksToWhatsApp().getLink();
                String url = link;
                intentWhatsapp.setData(Uri.parse(url));
                // intentWhatsapp.setPackage("com.whatsapp");
                startActivity(intentWhatsapp);
            }
        });
        textView_delete_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cardRef2 = FirebaseDatabase.getInstance()
                        .getReference("LinksToWhatsApp")
                        .child(arrayList.get(i).getLinksToWhatsApp().getNameCollegeEnglish())
                .child(arrayList.get(i).getLinksToWhatsApp().getNameGroup());
                cardRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management2Feedbacks.this, "לינק נמחק בהצלחה", Toast.LENGTH_SHORT).show();

                        deleteFeed(arrayList, i, d);
                    }
                });
            }
        });
        if (arrayList.get(i).getUidReportingOn().isEmpty()) {
            textView_block_he.setVisibility(View.GONE);
        }
        textView_block_he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Management2Feedbacks.this, Management.class);
                intent.putExtra("blockPhone", arrayList.get(i).getPhoneReportingOn());
                startActivity(intent);
            }
        });
        textView_block_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Management2Feedbacks.this, Management.class);
                intent.putExtra("blockPhone", arrayList.get(i).getPhoneReportingSend());
                startActivity(intent);
            }
        });


        d.show();

    }

    private void dialogSendMessageMenge(final FeedbackChat feedbackChat) {
        final Dialog d = new Dialog(Management2Feedbacks.this);
        d.setContentView(R.layout.dialog_send_message_menger);
        d.setTitle("Manage");

        d.setCancelable(true);
        Button button = d.findViewById(R.id.button_menge_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = d.findViewById(R.id.mange_send_message);
                String messageEdit = editText.getText().toString();
                if (messageEdit.length() < 2) {
                    editText.setError("הכנס הודעה תקינה");
                    editText.requestFocus();
                    return;
                }
                com.example.chat.Message message = new Message();
                message.setMessage(messageEdit);
                message.setName("הודעת מערכת");
                message.setPhone("הודעת מערכת");
                message.setUid("הודעת מערכת");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(calendar.getTime());

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                String time = simpleDateFormat.format(calendar.getTime());
                message.setTime(time);
                message.setDate(date);
                message.setDateTimeZone(ServerValue.TIMESTAMP);

                String uidI = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts")
                        .child(feedbackChat.getUidReportingSend()).child(uidI).push();
                databaseReference.setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management2Feedbacks.this, "הודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });

            }
        });
        d.show();
    }

    private void deleteFeed(ArrayList<FeedbackChat> arrayList, int i, final Dialog d) {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("FeedbackChat")
                .child(arrayList.get(i).getType()).child(arrayList.get(i).getKey());
        databaseReference2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //  Toast.makeText(Management2Feedbacks.this, "דיווח נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                d.dismiss();
                feedMake();

            }
        });
    }
}