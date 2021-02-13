package com.code.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

@IgnoreExtraProperties

public class BackgammonBoard {
    private TextView textViewTime;
    private int bigIndex = 0;
    private int countMove = 0;
    private Timer timer;

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
    protected  String rivalColor;
    private Stones stones;
    private ImageView imageViewBlackWin;
    private ImageView imageViewWhiteWin;
    private TextView textViewBlackWin;
    private TextView textViewWhiteWin;
    private boolean flagAllSotnsInEnd;
    // private int indexStone;

    private HashMap<ImageView, Integer> hashMapIndex;

    private int dice1;
    private int dice2;
    private int dice3 = -100;
    private int dice4 = -100;


    public BackgammonBoard(ArrayList<TextView> arrayListTextNum, ArrayList<ArrayList<ImageView>> arrayListStackImage,
                           ImageView imageViewDice1, ImageView imageViewDice2,
                           String color, TextView textViewDice, String uid, Activity activity, HashMap<ImageView, Integer> hashMapIndex,
                           ImageView imageViewWhiteOut,
                           ImageView imageViewBlackOut,
                           TextView textViewWhiteOut, TextView textViewBlackOut,
                           TextView textViewTime) {
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
        this.textViewTime = textViewTime;
        timer = new Timer(this.textViewTime, this.activity);

    }

    public BackgammonBoard() {

    }

    public void trowTowDice() {
        dice3 = -100;
        dice4 = -100;
        // textViewDice.setText(""+stones.getListStonesColor().get(17));
        imageViewDice1.setClickable(false);
        imageViewDice2.setClickable(false);

        flagDiceTrow++;
        dice1 = roolOneDice(imageViewDice1);
        dice2 = roolOneDice(imageViewDice2);
        if (dice1 == dice2) {
            dice3 = dice1;
            dice4 = dice1;
        }
        MovesGame movesGame = new MovesGame();
        movesGame.setDice1(dice1);
        movesGame.setDice2(dice2);
        movesGame.setInfoTo(rivalColor);
        movesGame.setType("dices");
        HashMap<String, List<Integer>> hashMapBord = new HashMap<>();
        hashMapBord.put(BackgammonActivity.uidMe, stones.getListStonesColor());
        hashMapBord.put(BackgammonActivity.uidRivel, stones.getListStonesColorRivel());

        HashMap<String, String> hashMapUid = new HashMap<>();
        hashMapUid.put(BackgammonActivity.uidMe, BackgammonActivity.uidRivel);
        hashMapUid.put(BackgammonActivity.uidRivel, BackgammonActivity.uidMe);
        movesGame.setHashMapUid(hashMapUid);
        HashMap<String, String> hashMapColor = new HashMap<>();
        hashMapColor.put(BackgammonActivity.uidMe, yourColor);
        hashMapColor.put(BackgammonActivity.uidRivel, rivalColor);
        movesGame.setUidPrimery(uid);
        movesGame.setHashMapColor(hashMapColor);
        movesGame.setHashMapBord(hashMapBord);
        movesGame.setTournUid(BackgammonActivity.uidMe);
        countMove++;
        movesGame.setCountMove(countMove);

        String uidOut;
        if (uid.equals(BackgammonActivity.uidMe))
            uidOut = BackgammonActivity.uidRivel;
        else
            uidOut = BackgammonActivity.uidMe;
        DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uidOut);
        databaseReference9.setValue(movesGame);

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference3.setValue(movesGame);


        chesSton();


    }

    public void chesSton() {
        textViewDice.setText("אנא הוזז אבן");
        flagAllSotnsInEnd = false;
        if (chakIfAllStonInAnd())
            flagAllSotnsInEnd = true;

        clearColorFilter();
        boolean flagDontHaveWatToDo = false;
        if (stones.getListStonesColor().get(24) > 0) {
            if (yourColor.equals("white")) {
                if (chakHaveWhatToDo(24)) {
                    flagDontHaveWatToDo = true;
                    imageViewWhiteOut.setClickable(true);


                    imageViewWhiteOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!conction(BackgammonActivity.contextGame)) {
                                textViewDice.setText("אין קליטה");
                                textViewDice.setError("אין קליטה");
                                textViewDice.requestFocus();
                                return;
                            }
                            textViewDice.clearFocus();
                            imageViewWhiteOut.setClickable(false);
                            imageViewWhiteOut.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);//the top ston index
                            textViewDice.setText("אנא הוזז אבן");

                            stonMoveTo(24);

                        }
                    });
                }
            } else {//black
                if (chakHaveWhatToDo(-1)) {
                    flagDontHaveWatToDo = true;


                    imageViewBlackOut.setClickable(true);
                    imageViewBlackOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!conction(BackgammonActivity.contextGame)) {
                                textViewDice.setText("אין קליטה");
                                textViewDice.setError("אין קליטה");
                                textViewDice.requestFocus();
                                return;
                            }
                            textViewDice.clearFocus();
                            imageViewBlackOut.setClickable(false);
                            imageViewBlackOut.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);//the top ston index
                            textViewDice.setText("אנא הוזז אבן");

                            stonMoveTo(-1);

                        }
                    });
                }

            }
        } else {
            for (int i = 0; i < 24; i++) {
                if (stones.getListStonesColor().get(i) > 0) {//only i have ston
                    if (chakHaveWhatToDo(i)) {
                        flagDontHaveWatToDo = true;

                        int length = stones.getListStonesColor().get(i);
                        if (length > 7) length = 7;
                        for (int j = 0; j < length; j++) {//only soton visibaliti

                            arrayListStackImage.get(i).get(j).setClickable(true);

                            arrayListStackImage.get(i).get(j).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(!conction(BackgammonActivity.contextGame)) {
                                        textViewDice.setText("אין קליטה");
                                        textViewDice.setError("אין קליטה");
                                        textViewDice.requestFocus();
                                        return;
                                    }
                                    textViewDice.clearFocus();
                                    ImageView imageView = ((ImageView) view);
                                    setCancalClick();
                                    int index = hashMapIndex.get(imageView);
                                    int length = stones.getListStonesColor().get(index) - 1;
                                    if (length > 6) length = 6;
                                    arrayListStackImage.get(index).get(length)
                                            .setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);//the top ston index
                                    textViewDice.setText("אנא הוזז אבן");
                                    stonMoveTo(hashMapIndex.get(imageView));

                                }
                            });
                        }
                    }
                }
            }
        }
        if (!flagDontHaveWatToDo) {
            sendMoveAndEndTourn(-2, -2);
        }
    }

    private boolean chakIfAllStonInAnd() {
        if (yourColor.equals("white")) {
            int sum = 0;
            for (int i = 0; i < 6; i++) {
                sum += stones.getListStonesColor().get(i);
            }
            sum += stones.getListStonesColor().get(25);
            if (sum == 15)
                return true;

        } else {
            int sum = 0;
            for (int i = 18; i < 24; i++) {
                sum += stones.getListStonesColor().get(i);
            }
            sum += stones.getListStonesColor().get(25);
            if (sum == 15)
                return true;
        }
        return false;
    }

    private boolean chakHaveWhatToDo(int index) {
        int length = 24;
        if (flagAllSotnsInEnd)
            length = 26;
        for (int i = 0; i < length; i++) {
            if (i != 24) {
                if (i == 25) return chak25End(index, 24);

                else if (stones.getListStonesColorRivel().get(i) < 2 && i != index) {//no go to same place and rivel 1+
                    if ((yourColor.equals("white") && (index - i == dice1 || index - i == dice2 ||
                            index - i == dice3 || index - i == dice4) || (i == 25 && (index - -1 == dice1 || index - -1 == dice2 ||
                            index - -1 == dice3 || index - -1 == dice4)))
                            || (yourColor.equals("black") && (index + dice1 == i || index + dice2 == i
                            || index + dice3 == i || index + dice4 == i))) {//dices can
                        return true;
                    }
                }
            }

        }
        return false;

    }

    private void clearColorFilter() {
        imageViewWhiteOut.setColorFilter(null);
        imageViewBlackOut.setColorFilter(null);
        imageViewWhiteWin.setColorFilter(null);
        imageViewBlackWin.setColorFilter(null);
        for (int i = 0; i < 24; i++) {
            for (ImageView image : arrayListStackImage.get(i)) {
                image.setColorFilter(null);
            }
        }
    }

    public void setCancalClick() {
        imageViewBlackOut.setClickable(false);
        imageViewWhiteOut.setClickable(false);
        imageViewBlackWin.setClickable(false);
        imageViewWhiteWin.setClickable(false);


        for (int i = 0; i < 24; i++) {
            for (ImageView image : arrayListStackImage.get(i)) {
                image.setClickable(false);
            }
        }
    }

    private void setCancalVIisiMarker() {
        for (int i = 0; i < 24; i++) {
            for (ImageView image : arrayListStackImage.get(i)) {
                if (stones.getListStonesColor().get(i).equals(0) && stones.getListStonesColorRivel().get(i).equals(0))
                    image.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void stonMoveTo(int stonIndex) {
        //  textViewDice.setText("פה מוזר" + indexStone);
        int size = 24;
        if (flagAllSotnsInEnd) {
            size = 26;

        }
        for (int i = 0; i < size; i++) {
            if (i == 24) {
                i++;
                goTo25(stonIndex, i);

            } else if (stones.getListStonesColorRivel().get(i) < 2 && i != stonIndex) {//no go to same place and rivel 1+
//                if(yourColor.equals("white")&&i==24)
//                    i=-1;
                if ((yourColor.equals("white") && (stonIndex - i == dice1 || stonIndex - i == dice2 ||
                        stonIndex - i == dice3 || stonIndex - i == dice4))
                        || (yourColor.equals("black") && (stonIndex + dice1 == i || stonIndex + dice2 == i
                        || stonIndex + dice3 == i || stonIndex + dice4 == i))) {//dices can
                    // textViewDice.setText("פה כן");
                    int langth;//max ston visibulity

                    if (stones.getListStonesColor().get(i) > stones.getListStonesColorRivel().get(i))
                        langth = stones.getListStonesColor().get(i);
                    else
                        langth = stones.getListStonesColorRivel().get(i);
                    if (langth == 0)
                        langth = 1;
                    if (langth > 7) langth = 7;
                    for (int j = 0; j < langth; j++) {

                        //  textViewDice.setText("פה???" + i + "kk" + j + "pp" + indexStone);
                        //  arrayListTextNum.get(i).setText("" + i);
                        ImageView imageView = arrayListStackImage.get(i).get(j);
                        if (yourColor.equals("black")) {
                            if (stones.getListStonesColorRivel().get(i) > 0)
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.white);
                            else
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.black);

                        } else {
                            if (stones.getListStonesColorRivel().get(i) > 0)
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.black);
                            else
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.white);
                        }


                        arrayListStackImage.get(i).get(j).setVisibility(View.VISIBLE);

                        //  arrayListTextNum.get(i).setVisibility(View.VISIBLE);
                        arrayListStackImage.get(i).get(j)
                                .setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);


                        arrayListStackImage.get(i).get(j).setClickable(true);


                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!conction(BackgammonActivity.contextGame)) {
                                    textViewDice.setText("אין קליטה");
                                    textViewDice.setError("אין קליטה");
                                    textViewDice.requestFocus();
                                    return;
                                }
                                textViewDice.clearFocus();
                                ImageView imageView = ((ImageView) view);
                                setCancalClick();

                                int indexGoTo = hashMapIndex.get(imageView);
                                if (yourColor.equals("white")) {//delte dice move
                                    if (stonIndex - indexGoTo == dice1)
                                        dice1 = -100;
                                    else if (stonIndex - indexGoTo == dice2)
                                        dice2 = -100;
                                    else if (stonIndex - indexGoTo == dice3)
                                        dice3 = -100;
                                    else
                                        dice4 = -100;
                                } else {                                //black
                                    if (stonIndex + dice1 == indexGoTo)
                                        dice1 = -100;
                                    else if (stonIndex + dice2 == indexGoTo)
                                        dice2 = -100;
                                    else if (stonIndex + dice3 == indexGoTo)
                                        dice3 = -100;
                                    else
                                        dice4 = -100;

                                }
                                if (stonIndex == 24 || stonIndex == -1) {
                                    stones.getListStonesColor().set(24, stones.getListStonesColor().get(24) - 1);//move stone

                                    if (stones.getListStonesColor().get(24) == 0) {
                                        if (yourColor.equals("white")) {
                                            textViewWhiteOut.setVisibility(View.INVISIBLE);
                                            imageViewWhiteOut.setVisibility(View.INVISIBLE);
                                        } else {
                                            textViewBlackOut.setVisibility(View.INVISIBLE);
                                            imageViewBlackOut.setVisibility(View.INVISIBLE);
                                        }
                                    } else {
                                        if (yourColor.equals("white")) {
                                            textViewWhiteOut.setText("" + stones.getListStonesColor().get(24));

                                        } else {
                                            textViewBlackOut.setText("" + stones.getListStonesColor().get(24));

                                        }

                                    }


                                } else {
                                    if (stones.getListStonesColor().get(stonIndex) < 8) {//move ston he moved

                                        ImageView imageViewUp1 =
                                                arrayListStackImage.get(stonIndex).get(stones.getListStonesColor()
                                                        .get(stonIndex) - 1);//ston move
                                        imageViewUp1.setVisibility(View.INVISIBLE);
                                    } else {

                                        int num = stones.getListStonesColor().get(stonIndex);
                                        num--;
                                        arrayListTextNum.get(stonIndex).setText("" + num);

                                    }


                                    stones.getListStonesColor().set(stonIndex, stones.getListStonesColor().get(stonIndex) - 1);//move stone
                                    // textViewDice.setText("פה???" + stones.getListStonesColor().get(indexGoTo) + "mm" + indexGoTo + "pp" + indexStone+"ll"+stones.getListStonesColor().get(indexStone) );

                                    if (stones.getListStonesColor().get(stonIndex) < 8) {
                                        arrayListTextNum.get(stonIndex).setVisibility(View.INVISIBLE);
                                    }
                                }


                                stones.getListStonesColor().set(indexGoTo, stones.getListStonesColor().get(indexGoTo) + 1);//add ston

                                if (stones.getListStonesColorRivel().get(indexGoTo) == 1) {//if i eat rivel
                                    stones.getListStonesColorRivel().set(24, stones.getListStonesColorRivel().get(24) + 1);//add ston out side
                                    stones.getListStonesColorRivel().set(indexGoTo, 0);//move ston eated

                                    if (rivalColor.equals("white")) {
                                        textViewWhiteOut.setText("" + stones.getListStonesColorRivel().get(24));
                                        textViewWhiteOut.setVisibility(View.VISIBLE);
                                        imageViewWhiteOut.setImageResource(R.drawable.white);
                                        imageViewWhiteOut.setVisibility(View.VISIBLE);
                                    } else {
                                        textViewBlackOut.setText("" + stones.getListStonesColorRivel().get(24));
                                        textViewBlackOut.setVisibility(View.VISIBLE);
                                        imageViewBlackOut.setImageResource(R.drawable.black);

                                        imageViewBlackOut.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (stones.getListStonesColor().get(indexGoTo) < 8) {//show ston move
                                    ImageView imageViewUp2 =
                                            arrayListStackImage.get(indexGoTo).get(stones.getListStonesColor()
                                                    .get(indexGoTo) - 1);
                                    if (yourColor.equals("white")) {
                                        imageViewUp2.setImageResource(R.drawable.white);
                                        imageViewUp2.setVisibility(View.VISIBLE);
                                    } else {
                                        imageViewUp2.setImageResource(R.drawable.black);
                                        imageViewUp2.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    arrayListTextNum.get(indexGoTo).setText("" +
                                            stones.getListStonesColor().get(indexGoTo));
                                    arrayListTextNum.get(indexGoTo).setVisibility(View.VISIBLE);

                                }


                                setCancalVIisiMarker();

                                if (dice1 != -100 || dice2 != -100 || dice3 != -100 || dice4 != -100) {
                                    sendMove(indexGoTo, stonIndex);
                                } else {
                                    sendMoveAndEndTourn(indexGoTo, stonIndex);
                                }

                            }

                        });
                    }
                }
            }
        }

    }

    private void stonMoveToAllStonEnd(int stonIndex) {
        for (int i = 0; i < 26; i++) {
            if (i == 24) {
                i++;
                goTo25(stonIndex, i);
                break;
            }
            if (stones.getListStonesColorRivel().get(i) < 2 && i != stonIndex) {//no go to same place and rivel 1+
//                if(yourColor.equals("white")&&i==24)
//                    i=-1;
                if ((yourColor.equals("white") && (stonIndex - i == dice1 || stonIndex - i == dice2 ||
                        stonIndex - i == dice3 || stonIndex - i == dice4) || (i == 24 &&
                        (stonIndex - -1 == dice1 || stonIndex - -1 == dice2 ||
                                stonIndex - -1 == dice3 || stonIndex - -1 == dice4)))
                        || (yourColor.equals("black") && (stonIndex + dice1 == i || stonIndex + dice2 == i
                        || stonIndex + dice3 == i || stonIndex + dice4 == i))) {//dices can
                    // textViewDice.setText("פה כן");
                    int langth;//max ston visibulity
                    if (i == 24) langth = 1;
                    else if (stones.getListStonesColor().get(i) > stones.getListStonesColorRivel().get(i))
                        langth = stones.getListStonesColor().get(i);
                    else
                        langth = stones.getListStonesColorRivel().get(i);
                    if (langth == 0)
                        langth = 1;
                    for (int j = 0; j < langth; j++) {

                        //  textViewDice.setText("פה???" + i + "kk" + j + "pp" + indexStone);
                        //  arrayListTextNum.get(i).setText("" + i);
                        ImageView imageView = arrayListStackImage.get(i).get(j);
                        if (yourColor.equals("black")) {
                            if (i == 24) {
                                imageView = imageViewBlackWin;
                                imageViewBlackWin.setImageResource(R.drawable.black);
                                imageViewBlackWin.setVisibility(View.VISIBLE);

                                imageViewBlackWin
                                        .setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                                imageViewBlackWin.setClickable(true);
                            } else if (stones.getListStonesColorRivel().get(i) > 0)
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.white);
                            else
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.black);

                        } else {
                            if (i == 24) {
                                imageView = imageViewWhiteWin;

                                imageViewWhiteWin.setImageResource(R.drawable.white);
                                imageViewWhiteWin.setVisibility(View.VISIBLE);

                                imageViewWhiteWin
                                        .setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                                imageViewWhiteWin.setClickable(true);
                            } else if (stones.getListStonesColorRivel().get(i) > 0)
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.black);
                            else
                                arrayListStackImage.get(i).get(j).setImageResource(R.drawable.white);
                        }


                        arrayListStackImage.get(i).get(j).setVisibility(View.VISIBLE);

                        //  arrayListTextNum.get(i).setVisibility(View.VISIBLE);
                        arrayListStackImage.get(i).get(j)
                                .setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);


                        arrayListStackImage.get(i).get(j).setClickable(true);


                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(!conction(BackgammonActivity.contextGame)) {
                                    textViewDice.setText("אין קליטה");
                                    textViewDice.setError("אין קליטה");
                                    textViewDice.requestFocus();
                                    return;
                                }
                                textViewDice.clearFocus();

                                ImageView imageView = ((ImageView) view);
                                setCancalClick();

                                int indexGoTo = hashMapIndex.get(imageView);
                                if (yourColor.equals("white")) {//delte dice move
                                    if (stonIndex - indexGoTo == dice1)
                                        dice1 = -100;
                                    else if (stonIndex - indexGoTo == dice2)
                                        dice2 = -100;
                                    else if (stonIndex - indexGoTo == dice3)
                                        dice3 = -100;
                                    else
                                        dice4 = -100;
                                } else {                                //black
                                    if (stonIndex + dice1 == indexGoTo)
                                        dice1 = -100;
                                    else if (stonIndex + dice2 == indexGoTo)
                                        dice2 = -100;
                                    else if (stonIndex + dice3 == indexGoTo)
                                        dice3 = -100;
                                    else
                                        dice4 = -100;

                                }
                                if (stonIndex == 24 || stonIndex == -1) {
                                    stones.getListStonesColor().set(24, stones.getListStonesColor().get(24) - 1);//move stone

                                    if (stones.getListStonesColor().get(24) == 0) {
                                        if (yourColor.equals("white")) {
                                            textViewWhiteOut.setVisibility(View.INVISIBLE);
                                            imageViewWhiteOut.setVisibility(View.INVISIBLE);
                                        } else {
                                            textViewBlackOut.setVisibility(View.INVISIBLE);
                                            imageViewBlackOut.setVisibility(View.INVISIBLE);
                                        }
                                    } else {
                                        if (yourColor.equals("white")) {
                                            textViewWhiteOut.setText("" + stones.getListStonesColor().get(24));

                                        } else {
                                            textViewBlackOut.setText("" + stones.getListStonesColor().get(24));

                                        }

                                    }


                                } else {
                                    if (stones.getListStonesColor().get(stonIndex) < 8) {//move ston he moved

                                        ImageView imageViewUp1 =
                                                arrayListStackImage.get(stonIndex).get(stones.getListStonesColor()
                                                        .get(stonIndex) - 1);//ston move
                                        imageViewUp1.setVisibility(View.INVISIBLE);
                                    } else {

                                        int num = stones.getListStonesColor().get(stonIndex) - 1;
                                        arrayListTextNum.get(stonIndex).setText("" + num);
                                        arrayListTextNum.get(stonIndex).setVisibility(View.VISIBLE);

                                    }


                                    stones.getListStonesColor().set(stonIndex, stones.getListStonesColor().get(stonIndex) - 1);//move stone
                                    // textViewDice.setText("פה???" + stones.getListStonesColor().get(indexGoTo) + "mm" + indexGoTo + "pp" + indexStone+"ll"+stones.getListStonesColor().get(indexStone) );

                                    if (stones.getListStonesColor().get(stonIndex) < 8) {
                                        arrayListTextNum.get(stonIndex).setVisibility(View.INVISIBLE);
                                    }
                                }

                                int tempIndexGoTo = indexGoTo;
                                if (indexGoTo == -1)
                                    tempIndexGoTo = 24;
                                stones.getListStonesColor().set(tempIndexGoTo, stones.getListStonesColor().get(tempIndexGoTo) + 1);//add ston
                                if (tempIndexGoTo == 24) {
                                    if (stones.getListStonesColor().get(tempIndexGoTo) > 1) {
                                        if (yourColor.equals("white"))
                                            textViewWhiteWin.setText("" + stones.getListStonesColor().get(tempIndexGoTo));
                                        else
                                            textViewBlackWin.setText("V" + stones.getListStonesColor().get(tempIndexGoTo));

                                    } else {
                                        if (yourColor.equals("white")) {
                                            textViewWhiteWin.setText("" + stones.getListStonesColor().get(tempIndexGoTo));
                                            imageViewWhiteWin.setImageResource(R.drawable.white);
                                            imageViewWhiteWin.setVisibility(View.VISIBLE);
                                            textViewWhiteWin.setVisibility(View.VISIBLE);
                                        } else {
                                            textViewBlackWin.setText("" + stones.getListStonesColor().get(tempIndexGoTo));
                                            imageViewBlackWin.setImageResource(R.drawable.black);
                                            imageViewBlackWin.setVisibility(View.VISIBLE);
                                            textViewBlackWin.setVisibility(View.VISIBLE);
                                        }
                                    }

                                } else {
                                    if (stones.getListStonesColorRivel().get(indexGoTo) == 1) {//if i eat rivel
                                        stones.getListStonesColorRivel().set(24, stones.getListStonesColorRivel().get(24) + 1);//add ston out side
                                        stones.getListStonesColorRivel().set(indexGoTo, 0);//move ston eated

                                        if (rivalColor.equals("white")) {
                                            textViewWhiteOut.setText("" + stones.getListStonesColorRivel().get(24));
                                            textViewWhiteOut.setVisibility(View.VISIBLE);
                                            imageViewWhiteOut.setImageResource(R.drawable.white);
                                            imageViewWhiteOut.setVisibility(View.VISIBLE);
                                        } else {
                                            textViewBlackOut.setText("" + stones.getListStonesColorRivel().get(24));
                                            textViewBlackOut.setVisibility(View.VISIBLE);
                                            imageViewBlackOut.setImageResource(R.drawable.black);

                                            imageViewBlackOut.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    if (stones.getListStonesColor().get(indexGoTo) < 8) {//show ston move
                                        ImageView imageViewUp2 =
                                                arrayListStackImage.get(indexGoTo).get(stones.getListStonesColor()
                                                        .get(indexGoTo) - 1);
                                        if (yourColor.equals("white")) {
                                            imageViewUp2.setImageResource(R.drawable.white);
                                            imageViewUp2.setVisibility(View.VISIBLE);
                                        } else {
                                            imageViewUp2.setImageResource(R.drawable.black);
                                            imageViewUp2.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        arrayListTextNum.get(indexGoTo).setText("" +
                                                stones.getListStonesColor().get(indexGoTo));
                                        arrayListTextNum.get(indexGoTo).setVisibility(View.VISIBLE);

                                    }

                                }
                                setCancalVIisiMarker();
                                if (dice1 != -100 || dice2 != -100 || dice3 != -100 || dice4 != -100) {
                                    sendMove(indexGoTo, stonIndex);
                                } else {
                                    sendMoveAndEndTourn(indexGoTo, stonIndex);
                                }

                            }

                        });
                    }
                }
            }
        }

    }

    private int bigIndexEnd() {
        int big = -3;
        if (yourColor.equals("white")) {
            for (int j = 5; j >= 0; j--) {
                if (stones.getListStonesColor().get(j) > 0) {
                    return j;
                }
            }
        } else {
            for (int j = 18; j < 24; j++) {
                if (stones.getListStonesColor().get(j) > 0) {
                    return j;
                }
            }
        }

        return big;

    }

    private boolean chak25End(int stonIndex, int i) {
        bigIndex = bigIndexEnd();

        if (bigIndex == -3) {
            return false;
        }
        if ((yourColor.equals("white") && (stonIndex - -1 == dice1 || stonIndex - -1 == dice2 ||
                stonIndex - -1 == dice3 || stonIndex - -1 == dice4))
                || (yourColor.equals("black") && (stonIndex + dice1 == i || stonIndex + dice2 == i
                || stonIndex + dice3 == i || stonIndex + dice4 == i)) ||
                (bigIndex == stonIndex && yourColor.equals("white") && (stonIndex - -1 <= dice1 || stonIndex - -1 <= dice2 ||
                        stonIndex - -1 <= dice3 || stonIndex - -1 <= dice4))
                || (bigIndex == stonIndex && yourColor.equals("black") && (stonIndex + dice1 >= i || stonIndex + dice2 >= i
                || stonIndex + dice3 >= i || stonIndex + dice4 >= i))) {//d
            return true;
        }
        return false;
    }

    private void goTo25(int stonIndex, int i) {

        i = 24;
        if (chak25End(stonIndex, i)) {


            //  textViewDice.setText("פה???" + i + "kk" + j + "pp" + indexStone);
            //  arrayListTextNum.get(i).setText("" + i);
            ImageView imageView;
            if (yourColor.equals("black")) {

                imageView = imageViewBlackWin;
                imageView.setImageResource(R.drawable.black);
                imageView.setVisibility(View.VISIBLE);

                imageView
                        .setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                imageView.setClickable(true);
            } else {
                imageView = imageViewWhiteWin;

                imageView.setImageResource(R.drawable.white);
                imageView.setVisibility(View.VISIBLE);

                imageView
                        .setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                imageView.setClickable(true);
            }


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!conction(BackgammonActivity.contextGame)) {
                        textViewDice.setText("אין קליטה");
                        textViewDice.setError("אין קליטה");
                        textViewDice.requestFocus();
                        return;
                    }
                    textViewDice.clearFocus();

                    ImageView imageView = ((ImageView) view);
                    setCancalClick();

                    int indexGoTo = hashMapIndex.get(imageView);

                    if (bigIndex == stonIndex) {
                        if (yourColor.equals("white")) {//delte dice move
                            if (stonIndex - indexGoTo <= dice1)
                                dice1 = -100;
                            else if (stonIndex - indexGoTo <= dice2)
                                dice2 = -100;
                            else if (stonIndex - indexGoTo <= dice3)
                                dice3 = -100;
                            else
                                dice4 = -100;
                        } else {                                //black
                            if (stonIndex + dice1 >= indexGoTo)
                                dice1 = -100;
                            else if (stonIndex + dice2 >= indexGoTo)
                                dice2 = -100;
                            else if (stonIndex + dice3 >= indexGoTo)
                                dice3 = -100;
                            else
                                dice4 = -100;

                        }

                    } else {
                        if (yourColor.equals("white")) {//delte dice move
                            if (stonIndex - indexGoTo == dice1)
                                dice1 = -100;
                            else if (stonIndex - indexGoTo == dice2)
                                dice2 = -100;
                            else if (stonIndex - indexGoTo == dice3)
                                dice3 = -100;
                            else
                                dice4 = -100;
                        } else {                                //black
                            if (stonIndex + dice1 == indexGoTo)
                                dice1 = -100;
                            else if (stonIndex + dice2 == indexGoTo)
                                dice2 = -100;
                            else if (stonIndex + dice3 == indexGoTo)
                                dice3 = -100;
                            else
                                dice4 = -100;

                        }
                    }

                    if (stones.getListStonesColor().get(stonIndex) < 8) {//move ston he moved

                        ImageView imageViewUp1 =
                                arrayListStackImage.get(stonIndex).get(stones.getListStonesColor()
                                        .get(stonIndex) - 1);//ston move
                        imageViewUp1.setVisibility(View.INVISIBLE);
                    } else {

                        int num = stones.getListStonesColor().get(stonIndex);
                        num--;
                        arrayListTextNum.get(stonIndex).setText("" + num);

                    }


                    stones.getListStonesColor().set(stonIndex, stones.getListStonesColor().get(stonIndex) - 1);//move stone
                    // textViewDice.setText("פה???" + stones.getListStonesColor().get(indexGoTo) + "mm" + indexGoTo + "pp" + indexStone+"ll"+stones.getListStonesColor().get(indexStone) );

                    if (stones.getListStonesColor().get(stonIndex) < 8) {
                        arrayListTextNum.get(stonIndex).setVisibility(View.INVISIBLE);
                    }


                    stones.getListStonesColor().set(25, stones.getListStonesColor().get(25) + 1);//add ston
                    if (stones.getListStonesColor().get(25) > 1) {
                        if (yourColor.equals("white"))
                            textViewWhiteWin.setText("" + stones.getListStonesColor().get(25));
                        else
                            textViewBlackWin.setText("" + stones.getListStonesColor().get(25));

                    } else {
                        if (yourColor.equals("white")) {
                            textViewWhiteWin.setText("" + stones.getListStonesColor().get(25));
                            imageViewWhiteWin.setImageResource(R.drawable.white);
                            imageViewWhiteWin.setVisibility(View.VISIBLE);
                            textViewWhiteWin.setVisibility(View.VISIBLE);
                        } else {
                            textViewBlackWin.setText("" + stones.getListStonesColor().get(25));
                            imageViewBlackWin.setImageResource(R.drawable.black);
                            imageViewBlackWin.setVisibility(View.VISIBLE);
                            textViewBlackWin.setVisibility(View.VISIBLE);
                        }
                    }


                    setCancalVIisiMarker();

                    indexGoTo = 25;//-2
                    bigIndex = bigIndexEnd();
                    if (dice1 != -100 || dice2 != -100 || dice3 != -100 || dice4 != -100) {
                        sendMove(indexGoTo, stonIndex);
                    } else {
                        sendMoveAndEndTourn(indexGoTo, stonIndex);
                    }

                }

            });

        }
    }


    private void sendMove(int indexStoneGoTo, int stonIndex) {
        MovesGame movesGame = new MovesGame();
        movesGame.setInfoTo(rivalColor);
        movesGame.setPosion1(stonIndex);
//        movesGame.setDice1(dice1);
//        movesGame.setDice2(dice2);
        HashMap<String, String> hashMapUid = new HashMap<>();
        hashMapUid.put(BackgammonActivity.uidMe, BackgammonActivity.uidRivel);
        hashMapUid.put(BackgammonActivity.uidRivel, BackgammonActivity.uidMe);
        movesGame.setHashMapUid(hashMapUid);
        HashMap<String, List<Integer>> hashMapBord = new HashMap<>();
        hashMapBord.put(BackgammonActivity.uidMe, stones.getListStonesColor());
        hashMapBord.put(BackgammonActivity.uidRivel, stones.getListStonesColorRivel());

        HashMap<String, String> hashMapColor = new HashMap<>();
        hashMapColor.put(BackgammonActivity.uidMe, yourColor);
        hashMapColor.put(BackgammonActivity.uidRivel, rivalColor);

        movesGame.setHashMapColor(hashMapColor);
        movesGame.setHashMapBord(hashMapBord);
        movesGame.setTournUid(BackgammonActivity.uidMe);
        movesGame.setPosion1MoveTo(indexStoneGoTo);
        movesGame.setType("onlyMoveSton");
        movesGame.setUidPrimery(uid);
        movesGame.setDice1(dice1);
        movesGame.setDice2(dice2);
        movesGame.setDice3(dice3);
        movesGame.setDice4(dice4);

        countMove++;
        if (bigIndex == -3)
            movesGame.setLose(true);
        movesGame.setCountMove(countMove);

        String uidOut;
        if (uid.equals(BackgammonActivity.uidMe))
            uidOut = BackgammonActivity.uidRivel;
        else
            uidOut = BackgammonActivity.uidMe;
        DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uidOut);
        databaseReference9.setValue(movesGame);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference3.setValue(movesGame).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (bigIndex != -3) chesSton();
                else
                    textViewDice.setText(" ניצחת");

            }
        });
    }

    private void sendMoveAndEndTourn(int indexStoneGoTo, int stonIndex) {
        clearColorFilter();
        setCancalClick();
        textViewDice.setText("תור השני: אנא המתן");
        MovesGame movesGame = new MovesGame();
        movesGame.setInfoTo(rivalColor);
        if (bigIndex == -3)
            movesGame.setLose(true);
        HashMap<String, String> hashMapUid = new HashMap<>();
        hashMapUid.put(BackgammonActivity.uidMe, BackgammonActivity.uidRivel);
        hashMapUid.put(BackgammonActivity.uidRivel, BackgammonActivity.uidMe);
        movesGame.setHashMapUid(hashMapUid);
        HashMap<String, List<Integer>> hashMapBord = new HashMap<>();
        hashMapBord.put(BackgammonActivity.uidMe, stones.getListStonesColor());
        hashMapBord.put(BackgammonActivity.uidRivel, stones.getListStonesColorRivel());

        HashMap<String, String> hashMapColor = new HashMap<>();
        hashMapColor.put(BackgammonActivity.uidMe, yourColor);
        hashMapColor.put(BackgammonActivity.uidRivel, rivalColor);

        movesGame.setUidPrimery(uid);

        movesGame.setHashMapColor(hashMapColor);
        movesGame.setHashMapBord(hashMapBord);
        movesGame.setTournUid(BackgammonActivity.uidRivel);
        movesGame.setPosion1(stonIndex);
        movesGame.setPosion1MoveTo(indexStoneGoTo);
        movesGame.setType("MoveStonAndEndTurn");
        countMove++;
        movesGame.setCountMove(countMove);

        String uidOut;
        if (uid.equals(BackgammonActivity.uidMe))
            uidOut = BackgammonActivity.uidRivel;
        else
            uidOut = BackgammonActivity.uidMe;
        DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uidOut);
        databaseReference9.setValue(movesGame);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference3.setValue(movesGame).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                timer.stop();

                if (bigIndex == -3) {
                    textViewDice.setText(" ניצחת");
                    BackgammonActivity.flagListener=0;
                    DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(BackgammonActivity.uidMe);
                    databaseReference9.removeValue();
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(BackgammonActivity.uidRivel);
                    databaseReference3.removeValue();
                }
                else
                    timer.start("תור השני");
            }

        });
        moveTowDice();
    }

    public void onlyMoveSton(int start, int end) {
        if (end == -2) {
            return;
        }
        if (end == 25) {


            if (stones.getListStonesColorRivel().get(start) < 8) {//move ston he moved

                ImageView imageViewUp1 =
                        arrayListStackImage.get(start).get(stones.getListStonesColorRivel()
                                .get(start) - 1);//ston move
                imageViewUp1.setVisibility(View.INVISIBLE);
            } else {

                int num = stones.getListStonesColorRivel().get(start);
                num--;
                arrayListTextNum.get(start).setText("" + num);

            }


            stones.getListStonesColorRivel().set(start, stones.getListStonesColorRivel().get(start) - 1);//move stone
            // textViewDice.setText("פה???" + stones.getListStonesColor().get(indexGoTo) + "mm" + indexGoTo + "pp" + indexStone+"ll"+stones.getListStonesColor().get(indexStone) );

            if (stones.getListStonesColorRivel().get(start) < 8) {
                arrayListTextNum.get(start).setVisibility(View.INVISIBLE);
            }


            int tempIndexGoTo = 25;

            stones.getListStonesColorRivel().set(tempIndexGoTo, stones.getListStonesColorRivel().get(25) + 1);//add ston
            if (stones.getListStonesColorRivel().get(tempIndexGoTo) > 1) {
                if (rivalColor.equals("white"))
                    textViewWhiteWin.setText("" + stones.getListStonesColorRivel().get(tempIndexGoTo));
                else
                    textViewBlackWin.setText("" + stones.getListStonesColorRivel().get(tempIndexGoTo));

            } else {
                if (rivalColor.equals("white")) {
                    textViewWhiteWin.setText("" + stones.getListStonesColorRivel().get(tempIndexGoTo));
                    imageViewWhiteWin.setImageResource(R.drawable.white);
                    imageViewWhiteWin.setVisibility(View.VISIBLE);
                    textViewWhiteWin.setVisibility(View.VISIBLE);
                } else {
                    textViewBlackWin.setText("" + stones.getListStonesColorRivel().get(tempIndexGoTo));
                    imageViewBlackWin.setImageResource(R.drawable.black);
                    imageViewBlackWin.setVisibility(View.VISIBLE);
                    textViewBlackWin.setVisibility(View.VISIBLE);
                }


            }
            return;
        }
        if (start == 24 || start == -1) {
            stones.getListStonesColorRivel().set(24, stones.getListStonesColorRivel().get(24) - 1);//move stone

            stones.getListStonesColorRivel().set(end, stones.getListStonesColorRivel().get(end) + 1);
            if (stones.getListStonesColorRivel().get(24) == 0) {
                if (rivalColor.equals("white")) {
                    textViewWhiteOut.setVisibility(View.INVISIBLE);
                    imageViewWhiteOut.setVisibility(View.INVISIBLE);
                } else {
                    textViewBlackOut.setVisibility(View.INVISIBLE);
                    imageViewBlackOut.setVisibility(View.INVISIBLE);
                }
            } else {
                if (rivalColor.equals("white")) {
                    textViewWhiteOut.setText("" + stones.getListStonesColorRivel().get(24));

                } else {
                    textViewBlackOut.setText("" + stones.getListStonesColorRivel().get(24));

                }

            }


        } else {
            if (stones.getListStonesColorRivel().get(start) < 8) {//move ston he moved

                ImageView imageViewUp1 =
                        arrayListStackImage.get(start).get(stones.getListStonesColorRivel()
                                .get(start) - 1);//ston move
                imageViewUp1.setVisibility(View.INVISIBLE);
            } else {

                int num = stones.getListStonesColorRivel().get(start);
                num--;
                arrayListTextNum.get(start).setText("" + num);

            }


            stones.getListStonesColorRivel().set(start, stones.getListStonesColorRivel().get(start) - 1);//move stone
            //  textViewDice.setText("פה???" + stones.getListStonesColor().get(indexGoTo) + "mm" + indexGoTo + "pp" + indexStone+"ll"+stones.getListStonesColor().get(indexStone) );

            stones.getListStonesColorRivel().set(end, stones.getListStonesColorRivel().get(end) + 1);//add ston
            if (stones.getListStonesColorRivel().get(start) < 8) {
                arrayListTextNum.get(start).setVisibility(View.INVISIBLE);
            }
        }
        if (stones.getListStonesColor().get(end) == 1) {//if i eat rivel
            stones.getListStonesColor().set(24, stones.getListStonesColor().get(24) + 1);//add ston out side
            stones.getListStonesColor().set(end, 0);//move ston eated

            if (yourColor.equals("white")) {
                textViewWhiteOut.setText("" + stones.getListStonesColor().get(24));
                textViewWhiteOut.setVisibility(View.VISIBLE);
                imageViewWhiteOut.setImageResource(R.drawable.white);
                imageViewWhiteOut.setVisibility(View.VISIBLE);
            } else {
                textViewBlackOut.setText("" + stones.getListStonesColor().get(24));
                textViewBlackOut.setVisibility(View.VISIBLE);
                imageViewBlackOut.setImageResource(R.drawable.black);
                imageViewBlackOut.setVisibility(View.VISIBLE);
            }
        }
        if (stones.getListStonesColorRivel().get(end) < 8) {//show ston move
            ImageView imageViewUp2 =
                    arrayListStackImage.get(end).get(stones.getListStonesColorRivel()
                            .get(end) - 1);
            if (rivalColor.equals("white")) {
                imageViewUp2.setImageResource(R.drawable.white);
                imageViewUp2.setVisibility(View.VISIBLE);
            } else {
                imageViewUp2.setImageResource(R.drawable.black);
                imageViewUp2.setVisibility(View.VISIBLE);
            }
        } else {
            arrayListTextNum.get(end).setText("" +
                    stones.getListStonesColorRivel().get(end));
            arrayListTextNum.get(end).setVisibility(View.VISIBLE);
        }


    }


    public void moveTowDice() {
        imageViewDice1.setVisibility(View.VISIBLE);
        imageViewDice2.setVisibility(View.VISIBLE);

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
                        Thread.sleep(300);
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
                        Thread.sleep(300);
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
        textViewWhiteOut.setVisibility(View.INVISIBLE);
        textViewBlackOut.setVisibility(View.INVISIBLE);
        textViewBlackWin.setVisibility(View.INVISIBLE);
        textViewWhiteWin.setVisibility(View.INVISIBLE);
        if (yourColor.equals("white")) {
            timer.start("תורך");
            textViewDice.setText("תורך: זרוק מי מתחיל");
            imageViewDice1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!conction(BackgammonActivity.contextGame)) {
                        textViewDice.setText("אין קליטה");
                        textViewDice.setError("אין קליטה");
                        textViewDice.requestFocus();
                        return;
                    }
                    textViewDice.clearFocus();
                    imageViewDice1.setClickable(false);
                    flagDiceTrow++;
                    int numDice = roolOneDice(imageViewDice1);
                    MovesGame movesGame = new MovesGame();
                    movesGame.setType("start1");
                    movesGame.setInfoTo("black");
                    movesGame.setDice1(numDice);
                    movesGame.setTournUid(BackgammonActivity.uidRivel);
                    HashMap<String, String> hashMapUid = new HashMap<>();
                    hashMapUid.put(BackgammonActivity.uidMe, BackgammonActivity.uidRivel);
                    hashMapUid.put(BackgammonActivity.uidRivel, BackgammonActivity.uidMe);
                    movesGame.setHashMapUid(hashMapUid);
                    HashMap<String, List<Integer>> hashMapBord = new HashMap<>();
                    hashMapBord.put(BackgammonActivity.uidMe, stones.getListStonesColor());
                    hashMapBord.put(BackgammonActivity.uidRivel, stones.getListStonesColorRivel());

                    HashMap<String, String> hashMapColor = new HashMap<>();
                    hashMapColor.put(BackgammonActivity.uidMe, yourColor);
                    hashMapColor.put(BackgammonActivity.uidRivel, rivalColor);

                    movesGame.setUidPrimery(uid);

                    movesGame.setHashMapColor(hashMapColor);
                    movesGame.setHashMapBord(hashMapBord);

                    countMove++;
                    movesGame.setSendUidToRive(BackgammonActivity.uidMe);
                    movesGame.setCountMove(countMove);
                    textViewDice.setText("תור השני: שיזרוק מי מתחיל");

                    String uidOut;
                    if (uid.equals(BackgammonActivity.uidMe))
                        uidOut = BackgammonActivity.uidRivel;
                    else
                        uidOut = BackgammonActivity.uidMe;
                    DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(uidOut);
                    databaseReference9.setValue(movesGame);

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
                            timer.stop();
                            timer.start("תור השני");
                        }
                    });


                }
            });
        } else {
            textViewDice.setText("תור השני: שיזרוק מי מתחיל");
            timer.start("תורך");

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
        for (int i = 0; i < 26; i++) {

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
                if(!conction(BackgammonActivity.contextGame)) {
                    textViewDice.setText("אין קליטה");
                    textViewDice.setError("אין קליטה");
                    textViewDice.requestFocus();
                    return;
                }
                textViewDice.clearFocus();

                trowTowDice();
            }
        });
        imageViewDice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!conction(BackgammonActivity.contextGame)) {
                    textViewDice.setText("אין קליטה");
                    textViewDice.setError("אין קליטה");
                    textViewDice.requestFocus();
                    return;
                }
                textViewDice.clearFocus();

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

    public ImageView getImageViewBlackOut() {
        return imageViewBlackOut;
    }

    public void setImageViewBlackOut(ImageView imageViewBlackOut) {
        this.imageViewBlackOut = imageViewBlackOut;
    }

    public ImageView getImageViewBlackWin() {
        return imageViewBlackWin;
    }

    public void setImageViewBlackWin(ImageView imageViewBlackWin) {
        this.imageViewBlackWin = imageViewBlackWin;
    }

    public ImageView getImageViewWhiteWin() {
        return imageViewWhiteWin;
    }

    public void setImageViewWhiteWin(ImageView imageViewWhiteWin) {
        this.imageViewWhiteWin = imageViewWhiteWin;
    }

    public TextView getTextViewBlackWin() {
        return textViewBlackWin;
    }

    public void setTextViewBlackWin(TextView textViewBlackWin) {
        this.textViewBlackWin = textViewBlackWin;
    }

    public TextView getTextViewWhiteWin() {
        return textViewWhiteWin;
    }

    public void setTextViewWhiteWin(TextView textViewWhiteWin) {
        this.textViewWhiteWin = textViewWhiteWin;
    }

    public int getCountMove() {
        return countMove;
    }

    public void setCountMove(int countMove) {
        this.countMove = countMove;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Stones getStones() {
        return stones;
    }

    public void setStones(Stones stones) {
        this.stones = stones;
    }

    public TextView getTextViewTime() {
        return textViewTime;
    }

    public void setTextViewTime(TextView textViewTime) {
        this.textViewTime = textViewTime;
    }

    public int getDice1() {
        return dice1;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    public int getDice3() {
        return dice3;
    }

    public void setDice3(int dice3) {
        this.dice3 = dice3;
    }

    public int getDice4() {
        return dice4;
    }

    public void setDice4(int dice4) {
        this.dice4 = dice4;
    }
    public boolean conction(Context context){
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        return false;
    }
}
