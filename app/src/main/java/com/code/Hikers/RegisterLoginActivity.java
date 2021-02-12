package com.code.Hikers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.code.R;
import com.code.chat.ActivitySettings;
import com.code.game.PlayActivity2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterLoginActivity extends AppCompatActivity {
    private Button buttonSendPass, buttonVerifi;
    private EditText editTextPhone, editTextPass;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mobile;
    private String deviceToken;
    private RegisterInformation2 registerInformation;
    private FirebaseAuth mAuth;
    private String nameCollegeEnglish = "";
    private String nameCollegeHebrow = "";
    private ProgressDialog progressDialog;
    private Boolean flagDelete = false;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(RegisterLoginActivity.this, PlayActivity2.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_register_login);
        buttonSendPass = findViewById(R.id.button_send_code);
        buttonVerifi = findViewById(R.id.button_verifi_code);
        editTextPass = findViewById(R.id.editText_feed_chat);
        editTextPhone = findViewById(R.id.editText_login_phone);
        spinner = findViewById(R.id.spinner_name_coleg);

        if (getIntent().hasExtra("flagDeleteEnglish")) {
            flagDelete = true;
            spinner.setVisibility(View.GONE);
            buttonVerifi.setText("אשר מחיקה");
            nameCollegeEnglish = getIntent().getExtras().getString("flagDeleteEnglish");
            nameCollegeHebrow = getIntent().getExtras().getString("flagDeleteHebrew");
        } else {
            setSpinner();
        }

        setmCallBacks();

        buttonSendPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("שולח קוד אישור אנא חכה להשלמת התהליך...");
                progressDialog.show();
                progressDialog.setCancelable(false);

                mobile = editTextPhone.getText().toString().trim();
                if (mobile.isEmpty() || (mobile.length() < 10 || mobile.length() > 13)) {
                    editTextPhone.setError("הכנס מספר תקין");
                    editTextPhone.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
//                if (nameCollegeEnglish.isEmpty() || nameCollegeEnglish.equals("בחר מוסד אקדמי")) {
//                    editTextPhone.setError("בחר מוסד אקדמי למטה");
//                    editTextPhone.requestFocus();
//                    progressDialog.dismiss();
//                    return;
//                }

                sendVerificationCode(mobile);
                buttonSendPass.setVisibility(View.GONE);
                buttonVerifi.setVisibility(View.VISIBLE);
                editTextPhone.setVisibility(View.GONE);
                editTextPass.setVisibility(View.VISIBLE);

            }
        });

        buttonVerifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("אנא המתן להשלמת התהליך..");
                progressDialog.show();
//                if (nameCollegeEnglish.isEmpty() || nameCollegeEnglish.equals("בחר מוסד אקדמי")) {
//                    editTextPass.setError("בחר מוסד אקדמי למטה");
//                    editTextPass.requestFocus();
//                    progressDialog.dismiss();
//                    return;
//                }
                String pass = editTextPass.getText().toString();
                if (pass.isEmpty() || pass.length() < 3) {
                    editTextPass.setError("הכנס קוד תקין");
                    editTextPass.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                verifyVerificationCode(pass);
            }
        });
        super.onCreate(savedInstanceState);


    }

    private void setSpinner() {
        final ArrayList<String> arrayListNameSpinnerHebrow = new ArrayList<>();
        final ArrayList<String> arrayListNameSpinnerEnglish = new ArrayList<>();
        arrayListNameSpinnerEnglish.add("בחר מוסד אקדמי");
        arrayListNameSpinnerHebrow.add("בחר מוסד אקדמי");

        final ArrayAdapter<String> nameClogeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayListNameSpinnerHebrow);
        spinner.setAdapter(nameClogeAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nameCollegeEnglish = arrayListNameSpinnerEnglish.get(i);
                nameCollegeHebrow = arrayListNameSpinnerHebrow.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("NamesColleges");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayListNameSpinnerEnglish.add(child.getKey());
                    arrayListNameSpinnerHebrow.add(child.getValue(String.class));

                }
                nameClogeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setmCallBacks() {
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressDialog.dismiss();
                String code = phoneAuthCredential.getSmsCode();

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    editTextPass.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegisterLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressDialog.dismiss();
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                Toast.makeText(RegisterLoginActivity.this, "קוד נשלח ל-" + mobile + " אנא המתן לקבלת הקוד", Toast.LENGTH_LONG).show();


                //  mResendToken = forceResendingToken;
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
            editTextPass.setError("משו השתבש לחץ כניסה שוב");
            editTextPass.requestFocus();
            return;

        }

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String uid = mAuth.getCurrentUser().getUid();
                            deviceToken = "";
//                            FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<InstallationTokenResult> task) {
//                                    deviceToken = task.getResult().getToken();
//                                }
//                            });
                            try {


                                deviceToken = FirebaseInstanceId.getInstance().getToken();
                            } catch (RuntimeException e) {
                                deviceToken = "";
                            }
                            registerInformation = null;
                            if (!mobile.startsWith("+972")) {
                                mobile = mobile.substring(1);
                                mobile = "+972" + mobile;
                            }
                            DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
                            ref3.orderByChild("email").equalTo(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {

                                        registerInformation = child.getValue(RegisterInformation2.class);
                                    }
                                    if (!snapshot.exists()) {

                                        registerInformation = new RegisterInformation2();
                                        registerInformation.setNameCollegeEnglish(nameCollegeEnglish);
                                        registerInformation.setNameCollegeHebrew(nameCollegeHebrow);
                                        registerInformation.setDeviceToken(deviceToken);
                                        registerInformation.setEmail(mobile);
                                        saveRegisterDataFireBase();

                                    } else {
                                        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation2")
                                                .child(uid);
                                        registerInformation.setNameCollegeEnglish(nameCollegeEnglish);
                                        registerInformation.setNameCollegeHebrew(nameCollegeHebrow);
                                        registerInformation.setDeviceToken(deviceToken);
                                        cardRef4.setValue(registerInformation);
                                        if (flagDelete) {
                                            deleteUser();
                                        } else {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(RegisterLoginActivity.this, ActivitySettings.class);
                                            intent.putExtra("flag", true);
                                            startActivity(intent);
                                        }


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            //verification successful we will start the profile activity

                        } else {

                            //verification unsuccessful.. display an error message

                            editTextPass.setError("Enter valid code");
                            editTextPass.requestFocus();

                            return;
                        }
                    }
                });
    }


    private void saveRegisterDataFireBase() {
        Calendar calendar = Calendar.getInstance();//new users count
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());
        DatabaseReference cardRef5 = FirebaseDatabase.getInstance()
                .getReference("NewUsers").child(date).push();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("nameCollegeEnglish", nameCollegeEnglish);
        hashMap.put("nameCollegeHebrow", nameCollegeHebrow);

        cardRef5.setValue(hashMap);
        registerInformation.setDateRegister(date);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation2").child(uid);
        cardRef4.setValue(registerInformation);

        if (flagDelete) {
            deleteUser();
        } else {
            progressDialog.dismiss();

            Intent intent = new Intent(RegisterLoginActivity.this, ActivitySettings.class);
            intent.putExtra("flag", true);
            startActivity(intent);
        }
    }

    private void deleteUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Calendar calendar = Calendar.getInstance();//delete users count
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String date = simpleDateFormat.format(calendar.getTime());
                            DatabaseReference cardRef5 = FirebaseDatabase.getInstance()
                                    .getReference("DeleteUsers").child(date).push();
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("nameCollegeEnglish", nameCollegeEnglish);
                            hashMap.put("nameCollegeHebrow", nameCollegeHebrow);

                            cardRef5.setValue(hashMap);

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference("RegisterInformation2").child(uid);
                            databaseReference.removeValue();

                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                                    .getReference("Contacts").child(uid);
                            databaseReference1.removeValue();

                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                                    .getReference("NotificationsIdSeeLast").child(uid);
                            databaseReference2.removeValue();
                            if (!registerInformation.getImageUrl().isEmpty()) {
                                StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                                        .child("profileImage/" + uid + ".jpg");
                                riversRef.delete();
                            }
                            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                                    .getReference("MyGroups").child(uid);
                            databaseReference3.removeValue();
                            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance()
                                    .getReference("Groups details");
                            databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        for (DataSnapshot child2 : child.getChildren()) {
                                            if (child2.getKey().equals(uid))
                                                child2.getRef().removeValue();
                                            // textViewGroupDetails.setText(child2.getKey());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(RegisterLoginActivity.this, "החשבון נמחק בהצלחה", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(RegisterLoginActivity.this, RegisterLoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+972" + mobile)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(RegisterLoginActivity.this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_login, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {
            case R.id.mainIconMenu:
                intent2 = new Intent(RegisterLoginActivity.this, RegisterLoginActivity.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}