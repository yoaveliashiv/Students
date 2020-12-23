package com.example.Hikers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import com.example.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.example.chat.ActivitySettings;
import com.example.chat.MainActivity3;
import com.example.students.CardCarAdapter;
import com.example.students.CardTutor;
import com.example.students.ErrWarn;
import com.example.students.MainActivityCardView;
import com.example.students.MainActivityRegisterTutor;
import com.example.students.MainActivity_Feedback;
import com.example.students.RegisterInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private CheckBox checkBox;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mobile;
    private String mVerificationId;
    private Boolean flagCode = false;
    private Dialog d;
    private EditText editTextEmail, editTextPassword;
    private Button loginButtonMain, loginButtonIn, searchButton, buttonSeeAll;
    private ProgressDialog progressDialog;
    private Button registerButton;
    private Button feedbekButton;
    private Spinner areaSpinner;
    private Spinner priceSpinner;
    private ArrayList<String> areaList = new ArrayList<>();
    private ArrayList<String> priceList = new ArrayList<>();
    private DatabaseReference cardRef2;
    private DatabaseReference cardRef;
    private String dateStart = "", dateEnd = "";

    private TextView textViewWarnEmail, textViewWarnPassword, textViewWarnAll, textVieeTittel;

    private RegisterInformation registerInformation;

    private CardCarAdapter cardCarAdapter = null;
    private ListView lv1;
    private CardCarAdapter toyAdapter1;
    private ListView lv11;
    private String area = "";
    private String price = "";
    private List<CardTutor> arrayListCards;
    private List<CardTutor> arrayListCards2;
private  String email ;
    private Boolean flagConnected=false;
    private TextView textViewWarnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        progressDialog = new ProgressDialog(this);

        flagCode = false;
        setmCallBacks();
        mAuth = FirebaseAuth.getInstance();
        textVieeTittel = findViewById(R.id.title);

        areaSpinner = findViewById(R.id.area);
        textViewWarnSearch = findViewById(R.id.textViewWarnSearch);
        priceSpinner = findViewById(R.id.price);
        setLists();
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, areaList);
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, priceList);
        areaSpinner.setAdapter(areaAdapter);
        priceSpinner.setAdapter(priceAdapter);
        setSpinerArea();


        searchButton = findViewById(R.id.search);
        buttonSeeAll = findViewById(R.id.buttonSeeAll);
        registerButton = findViewById(R.id.register);
        feedbekButton = findViewById(R.id.feedback);
        loginButtonMain = findViewById(R.id.login);
        firebaseUser = mAuth.getCurrentUser();
        flagConnected = false;
        if (firebaseUser != null) {
            ImageView imageView = findViewById(R.id.imageViewConnected);
            imageView.setVisibility(View.VISIBLE);
            flagConnected = true;
            loginButtonMain.setText("יציאה");
            registerButton.setText("לעמוד שלי");
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         email = "";
        if (firebaseUser != null) {
            try {
                if (!firebaseUser.getEmail().toString().isEmpty())
                    email = firebaseUser.getEmail().toString();
                else email = firebaseUser.getPhoneNumber().toString();
            } catch (RuntimeException e) {
                email = firebaseUser.getPhoneNumber().toString();
            }

        }

         if(email.length()==0)
             viewAllGroupsׂ();
         else
             viewMyGroups();

        // viewCards();//make cards list view

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("טוען כרטיסים...");
                progressDialog.show();
                search();


            }
        });
        loginButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flagConnected) {
                    Toast.makeText(MainActivity2.this, "!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                    startActivity(intent);


                } else
                    dialod();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flagConnected) {
                    Intent intent = new Intent(MainActivity2.this, MainActivityPageUser2.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(MainActivity2.this, MainActivityRegister2.class);
                    startActivity(intent);
                }
            }
        });

        buttonSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCards();

            }
        });

        feedbekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
//                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                String emailReport = "";
//                if (firebaseUser != null) {
//                    try {
//                        if (!firebaseUser.getEmail().toString().isEmpty())
//                            emailReport = firebaseUser.getEmail().toString();
//                        else emailReport = firebaseUser.getPhoneNumber().toString();
//                    } catch (RuntimeException e) {
//                        emailReport = firebaseUser.getPhoneNumber().toString();
//                    }
//
//                } else {
//                    Toast.makeText(MainActivity2.this, "אנא התחבר או הירשם", Toast.LENGTH_SHORT).show();
//
//                    feedbekButton.setError("אנא הירשם או התחבר");
//                    feedbekButton.requestFocus();
//                    return;
//                }
//
//                Intent intent = new Intent(MainActivity2.this, MainActivity_Feedback.class);
//                intent.putExtra("flag", true);
//                intent.putExtra("emailReporting", emailReport);
//                startActivity(intent);
            }
        });
        setDate();
    }

    private void viewMyGroups() {
        DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("RegisterInformation2");

        cardRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterInformation2 registerInformation = new RegisterInformation2();
                for (DataSnapshot child : snapshot.getChildren()) {
                    registerInformation = child.getValue(RegisterInformation2.class);
                }
                if(registerInformation.getHitchhikingGroups().size()==0){
                    viewAllGroupsׂ();
                    return;
                }
                ListView lv1 = findViewById(R.id.lvMange2);
                MyGroupsAdapter myGroupsAdapter = new MyGroupsAdapter(MainActivity2.this, 0, 0, registerInformation.getHitchhikingGroups(),email);
                lv1.setAdapter(myGroupsAdapter);
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void viewAllGroupsׂ() {
        DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("NamesGroups");

        cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> arrayList=new ArrayList<>();
              //  buttonSeeAll.setText("ff");

                for (DataSnapshot child : snapshot.getChildren()) {
                    String grohp=child.getKey();
                    arrayList.add(grohp);
                  //  buttonSeeAll.setText(""+grohp);

                }
                ListView lv1 = findViewById(R.id.lvMange2);
                AllGroupsAdapter allGroupsAdapter = new AllGroupsAdapter(MainActivity2.this, 0, 0, arrayList,email);
                lv1.setAdapter(allGroupsAdapter);
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void search() {
        textViewWarnSearch.setVisibility(View.GONE);
        cardRef = FirebaseDatabase.getInstance().getReference("CardsApprov");

        cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards2 = new ArrayList<>();
                CardTutor cardTutor;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    cardTutor = child.getValue(CardTutor.class);
                    searchMatch(area, price, cardTutor);
                }
                if (arrayListCards2.size() > 0) {
                    if (cardCarAdapter != null) cardCarAdapter.clear();
                    //phase 4 reference to listview

                    buttonSeeAll.setVisibility(View.VISIBLE);
                    toyAdapter1 = new CardCarAdapter(MainActivity2.this, 0, 0, arrayListCards2, false, 2);
                    lv11 = (ListView) findViewById(R.id.lvMange2);
                    lv11.setAdapter(toyAdapter1);

                    lv11.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity2.this, MainActivityCardView.class);
                            intent.putExtra("flagMain", true);
                            pass(intent, arrayListCards2.get(i));


                            startActivityForResult(intent, 0);
                        }
                    });
                } else textViewWarnSearch.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchMatch(String area, String price, CardTutor card) {
        EditText editTextCourse = findViewById(R.id.editTextCourse);
        String course = "" + editTextCourse.getText();
        EditText editTextProfession = findViewById(R.id.editTextProfession);
        String profession = "" + editTextProfession.getText();
        if ((course.length() == 0 && profession.length() == 0) || (checkProfessionAndCourse(course, card) || checkProfessionAndCourse(profession, card))) {
            if (area.equals(card.getArea()) || area.length() == 0 || area.equals("בחר אזור")) {

                if (price.length() == 0 || price.equals("טווח מחיר")) {
                    arrayListCards2.add(card);
                    return;
                }
                String priceCardString = card.getPriceToLesson();
                int priceCard = Integer.valueOf(priceCardString);
                if (price.equals("50-150")) {
                    if (priceCard >= 50 && priceCard < 150) arrayListCards2.add(card);
                } else if (price.equals("150-250")) {
                    if (priceCard >= 150 && priceCard < 250) arrayListCards2.add(card);
                } else if (price.equals("250-350")) {
                    if (priceCard >= 250 && priceCard < 350) arrayListCards2.add(card);
                } else if (price.equals("350-450")) {
                    if (priceCard >= 350 && priceCard < 450)
                        arrayListCards2.add(card);
                } else if (price.equals("450-550")) {
                    if (priceCard >= 450 && priceCard < 550)
                        arrayListCards2.add(card);
                } else if (price.equals("550+")) {
                    if (priceCard >= 550) arrayListCards2.add(card);
                }
            }
        }


    }


    private void viewCards() {
        progressDialog.setMessage("טוען כרטיסים...");
        progressDialog.show();
        textViewWarnSearch.setVisibility(View.GONE);

        cardRef2 = FirebaseDatabase.getInstance().getReference("CardsApprov");

        cardRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCards = new ArrayList();
                CardTutor card;
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    i++;
                    if (i > 30) break;
                    card = child.getValue(CardTutor.class);
                    arrayListCards.add(card);
                }
                if (arrayListCards.size() > 0) {
                    buttonSeeAll.setVisibility(View.GONE);

                    cardCarAdapter = new CardCarAdapter(MainActivity2.this, 0, 0, arrayListCards, false, 2);
                    //phase 4 reference to listview
                    lv1 = (ListView) findViewById(R.id.lvMange2);
                    lv1.setAdapter(cardCarAdapter);

                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity2.this, MainActivityCardView.class);
                            intent.putExtra("flagMain", true);
                            pass(intent, arrayListCards.get(i));
                            startActivityForResult(intent, 0);
                        }
                    });
                }
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


    }


    private void setLists() {
        areaList.add("בחר אזור");
        areaList.add("צפון");
        areaList.add("מרכז");
        areaList.add("דרום");

        priceList.add("טווח מחיר");
        priceList.add("50-150");
        priceList.add("150-250");
        priceList.add("250-350");
        priceList.add("350-450");
        priceList.add("450-550");
        priceList.add("550+");
    }


    public void setSpinerArea() {
        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                price = priceList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = areaList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public static void pass(Intent intent, CardTutor cardTutor) {
        intent.putExtra("name", cardTutor.getName());
        intent.putExtra("area", cardTutor.getArea());
        intent.putExtra("city", cardTutor.getCity());
        intent.putExtra("id", cardTutor.getId());
        intent.putExtra("numImage", cardTutor.getImageViewArrayListName().size());
        intent.putExtra("permissionToPublish", cardTutor.getPermissionToPublish());
        intent.putExtra("phone", cardTutor.getPhone());
        intent.putExtra("priceDay", cardTutor.getPriceToLesson());
        intent.putExtra("remarks", cardTutor.getRemarks());

        intent.putExtra("numExpertise", cardTutor.getArrayListExpertise().size());
        intent.putExtra("email", cardTutor.getEmail());
        intent.putExtra("key", cardTutor.getKey());
        intent.putExtra("rejection", cardTutor.getRejection());
        for (int i = 0; i < cardTutor.getArrayListExpertise().size(); i++) {
            intent.putExtra("expertise" + i, cardTutor.getArrayListExpertise().get(i).toString());
        }
        for (int i = 1; i <= cardTutor.getImageViewArrayListName().size(); i++) {
            intent.putExtra("image" + i, cardTutor.getImageViewArrayListName().get(i - 1));
        }


    }

    private void dialod() {
        d = new Dialog(this);
        d.setContentView(R.layout.login);
        d.setTitle("Login");

        d.setCancelable(true);
        checkBox = (CheckBox) d.findViewById(R.id.checkBoxPhone);
//        if (checkBox.isChecked()) {
//           // checkBox.setChecked(false);
//        }

        editTextEmail = (EditText) d.findViewById(R.id.loginEmail);
        editTextPassword = (EditText) d.findViewById(R.id.loginPassword);
        loginButtonIn = (Button) d.findViewById(R.id.loginButton2);
        textViewWarnAll = (TextView) d.findViewById(R.id.textWarnAll);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editTextEmail.setText("");
                    editTextEmail.setInputType(3);
                    editTextEmail.setHint("פלאפון");
                    editTextPassword.setVisibility(View.GONE);
                    editTextPassword.setText("");
                } else {
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    editTextEmail.setInputType(1);
                    editTextEmail.setHint("אימייל");
                    editTextPassword.setVisibility(View.VISIBLE);
                }
            }
        });
        loginButtonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkBox.isChecked()) {//login email

                    if (editTextEmail.getText().toString().isEmpty() ||
                            ErrWarn.errEmail(editTextEmail.getText().toString())) {
                        editTextEmail.setError("אימייל לא תקין");
                        editTextEmail.requestFocus();
                        return;
                    }
                    if (editTextPassword.getText().toString().isEmpty()) {
                        editTextPassword.setError("סיסמא לא תקינה");
                        editTextPassword.requestFocus();
                        return;
                    }
                    loginIn();
                } else {        //login phome
                    if (flagCode) {//VerificationCode
                        String code = editTextPassword.getText().toString().trim();
                        if (code.isEmpty() || code.length() < 6) {
                            editTextPassword.setError("Enter valid code");
                            editTextPassword.requestFocus();
                            return;
                        } else
                            verifyVerificationCode(code);
                    } else {//send code

                        mobile = editTextEmail.getText().toString().trim();
                        if (mobile.isEmpty() || mobile.length() < 10) {
                            editTextEmail.setError("Enter a valid mobile");
                            editTextEmail.requestFocus();
                            return;
                        } else {
                            flagCode = true;


                            sendVerificationCode(mobile);
                            editTextPassword.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        d.show();

    }

    private void loginIn() {
        progressDialog.setMessage("נכנס...");
        progressDialog.show();
        String emailLogin = editTextEmail.getText().toString();
        String passwordLogin = editTextPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity2.this, "הכניסה הצליחה", Toast.LENGTH_SHORT).show();
                            loginButtonMain.setText("יציאה");
                            progressDialog.dismiss();
                            Intent intent = new Intent(MainActivity2.this, MainActivityPageUser2.class);
                            startActivity(intent);
                            d.dismiss();
                        } else {
                            progressDialog.dismiss();
//                                textViewWarnAll.setText("אימייל או סיסמא לא נכונים");
//                                textViewWarnAll.setVisibility(View.VISIBLE);
                            editTextPassword.setError("אימייל או סיסמא לא נכונים");
                            editTextPassword.requestFocus();
                            Toast.makeText(MainActivity2.this, "הכניסה נכשלה", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void setmCallBacks() {
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();
                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    editTextPassword.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
            }
        };
    }

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential;
        //creating the credential
        // editTextEmail.setText(mVerificationId);
        try {
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        } catch (RuntimeException e) {
            editTextPassword.setError("משו השתבש לחץ כניסה שוב");
            editTextPassword.requestFocus();
            return;

        }

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setMessage("נכנס...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerInformation = null;
                            mobile = mobile.substring(1);
                            DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
                            ref3.orderByChild("email").equalTo("+972" + mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {

                                        registerInformation = child.getValue(RegisterInformation.class);
                                    }
                                    if (!snapshot.exists()) {

                                        registerInformation = new RegisterInformation();
                                        registerInformation.setEmail("+972" + mobile);
                                        saveRegisterDataFireBase();
                                    } else {
                                        progressDialog.dismiss();
                                        d.dismiss();
                                        Intent intent = new Intent(MainActivity2.this, MainActivityPageUser2.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {

                            //verification unsuccessful.. display an error message
                            progressDialog.dismiss();
                            editTextPassword.setError("Enter valid code");
                            editTextPassword.requestFocus();

                            return;

                        }
                    }
                });
    }

    private void saveRegisterDataFireBase() {

        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation").push();
        cardRef4.setValue(registerInformation);
        progressDialog.dismiss();
        d.dismiss();
        Intent intent = new Intent(MainActivity2.this, MainActivityPageUser2.class);
        startActivity(intent);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+972" + mobile)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(MainActivity2.this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void setDate() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        // MaterialDatePicker.Builder builder=MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("בחר תאריך");
        final MaterialDatePicker materialDatePicker = builder.build();


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair selectedDates = (Pair) materialDatePicker.getSelection();
//              then obtain the startDate & endDate from the range
                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//              assigned variables
                Date startDate = rangeDate.first;
                Date endDate = rangeDate.second;
//              Format the dates in ur desired display mode

                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//              Display it by setText
                dateStart = simpleFormat.format(startDate).toString();
                dateEnd = simpleFormat.format(endDate);


            }
        });

    }

    public boolean dateSearch(String dateStartUser, String dateEndUser) {
//        Toast.makeText(MainActivity1.this, "" + dateStartUser + " " + dateEndUser + ";" + dateStart + " " + dateEnd, Toast.LENGTH_LONG).show();
//        final Button buttonData = findViewById(R.id.buttonDate2);
//        buttonData.setTextSize(8);

        int yearStartUser = Integer.valueOf(dateStartUser.substring(6));
        int yearEndUser = Integer.valueOf(dateEndUser.substring(6));
        int monthStartUser = Integer.valueOf(dateStartUser.substring(3, 5));
        int monthEndUser = Integer.valueOf(dateEndUser.substring(3, 5));
        int dayStartUser = Integer.valueOf(dateStartUser.substring(0, 2));
        int dayEndUser = Integer.valueOf(dateEndUser.substring(0, 2));

        int yearStart = Integer.valueOf(dateStart.substring(6));
        int yearEnd = Integer.valueOf(dateEnd.substring(6));
        int monthStart = Integer.valueOf(dateStart.substring(3, 5));
        int monthEnd = Integer.valueOf(dateEnd.substring(3, 5));
        int dayStart = Integer.valueOf(dateStart.substring(0, 2));
        int dayEnd = Integer.valueOf(dateEnd.substring(0, 2));

        if (yearStart < yearStartUser) return false;
        if (yearStart == yearStartUser && monthStart < monthStartUser) return false;
        if (yearStart == yearStartUser && monthStart == monthStartUser && dayStart < dayStartUser)
            return false;
        if (yearEnd > yearEndUser) return false;
        if (yearEnd == yearEndUser && monthEnd > monthEndUser) return false;
        if (yearEnd == yearEndUser && monthEnd == monthEndUser && dayEnd > dayEndUser) return false;


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (flagConnected) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_app, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {
            case R.id.mainMenu:
                intent2 = new Intent(MainActivity2.this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.mainIconMenu:
                intent2 = new Intent(MainActivity2.this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.myPageMenu:
                intent2 = new Intent(MainActivity2.this, MainActivityPageUser2.class);
                startActivity(intent2);
                return true;
            case R.id.cardCarMenu:
                intent2 = new Intent(MainActivity2.this, MainActivityRegisterTutor.class);
                startActivity(intent2);
                return true;
            case R.id.settingsMenu:
                if(!flagConnected){
                    intent2 = new Intent(MainActivity2.this, MainActivityRegister2.class);
                    startActivity(intent2);
                }
                else {
                    intent2 = new Intent(MainActivity2.this, ActivitySettings.class);
                    intent2.putExtra("email",email);
                    startActivity(intent2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkProfessionAndCourse(String course, CardTutor card) {
        if (course.length() == 0) return false;
        ArrayList<String> arrayList = new ArrayList<>(card.getArrayListExpertise());
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).contains(course)) return true;

        }
        return false;
    }
}
