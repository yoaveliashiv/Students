package com.example.Hikers;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.students.CardCarAdapter;
import com.example.students.CardTutor;
import com.example.students.Feedback;
import com.example.students.FeedbeckAdapter;
import com.example.students.MainActivityCardView;
import com.example.students.MainActivityRegisterTutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.example.R;

public class MainActivityManagementCardsApprov2 extends AppCompatActivity {
    private Dialog d;

    private DatabaseReference cardRef2;
    private ArrayList<CardTutor> arrayListCards;
    private ArrayList<CardTutor> arrayListCards2;

    private CardCarAdapter cardCarAdapter = null;
    private ListView lv1;
    private Button buttonMainReturn, buttonRejection, buttonApprov, buttonWitheApprov,
            buttonRejectionFeed, buttonApprovFedd, buttonWitheApprovFeed;
    private TextView textViewRejection, textViewApprov, textViewWitheApprov;

    private int numCards = -1;
    private CardTutor cardTutor;
    private ArrayList<CardTutor> arrayListCardsPassDate;
    EditText editTextFrom;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_management_cards_approv2);
        editTextFrom = findViewById(R.id.editTextGropeFrom);
        editTextFrom.setText("אריאל");
        Button buttonDo=findViewById(R.id.buttonDoGrohp);
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("NamesGroups");

                cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                               EditText editTexto = findViewById(R.id.editTextGrohpTo);
                                                               String from=editTextFrom.getText().toString();
                                                               String to=editTexto.getText().toString();
                                                               Boolean flagExsis=false;
                                                               for (DataSnapshot child : snapshot.getChildren()) {
                                                                   String grohp=child.getKey();
                                                                   if (("" + from + "-" + to).equals(grohp)||to.length()<3)
                                                                       flagExsis=true;
                                                                   editTexto.setError("הקבוצה קיימת או קצרה");
                                                                   editTexto.requestFocus();
                                                               }
                                                                  if(!flagExsis) {
                                                                      DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("NamesGroups");
                                                                      cardRef2.child("" + from + "-" + to).setValue("");
                                                                      Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityManagementCardsApprov2.class);
                                                                      startActivityForResult(intent, 0);
                                                                  }

                                                           }

                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError error) {

                                                           }
                                                       });

            }
        });
        setNumUsers();
        setId();
        numFeed("FeedbackWaitApprov");
        numFeed("FeedbackApprov");
        numFeed("FeedbackRejection");
        numCards();
        buttonApprov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playCards2("CardsApprov");
            }
        });
        buttonWitheApprov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playCards2("CardsWaitApprov");
            }
        });
        buttonRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playCards2("CardsRejection");

            }
        });
        buttonMainReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivity2.class);
                startActivityForResult(intent, 0);
            }
        });
        buttonWitheApprovFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playFeed("FeedbackWaitApprov");

            }
        });
        buttonApprovFedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playFeed("FeedbackApprov");

            }
        });
        buttonRejectionFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playFeed("FeedbackRejection");

            }
        });


        playCards2("CardsWaitApprov");

    }


    private void numFeed(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    numCards = 0;
                } else {

                    final ArrayList<Feedback> arrayListFeed = new ArrayList();
                    Feedback feedback;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        feedback = child.getValue(Feedback.class);
                        arrayListFeed.add(feedback);

                    }
                    numCards = arrayListFeed.size();
                }

                if (type.equals("FeedbackWaitApprov")) {
                    TextView textView = findViewById(R.id.textViewNumFeedWait);
                    textView.setText("" + numCards);
                } else if (type.equals("FeedbackRejection")) {
                    TextView textView = findViewById(R.id.textViewNumFeedRejecsen);
                    textView.setText("" + numCards);
                } else if (type.equals("FeedbackApprov")) {
                    TextView textView = findViewById(R.id.textViewNumFeedApprove);
                    textView.setText("" + numCards);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setId() {
        textViewWitheApprov = findViewById(R.id.textViewNumWaitAprove);
        textViewApprov = findViewById(R.id.textViewNumAprove);
        textViewRejection = findViewById(R.id.textViewNumReject);
        buttonApprovFedd = findViewById(R.id.buttonAprrovFeedbeck);
        buttonRejectionFeed = findViewById(R.id.buttonRejecsanFeedback);
        buttonWitheApprovFeed = findViewById(R.id.buttonWiatAprrovFeedbeck);
        buttonApprov = findViewById(R.id.buttonApprov);
        buttonWitheApprov = findViewById(R.id.buttonWhite);
        buttonRejection = findViewById(R.id.buttonRejection);
        buttonMainReturn = findViewById(R.id.buttonMainReturn);
    }

    private void numCards() {
        playCards("CardsApprov");
        playCards("CardsRejection");
        playCards("CardsWaitApprov");
    }

    private void playCards(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards = new ArrayList();
                cardTutor = new CardTutor();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try {
                        cardTutor = child.getValue(CardTutor.class);
                        arrayListCards.add(cardTutor);

                    } catch (RuntimeException e) {

                    }
                }

                numCards = arrayListCards.size();
                if (type.equals("CardsWaitApprov"))
                    textViewWitheApprov.setText("" + numCards);
                else if (type.equals("CardsRejection"))
                    textViewRejection.setText("" + numCards);
                else if (type.equals("CardsApprov"))
                    textViewApprov.setText("" + numCards);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void playCards2(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards2 = new ArrayList();
                CardTutor cardTutor;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    try {
                        cardTutor = child.getValue(CardTutor.class);
                        arrayListCards2.add(cardTutor);

                    } catch (RuntimeException e) {

                    }
                }

                if (arrayListCards2.size() > 0) {
                    // Toast.makeText(MainActivityPageUser.this, ""+registerInformation.getCardsUser().get(0).getImageViewArrayListName().get(0), Toast.LENGTH_LONG).show();
                    boolean flagSee = type.equals("CardsApprov");
                    int x = 1;
                    if (type.equals("CardsDeleteHistory")) x = 2;
                    cardCarAdapter = new CardCarAdapter(MainActivityManagementCardsApprov2.this, 0, 0, arrayListCards2, flagSee, x);
                    //phase 4 reference to listview
                    lv1 = (ListView) findViewById(R.id.lvMange);
                    lv1.setAdapter(cardCarAdapter);

                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (type.equals("CardsDeleteHistory")) {
                                Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityCardView.class);
                                intent.putExtra("flagMain", true);
                                pass(intent, arrayListCards2.get(i));
                                startActivityForResult(intent, 0);
                                return;
                            }
                            Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityRegisterTutor.class);
                            intent.putExtra("flagManagement", true);
                            intent.putExtra("flagEdit", false);
                            intent.putExtra("flagMain", true);
                            Toast.makeText(MainActivityManagementCardsApprov2.this, "" + i + "kk" + arrayListCards2.size(), Toast.LENGTH_LONG).show();

                            pass(intent, arrayListCards2.get(i));
                            startActivityForResult(intent, 0);
                        }
                    });
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void playFeed(final String type) {
        cardRef2 = FirebaseDatabase.getInstance().getReference(type);
        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                final ArrayList<Feedback> arrayListFeed = new ArrayList();
                Feedback feedback;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    feedback = child.getValue(Feedback.class);
                    arrayListFeed.add(feedback);

                }

                //   Toast.makeText(MainActivityManagementCardsApprov.this, "sss"+arrayListFeed.size(), Toast.LENGTH_LONG).show();
//
                FeedbeckAdapter feedbeckAdapter = new FeedbeckAdapter(MainActivityManagementCardsApprov2.this, 0, 0, arrayListFeed, true);
                //phase 4 reference to listview
                lv1 = (ListView) findViewById(R.id.lvMange);
                lv1.setAdapter(feedbeckAdapter);
//
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialod(arrayListFeed.get(i), type);
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void pass(Intent intent, CardTutor cardTutor) {
        intent.putExtra("flag", true);
        intent.putExtra("name", cardTutor.getName());
        intent.putExtra("area", cardTutor.getArea());
        intent.putExtra("city", cardTutor.getCity());
        intent.putExtra("id", cardTutor.getId());
        intent.putExtra("numImage", cardTutor.getImageViewArrayListName().size());
        intent.putExtra("permissionToPublish", cardTutor.getPermissionToPublish());
        intent.putExtra("phone", cardTutor.getPhone());
        intent.putExtra("priceDay", cardTutor.getPriceToLesson());
        intent.putExtra("remarks", cardTutor.getRemarks());
        intent.putExtra("email", cardTutor.getEmail());
        intent.putExtra("key", cardTutor.getKey());
        intent.putExtra("rejection", cardTutor.getRejection());
        for (int i = 0; i < cardTutor.getArrayListExpertise().size(); i++) {
            intent.putExtra("expertise" + i, cardTutor.getImageViewArrayListName().get(i));
        }
        for (int i = 1; i <= cardTutor.getImageViewArrayListName().size(); i++) {
            intent.putExtra("image" + i, cardTutor.getImageViewArrayListName().get(i - 1));
        }


    }

    private void dialod(final Feedback feedback, final String type) {
        d = new Dialog(this);
        d.setContentView(R.layout.feedback_approve);
        d.setTitle("Manage");

        d.setCancelable(true);
        Button buttonApproveManage = (Button) d.findViewById(R.id.buttonApproveManage);
        Button buttonRejectionManege = (Button) d.findViewById(R.id.buttonRejectionManage);
        Button buttonDeleteManege = (Button) d.findViewById(R.id.buttonDeleteFeed);


        buttonApproveManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FeedbackApprov").push();
                final String keyApprov = ref.getKey();
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("FeedbackWaitApprov");
                ref2.child(feedback.getKey()).removeValue();
                feedback.setKey(keyApprov);
                ref.setValue(feedback);
                Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityManagementCardsApprov2.class);
                startActivityForResult(intent, 0);
            }
        });
        buttonRejectionManege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FeedbackRejection").push();
                final String keyApprov = ref.getKey();
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("FeedbackWaitApprov");
                ref2.child(feedback.getKey()).removeValue();
                feedback.setKey(keyApprov);
                ref.setValue(feedback);
                Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityManagementCardsApprov2.class);

                startActivityForResult(intent, 0);
            }
        });
        buttonDeleteManege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(type);
                ref2.child(feedback.getKey()).removeValue();
                Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityManagementCardsApprov2.class);

                startActivityForResult(intent, 0);

            }
        });
        d.show();

    }

    private void setNumUsers() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RegisterInformation");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    return;
                }
                int sum = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    sum++;
                }
                TextView textView = findViewById(R.id.textViewNumUsers);
                textView.setText("" + sum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_management, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {
            case R.id.mainMenuManaga:
                intent2 = new Intent(MainActivityManagementCardsApprov2.this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.mainIconMenuManga:
                intent2 = new Intent(MainActivityManagementCardsApprov2.this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.cardsAproveMenu:
                playCards2("CardsApprov");
                return true;
            case R.id.cardsRejecsenMenu:
                playCards2("CardsRejection");

                return true;
            case R.id.cardsWaitAproveMenu:
                playCards2("CardsWaitApprov");

                return true;
            case R.id.feedsAproveMenu:
                playFeed("FeedbackApprov");

                return true;
            case R.id.feedsRejecsenMenu:
                playFeed("FeedbackRejection");

                return true;
            case R.id.feedsWaitAproveMenu:
                playFeed("FeedbackWaitApprov");
                return true;
            case R.id.deletePassDateMenu:
                arrayListCardsPassDate = new ArrayList<>();
                deletePassDate("CardsApprov");
                deletePassDate("CardsRejection");
                deletePassDate("CardsWaitApprov");

                return true;
            case R.id.cardsDeleteHistotyMenu:
                playCards2("CardsDeleteHistory");

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deletePassDate(final String type) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(type);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    CardTutor temp = child.getValue(CardTutor.class);
//                    if (passDate(temp.getDateStart())) {//delete crad if pass daete and save in history
//                        arrayListCardsPassDate.add(child.getValue(CardTutor.class));
//                    }
//
//                }
//                if (type.equals("CardsWaitApprov")) {
//                    if (arrayListCardsPassDate.size() == 0) {
//
//                        return;
//                    }
//                    CardCarAdapter cardCarAdapter = new CardCarAdapter(MainActivityManagementCardsApprov.this, 0, 0, arrayListCardsPassDate, true);
//                    //phase 4 reference to listview
//                    lv1 = (ListView) findViewById(R.id.lvMange);
//                    lv1.setAdapter(cardCarAdapter);
////
//                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            dialogCardPassDate(arrayListCardsPassDate.get(i));
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void dialogCardPassDate(final CardTutor cardTutorPass) {
        d = new Dialog(this);
        d.setContentView(R.layout.delet_pass_date);
        d.setTitle("Manage");
        d.setCancelable(true);
        Button buttonDelete = (Button) d.findViewById(R.id.buttonDeletePassDate);
        Button buttonView = (Button) d.findViewById(R.id.buttonViewCards);
        Button buttonCloseDialog = (Button) d.findViewById(R.id.buttonCloseWindow);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("CardsDeleteHistory").push();
                DatabaseReference cardRef3;
                if (cardTutorPass.getPermissionToPublish() == 0)
                    cardRef3 = FirebaseDatabase.getInstance().getReference("CardsWaitApprov");
                else if (cardTutorPass.getPermissionToPublish() == 2)
                    cardRef3 = FirebaseDatabase.getInstance().getReference("CardsRejection");
                else
                    cardRef3 = FirebaseDatabase.getInstance().getReference("CardsApprov");
                cardRef3.child(cardTutorPass.getKey()).removeValue();
                cardTutorPass.setKey(cardRef4.getKey());
                cardRef4.setValue(cardTutorPass);
                Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityManagementCardsApprov2.class);

                startActivityForResult(intent, 0);

            }
        });
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityManagementCardsApprov2.this, MainActivityCardView.class);
                intent.putExtra("flagMain", true);
                pass(intent, cardTutorPass);
                startActivityForResult(intent, 0);
            }
        });
        buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean passDate(String dateStart) {
        Date dNow = new Date();
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        String thisData = simpleFormat.format(dNow);
        int yearStartToday = Integer.valueOf(thisData.substring(6));
        int monthStartToday = Integer.valueOf(thisData.substring(3, 5));
        int dayStartToday = Integer.valueOf(thisData.substring(0, 2));

        int yearStart = Integer.valueOf(dateStart.substring(6));
        int monthStart = Integer.valueOf(dateStart.substring(3, 5));
        int dayStart = Integer.valueOf(dateStart.substring(0, 2));

        if (yearStart < yearStartToday) return true;
        if (yearStart == yearStartToday && monthStart < monthStartToday) return true;
        if (yearStart == yearStartToday && monthStart == monthStartToday && dayStart < dayStartToday)
            return true;

        return false;
    }


}