package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.Hikers.RegisterLoginActivity;
import com.example.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity3 extends AppCompatActivity {
    private ViewPager viewPager;
    protected TabLayout tabLayout;
    private TabsAccessorAdapter tabsAccessorAdapter;
    protected static String search = "";
    private boolean flagBloked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        blocked();
//if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//    NotificationChannel notificationChannel= new NotificationChannel
//            ("new message","new message", NotificationManager.IMPORTANCE_DEFAULT);
//    NotificationManager notificationManager=getSystemService(NotificationManager.class);
//    notificationManager.createNotificationChannel(notificationChannel);
//}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity3.this,"new message");
//        builder.setContentTitle("my tytle");
//        builder.setContentText("מתי יוצא?");
//        builder.setSmallIcon(R.drawable.profile_activity);
//        builder.setAutoCancel(true);
//
//        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(MainActivity3.this);
//        managerCompat.notify(1, builder.build());
//       String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newPlainText("1", uid);
//        clipboard.setPrimaryClip(clip);
//        Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
//        String num="+972523677061";
//        String url="https://api.whatsapp.com/send?phone="+num + "&text=" + "Your text here";
//        intentWhatsapp.setData(Uri.parse(url));
//       // intentWhatsapp.setPackage("com.whatsapp");
//        startActivity(intentWhatsapp);


    }

    private void blocked() {
        final String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Blocked")
                .child(phone);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    build();
                    return;
                }

                Blocked blocked = snapshot.getValue(Blocked.class);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(calendar.getTime());
                if (dateSearch(date,blocked.getDate())) {
                    dialodBlockMenge(blocked.getDate());
                   flagBloked=true;
                    return;
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BlockedHistory")
                            .child(phone).push();
                    reference.setValue(blocked);
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Blocked")
                            .child(phone);
                    databaseReference2.removeValue();
                    build();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void build() {
        if (getIntent().hasExtra("flag_serch")) {
            search = getIntent().getExtras().getString("flag_serch");
        }
        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        tabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccessorAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);
        tabLayout.setupWithViewPager(viewPager);
        // viewPager.setCurrentItem(1);
        if (getIntent().hasExtra("flagPage"))
            viewPager.setCurrentItem(getIntent().getExtras().getInt("flagPage"));
        if (getIntent().hasExtra("flag_serch")) {
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_chat, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {

            case R.id.mainIconMenu:
                if (!flagBloked) {
                    intent2 = new Intent(MainActivity3.this, MainActivity3.class);
                    startActivity(intent2);
                }
                return true;

            case R.id.settingsMenu:
                if (!flagBloked) {

                    intent2 = new Intent(MainActivity3.this, ActivitySettings.class);
                    intent2.putExtra("flag", false);
                    startActivity(intent2);
                }
                return true;
            case R.id.feedbackMenu:
                if (!flagBloked) {
                    intent2 = new Intent(MainActivity3.this, ActivityFeedbackChat.class);
                    intent2.putExtra("flag", false);
                    startActivity(intent2);
                }
                return true;

            case R.id.refresh:
                if (!flagBloked) {

                    try {

                        if (FirebaseAuth.getInstance().getCurrentUser().
                                getPhoneNumber().equals("+972544540185")) {
                            intent2 = new Intent(MainActivity3.this, Management.class);
                            startActivity(intent2);
                            return true;
                        }
                    } catch (RuntimeException e) {
                    }
                    intent2 = new Intent(MainActivity3.this, MainActivity3.class);
                    startActivity(intent2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean dateSearch(String o1_date, String o2_date) {
//        Toast.makeText(MainActivity1.this, "" + dateStartUser + " " + dateEndUser + ";" + dateStart + " " + dateEnd, Toast.LENGTH_LONG).show();
//        final Button buttonData = findViewById(R.id.buttonDate2);
//        buttonData.setTextSize(8);

        int yearStartUser = Integer.valueOf(o1_date.substring(6));
        int monthStartUser = Integer.valueOf(o1_date.substring(3, 5));
        int dayStartUser = Integer.valueOf(o1_date.substring(0, 2));

        int yearEnd = Integer.valueOf(o2_date.substring(6));
        int monthEnd = Integer.valueOf(o2_date.substring(3, 5));
        int dayEnd = Integer.valueOf(o2_date.substring(0, 2));

        if (yearEnd > yearStartUser)
            return true;
        if (yearEnd == yearStartUser && monthEnd > monthStartUser)
            return true;
        if (yearEnd == yearStartUser && monthEnd == monthStartUser && dayEnd > dayStartUser)
            return true;


        return false;
    }
    private void dialodBlockMenge(String date) {
        final Dialog d = new Dialog(MainActivity3.this);
        d.setContentView(R.layout.dialog_is_blocking);
        d.setTitle("Manage");

        d.setCancelable(false);
        TextView textView=d.findViewById(R.id.textView_date_blocked);
        Button buttonClose = d.findViewById(R.id.button_close_window);
        buttonClose.setVisibility(View.GONE);
        textView.setText("נחסמת עד תאריך"+date);

        d.show();

    }
}