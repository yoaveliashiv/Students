package com.code.game;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.R;
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
    public BackgammonBoard(ArrayList<TextView> arrayListTextNum, ArrayList<ArrayList<ImageView>> arrayListStackImage,
                           ImageView imageViewDice1, ImageView imageViewDice2,
                           String color, TextView textViewDice) {
        this.arrayListTextNum = arrayListTextNum;
        this.arrayListStackImage = arrayListStackImage;
        this.imageViewDice1 = imageViewDice1;
        this.imageViewDice2 = imageViewDice2;
        this.color = color;
        this.textViewDice = textViewDice;
    }

    public BackgammonBoard() {

    }
    public void moveDice(ImageView imageViewDice){

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (!flagDiceTrow) {
                        int numDice = Dice.throwDice();
                        setImage(numDice, imageViewDice);
                        handler.postDelayed(this, 1500);
                    }
                }
            };
            handler.postDelayed(runnable, 1500);

    }

    private void setImage(int numDice, ImageView imageViewDice) {
        switch(numDice)
        {
            case 1:
              imageViewDice.setImageResource(R.drawable.dice1);
            case 2:
                imageViewDice.setImageResource(R.drawable.dice2);
            case 3:
                imageViewDice.setImageResource(R.drawable.dice3);
            case 4:
                imageViewDice.setImageResource(R.drawable.dice4);
            case 5:
                imageViewDice.setImageResource(R.drawable.dice5);
            case 6:
                imageViewDice.setImageResource(R.drawable.dice6);
            default:

        }
    }

    public void build() {
        imageViewDice2.setVisibility(View.INVISIBLE);
        imageViewDice1.setVisibility(View.VISIBLE);
        if (color.equals("white"))
            textViewDice.setText("תורך: זרוק מי מתחיל");
        else
            textViewDice.setText("תור השני: שיזרוק מי מתחיל");
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
