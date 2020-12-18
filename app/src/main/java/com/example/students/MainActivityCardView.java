package com.example.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityCardView extends AppCompatActivity {
    private TextView editTextCity, editTextTypeCar, editTextInsurance,
             editTextRemarks, textViewFeedbackGrade;
    private CardTutor cardTutor;
    private Button buttonReturnMain, buttonImage, buttonSendFeed, buttonSeeFeedback;
    private ImageView imageView;
    private int position;
    // private Feedback feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_card_view);

        setCard();
        position = 1;
        textViewFeedbackGrade = findViewById(R.id.textViewFeedbackGrade);
        buttonImage = findViewById(R.id.buttonImage);
        buttonReturnMain = findViewById(R.id.buttonMainM);
        buttonSendFeed = findViewById(R.id.buttonSendFeed);
        buttonSeeFeedback = findViewById(R.id.buttonSeeFeedback);

        editTextCity = findViewById(R.id.textViewPriceAndAreaM);
        editTextInsurance = findViewById(R.id.textViewInsuranceAndPhoneM);
        editTextRemarks = findViewById(R.id.textViewRemarksM);
        imageView = findViewById(R.id.imageMainM);

        editTextRemarks.setText("הערות: " + cardTutor.getRemarks());

        editTextCity.setText("מחיר ליום: " + cardTutor.getPriceToLesson() + "     אזור " + cardTutor.getArea() + ": " + cardTutor.getCity());
        Glide.with(this)
                .load(cardTutor.getImageViewArrayListName().get(0))
                .into(imageView);
        if (cardTutor.getImageViewArrayListName().size() == 1)
            buttonImage.setVisibility(View.GONE);

        if (cardTutor.getRemarks().isEmpty())
            editTextRemarks.setVisibility(View.GONE);
        buttonReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityCardView.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(view)
                        .load(cardTutor.getImageViewArrayListName().get(position))
                        .into(imageView);
                position++;
                if (position == cardTutor.getImageViewArrayListName().size())
                    position = 0;

            }
        });

        buttonSendFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String emailReport = "";
                if (firebaseUser != null) {
                    try {
                        if (!firebaseUser.getEmail().toString().isEmpty())
                            emailReport = firebaseUser.getEmail().toString();
                        else emailReport = firebaseUser.getPhoneNumber().toString();
                    } catch (RuntimeException e) {
                        emailReport = firebaseUser.getPhoneNumber().toString();
                    }

                } else {
                    TextView textView = findViewById(R.id.textViewWarnFeed);
                    textView.setVisibility(View.VISIBLE);
                    buttonSendFeed.setError("אנא הירשם או התחבר");
                    buttonSendFeed.requestFocus();
                    return;
                }
                Intent intent = new Intent(MainActivityCardView.this, MainActivity_Feedback.class);
                intent.putExtra("email", cardTutor.getEmail());
                intent.putExtra("emailReporting", emailReport);
                intent.putExtra("id", cardTutor.getId());

                startActivity(intent);
            }
        });
        buttonSeeFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivityCardView.this, MainActivityListFeedbackPage.class);
                intent.putExtra("email", cardTutor.getEmail());
                startActivity(intent);
            }
        });
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String emailUser = "";
//        if (firebaseUser != null) {
//            try {
//                if (!firebaseUser.getEmail().toString().isEmpty())
//                    emailUser = firebaseUser.getEmail().toString();
//                else emailUser = firebaseUser.getPhoneNumber().toString();
//            } catch (RuntimeException e) {
//                emailUser = firebaseUser.getPhoneNumber().toString();
//            }
//        }
//if(!cardTutor.getEmail().equals(emailUser))
        seeMePlus();
        TextView phone=findViewById(R.id.textViewInsuranceAndPhoneM);

        ListView lv1 = findViewById(R.id.listViewCardViewExpeirince);
        ExpertiseAdapter expertiseAdapter = new ExpertiseAdapter(MainActivityCardView.this, 0, 0, cardTutor.getArrayListExpertise());
        lv1.setAdapter(expertiseAdapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    private void seeMePlus() {
        DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("CardsApprov");
        cardRef2.child(cardTutor.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;
                CardTutor cardTutor2 = new CardTutor();
                cardTutor2 = dataSnapshot.getValue(CardTutor.class);
                cardTutor2.addOneSeeCard();
                DatabaseReference cardRef6 = FirebaseDatabase.getInstance().getReference();
                cardRef6.child("CardsApprov").child(cardTutor.getKey()).setValue(cardTutor2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }


    private void setCard() {
        cardTutor = new CardTutor();
        Bundle bundle = getIntent().getExtras();
        cardTutor.setKey(bundle.getString("key"));
        cardTutor.setEmail(bundle.getString("email"));
        cardTutor.setArea(bundle.getString("area"));
        cardTutor.setNumImage(bundle.getInt("numImage"));
        cardTutor.setCity(bundle.getString("city"));
        cardTutor.setPhone(bundle.getString("phone"));

        cardTutor.setRemarks(bundle.getString("remarks"));
        cardTutor.setPriceToLesson(bundle.getString("priceDay"));
        cardTutor.setNumExpertise(bundle.getInt("numExpertise"));

        cardTutor.setName(bundle.getString("name"));
        cardTutor.setId(bundle.getInt("id"));
        cardTutor.setPermissionToPublish(bundle.getInt("permissionToPublish"));
        cardTutor.setRejection(bundle.getString("rejection"));
        for (int i = 0; i < cardTutor.getNumExpertise(); i++)
            cardTutor.addExpertise(bundle.getString("expertise" + i));
        for (int i = 1; i <= cardTutor.getNumImage(); i++)
            cardTutor.addImageViewArrayListName(bundle.getString("image" + i));

    }

}