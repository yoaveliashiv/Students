package com.example.Hikers;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.students.ErrWarn;
import com.example.students.MainActivity1;
import com.example.students.MainActivityPageUser;

import com.example.students.MainActivityRegisterTutor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.concurrent.TimeUnit;

import com.example.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivityRegister2 extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference cardRef;

    private TextView textViewWarnEmail, textViewWarnPassword1, textViewWarnPassword2, textViewWarnAll;
    private FirebaseAuth mAuth;
    private RegisterInformation2 registerInformation = null;
    private Button loginButtonIn;
    private Button registerButton, buttonPhone, buttonSmsCode;
    private Button returnButton;
    private EditText editTextEmail, editTextPhone, editTextSmsCode;
    private EditText editTextPass1;
    private EditText editTextPass2;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mobile;

    private CheckBox checkBox;
    private CheckBox checkBoxTerms;

    private Boolean flagCode = false;
    private ImageView imageView;
    private Uri uriImage = null;
    private Dialog d;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register2);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        setmCallBacks();//register phone

        checkBoxTerms = findViewById(R.id.checkBoxTerms);
        textViewWarnEmail = findViewById(R.id.textWarnEmail);
        textViewWarnPassword1 = findViewById(R.id.textWarnPassword1);
        textViewWarnPassword2 = findViewById(R.id.textWarnPassword2);
        textViewWarnAll = findViewById(R.id.textWarnAll);

        registerButton = findViewById(R.id.register);
        buttonSmsCode = findViewById(R.id.buttonCodeGo);
        buttonPhone = findViewById(R.id.buttonRegisterPhone);
        returnButton = findViewById(R.id.mainPage);
        editTextSmsCode = findViewById(R.id.editTextSmsCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.email);
        editTextPass1 = findViewById(R.id.editTextTextPassword);
        editTextPass2 = findViewById(R.id.password2);
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialod();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {//email
            @Override
            public void onClick(View view) {
                register();
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRegister2.this, MainActivity1.class);
                startActivity(intent);
            }
        });

        checkBoxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                textViewWarnAll.setText("תקנון חייב להיות מאושר");
                checkBoxTerms.setChecked(true);
                textViewWarnAll.setVisibility(View.VISIBLE);


            }
        });

        imageView = findViewById(R.id.imageViewFaceRegister);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
    }


    private void registerFirebase(String email, String password) {
        final String TAG = "tag";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            fireBaseImage();
                        } else {
                            textViewWarnAll.setText("אימייל או סיסמא לא תקינים");
                            textViewWarnAll.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    public void register() {//warng err
        textViewWarnPassword2.setVisibility(View.GONE);
        textViewWarnPassword1.setVisibility(View.GONE);
        textViewWarnAll.setVisibility(View.GONE);
        textViewWarnEmail.setVisibility(View.GONE);

        String email = editTextEmail.getText().toString();
        if (ErrWarn.errEmail(email)) {
            textViewWarnEmail.setText("אימייל לא קיים");
            textViewWarnEmail.setVisibility(View.VISIBLE);
            return;
        }
        String pass1 = "" + editTextPass1.getText().toString();
        if (pass1.length() < 6) {
            textViewWarnPassword1.setText("סיסמא קצרה מדי, לפחות 6 תווים");
            textViewWarnPassword1.setVisibility(View.VISIBLE);
            return;
        }
        String pass2 = editTextPass2.getText().toString();
        if (!pass1.equals(pass2)) {
            textViewWarnPassword2.setText("סיסמא לא תואמת");
            textViewWarnPassword2.setVisibility(View.VISIBLE);
            return;
        }
        EditText editTextName=findViewById(R.id.editTextNameRegister);
        registerInformation.setName(""+editTextName.getText().toString());
        registerInformation = new RegisterInformation2();
        registerInformation.setEmail(email);
        registerInformation.setPassword(pass1);
        registerFirebase(email, pass1);            //register


    }


    private void dialod() {

        d = new Dialog(this);
        d.setContentView(R.layout.login);
        d.setTitle("Login");

        d.setCancelable(true);
        checkBox = (CheckBox) d.findViewById(R.id.checkBoxPhone);

        checkBox.setVisibility(View.GONE);


        editTextEmail = (EditText) d.findViewById(R.id.loginEmail);
        editTextPassword = (EditText) d.findViewById(R.id.loginPassword);
        loginButtonIn = (Button) d.findViewById(R.id.loginButton2);
        editTextEmail.setText("");
        editTextEmail.setInputType(3);
        editTextEmail.setHint("פלאפון");
        editTextPassword.setVisibility(View.GONE);
        editTextPassword.setText("");

        loginButtonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flagCode) {
                    String code = editTextPassword.getText().toString().trim();
                    if (code.isEmpty() || code.length() < 6) {
                        editTextPassword.setError("Enter valid code");
                        editTextPassword.requestFocus();
                        return;
                    } else
                        verifyVerificationCode(code);
                } else {

                    mobile = editTextEmail.getText().toString().trim();
                    if (mobile.isEmpty() || mobile.length() < 10) {
                        editTextEmail.setError("Enter a valid mobile");
                        editTextEmail.requestFocus();
                        return;
                    }
                    mobile = mobile;

                    sendVerificationCode(mobile);
                     editTextPassword.setVisibility(View.VISIBLE);

                }
            }

        });
        d.show();

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
                Toast.makeText(MainActivityRegister2.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                flagCode = true;
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
            editTextPassword.setError("משו השתבש לחץ כניסה שוב");
            editTextPassword.requestFocus();
            return;

        }

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivityRegister2.this, new OnCompleteListener<AuthResult>() {
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

                                        registerInformation = child.getValue(RegisterInformation2.class);
                                    }
                                    if (!snapshot.exists()) {

                                        registerInformation = new RegisterInformation2();
                                        registerInformation.setEmail("+972" + mobile);
                                        fireBaseImage();

                                    } else {
                                        d.dismiss();
                                        Intent intent = new Intent(MainActivityRegister2.this, MainActivityPageUser2.class);

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

                            editTextPassword.setError("Enter valid code");
                            editTextPassword.requestFocus();

                            return;
                        }
                    }
                });
    }

    private void saveRegisterDataFireBase() {
        registerInformation.foundId();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        registerInformation.setIdFirebase(uid);
        editTextEmail.setText(uid);
        DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation2").child(uid);
        cardRef4.setValue(registerInformation);

        d.dismiss();

        Intent intent = new Intent(MainActivityRegister2.this, MainActivityPageUser.class);
        startActivity(intent);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+972" + mobile)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(MainActivityRegister2.this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_app, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {
            case R.id.mainMenuRegister:
                intent2 = new Intent(MainActivityRegister2.this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.mainIconMenuRegister:
                intent2 = new Intent(MainActivityRegister2.this, MainActivity1.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openImage() {


        try {
            if (ActivityCompat.checkSelfPermission(MainActivityRegister2.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivityRegister2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
//            case 0:
////                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    imageViewTemp.setImageURI(selectedImage);
//                    setUriImage(selectedImage);
//                    arrayListImageViewUri.add(selectedImage);
//
//                }
//
//                break;
            case 1:

                uriImage = imageReturnedIntent.getData();
                imageView.setImageURI(uriImage);


                break;
        }
    }

    public void fireBaseImage() {
        if (uriImage == null){
            saveRegisterDataFireBase();
            return;
        }
        StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                .child(registerInformation.getEmail() + "/image");
        riversRef.putFile(uriImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                registerInformation.setImageUrl(uri.toString());

                                saveRegisterDataFireBase();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        saveRegisterDataFireBase();
                        Toast.makeText(MainActivityRegister2.this, "תמונה לא נשמרה", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}