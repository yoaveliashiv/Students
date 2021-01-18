package com.code.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.code.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    ArrayList<String> arrayListNumUsersNew;
    ArrayAdapter<String> arrayAdapterNumUsersNew;
    ArrayList<String> arrayListNumUsersDelete;
    ArrayAdapter<String> arrayAdapterNumUsersDelete;

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
        numNewUesrsMake();
        numDeleteUesrsMake();

    }
    private void numDeleteUesrsMake() {
        Calendar calendar = Calendar.getInstance();//new users count
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final String date = simpleDateFormat.format(calendar.getTime());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("DeleteUsers").child(date);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num=0;
                if(snapshot.exists()) {
                    num = (int) snapshot.getChildrenCount();
                }
                buttonNumDeleteUsers.setText("משתמשים שנמחקו היום: "+num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                .getReference("NamesColleges");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListNumUsersDelete=new ArrayList<>();
                for(DataSnapshot child:snapshot.getChildren()){
                    final String collegeHebrew = child.getValue(String.class);
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                            .getReference("DeleteUsers").child(date);
                    databaseReference3.orderByChild("nameCollegeHebrow").equalTo(collegeHebrew)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int num=0;
                                    if(snapshot.exists()) {
                                        num = (int) snapshot.getChildrenCount();
                                    }
                                    else return;
                                    arrayListNumUsersDelete.add(collegeHebrew+": "+num);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        buttonNumDeleteUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNumUesrsDelete();
            }
        });
    }
    private void dialogNumUesrsDelete() {
        final Dialog d = new Dialog(Management3.this);
        d.setContentView(R.layout.dialog_list_menage);
        d.setTitle("Manage");

        d.setCancelable(true);
        ListView listView = d.findViewById(R.id.dialog_list_mange);

        arrayAdapterNumUsersDelete = new ArrayAdapter<String>(Management3.this,
                android.R.layout.simple_list_item_1, arrayListNumUsersDelete);
        listView.setAdapter(arrayAdapterNumUsersDelete);
        arrayAdapterNumUsersDelete.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });

        d.show();
    }
    private void numNewUesrsMake() {
        Calendar calendar = Calendar.getInstance();//new users count
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
       final String date = simpleDateFormat.format(calendar.getTime());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("NewUsers").child(date);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num=0;
                if(snapshot.exists()) {
                    num = (int) snapshot.getChildrenCount();
                }
                buttonNumNewUsers.setText("משתמשים חדשים היום: "+num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                .getReference("NamesColleges");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListNumUsersNew=new ArrayList<>();
                for(DataSnapshot child:snapshot.getChildren()){
                    final String collegeHebrew = child.getValue(String.class);
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                            .getReference("NewUsers").child(date);
                    databaseReference3.orderByChild("nameCollegeHebrow").equalTo(collegeHebrew)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int num=0;
                                            if(snapshot.exists()) {
                                                num = (int) snapshot.getChildrenCount();
                                            }
                                            else return;
                                    arrayListNumUsersNew.add(collegeHebrew+": "+num);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        buttonNumNewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNumUesrsNew();
            }
        });
    }
    private void dialogNumUesrsNew() {
        final Dialog d = new Dialog(Management3.this);
        d.setContentView(R.layout.dialog_list_menage);
        d.setTitle("Manage");

        d.setCancelable(true);
        ListView listView = d.findViewById(R.id.dialog_list_mange);

        arrayAdapterNumUsersNew = new ArrayAdapter<String>(Management3.this,
                android.R.layout.simple_list_item_1, arrayListNumUsersNew);
        listView.setAdapter(arrayAdapterNumUsersNew);
        arrayAdapterNumUsersNew.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });

        d.show();
    }
    private void numUesrsMake() {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                .getReference("NamesColleges");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("RegisterInformation2");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int num=0;
                        if(snapshot.exists()) {
                            num = (int) snapshot.getChildrenCount();
                        }
                        buttonNumUsers.setText("מספר משתמשים: "+num);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                arrayListNumUsers = new ArrayList<>();

                for (DataSnapshot child:snapshot.getChildren()) {
                   final String collegeHebrew = child.getValue(String.class);
                   databaseReference.orderByChild("nameCollegeHebrew")
                           .equalTo(collegeHebrew).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull final DataSnapshot snapshot) {
                           if (!snapshot.exists())
                               return;
                               arrayListNumUsers.add(collegeHebrew+": "+(int)snapshot.getChildrenCount());

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

buttonNumUsers.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        dialogNumUesrs();
    }
});
    }

    private void dialogNumUesrs() {
        final Dialog d = new Dialog(Management3.this);
        d.setContentView(R.layout.dialog_list_menage);
        d.setTitle("Manage");

        d.setCancelable(true);
        ListView listView = d.findViewById(R.id.dialog_list_mange);

        arrayAdapterNumUsers = new ArrayAdapter<String>(Management3.this,
                android.R.layout.simple_list_item_1, arrayListNumUsers);
        listView.setAdapter(arrayAdapterNumUsers);
       arrayAdapterNumUsers.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


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