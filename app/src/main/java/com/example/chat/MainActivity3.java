package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

       String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        viewPager=(ViewPager)findViewById(R.id.viewPagerMain);
        tabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccessorAdapter);
        tabLayout=(TabLayout)findViewById(R.id.tabLayoutMain);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // if (flagConnected) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_chat, menu);
            return true;
     //   }
     //   return false;
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
                    startActivity(intent2);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                intent2 = new Intent(MainActivity3.this, MainActivityRegister2.class);
                startActivity(intent2);
                return true;
            case R.id.refresh:
                intent2 = new Intent(MainActivity3.this, MainActivity3.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}