package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Management3 extends AppCompatActivity {
    Button buttonFeed, buttonLinks, buttonGroups, buttonPrice, buttonNumUsers, buttonNumNewUsers, buttonNumDeleteUsers;
    ArrayList<String> arrayListLink;
    ArrayAdapter<String> arrayAdapterLink;
    ArrayList<String> arrayListGroups;
    ArrayAdapter<String> arrayAdapterGroups;
    ArrayList<String> arrayListPrice;
    ArrayAdapter<String> arrayAdapterPrice;
    ArrayList<String> arrayListNumUsers;
    ArrayAdapter<String> arrayAdapterNumUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management3);
        buttonFeed = findViewById(R.id.button3_feed);
        buttonLinks = findViewById(R.id.button3_links);
        buttonGroups = findViewById(R.id.button3_groups);
        buttonPrice = findViewById(R.id.button3_price);
        buttonNumUsers = findViewById(R.id.button3_num_uesers);
        buttonNumNewUsers = findViewById(R.id.button3_num_new_uesers);
        buttonNumDeleteUsers = findViewById(R.id.button3_num_delete_uesers);

        feedMake();
        linksMake();
        groupsMake();
        priceMake();
        numUesrsMake();


    }

    private void numUesrsMake() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("RegisterInformation2");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if (snapshot.exists())
                    buttonNumUsers.setText("מספר משתמשים: " + snapshot.getChildrenCount());
                buttonNumUsers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogNumUesrs(snapshot);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialogNumUesrs(DataSnapshot snapshot) {
        final Dialog d = new Dialog(Management3.this);
        d.setContentView(R.layout.dialog_list_menage);
        d.setTitle("Manage");

        d.setCancelable(true);
        ListView listView = d.findViewById(R.id.dialog_list_mange);

        arrayListNumUsers = new ArrayList<>();
        arrayAdapterNumUsers = new ArrayAdapter<String>(Management3.this,
                android.R.layout.simple_list_item_1, arrayListNumUsers);
        listView.setAdapter(arrayAdapterPrice);
        // arrayAdapterPrice.notifyDataSetChanged();
        for (DataSnapshot child:snapshot.getChildren()) {
            

        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Management3.this, Management.class);
                String nameCollegeEnglish = arrayListPrice.get(i).substring(0,
                        arrayListPrice.get(i).indexOf(":"));
                intent.putExtra("flagPrice", nameCollegeEnglish);
                startActivity(intent);

            }
        });

        d.show();
    }

    private void priceMake() {
        arrayListPrice = new ArrayList<>();
        arrayAdapterPrice = new ArrayAdapter<String>(Management3.this,
                android.R.layout.simple_list_item_1, arrayListPrice);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("GroupsPriceOffer");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;
                int sum = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String text = "";
                    sum += child.getChildrenCount();
                    text += child.getKey() + ": " + child.getChildrenCount();
                    arrayListPrice.add(text);
                }
                buttonPrice.setText("מספר הצעות מחיר:  " + sum);
                buttonPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPrice();
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void dialogPrice() {
        final Dialog d = new Dialog(Management3.this);
        d.setContentView(R.layout.dialog_list_menage);
        d.setTitle("Manage");

        d.setCancelable(true);
        ListView listView = d.findViewById(R.id.dialog_list_mange);
        listView.setAdapter(arrayAdapterPrice);
        arrayAdapterPrice.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Management3.this, Management.class);
                String nameCollegeEnglish = arrayListPrice.get(i).substring(0,
                        arrayListPrice.get(i).indexOf(":"));
                intent.putExtra("flagPrice", nameCollegeEnglish);
                startActivity(intent);

            }
        });

        d.show();
    }

    private void groupsMake() {
        arrayListGroups = new ArrayList<>();
        arrayAdapterGroups = new ArrayAdapter<String>(Management3.this,
                android.R.layout.simple_list_item_1, arrayListGroups);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("NewGroups");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;
                int sum = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String text = "";
                    sum += child.getChildrenCount();
                    text += child.getKey() + ": " + child.getChildrenCount();
                    arrayListGroups.add(text);
                }
                buttonGroups.setText("מספר הצעות לקבוצות:  " + sum);
                buttonGroups.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogGroups();
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void dialogGroups() {
        final Dialog d = new Dialog(Management3.this);
        d.setContentView(R.layout.dialog_list_menage);
        d.setTitle("Manage");

        d.setCancelable(true);
        ListView listView = d.findViewById(R.id.dialog_list_mange);
        listView.setAdapter(arrayAdapterGroups);
        arrayAdapterGroups.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Management3.this, Management.class);
                String nameCollegeEnglish = arrayListGroups.get(i).substring(0,
                        arrayListGroups.get(i).indexOf(":"));
                intent.putExtra("flagGroups", nameCollegeEnglish);
                startActivity(intent);
            }
        });

        d.show();
    }

    private void linksMake() {
        arrayListLink = new ArrayList<>();
        arrayAdapterLink = new ArrayAdapter<String>(Management3.this,
                android.R.layout.simple_list_item_1, arrayListLink);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("NewLinks");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;
                int sum = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String text = "";
                    sum += child.getChildrenCount();
                    text += child.getKey() + ": " + child.getChildrenCount();
                    arrayListLink.add(text);
                }
                buttonLinks.setText("מספר הצעות ללינקים:  " + sum);
                buttonLinks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogLinks();
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void dialogLinks() {
        final Dialog d = new Dialog(Management3.this);
        d.setContentView(R.layout.dialog_list_menage);
        d.setTitle("Manage");

        d.setCancelable(true);
        ListView listView = d.findViewById(R.id.dialog_list_mange);
        listView.setAdapter(arrayAdapterLink);
        arrayAdapterLink.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Management3.this, Management.class);
                String nameCollegeEnglish = arrayListLink.get(i).substring(0,
                        arrayListLink.get(i).indexOf(":"));
                intent.putExtra("flagLinks", nameCollegeEnglish);
                startActivity(intent);
            }
        });

        d.show();
    }

    private void feedMake() {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("FeedbackChat");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;
                int numFeedAll = 0;
                String textFeed = "";

                for (DataSnapshot child2 : snapshot.getChildren()) {
                    String nameGrop = child2.getKey();
                    numFeedAll += (int) child2.getChildrenCount();
                    textFeed += "               " + nameGrop + ": " + (int) child2.getChildrenCount() + "\n";
                }
                textFeed = "מספר משובים:" + numFeedAll + "\n" + textFeed;
                textFeed = textFeed.substring(0, textFeed.length() - 1);
                buttonFeed.setText(textFeed);
                buttonFeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Management3.this, Management2Feedbacks.class);

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}