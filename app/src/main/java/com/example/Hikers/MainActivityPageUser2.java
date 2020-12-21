package com.example.Hikers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.students.CardCarAdapter;
import com.example.students.CardTutor;
import com.example.students.MainActivityRegisterTutor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.example.R;

@IgnoreExtraProperties

public class MainActivityPageUser2 extends AppCompatActivity {
    private Button cardButton;
    private Button pageMainButton;
    private CardCarAdapter cardCarAdapter;
    private ListView lv;
    private DatabaseReference cardRef2;
    private TextView textViewExplanation;
    private ProgressDialog progressDialog;
    private ArrayList<CardTutor> arrayListCards = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_user2);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("טוען כרטיסים...");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        try {
            if (!firebaseUser.getEmail().toString().isEmpty())
                email = firebaseUser.getEmail().toString();
            else email = firebaseUser.getPhoneNumber().toString();
        } catch (RuntimeException e) {
            email = firebaseUser.getPhoneNumber().toString();
        }


        if (email.equals("arielrentit@gmail.com")) {
            Intent intent = new Intent(MainActivityPageUser2.this, MainActivityManagementCardsApprov2.class);
            startActivityForResult(intent, 0);
            //  return;
        }
        dataBase("CardsApprov");
        dataBase("CardsWaitApprov");
        dataBase("CardsRejection");

        cardButton = findViewById(R.id.publish);
        pageMainButton = findViewById(R.id.pageMain2);

        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityPageUser2.this, MainActivityRegisterTutor.class);
                startActivity(intent);
            }
        });

        pageMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityPageUser2.this, MainActivity2.class);
                startActivity(intent);
            }
        });


    }

    private void dataBase(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);

        cardRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                      CardTutor cardTutor = child.getValue(CardTutor.class);
                        arrayListCards.add(cardTutor);
                    }

                if (type.equals("CardsRejection") && arrayListCards.size() > 0) {
                    cardCarAdapter = new CardCarAdapter(MainActivityPageUser2.this, 0, 0, arrayListCards,true,0);
                    //phase 4 reference to listview
                    lv = (ListView) findViewById(R.id.lv);
                    lv.setAdapter(cardCarAdapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivityPageUser2.this, MainActivityRegisterTutor.class);
                            intent.putExtra("position", i);
                            intent.putExtra("flagEdit", true);

                            MainActivity2.pass(intent, arrayListCards.get(i));
                            startActivityForResult(intent, 0);
                        }
                    });
                }  if (type.equals("CardsRejection") && arrayListCards.size() == 0) {
                    textViewExplanation = findViewById(R.id.textViewExplanationPage);
                    textViewExplanation.setText("אין כרטיסים להצגה");
                    textViewExplanation.setTextColor(-65536);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void pass(Intent intent, CardTutor cardTutor) {
        intent.putExtra("flagEdit", true);
        intent.putExtra("name", cardTutor.getName());
        intent.putExtra("area", cardTutor.getArea());
        intent.putExtra("city", cardTutor.getCity());

        intent.putExtra("id", cardTutor.getId());
        intent.putExtra("numImage", cardTutor.getNumImage());
        intent.putExtra("permissionToPublish", cardTutor.getPermissionToPublish());
        intent.putExtra("phone", cardTutor.getPhone());
        intent.putExtra("priceDay", cardTutor.getPriceToLesson());
        intent.putExtra("remarks", cardTutor.getRemarks());
        intent.putExtra("email", cardTutor.getEmail());
        intent.putExtra("key", cardTutor.getKey());
        intent.putExtra("rejection", cardTutor.getRejection());

        for (int i = 1; i <= cardTutor.getImageViewArrayListName().size(); i++) {
            intent.putExtra("image" + i, cardTutor.getImageViewArrayListName().get(i - 1));
        }




    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_app,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch(item.getItemId()){
            case R.id.mainMenu:
                intent2 = new Intent(MainActivityPageUser2.this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.mainIconMenu:
                intent2 = new Intent(MainActivityPageUser2.this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.myPageMenu:
                intent2 = new Intent(MainActivityPageUser2.this, MainActivityPageUser2.class);
                startActivity(intent2);
                return true;
            case R.id.cardCarMenu:
                intent2 = new Intent(MainActivityPageUser2.this, MainActivityRegisterTutor.class);
                startActivity(intent2);
                return true;
            default:   return super.onOptionsItemSelected(item);
        }
    }

}