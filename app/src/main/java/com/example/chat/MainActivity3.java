package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.Hikers.MainActivityPageUser2;
import com.example.Hikers.MainActivityRegister2;
import com.example.R;
import com.example.students.MainActivityRegisterTutor;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity3 extends AppCompatActivity {
private ViewPager viewPager;
protected TabLayout tabLayout;
private TabsAccessorAdapter tabsAccessorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        viewPager=(ViewPager)findViewById(R.id.viewPagerMain);
        tabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccessorAdapter);
        tabLayout=(TabLayout)findViewById(R.id.tabLayoutMain);
        tabLayout.setupWithViewPager(viewPager);
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
                intent2 = new Intent(MainActivity3.this, MainActivity3.class);
                startActivity(intent2);
                return true;

            case R.id.settingsMenu:
                    intent2 = new Intent(MainActivity3.this, ActivitySettings.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                return true;
            case R.id.feedbackMenu:
                intent2 = new Intent(MainActivity3.this, ActivityFeedbackChat.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                intent2 = new Intent(MainActivity3.this, MainActivityRegister2.class);
                startActivity(intent2);
                return true;
            case R.id.refresh:
                try {

                    if (FirebaseAuth.getInstance().getCurrentUser().
                            getPhoneNumber().equals("+972544540185")){
                        intent2 = new Intent(MainActivity3.this, Management.class);
                        startActivity(intent2);
                        return true;
                    }
                }catch (RuntimeException e){}
                intent2 = new Intent(MainActivity3.this, MainActivity3.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}