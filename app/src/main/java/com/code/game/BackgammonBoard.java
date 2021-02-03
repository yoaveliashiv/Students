package com.code.game;

import android.app.Activity;
import android.graphics.Color;
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
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

@IgnoreExtraProperties

public class BackgammonBoard {
    private ArrayList<Integer> boardNum = new ArrayList<>();
    private ArrayList<String> boardColor = new ArrayList<>();
    private ArrayList<TextView> arrayListTextNum = new ArrayList<>();

    private ImageView imageViewBlackOut;
    private ImageView imageViewWhiteOut;
    private TextView textViewBlackOut;
    private TextView textViewWhiteOut;
    private ArrayList<ArrayList<ImageView>> arrayListStackImage;
    private ImageView imageViewDice1;
    private ImageView imageViewDice2;
    private String yourColor;
    private TextView textViewDice;
    private int flagDiceTrow = 0;
    private String uid = "";
    private Activity activity;
    private String rivalColor;
    private Stones stones;
    private int indexStone;
    private HashMap<ImageView, Integer> hashMapIndex;

    private int dice1;
    private int dice2;


    public BackgammonBoard(ArrayList<TextView> arrayListTextNum, ArrayList<ArrayList<ImageView>> arrayListStackImage,
                           ImageView imageViewDice1, ImageView imageViewDice2,
                           String color, TextView textViewDice, String uid, Activity activity, HashMap<ImageView, Integer> hashMapIndex,
                           ImageView imageViewBlackOut,
                           ImageView imageViewWhiteOut,
                           TextView textViewBlackOut,
                           TextView textViewWhiteOut) {
        this.arrayListTextNum = arrayListTextNum;
        this.arrayListStackImage = arrayListStackImage;
        this.imageViewDice1 = imageViewDice1;
        this.imageViewDice2 = imageViewDice2;
        this.yourColor = color;
        this.textViewDice = textViewDice;
        this.uid = uid;
        this.activity = activity;
        if (color.equals("black"))
            rivalColor = "white";
        else
            rivalColor = "black";

        this.hashMapIndex = hashMapIndex;
        this.imageViewBlackOut = imageViewBlackOut;
        this.imageViewWhiteOut = imageViewWhiteOut;
        this.textViewBlackOut = textViewBlackOut;
        this.textViewWhiteOut = textViewWhiteOut;


    }

    public BackgammonBoard() {

    }

    public void trowTowDice() {
        imageViewDice1.setClickable(false);
        imageViewDice2.setClickable(false);

        flagDiceTrow++;
        dice1 = roolOneDice(imageViewDice1);
        dice2 = roolOneDice(imageViewDice2);
        MovesGame movesGame = new MovesGame();
        movesGame.setDice1(dice1);
        movesGame.setDice2(dice2);
        movesGame.setInfoTo(rivalColor);
        movesGame.setType("dices");

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference3.setValue(movesGame);

        chesSton();


    }

    private void chesSton() {
        for (int i = 0; i < 24; i++) {
            if (stones.getListStonesColor().get(i) > 0) {//only i have ston
                for (int j = 0; j < stones.getListStonesColor().get(i); j++) {//only soton visibaliti

                    arrayListStackImage.get(i).get(j).setClickable(true);

                    arrayListStackImage.get(i).get(j).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clearColorFilter();
                            ImageView imageView = ((ImageView) view);
                           // setCancalClick();
                            indexStone = hashMapIndex.get(imageView);
                            arrayListStackImage.get(indexStone).get(stones.getListStonesColor()
                                    .get(indexStone) - 1)
                                    .setColorFilter(Color.parseColor("#F53241"));//the top ston index

                            stonMoveTo();

                        }
                    });
                }
            }


        }
    }

    private void clearColorFilter() {
        for (int i = 0; i < 24; i++) {
            for (ImageView image : arrayListStackImage.get(i)) {
                image.clearColorFilter();
            }
        }
    }

    private void setCancalClick() {
        for (int i = 0; i < 24; i++) {
            for (ImageView image : arrayListStackImage.get(i)) {
                image.setClickable(false);
            }
        }
    }

    private void stonMoveTo() {
        textViewDice.setText("פה מוזר"+indexStone);

        for (int i = 0; i < 24; i++) {
            if (stones.getListStonesColorRivel().get(i) < 2 && i != indexStone) {//no go to same place and rivel 1+
                if ((yourColor.equals("white") && (indexStone - i == dice1 || indexStone - i == dice2))
                        || (yourColor.equals("black") && (indexStone + dice1 == i || indexStone + dice2 == i))) {//dices can

                    textViewDice.setText("פה כן");
                    int langth;//max ston visibulity
                    if (stones.getListStonesColor().get(i) > stones.getListStonesColorRivel().get(i))
                        langth = stones.getListStonesColor().get(i);
                    else
                        langth = stones.getListStonesColorRivel().get(i);
                    if (langth == 0)
                        langth = 1;
                    for (int j = 0; j < langth; j++) {
                        textViewDice.setText("פה???" + i + "kk" + j + "pp" + indexStone);
                        arrayListTextNum.get(i).setText("" + i);
                        arrayListStackImage.get(i).get(j).setImageResource(R.drawable.black);
                        arrayListStackImage.get(i).get(j).setVisibility(View.VISIBLE);

                        arrayListTextNum.get(i).setVisibility(View.VISIBLE);
                        arrayListStackImage.get(i).get(j)
                                .setColorFilter(Color.parseColor("#F53241"));
                        arrayListStackImage.get(i).get(j).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ImageView imageView = ((ImageView) view);
                                setCancalClick();

                                int index = hashMapIndex.get(imageView);
                                if (yourColor.equals("white")) {//delte dice move
                                    if (indexStone - index == dice1)
                                        dice1 = -100;
                                    else
                                        dice2 = -100;
                                } else {                                //black
                                    if (indexStone + index == dice1)
                                        dice1 = -100;
                                    else
                                        dice2 = -100;

                                }
//                            arrayListStackImage.get(index).get(stones.getListStonesColor()
//                                    .get(index) - 1)
//                                    .setColorFilter(Color.parseColor("#F53241"));
                                if (stones.getListStonesColor().get(index) < 8) {//move ston he moved

                                    ImageView imageViewUp1 =
                                            arrayListStackImage.get(indexStone).get(stones.getListStonesColor()
                                                    .get(indexStone) - 1);//ston move
                                    imageViewUp1.setVisibility(View.INVISIBLE);
                                } else {

                                    int num = stones.getListStonesColor().get(indexStone) - 1;
                                    arrayListTextNum.get(indexStone).setText("" + num);
                                    arrayListTextNum.get(indexStone).setVisibility(View.VISIBLE);

                                }


                                stones.getListStonesColor().add(indexStone, stones.getListStonesColor().get(indexStone) - 1);//move stone
                                stones.getListStonesColor().add(index, stones.getListStonesColor().get(index) + 1);//add ston
                                if (stones.getListStonesColorRivel().get(index) == 1) {//if i eat rivel
                                    stones.getListStonesColorRivel().add(24, stones.getListStonesColorRivel().get(24) + 1);//add ston out side
                                    stones.getListStonesColorRivel().add(index, 0);//move ston eated

                                    if (rivalColor.equals("white")) {
                                        textViewWhiteOut.setText("" + stones.getListStonesColorRivel().get(24));
                                        imageViewWhiteOut.setVisibility(View.VISIBLE);
                                    } else {
                                        textViewBlackOut.setText("" + stones.getListStonesColorRivel().get(24));
                                        imageViewWhiteOut.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (stones.getListStonesColor().get(index) < 7) {//show ston move
                                    ImageView imageViewUp2 =
                                            arrayListStackImage.get(index).get(stones.getListStonesColor()
                                                    .get(index) - 1);
                                    if (yourColor.equals("white")) {
                                        imageViewUp2.setImageResource(R.drawable.white);
                                        imageViewUp2.setVisibility(View.VISIBLE);
                                    } else {
                                        imageViewUp2.setImageResource(R.drawable.black);
                                        imageViewUp2.setVisibility(View.VISIBLE);
                                    }
                                } else
                                    arrayListTextNum.get(index).setText("" +
                                            stones.getListStonesColor().get(index));

                                if (stones.getListStonesColor().get(index) < 8) {
                                    arrayListTextNum.get(indexStone).setVisibility(View.INVISIBLE);
                                }
                                if (dice1 != 100 || dice2 != -100)
                                    chesSton();
                                else {
                                    sendDataEndTourn();
                                }

                            }

                        });
                        arrayListStackImage.get(i).get(j).setClickable(true);
                    }
                }
            }
        }
    }

    private void sendDataEndTourn() {
    }


    public void moveTowDice() {
        new Thread() {
            public void run() {
                final int num = flagDiceTrow;
                while (num == flagDiceTrow) {
                    try {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                int numDice1 = Dice.throwDice();
                                int numDice2 = Dice.throwDice();
                                setImage(numDice1, imageViewDice1);
                                setImage(numDice2, imageViewDice2);
                            }
                        });
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


    }

    public void moveDice(ImageView imageViewDice) {
        new Thread() {
            public void run() {
                final int num = flagDiceTrow;
                while (num == flagDiceTrow) {
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


    }

    public void setImage(int numDice, ImageView imageViewDice) {
        if (numDice == 1)
            imageViewDice.setImageResource(R.drawable.dice1);
        else if (numDice == 2)
            imageViewDice.setImageResource(R.drawable.dice2);
        else if (numDice == 3)
            imageViewDice.setImageResource(R.drawable.dice3);
        else if (numDice == 4)
            imageViewDice.setImageResource(R.drawable.dice4);
        else if (numDice == 5)
            imageViewDice.setImageResource(R.drawable.dice5);
        else if (numDice == 6)
            imageViewDice.setImageResource(R.drawable.dice6);

    }

    public void build() {
//        imageViewDice2.setVisibility(View.INVISIBLE);
//        imageViewDice1.setVisibility(View.VISIBLE);
        if (yourColor.equals("white")) {
            textViewDice.setText("תורך: זרוק מי מתחיל");
            imageViewDice1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageViewDice1.setClickable(false);
                    flagDiceTrow++;
                    int numDice = roolOneDice(imageViewDice1);
                    MovesGame movesGame = new MovesGame();
                    movesGame.setType("start1");
                    movesGame.setInfoTo("black");
                    movesGame.setDice1(numDice);
                    textViewDice.setText("תור השני: שיזרוק מי מתחיל");
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(uid);
                    databaseReference3.setValue(movesGame).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            imageViewDice1.setClickable(true);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            moveDice(imageViewDice2);
                        }
                    });


                }
            });
        } else {
            textViewDice.setText("תור השני: שיזרוק מי מתחיל");

        }
        for (ArrayList<ImageView> array : arrayListStackImage) {
            for (ImageView imageView : array) {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
        for (TextView textView : arrayListTextNum) {
            textView.setVisibility(View.INVISIBLE);
        }

        stones = new Stones();
        for (int i = 0; i < 25; i++) {

            switch (i) {
                case 0:
                    stones.getListStonesWhite().add(0);
                    stones.getListStonesBlack().add(2);
                    break;
                case 5:
                    stones.getListStonesBlack().add(0);
                    stones.getListStonesWhite().add(3);
                    break;
                case 7:
                    stones.getListStonesBlack().add(0);
                    stones.getListStonesWhite().add(5);
                    break;
                case 11:
                    stones.getListStonesWhite().add(0);
                    stones.getListStonesBlack().add(5);
                    break;
                case 23:
                    stones.getListStonesBlack().add(0);
                    stones.getListStonesWhite().add(2);
                    break;
                case 18:
                    stones.getListStonesWhite().add(0);
                    stones.getListStonesBlack().add(3);
                    break;
                case 16:
                    stones.getListStonesWhite().add(0);
                    stones.getListStonesBlack().add(5);
                    break;
                case 12:
                    stones.getListStonesBlack().add(0);
                    stones.getListStonesWhite().add(5);
                    break;

                default:
                    stones.getListStonesWhite().add(0);
                    stones.getListStonesBlack().add(0);
                    break;
            }


        }
        stones.setListStonesByColor(yourColor);
//        for (int i = 0; i <24 ;i++) {
//            int num=stones.getListStonesColorRivel().get(i);
//            if(stones.getListStonesColorRivel().get(i)<stones.getListStonesColor().get(i))
//                num=stones.getListStonesColor().get(i);
//            arrayListTextNum.get(i).setText(""+num);
//        }
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

    public void setClick2Dices() {
        imageViewDice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trowTowDice();
            }
        });
        imageViewDice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trowTowDice();
            }
        });
        imageViewDice1.setClickable(true);
        imageViewDice2.setClickable(true);
    }

    public int roolOneDice(ImageView imageViewDice) {
        int numDice = Dice.throwDice();
        setImage(numDice, imageViewDice);
        return numDice;
    }

    public void flagDiceTrowe() {
        flagDiceTrow++;
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
