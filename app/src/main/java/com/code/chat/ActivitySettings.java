package com.code.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.code.Hikers.RegisterInformation2;
import com.code.Hikers.RegisterLoginActivity;
import com.code.R;
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

import java.util.ArrayList;

public class ActivitySettings extends AppCompatActivity {
    private Uri uriImage = null;
    private ImageView imageView;
    private static final int GallaryPick = 1;
    private RegisterInformation2 registerInformation;
    private String uid;
    private ProgressDialog progressDialog;
    private String dataBlock;
    private String nameCollegeEnglishSpinerr = "";
    private String nameCollegeHebrowSpinerr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_settings);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("אנא חכה להשלמת התהליך..");
        Boolean flag = false;
        if (getIntent().hasExtra("flag")) {
            flag = getIntent().getExtras().getBoolean("flag");
        }
        if (getIntent().hasExtra("flagBloked")) {
            dataBlock = getIntent().getExtras().getString("flagBloked");
            dialodBlockMenge();
        }
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

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        Button buttonAdd = findViewById(R.id.button_set_settings);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                if (name.length() == 0) {
                    editTextName.setError("אנא הוסף/שנה שם");
                    editTextName.requestFocus();
                    return;
                }
                progressDialog.show();
                fireBaseImage(name);

            }
        });
        Button button_setting_account = findViewById(R.id.button_setting_account);
        button_setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialodSettingAccount();
            }
        });

        Button buttonLogOut = findViewById(R.id.button_log_out);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogLogOut();
            }
        });

        if (flag) {
            buttonLogOut.setVisibility(View.GONE);
            button_setting_account.setVisibility(View.GONE);

            Button buttonSkip = findViewById(R.id.button_skip);
            buttonSkip.setVisibility(View.VISIBLE);
            buttonSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivitySettings.this, MainActivity3.class);
                    startActivity(intent);

                }
            });
        }
        super.onCreate(savedInstanceState);
    }

    private void dialodSettingAccount() {
        final Dialog d = new Dialog(ActivitySettings.this);
        d.setContentView(R.layout.dialog_setting_account);
        d.setTitle("Manage");
        d.setCancelable(true);
        Button buttonDelete = d.findViewById(R.id.button_delete_account);
        Button buttonChanghColege = d.findViewById(R.id.button_chang_cologe);
        Button buttonBloke = d.findViewById(R.id.button_i_bloke);

        final Spinner spinner = d.findViewById(R.id.spinner_name_coleg);
        setSpinner(spinner);
buttonBloke.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        dialogIBlock();
    }
});
        buttonChanghColege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cardRef4 = FirebaseDatabase.getInstance().getReference("RegisterInformation2")
                        .child(uid);
                registerInformation.setNameCollegeEnglish(nameCollegeEnglishSpinerr);
                registerInformation.setNameCollegeHebrew(nameCollegeHebrowSpinerr);
                cardRef4.setValue(registerInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActivitySettings.this, "מוסד אקדמי השתנה בהצלחה", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialodDelete();

            }

        });

        d.show();

    }

    private void dialogIBlock() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Blocked").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String>arrayListBlok=new ArrayList<>();
                final ArrayList<Blocked>arrayListBlok2=new ArrayList<>();

                for(DataSnapshot child:snapshot.getChildren()){
                    Blocked blocked=child.getValue(Blocked.class);
                    if(blocked.getIBloked()) {
                        arrayListBlok.add(child.getKey());
                        arrayListBlok2.add(blocked);
                    }
                }
                final Dialog d = new Dialog(ActivitySettings.this);
                d.setContentView(R.layout.dialog_list_menage);
                d.setTitle("Manage");



                d.setCancelable(true);
                ListView listView = d.findViewById(R.id.dialog_list_mange);
                ArrayAdapter<String> arrayAdapterBlocked = new ArrayAdapter<String>(ActivitySettings.this,
                        android.R.layout.simple_list_item_1, arrayListBlok);
                listView.setAdapter( arrayAdapterBlocked);
                arrayAdapterBlocked.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
dialogDeleteBlock(arrayListBlok2.get(i),d);

                    }
                });

                d.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void dialogDeleteBlock(final Blocked blocked, final Dialog dialog) {
        final Dialog d = new Dialog(ActivitySettings.this);
        d.setContentView(R.layout.dialog_i_blocked);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView buttonDelete = d.findViewById(R.id.delete_blocked);
        final TextView buttonProfile = d.findViewById(R.id.profile_blocked);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("RegisterInformation2")
                       .child(blocked.getUidBloked());
               databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(!snapshot.exists()){
                           buttonProfile. setText("משתמש מחק את החשבון שחסמת");
                           buttonProfile.setTextColor(getResources().getColor(R.color.colorWarng));
                           return;
                       }
                       Intent intent = new Intent(ActivitySettings.this, ProfileActivity.class);
                       intent.putExtra("profile_user_id", blocked.getUidBloked());
                       intent.putExtra("visit_user_id", uid);
                       startActivity(intent);

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("Blocked").child(uid).child(blocked.getPhone());
                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference("Blocked").child(blocked.getUidBloked()).child(registerInformation.getEmail());
                        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                d.dismiss();
                                dialog.dismiss();
                                dialogIBlock();
                                Toast.makeText(ActivitySettings.this, "חסימה נמחקה בהצלחה", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });




            }

        });

        d.show();

    }

    private void dialodDelete() {
        final Dialog d = new Dialog(ActivitySettings.this);
        d.setContentView(R.layout.dialog_delete);
        d.setTitle("Manage");

        d.setCancelable(true);
        Button buttonDeleteDialog = d.findViewById(R.id.button_delete_dialog);
        Button buttonNodelete = d.findViewById(R.id.button_exit_delete);
        buttonNodelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                return;
            }
        });
        buttonDeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ActivitySettings.this, RegisterLoginActivity.class);
                intent.putExtra("flagDeleteEnglish", registerInformation.getNameCollegeEnglish());
                intent.putExtra("flagDeleteHebrew", registerInformation.getNameCollegeHebrew());
                startActivity(intent);


            }

        });

        d.show();

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
            progressDialog.dismiss();
            Intent intent = new Intent(ActivitySettings.this, MainActivity3.class);
            startActivity(intent);
            finish();
            return;
        }

        StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                .child("profileImage/" + uid + ".jpg");
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
                                progressDialog.dismiss();
                                Intent intent = new Intent(ActivitySettings.this, MainActivity3.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_chat, menu);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.refresh);
        item.setTitle("לדף הראשי");

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;


        switch (item.getItemId()) {

            case R.id.mainIconMenu:
                intent2 = new Intent(ActivitySettings.this, MainActivity3.class);
                startActivity(intent2);
                return true;

            case R.id.settingsMenu:
                intent2 = new Intent(ActivitySettings.this, ActivitySettings.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                return true;

            case R.id.refresh:


                intent2 = new Intent(ActivitySettings.this, MainActivity3.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dialodBlockMenge() {
        final Dialog d = new Dialog(ActivitySettings.this);
        d.setContentView(R.layout.dialog_is_blocking);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView textView = d.findViewById(R.id.textView_date_blocked);
        Button buttonClose = d.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        textView.setText("נחסמת עד תאריך" + dataBlock);

        d.show();

    }

    private void setSpinner(final Spinner spinner) {
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
                nameCollegeEnglishSpinerr = arrayListNameSpinnerEnglish.get(i);
                nameCollegeHebrowSpinerr = arrayListNameSpinnerHebrow.get(i);
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
                int i = 1;
                int index = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayListNameSpinnerEnglish.add(child.getKey());
                    arrayListNameSpinnerHebrow.add(child.getValue(String.class));
                    if (registerInformation.getNameCollegeHebrew()
                            .equals(arrayListNameSpinnerHebrow.get(i)))
                        index = i;
                    i++;
                }
                nameClogeAdapter.notifyDataSetChanged();
                spinner.setSelection(index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void dialogLogOut() {
        final Dialog d2 = new Dialog(ActivitySettings.this);
        d2.setContentView(R.layout.dialog_feedback_link_dont_work);
        d2.setTitle("Manage");

        d2.setCancelable(true);
        TextView textView = d2.findViewById(R.id.textView__link_log_out);
        textView.setText("האם אתה בטוח שאתה רוצה לצאת מהאפליקציה?");
        Button buttonYes = d2.findViewById(R.id.buttonYes);
        buttonYes.setText("התנתק");
        Button buttonNo = d2.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d2.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ActivitySettings.this, RegisterLoginActivity.class);
                startActivity(intent);
            }
        });

        d2.show();

    }
}