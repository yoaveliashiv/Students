package com.code.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.R;

import java.util.ArrayList;
import java.util.Stack;

public class BackgammonActivity extends AppCompatActivity {
    private String color="";
    private String uid="";
    private TextView textViewColor;
    private ArrayList<ArrayList<ImageView>> arrayListStackImage;
    private ArrayList<TextView> arrayListTextNum;
    private ImageView imageViewDice1;
    private ImageView imageViewDice2;
private TextView textViewDice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_backgammon);
        getSupportActionBar().hide();
        if (getIntent().hasExtra("color")) {
            color = getIntent().getExtras().getString("color");
            uid = getIntent().getExtras().getString("uid");
        }
       setId();
      textViewColor.setText("הצבע שלך: " + color);
      if(color.equals("white"))textViewColor.setTextColor(getResources().getColor(R.color.colorWhite));
     BackgammonBoard board=new BackgammonBoard(arrayListTextNum,arrayListStackImage,
             imageViewDice1,imageViewDice2,color,textViewDice);
      board.build();
     // board.moveDice(imageViewDice1);
        super.onCreate(savedInstanceState);
    }

    private void setId() {
      imageViewDice1= findViewById(R.id.imageView_dice1);
        imageViewDice2= findViewById(R.id.imageView_dice2);
        textViewColor = findViewById(R.id.textView_color9);
        textViewDice=findViewById(R.id.textView_dice);
        arrayListStackImage = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            ArrayList<ImageView> stack = new ArrayList<>();

            for (int j = 1; j < 8; j++) {
                String imageId = "imageVie" + i + "" + j;
                int resIDimage = getResources().getIdentifier(imageId, "id", getPackageName());
                ImageView imageView = findViewById(resIDimage);
                //imageView.setVisibility(View.GONE);
                stack.add(imageView);
            }
            arrayListStackImage.add(stack);
        }
        arrayListTextNum = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            String textViewId = "textVie" + i;
            int resIDtext = getResources().getIdentifier(textViewId, "id", getPackageName());
            TextView textView = findViewById(resIDtext);
            arrayListTextNum.add(textView);
        }
    }
    }
