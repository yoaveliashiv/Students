package com.example.Hikers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.chat.ActivitySettings;
import com.example.chat.MainActivity3;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(RegisterLoginActivity.this, MainActivity3.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_register_login);
        buttonSendPass = findViewById(R.id.button_send_code);
        buttonVerifi = findViewById(R.id.button_verifi_code);
        editTextPass = findViewById(R.id.editText_feed_chat);
        editTextPhone = findViewById(R.id.editText_login_phone);
        setmCallBacks();

        buttonSendPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = editTextPhone.getText().toString().trim();
                if (mobile.isEmpty() || (mobile.length() < 10 || mobile.length() > 13)) {
                    editTextPhone.setError("הכנס מספר תקין");
                    editTextPhone.requestFocus();
                    return;
                }

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
                String pass = editTextPass.getText().toString();
                if (pass.isEmpty() || pass.length() < 3) {
                    editTextPhone.setError("הכנס קוד תקין");
                    editTextPhone.requestFocus();
                    return;
                }
                verifyVerificationCode(pass);
            }
        });
        super.onCreate(savedInstanceState);


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
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;


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
                                mobile = "+972"+mobile;
                            }
                            DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("RegisterInformation");
                            ref3.orderByChild("email").equalTo(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {

                                        registerInformation = child.getValue(RegisterInformation2.class);
                                    }
                                    if (!snapshot.exists()) {

                                        registerInformation = new RegisterInformation2();
                                        registerInformation.setDeviceToken(deviceToken);
                                        registerInformation.setEmail(mobile);
                                        registerInformation.setName(mobile);
                                        saveRegisterDataFireBase();

                                    } else {
                                        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation2")
                                                .child(uid).child("deviceToken");
                                        cardRef4.setValue(deviceToken);
                                        Intent intent = new Intent(RegisterLoginActivity.this, ActivitySettings.class);
                                        intent.putExtra("flag", true);
                                        startActivity(intent);


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
        // registerInformation.foundId();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        registerInformation.setIdFirebase(uid);
        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation2").child(uid);
        cardRef4.setValue(registerInformation);


        Intent intent = new Intent(RegisterLoginActivity.this, ActivitySettings.class);
        intent.putExtra("flag", true);
        startActivity(intent);
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