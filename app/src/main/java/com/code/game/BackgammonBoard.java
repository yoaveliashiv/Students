package com.code.game;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.code.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@IgnoreExtraProperties

public class BackgammonBoard {
    private ArrayList<Integer> boardNum = new ArrayList<>();
    private ArrayList<String> boardColor = new ArrayList<>();
    private ArrayList<TextView> arrayListTextNum = new ArrayList<>();


    private ArrayList<ArrayList<ImageView>> arrayListStackImage;
    private ImageView imageViewDice1;
    private ImageView imageViewDice2;
    private String color;
    private TextView textViewDice;
private boolean flagDiceTrow=false;
private String uid="";
    public BackgammonBoard(ArrayList<TextView> arrayListTextNum, ArrayList<ArrayList<ImageView>> arrayListStackImage,
                           ImageView imageViewDice1, ImageView imageViewDice2,
                           String color, TextView textViewDice,String uid) {
        this.arrayListTextNum = arrayListTextNum;
        this.arrayListStackImage = arrayListStackImage;
        this.imageViewDice1 = imageViewDice1;
        this.imageViewDice2 = imageViewDice2;
        this.color = color;
        this.textViewDice = textViewDice;
        this.uid=uid;
    }

    public BackgammonBoard() {

    }

    public void DiceTrowStart(){

    }
    public void moveDice(ImageView imageViewDice, Activity activity) {
        new Thread() {
            public void run() {
                while (!flagDiceTrow){
                    try {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                int numDice = Dice.throwDice();
                                setImage(numDice, imageViewDice);
                            }
                        });
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
//            Handler handler = new Handler();
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < 20; i++) {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //    for (int i = 0; i < 20; i++) {
//                                int numDice = Dice.throwDice();
//                                textViewDice.setText("" + numDice);
//                                setImage(numDice, imageViewDice);
//
//                                try {
//                                    Thread.sleep(1500);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                // }
//                            }
//                        });
//                    }
//
//
//                }
//            });



        }

    private void setImage(int numDice, ImageView imageViewDice) {
       if(numDice==1)
              imageViewDice.setImageResource(R.drawable.dice1);
       else if(numDice==2)
                imageViewDice.setImageResource(R.drawable.dice2);
       else if(numDice==3)
                imageViewDice.setImageResource(R.drawable.dice3);
       else if(numDice==4)
                imageViewDice.setImageResource(R.drawable.dice4);
       else if(numDice==5)
                imageViewDice.setImageResource(R.drawable.dice5);
       else if(numDice==6)
                imageViewDice.setImageResource(R.drawable.dice6);

    }

    public void build() {
//        imageViewDice2.setVisibility(View.INVISIBLE);
//        imageViewDice1.setVisibility(View.VISIBLE);
        if (color.equals("white")) {
            textViewDice.setText("תורך: זרוק מי מתחיל");
            imageViewDice1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageViewDice1.setClickable(false);
                    int numDice=roolOneDice();
                    MovesGame movesGame=new MovesGame();
                    movesGame.setType("start1");
                    movesGame.setDice1(numDice);
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(uid);
                    databaseReference3.setValue(movesGame).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            imageViewDice1.setClickable(true);
                        }
                    });


                }
            });
        }
        else {
            textViewDice.setText("תור השני: שיזרוק מי מתחיל");
        }
        for (ArrayList<ImageView> array : arrayListStackImage) {
            for (ImageView imageView : array) {
             imageView.setVisibility(View.INVISIBLE);
            }
        }
        for (TextView textView:arrayListTextNum) {
            textView.setVisibility(View.INVISIBLE);
        }
        arrayListStackImage.get(0).get(0).setImageResource(R.drawable.black);
        arrayListStackImage.get(0).get(1).setImageResource(R.drawable.black);
        arrayListStackImage.get(0).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(0).get(1).setVisibility(View.VISIBLE);

        arrayListStackImage.get(5).get(0).setImageResource(R.drawable.white);
        arrayListStackImage.get(5).get(1).setImageResource(R.drawable.white);
        arrayListStackImage.get(5).get(2).setImageResource(R.drawable.white);
        arrayListStackImage.get(5).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(5).get(1).setVisibility(View.VISIBLE);
        arrayListStackImage.get(5).get(2).setVisibility(View.VISIBLE);

        arrayListStackImage.get(7).get(0).setImageResource(R.drawable.white);
        arrayListStackImage.get(7).get(1).setImageResource(R.drawable.white);
        arrayListStackImage.get(7).get(2).setImageResource(R.drawable.white);
        arrayListStackImage.get(7).get(3).setImageResource(R.drawable.white);
        arrayListStackImage.get(7).get(4).setImageResource(R.drawable.white);
        arrayListStackImage.get(7).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(7).get(1).setVisibility(View.VISIBLE);
        arrayListStackImage.get(7).get(2).setVisibility(View.VISIBLE);
        arrayListStackImage.get(7).get(3).setVisibility(View.VISIBLE);
        arrayListStackImage.get(7).get(4).setVisibility(View.VISIBLE);

        arrayListStackImage.get(11).get(0).setImageResource(R.drawable.black);
        arrayListStackImage.get(11).get(1).setImageResource(R.drawable.black);
        arrayListStackImage.get(11).get(2).setImageResource(R.drawable.black);
        arrayListStackImage.get(11).get(3).setImageResource(R.drawable.black);
        arrayListStackImage.get(11).get(4).setImageResource(R.drawable.black);
        arrayListStackImage.get(11).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(11).get(1).setVisibility(View.VISIBLE);
        arrayListStackImage.get(11).get(2).setVisibility(View.VISIBLE);
        arrayListStackImage.get(11).get(3).setVisibility(View.VISIBLE);
        arrayListStackImage.get(11).get(4).setVisibility(View.VISIBLE);


        arrayListStackImage.get(23).get(0).setImageResource(R.drawable.white);
        arrayListStackImage.get(23).get(1).setImageResource(R.drawable.white);
        arrayListStackImage.get(23).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(23).get(1).setVisibility(View.VISIBLE);

        arrayListStackImage.get(18).get(0).setImageResource(R.drawable.black);
        arrayListStackImage.get(18).get(1).setImageResource(R.drawable.black);
        arrayListStackImage.get(18).get(2).setImageResource(R.drawable.black);
        arrayListStackImage.get(18).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(18).get(1).setVisibility(View.VISIBLE);
        arrayListStackImage.get(18).get(2).setVisibility(View.VISIBLE);

        arrayListStackImage.get(16).get(0).setImageResource(R.drawable.black);
        arrayListStackImage.get(16).get(1).setImageResource(R.drawable.black);
        arrayListStackImage.get(16).get(2).setImageResource(R.drawable.black);
        arrayListStackImage.get(16).get(3).setImageResource(R.drawable.black);
        arrayListStackImage.get(16).get(4).setImageResource(R.drawable.black);
        arrayListStackImage.get(16).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(16).get(1).setVisibility(View.VISIBLE);
        arrayListStackImage.get(16).get(2).setVisibility(View.VISIBLE);
        arrayListStackImage.get(16).get(3).setVisibility(View.VISIBLE);
        arrayListStackImage.get(16).get(4).setVisibility(View.VISIBLE);

        arrayListStackImage.get(12).get(0).setImageResource(R.drawable.white);
        arrayListStackImage.get(12).get(1).setImageResource(R.drawable.white);
        arrayListStackImage.get(12).get(2).setImageResource(R.drawable.white);
        arrayListStackImage.get(12).get(3).setImageResource(R.drawable.white);
        arrayListStackImage.get(12).get(4).setImageResource(R.drawable.white);
        arrayListStackImage.get(12).get(0).setVisibility(View.VISIBLE);
        arrayListStackImage.get(12).get(1).setVisibility(View.VISIBLE);
        arrayListStackImage.get(12).get(2).setVisibility(View.VISIBLE);
        arrayListStackImage.get(12).get(3).setVisibility(View.VISIBLE);
        arrayListStackImage.get(12).get(4).setVisibility(View.VISIBLE);
//        for (int i = 0; i < 24; i++) {
//            boardNum.add(0);
//            boardColor.add("no");
//        }
    }

    private int roolOneDice() {
        int numDice = Dice.throwDice();
        setImage(numDice, imageViewDice1);
        return  numDice;
    }

    //public void



    public ArrayList<Integer> getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(ArrayList<Integer> boardNum) {
        this.boardNum = boardNum;
    }

    public ArrayList<String> getBoardColor() {
        return boardColor;
    }

    public void setBoardColor(ArrayList<String> boardColor) {
        this.boardColor = boardColor;
    }

    public ArrayList<ArrayList<ImageView>> getArrayListStackImage() {
        return arrayListStackImage;
    }

    public void setArrayListStackImage(ArrayList<ArrayList<ImageView>> arrayListStackImage) {
        this.arrayListStackImage = arrayListStackImage;
    }

    public ArrayList<TextView> getArrayListTextNum() {
        return arrayListTextNum;
    }

    public void setArrayListTextNum(ArrayList<TextView> arrayListTextNum) {
        this.arrayListTextNum = arrayListTextNum;
    }

    public ImageView getImageViewDice1() {
        return imageViewDice1;
    }

    public void setImageViewDice1(ImageView imageViewDice1) {
        this.imageViewDice1 = imageViewDice1;
    }

    public ImageView getImageViewDice2() {
        return imageViewDice2;
    }

    public void setImageViewDice2(ImageView imageViewDice2) {
        this.imageViewDice2 = imageViewDice2;
    }
}
