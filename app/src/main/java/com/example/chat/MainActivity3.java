package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity3 extends AppCompatActivity {
private ViewPager viewPager;
protected TabLayout tabLayout;
private TabsAccessorAdapter tabsAccessorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        viewPager=(ViewPager)findViewById(R.id.viewPagerMain);
        tabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccessorAdapter);
        tabLayout=(TabLayout)findViewById(R.id.tabLayoutMain);
        tabLayout.setupWithViewPager(viewPager);
    }
}