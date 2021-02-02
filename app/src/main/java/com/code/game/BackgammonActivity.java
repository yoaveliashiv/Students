package com.code.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private int flagListener=0;
private   BackgammonBoard board;
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
     board=new BackgammonBoard(arrayListTextNum,arrayListStackImage,
             imageViewDice1,imageViewDice2,color,textViewDice,uid);
     board.build();
      board.moveDice(imageViewDice1,BackgammonActivity.this);
      setOnListener();
        super.onCreate(savedInstanceState);
    }

    private void setOnListener() {
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flagListener>0&&snapshot.exists()){
                    MovesGame movesGame=snapshot.getValue(MovesGame.class);
                    moves(movesGame);
                }
               else flagListener++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void moves(MovesGame movesGame) {
        switch(movesGame.getType())
        {
            case "dice1":
                System.out.println("one");
                break;
            case "two":
                System.out.println("two");
                break;
            case "three":
                System.out.println("three");
                break;
            default:
                System.out.println("no match");
        }
    }

    private void setId() {
      imageViewDice1= findViewById(R.id.imageView_dice1);
        imageViewDice2= findViewById(R.id.imageView_dice2);
        imageViewDice1.setImageResource(R.drawable.dice6);
        imageViewDice2.setImageResource(R.drawable.dice6);
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
