package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Hikers.MainActivity2;
import com.example.Hikers.MainActivityRegister2;
import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ActivitySettings extends AppCompatActivity {
    private Uri uriImage = null;
    private ImageView imageView;
    private static final int GallaryPick = 1;
    private RegisterInformation2 registerInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        imageView = findViewById(R.id.profile_image);
        getSupportActionBar().setTitle("הגדרות חשבון");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGallary = new Intent();
                intentGallary.setAction(Intent.ACTION_GET_CONTENT);
                intentGallary.setType("image/*");
                startActivityForResult(intentGallary, GallaryPick);
                //  openImage();
            }
        });
        final EditText editTextName = findViewById(R.id.edit_name);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerInformation = new RegisterInformation2();
                registerInformation = snapshot.getValue(RegisterInformation2.class);
                editTextName.setText(registerInformation.getName());
                if (registerInformation.getImageUrl().length() > 0) {
                    Glide.with(ActivitySettings.this)
                            .load(registerInformation.getImageUrl())
                            .into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button button = findViewById(R.id.button_set_settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                if (name.length() == 0) {
                    editTextName.setError("אנא הוסף/שנה שם");
                    editTextName.requestFocus();
                    return;
                }

                fireBaseImage(name);

            }
        });

    }

    public void openImage() {


        try {
            if (ActivityCompat.checkSelfPermission(ActivitySettings.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivitySettings.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GallaryPick && resultCode == RESULT_OK && data != null) {
//    CropImage.activity()
//            .setGuidelines(CropImageView.Guidelines.ON)
//
//            .start(this);
//    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//        CropImage.ActivityResult result = CropImage.getActivityResult(data);
//       // if(resultCode==RESULT_OK) {
//            uriImage = result.getUri();
//            imageView.setImageURI(uriImage);
//      //  }
//    }
            uriImage = data.getData();
            imageView.setImageURI(uriImage);
        }
    }
    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch (requestCode) {
//
//            case 1:
//
//                uriImage = imageReturnedIntent.getData();
//                imageView.setImageURI(uriImage);
//
//
//                break;
//        }
//    }

    public void fireBaseImage(final String name) {
        if (uriImage == null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
            databaseReference.child(uid).child("name").setValue(name);
            Intent intent = new Intent(ActivitySettings.this, MainActivity2.class);
            startActivity(intent);
            finish();
            return;
        }

        StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                .child("profileImage/"+registerInformation.getEmail()+".jpg");
        riversRef.putFile(uriImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
                                databaseReference.child(uid).child("name").setValue(name);
                                databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
                                databaseReference.child(uid).child("imageUrl").setValue(uri.toString());
                                Intent intent = new Intent(ActivitySettings.this, MainActivity2.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ActivitySettings.this, "תמונה לא נשמרה", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}