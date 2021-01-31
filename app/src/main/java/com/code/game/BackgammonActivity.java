package com.code.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.code.R;

public class BackgammonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backgammon);
    }
}