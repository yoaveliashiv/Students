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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class BackgammonActivity extends AppCompatActivity {
    private String color = "";
    private String uid = "";
    private TextView textViewColor;
    private ArrayList<ArrayList<ImageView>> arrayListStackImage;
    private ArrayList<TextView> arrayListTextNum;
    private ImageView imageViewBlackOut;
    private ImageView imageViewWhiteOut;
    private TextView textViewBlackOut;
    private TextView textViewWhiteOut;
    private ImageView imageViewBlackWin;
    private ImageView imageViewWhiteWin;
    private TextView textViewBlackWin;
    private TextView textViewWhiteWin;
    private ImageView imageViewDice2;
    private ImageView imageViewDice1;
    private TextView textViewDice;
    private TextView textViewTime;
    protected static String uidMe;
    protected static String uidRivel;
    private int flagListener = 0;
    private BackgammonBoard board;
    private HashMap<ImageView, Integer> hashMapIndex;

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

        if (getIntent().hasExtra("middelGame")) {
            buildBord();
            board = new BackgammonBoard(arrayListTextNum, arrayListStackImage,
                    imageViewDice1, imageViewDice2, color, textViewDice, uid, BackgammonActivity.this, hashMapIndex
                    , imageViewWhiteOut, imageViewBlackOut, textViewWhiteOut, textViewBlackOut, textViewTime);
            board.setImageViewBlackWin(imageViewBlackWin);
            board.setImageViewWhiteWin(imageViewWhiteWin);
            board.setTextViewBlackWin(textViewBlackWin);
            board.setTextViewWhiteWin(textViewWhiteWin);

            continiouBuild();
        } else {
            board = new BackgammonBoard(arrayListTextNum, arrayListStackImage,
                    imageViewDice1, imageViewDice2, color, textViewDice, uid, BackgammonActivity.this, hashMapIndex
                    , imageViewWhiteOut, imageViewBlackOut, textViewWhiteOut, textViewBlackOut, textViewTime);
            board.setImageViewBlackWin(imageViewBlackWin);
            board.setImageViewWhiteWin(imageViewWhiteWin);
            board.setTextViewBlackWin(textViewBlackWin);
            board.setTextViewWhiteWin(textViewWhiteWin);

            board.build();
            board.moveDice(imageViewDice1);
        }
        textViewColor.setText("הצבע שלך: " + color);
        if (color.equals("white"))
            textViewColor.setTextColor(getResources().getColor(R.color.colorWhite));
        setOnListener();
        super.onCreate(savedInstanceState);
    }


    private void setOnListener() {
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (flagListener > 0 && snapshot.exists()) {
                    MovesGame movesGame = snapshot.getValue(MovesGame.class);
                    moves(movesGame);
                } else flagListener++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void moves(MovesGame movesGame) {

        if (!movesGame.getInfoTo().contains(color))
            return;

        switch (movesGame.getType()) {
            case "start1":
                uidRivel = movesGame.getSendUidToRive();
                board.getTimer().stop();
                board.getTimer().start("תורך");
                board.flagDiceTrowe();
                board.setImage(movesGame.getDice1(), imageViewDice1);
                textViewDice.setText("תורך: זרוק מי מתחיל");
                board.moveDice(imageViewDice2);
                imageViewDice2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                                .getReference("Play backgammon").child(uid);
                        imageViewDice2.setClickable(false);
                        board.flagDiceTrowe();
                        int numDice = board.roolOneDice(imageViewDice2);
                        MovesGame movesGame2 = new MovesGame();
                        movesGame2.setDice1(movesGame.getDice1());
                        movesGame2.setDice2(numDice);
                        movesGame2.setType("start2");
                        movesGame2.setSendUidToRive(uidMe);
                        board.setCountMove(board.getCountMove() + 1);
                        movesGame2.setCountMove(board.getCountMove());
                        if (numDice == movesGame.getDice1()) {


                            movesGame2.setType("equal");
                            movesGame2.setInfoTo("black white");
                            databaseReference3.setValue(movesGame2);
                            return;
                        }
                        movesGame2.setInfoTo("black white");


                        databaseReference3.setValue(movesGame2).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // imageViewDice1.setClickable(true);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                board.getTimer().stop();
                                board.getTimer().start("תור השני");
                            }
                        });


                    }
                });
                break;
            case "start2":

                new Thread() {

                    public void run() {
                        if (color.equals("white")) {
                            uidRivel = movesGame.getSendUidToRive();
                            board.flagDiceTrowe();
                            board.setImage(movesGame.getDice2(), imageViewDice2);
                        }

                        try {
                            Thread.sleep(5000);

                            BackgammonActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (color.equals("white")) {
                                        board.flagDiceTrowe();
                                        board.moveTowDice();

                                        if (movesGame.getDice1() > movesGame.getDice2()) {
                                            textViewDice.setText("תורך: זרוק קוביות");
                                            board.setClick2Dices();
                                            board.getTimer().stop();
                                            board.getTimer().start("תורך");

                                        } else {
                                            board.getTimer().stop();
                                            board.getTimer().start("תור השני");
                                            textViewDice.setText("תור השני: אנא המתן");
                                        }
                                    } else {//black
                                        board.moveTowDice();
                                        if (movesGame.getDice1() < movesGame.getDice2()) {
                                            board.getTimer().stop();
                                            board.getTimer().start("תורך");
                                            textViewDice.setText("תורך: זרוק קוביות");
                                            board.setClick2Dices();
                                        } else {
                                            board.getTimer().stop();
                                            board.getTimer().start("תור השני");
                                            textViewDice.setText("תור השני: אנא המתן");
                                        }
                                    }


                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }.start();
                break;
            case "equal":
                board.flagDiceTrowe();
                board.build();
                board.moveDice(imageViewDice1);

                break;
            case "dices":
                board.flagDiceTrowe();
                board.setImage(movesGame.getDice1(), imageViewDice1);
                board.setImage(movesGame.getDice2(), imageViewDice2);

                break;
            case "onlyMoveSton":
                board.onlyMoveSton(movesGame.getPosion1(), movesGame.getPosion1MoveTo());
                if (movesGame.getLose())
                    textViewDice.setText("הפסדת");
                break;
            case "MoveStonAndEndTurn":
                board.getTimer().stop();
                board.onlyMoveSton(movesGame.getPosion1(), movesGame.getPosion1MoveTo());
                if (movesGame.getLose())
                    textViewDice.setText("הפסדת");
                else {
                    board.getTimer().start("תורך");
                    textViewDice.setText("תורך: זרוק קוביות");
                    board.setClick2Dices();
                    board.moveTowDice();
                }
                break;//"onlyMoveSton"
            default:
                break;
        }
    }

    private void roolIsYouToun() {

    }

    private void setId() {
        uidMe = FirebaseAuth.getInstance().getUid();
        imageViewBlackOut = findViewById(R.id.imageVieOutSideBlack);
        imageViewWhiteOut = findViewById(R.id.imageVieOutSideWhite);
        textViewBlackOut = findViewById(R.id.textViewBlack);
        textViewWhiteOut = findViewById(R.id.textViewWhite);

        imageViewBlackWin = findViewById(R.id.imageVieWinBlack);
        imageViewWhiteWin = findViewById(R.id.imageVieWinWhite);
        textViewBlackWin = findViewById(R.id.textViewWinBlack);
        textViewWhiteWin = findViewById(R.id.textViewWinWhite);

        hashMapIndex = new HashMap<>();
        imageViewDice1 = findViewById(R.id.imageView_dice1);
        imageViewDice2 = findViewById(R.id.imageView_dice2);
        imageViewDice1.setImageResource(R.drawable.dice6);
        imageViewDice2.setImageResource(R.drawable.dice6);
        textViewColor = findViewById(R.id.textView_color9);
        textViewDice = findViewById(R.id.textView_dice);
        textViewTime = findViewById(R.id.textView_time_b);
        arrayListStackImage = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            ArrayList<ImageView> stack = new ArrayList<>();

            for (int j = 1; j < 8; j++) {
                String imageId = "imageVie" + i + "" + j;
                int resIDimage = getResources().getIdentifier(imageId, "id", getPackageName());
                ImageView imageView = findViewById(resIDimage);
                hashMapIndex.put(imageView, i - 1);
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

        hashMapIndex.put(imageViewBlackWin, 24);
        hashMapIndex.put(imageViewWhiteWin, -1);


    }

    private void buildBord() {
        textViewWhiteOut.setVisibility(View.INVISIBLE);
        textViewBlackOut.setVisibility(View.INVISIBLE);
        textViewBlackWin.setVisibility(View.INVISIBLE);
        textViewWhiteWin.setVisibility(View.INVISIBLE);
        for (ArrayList<ImageView> array : arrayListStackImage) {
            for (ImageView imageView : array) {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
        for (TextView textView : arrayListTextNum) {
            textView.setVisibility(View.INVISIBLE);
        }


        MovesGame movesGameMiddel = PlayActivity.movesGame;
        color = movesGameMiddel.getHashMapColor().get(uidMe);
        uid = movesGameMiddel.getUidPrimery();
        uidRivel = movesGameMiddel.getHashMapUid().get(uidMe);

        for (int i = 0; i < arrayListStackImage.size(); i++) {
            String colorr;
            int length;
            if (movesGameMiddel.getHashMapBord().get(uidMe).get(i) > movesGameMiddel.getHashMapBord().get(uidRivel).get(i)) {
                length = movesGameMiddel.getHashMapBord().get(uidMe).get(i);
                colorr = color;
            } else {
                length = movesGameMiddel.getHashMapBord().get(uidRivel).get(i);
                colorr = movesGameMiddel.getHashMapColor().get(uidRivel);
                int a = R.drawable.white;
            }
            int id;
            if (colorr.equals("white"))
                id = R.drawable.white;
            else
                id = R.drawable.black;
            if (length > 7) {
                arrayListTextNum.get(i).setText("" + length);
                length = 7;
            }
            for (int j = 0; j < length; j++) {
                arrayListStackImage.get(i).get(j).setImageResource(id);
                arrayListStackImage.get(i).get(j).setVisibility(View.VISIBLE);
            }
        }
        if (movesGameMiddel.getHashMapBord().get(uidMe).get(24) > 0) {
            if (color.equals("white")) {
                imageViewWhiteOut.setImageResource(R.drawable.white);
                imageViewWhiteOut.setVisibility(View.VISIBLE);
                textViewWhiteOut.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(24));
                textViewWhiteOut.setVisibility(View.VISIBLE);
            } else {
                imageViewBlackOut.setImageResource(R.drawable.black);
                imageViewBlackOut.setVisibility(View.VISIBLE);
                textViewBlackOut.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(24));
                textViewBlackOut.setVisibility(View.VISIBLE);
            }
        }
        if (movesGameMiddel.getHashMapBord().get(uidRivel).get(24) > 0) {
            if (movesGameMiddel.getHashMapColor().get(uidRivel).equals("white")) {
                imageViewWhiteOut.setImageResource(R.drawable.white);
                imageViewWhiteOut.setVisibility(View.VISIBLE);
                textViewWhiteOut.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(24));
                textViewWhiteOut.setVisibility(View.VISIBLE);
            } else {
                imageViewBlackOut.setImageResource(R.drawable.black);
                imageViewBlackOut.setVisibility(View.VISIBLE);
                textViewBlackOut.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(24));
                textViewBlackOut.setVisibility(View.VISIBLE);
            }
        }


        if (movesGameMiddel.getHashMapBord().get(uidMe).get(25) > 0) {
            if (color.equals("white")) {
                imageViewWhiteWin.setImageResource(R.drawable.white);
                imageViewWhiteWin.setVisibility(View.VISIBLE);
                textViewWhiteWin.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(25));
                textViewWhiteWin.setVisibility(View.VISIBLE);
            } else {
                imageViewBlackWin.setImageResource(R.drawable.black);
                imageViewBlackWin.setVisibility(View.VISIBLE);
                textViewBlackWin.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(25));
                textViewBlackWin.setVisibility(View.VISIBLE);
            }
        }
        if (movesGameMiddel.getHashMapBord().get(uidRivel).get(25) > 0) {
            if (movesGameMiddel.getHashMapColor().get(uidRivel).equals("white")) {
                imageViewWhiteWin.setImageResource(R.drawable.white);
                imageViewWhiteWin.setVisibility(View.VISIBLE);
                textViewWhiteWin.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(25));
                textViewWhiteWin.setVisibility(View.VISIBLE);
            } else {
                imageViewBlackWin.setImageResource(R.drawable.black);
                imageViewBlackWin.setVisibility(View.VISIBLE);
                textViewBlackWin.setText("" + movesGameMiddel.getHashMapBord().get(uidMe).get(25));
                textViewBlackWin.setVisibility(View.VISIBLE);
            }
        }


    }

    private void continiouBuild() {
        Stones stones = new Stones();
        stones.setListStonesColor(PlayActivity.movesGame.getHashMapBord().get(uidMe));
        stones.setListStonesColorRivel(PlayActivity.movesGame.getHashMapBord().get(uidRivel));
        board.setStones(stones);

        MovesGame movesGameMiddel = PlayActivity.movesGame;

        if (movesGameMiddel.getTournUid().equals(uidMe)) {//my tourn
            switch (movesGameMiddel.getType()) {
                case "dices":
                    board.setImage(movesGameMiddel.getDice1(), imageViewDice1);
                    board.setImage(movesGameMiddel.getDice2(), imageViewDice2);
                    board.setDice1(movesGameMiddel.getDice1());
                    board.setDice2(movesGameMiddel.getDice2());
                    if (movesGameMiddel.getDice1() == movesGameMiddel.getDice2()) {
                        board.setDice3(movesGameMiddel.getDice1());
                        board.setDice4(movesGameMiddel.getDice2());
                    }
                    textViewDice.setText("תורך: אנא הוזז אבן");
                    board.chesSton();
                    break;
                case "onlyMoveSton":
                    textViewDice.setText("תורך: אנא הוזז אבן");

                    if (movesGameMiddel.getDice1() != -100) {
                        board.setImage(movesGameMiddel.getDice1(), imageViewDice1);
                        imageViewDice2.setVisibility(View.INVISIBLE);
                        board.setDice1(movesGameMiddel.getDice1());
                    } else if (movesGameMiddel.getDice4() == -100) {
                        board.setImage(movesGameMiddel.getDice1(), imageViewDice2);
                        imageViewDice1.setVisibility(View.INVISIBLE);
                    } else {
                        board.setImage(movesGameMiddel.getDice4(), imageViewDice1);
                        board.setImage(movesGameMiddel.getDice4(), imageViewDice2);
                    }
                    board.setDice1(movesGameMiddel.getDice1());
                    board.setDice2(movesGameMiddel.getDice2());
                    board.setDice3(movesGameMiddel.getDice3());
                    board.setDice4(movesGameMiddel.getDice4());
                    board.chesSton();

                    break;
                case "MoveStonAndEndTurn":
                    board.getTimer().stop();
                    if (movesGameMiddel.getLose())
                        textViewDice.setText("הפסדת");
                    else {
                        board.getTimer().start("תורך");
                        textViewDice.setText("תורך: זרוק קוביות");
                        board.setClick2Dices();
                        board.moveTowDice();
                    }
                    break;
                default:
                    break;
            }
        }
        else{
            switch (movesGameMiddel.getType()) {
                case "dices":
                    board.setImage(movesGameMiddel.getDice1(), imageViewDice1);
                    board.setImage(movesGameMiddel.getDice2(), imageViewDice2);
                    board.setDice1(movesGameMiddel.getDice1());
                    board.setDice2(movesGameMiddel.getDice2());
                    if (movesGameMiddel.getDice1() == movesGameMiddel.getDice2()) {
                        board.setDice3(movesGameMiddel.getDice1());
                        board.setDice4(movesGameMiddel.getDice2());
                    }
                    textViewDice.setText("תור השני אנא המתן");
                    break;
                case "onlyMoveSton":
                    textViewDice.setText("תור השני אנא המתן");

                    if (movesGameMiddel.getDice1() != -100) {
                        board.setImage(movesGameMiddel.getDice1(), imageViewDice1);
                        imageViewDice2.setVisibility(View.INVISIBLE);
                        board.setDice1(movesGameMiddel.getDice1());
                    } else if (movesGameMiddel.getDice4() == -100) {
                        board.setImage(movesGameMiddel.getDice1(), imageViewDice2);
                        imageViewDice1.setVisibility(View.INVISIBLE);
                    } else {
                        board.setImage(movesGameMiddel.getDice4(), imageViewDice1);
                        board.setImage(movesGameMiddel.getDice4(), imageViewDice2);
                    }


                    break;
                case "MoveStonAndEndTurn":
                    board.getTimer().stop();
                    if (movesGameMiddel.getLose())
                        textViewDice.setText("ניצחת");
                    else {
                        board.getTimer().start("תור השני");
                        textViewDice.setText("תור השני: אנא המתן");
                        board.moveTowDice();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
