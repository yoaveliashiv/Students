package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {
    private Uri uriImage = null;
    private ImageView imageView;
    private static final int GallaryPick = 1;
    private RegisterInformation2 registerInformationSend;
    private RegisterInformation2 registerInformationRecive;
    private DatabaseReference databaseReferenceOn;
    private ArrayList<Message> arrayListMessage;
    private MessageAdapter arrayAdapter;
    private EditText editText;
    private ListView listView;
    private String uidSend;
    private String uidRecive;
    private CircleImageView circleImageView;
    private Boolean flagDataChange=true;
    private int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        circleImageView = findViewById(R.id.profile_image);
        getSupportActionBar().hide();
        uidSend = getIntent().getExtras().getString("send_message_user_id");
        uidRecive = getIntent().getExtras().getString("to_message_user_id");


        databaseReferenceOn = FirebaseDatabase.getInstance().getReference("Contacts").child(uidSend).child(uidRecive);
        ImageButton imageButtonPic = findViewById(R.id.imageButton_send_pick);
        ImageButton imageButtonText = findViewById(R.id.imageButtonSendMassege);
        editText = findViewById(R.id.editTextMssege);

        getUserReciveInfo();
        getUserSendInfo();
        imageButtonPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        imageButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sms = editText.getText().toString();
                if (sms.isEmpty()) {
                    editText.setError("אנא הכנס הודעה");
                    editText.requestFocus();
                    return;
                }

                saveDatabase(sms);


                editText.setText("");
            }
        });
        listView = findViewById(R.id.list_view_message);

        // String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        arrayListMessage = new ArrayList<>();

        arrayAdapter = new MessageAdapter(ChatActivity.this, 0, 0, arrayListMessage, uidSend);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialodSendPrivateMassege(i);
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                intent.putExtra("profile_user_id", uidRecive);
                intent.putExtra("visit_user_id", uidSend);
                startActivity(intent);
            }
        });
        add();
    }

    private void add() {
        databaseReferenceOn.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    k++;
                    editText.setText("l"+k);
                    displayMessage(snapshot);
                }
            }

            @Override

            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //                if (snapshot.exists()) {
//                    displayMessage(snapshot);
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void dialodSendPrivateMassege(final int i) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_massge);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView textViewSendMassge = d.findViewById(R.id.textView_send_massage);
        TextView textViewOpenProfile = d.findViewById(R.id.textView_propile_open);
        TextView textViewSendWhatappsMassge = d.findViewById(R.id.textView_send_whatapps);
        TextView textViewCopyPhone = d.findViewById(R.id.textView_copy_phone);
        TextView textViewCopyMessage = d.findViewById(R.id.textView_copy_massage);
        TextView textViewFeed = d.findViewById(R.id.textView_feed);

        textViewOpenProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                intent.putExtra("profile_user_id", uidRecive);
                intent.putExtra("visit_user_id", uidSend);
                startActivity(intent);

            }
        });
        textViewSendMassge.setVisibility(View.GONE);
        textViewSendWhatappsMassge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String uidVisit=arrayListMessage.get(i).getUid();
//                if (uidSend.equals(uidVisit)) {
//                    Toast.makeText(ChatActivity.this, "לא ניתן לשלוח הודעה לעצמך", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String num = arrayListMessage.get(i).getPhone();
                String url = "https://api.whatsapp.com/send?phone=" + num;
                intentWhatsapp.setData(Uri.parse(url));
                intentWhatsapp.setPackage("com.whatsapp");
                startActivity(intentWhatsapp);
            }
        });
        textViewCopyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListMessage.get(i).getPhone());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ChatActivity.this, "המספר הועתק", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewCopyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListMessage.get(i).getMessage());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ChatActivity.this, "ההודעה הועתקה", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewFeed.setVisibility(View.GONE);
        d.show();

    }


    private void getUserReciveInfo() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uidRecive).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerInformationRecive = new RegisterInformation2();
                registerInformationRecive = snapshot.getValue(RegisterInformation2.class);


                TextView textViewName = findViewById(R.id.textView_name_chat);
                String name = registerInformationRecive.getName();
                if (name.isEmpty())
                    name = registerInformationRecive.getEmail();
                textViewName.setText(name);
                if (!registerInformationRecive.getImageUrl().isEmpty()) {
                    Glide.with(ChatActivity.this)
                            .load(registerInformationRecive.getImageUrl())
                            .into(circleImageView);
                }
                // final MenuItem menuItem = menu2.findItem(R.id.mainIconMenu2);


//                Glide.with(ChatActivity.this).asDrawable()
//                        .load(registerInformationRecive.getImageUrl()).into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        menuItem.setIcon(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });

                //  Drawable myDrawable = imageView.getDrawable();

                //  menuItem.setIcon(R.drawable.profile_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveDatabase(String sms) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());

        final Message message = new Message();
        message.setDate(date);
        message.setMessage(sms);
        if (!registerInformationSend.getName().isEmpty())
            message.setName(registerInformationSend.getName());
        else
            message.setName(registerInformationSend.getEmail());

        message.setPhone(registerInformationSend.getEmail());
        message.setUid(uidSend);
        message.setDeviceToken(registerInformationRecive.getDeviceToken());
        message.setTime(time);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Contacts")
                .child(uidSend).child(uidRecive);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num = 0;
                if (snapshot.exists()) num = (int) snapshot.getChildrenCount();
                message.setId(num);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Contacts")
                        .child(uidSend).child(uidRecive).push();
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Contacts")
                        .child(uidRecive).child(uidSend).push();

                databaseReference1.setValue(message);
                databaseReference2.setValue(message);
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Notifications")
                        .push();

                databaseReference3.setValue(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getUserSendInfo() {

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference1.child(uidSend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerInformationSend = new RegisterInformation2();
                registerInformationSend = snapshot.getValue(RegisterInformation2.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void displayMessage(DataSnapshot snapshot) {

        Message message;

        message = snapshot.getValue(Message.class);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("NotificationsIdSeeLast").child(uidSend).child(uidRecive);//set Notifications
        databaseReference3.setValue(message.getId());
        arrayListMessage.add(message);

        arrayAdapter.notifyDataSetChanged();
// Force scroll to the top of the scroll view.
// Because, when the list view gets loaded it focuses the list view
// automatically at the bottom of this page.
        listView.setSelection(listView.getAdapter().getCount() - 1);


    }

    //    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.manu_chat, menu);
//        menu2 = menu;
//        getUserReciveInfo();
////            if(!registerInformationRecive.getImageUrl().isEmpty())
////        Glide.with(this).asBitmap().load(registerInformationRecive.getImageUrl()).into(new CustomTarget<Bitmap>() {
////            @Override
////            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
////            }
////
////            @Override
////            public void onLoadCleared(@Nullable Drawable placeholder) {
////
////            }
////        });
//
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent intent;
//        switch (item.getItemId()) {
//            case R.id.mainIconMenu:
//                intent = new Intent(ChatActivity.this, ProfileActivity.class);
//                intent.putExtra("profile_user_id", uidSend);
//                intent.putExtra("visit_user_id", uidRecive);
//                startActivity(intent);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    private void saveNotifications() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NotificationsPrivate");
        databaseReference.child(uidRecive).child(uidSend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int num = snapshot.getValue(Integer.class);
                    num++;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NotificationsPrivate");
                    databaseReference.child(uidRecive).child(uidSend).setValue(num);
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NotificationsPrivate");
                    databaseReference.child(uidRecive).child(uidSend).setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openImage() {


        try {
            if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
            dialogSendImage();
        }
    }

    private void dialogSendImage() {
        final Dialog d = new Dialog(ChatActivity.this);
        d.setContentView(R.layout.dialog_send_imag);
        d.setTitle("Manage");

        d.setCancelable(true);
        Button buttonSend = d.findViewById(R.id.button_send_image);
        Button buttonExsit = d.findViewById(R.id.button_exsit);
        ImageView imageButton = d.findViewById(R.id.imageButton_change_image);

        Glide.with(ChatActivity.this)
                .load(uriImage)
                .into(imageButton);
        Boolean flag=true;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
                d.cancel();
                return;
            }
        });
        buttonExsit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                        .child("sendMessageImage/" + registerInformationSend.getEmail() + "/" +
                                registerInformationRecive.getEmail() + "/image" + registerInformationSend.getIdImageSend() + ".jpg");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2").child("idImageSend");
                databaseReference.setValue(registerInformationSend.getIdImageSend() + 1);
                riversRef.putFile(uriImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {


                                        saveDatabase(uri.toString(), "");

                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ChatActivity.this, "תמונה לא נשמרה", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        d.show();

    }

    private void saveDatabase(String uri,String a) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());

        final Message message = new Message();
        message.setDate(date);
        message.setImageMessage(uri);
        if (!registerInformationSend.getName().isEmpty())
            message.setName(registerInformationSend.getName());
        else
            message.setName(registerInformationSend.getEmail());

        message.setPhone(registerInformationSend.getEmail());
        message.setUid(uidSend);
        message.setDeviceToken(registerInformationRecive.getDeviceToken());
        message.setTime(time);
        final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Contacts")
                .child(uidSend).child(uidRecive);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int num = 0;
                    if (snapshot.exists()) num = (int) snapshot.getChildrenCount();
                    message.setId(num);
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Contacts")
                            .child(uidSend).child(uidRecive).push();
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Contacts")
                            .child(uidRecive).child(uidSend).push();

                    databaseReference1.setValue(message);
                    databaseReference2.setValue(message);
                    DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Notifications")
                            .push();

                    databaseReference4.setValue(message);

                    databaseReference3.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
