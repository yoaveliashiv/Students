package com.code.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.R;
import com.code.chat.MainActivity3;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class BackgammonActivity extends AppCompatActivity {
    private ImageView buttonMic;
    private boolean flagCall = true;
    private boolean flagCallBlakStart = true;
    protected static Context contextGame;
    private SinchClient sinchClient;
    private Call call = null;
    private DatabaseReference databaseRef;
    private ValueEventListener valueEventListener;
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
    protected static int flagListener = 0;
    private boolean flagExsit = false;
    private BackgammonBoard board;
    private HashMap<ImageView, Integer> hashMapIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextGame=this;
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
        calling();
        super.onCreate(savedInstanceState);
    }

    private void eXsitCallingFriend() {
    }


    private void setOnListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (flagListener > 0 && snapshot.exists()) {
                    MovesGame movesGame;
                    try {
                         movesGame = snapshot.getValue(MovesGame.class);

                    }
                   catch (DatabaseException e){
                        return;
                   }
                    moves(movesGame);
                } else if (snapshot.exists()) {
                    flagListener++;
                    MovesGame movesGame;
                    try {
                        movesGame = snapshot.getValue(MovesGame.class);

                    }
                    catch (DatabaseException e){
                        return;
                    }

                    uidRivel = movesGame.getHashMapUid().get(uidMe);


                } else {
                    flagListener++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseRef = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseRef.addValueEventListener(valueEventListener);
    }

    private void moves(MovesGame movesGame) {

        if (!movesGame.getInfoTo().contains(color))
            return;

        switch (movesGame.getType()) {
            case "start1":
                start1(movesGame);
                break;
            case "start2":

                start2(movesGame);
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
                if (movesGame.getLose()) {
                    textViewDice.setText("הפסדת");
                    DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(uidMe);
                    databaseReference9.removeValue();
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(uidRivel);
                    databaseReference3.removeValue();
                    flagListener = 0;
                } else {
                    board.getTimer().start("תורך");
                    textViewDice.setText("תורך: זרוק קוביות");
                    board.setClick2Dices();
                    board.moveTowDice();
                }
                break;//"onlyMoveSton"
            case "exsit":
                databaseRef.removeEventListener(valueEventListener);

                flagListener = 0;
                flagExsit = true;
                board.setCancalClick();
                imageViewDice1.setClickable(false);
                imageViewDice2.setClickable(false);
                board.getTimer().stop();
                textViewDice.setText("ניצחת: השני יצא מהמשחק");
                DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                        .getReference("Play backgammon").child(uidMe);
                databaseReference9.removeValue();
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                        .getReference("Play backgammon").child(uidRivel);
                databaseReference3.removeValue();
                break;
            default:
                break;
        }
    }

    private void start2(MovesGame movesGame) {
        new Thread() {

            public void run() {
                if (color.equals("white")) {
                    uidRivel = movesGame.getSendUidToRive();
                    board.flagDiceTrowe();
                    board.setImage(movesGame.getDice1(), imageViewDice1);

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

    }

    private void start1(MovesGame movesGame) {
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
                if(!board.conction(BackgammonActivity.contextGame)) {
                    textViewDice.setText("אין קליטה");
                    textViewDice.setError("אין קליטה");
                    textViewDice.requestFocus();
                    return;
                }
                textViewDice.clearFocus();

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                        .getReference("Play backgammon").child(uid);
                imageViewDice2.setClickable(false);
                board.flagDiceTrowe();
                int numDice = board.roolOneDice(imageViewDice2);
                MovesGame movesGame2 = new MovesGame();
                movesGame2.setDice1(movesGame.getDice1());
                movesGame2.setDice2(numDice);
                HashMap<String, String> hashMapUid = new HashMap<>();
                hashMapUid.put(BackgammonActivity.uidMe, BackgammonActivity.uidRivel);
                hashMapUid.put(BackgammonActivity.uidRivel, BackgammonActivity.uidMe);
                movesGame2.setHashMapUid(hashMapUid);
                HashMap<String, List<Integer>> hashMapBord = new HashMap<>();
                hashMapBord.put(BackgammonActivity.uidMe, board.getStones().getListStonesColor());
                hashMapBord.put(BackgammonActivity.uidRivel, board.getStones().getListStonesColorRivel());

                HashMap<String, String> hashMapColor = new HashMap<>();
                hashMapColor.put(BackgammonActivity.uidMe, color);
                hashMapColor.put(BackgammonActivity.uidRivel, board.rivalColor);

                movesGame2.setUidPrimery(uid);

                movesGame2.setHashMapColor(hashMapColor);
                movesGame2.setHashMapBord(hashMapBord);
                movesGame2.setType("start2");
                movesGame2.setSendUidToRive(uidMe);
                board.setCountMove(board.getCountMove() + 1);
                movesGame2.setCountMove(board.getCountMove());
                movesGame2.setTournUid(uidMe);
                if (numDice == movesGame.getDice1()) {


                    movesGame2.setType("equal");
                    movesGame2.setInfoTo("black white");
                    databaseReference3.setValue(movesGame2);
                    return;
                }

                movesGame2.setInfoTo("black white");

                String uidOut;
                if (uid.equals(BackgammonActivity.uidMe))
                    uidOut = BackgammonActivity.uidRivel;
                else
                    uidOut = BackgammonActivity.uidMe;
                DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                        .getReference("Play backgammon").child(uidOut);
                databaseReference9.setValue(movesGame2);

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

        buttonMic = findViewById(R.id.imageView_mic);
        buttonMic.setImageResource(R.drawable.microphone_off);
        setBottunExit();

    }


    private void buildBord() {
        MovesGame movesGameMiddel = PlayActivity2.movesGame;
        color = movesGameMiddel.getHashMapColor().get(uidMe);
        uid = movesGameMiddel.getUidPrimery();
        uidRivel = movesGameMiddel.getHashMapUid().get(uidMe);
        if (movesGameMiddel.getType().equals("start")) {

            return;
        }


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
                textViewWhiteOut.setText("" + movesGameMiddel.getHashMapBord().get(uidRivel).get(24));
                textViewWhiteOut.setVisibility(View.VISIBLE);
            } else {
                imageViewBlackOut.setImageResource(R.drawable.black);
                imageViewBlackOut.setVisibility(View.VISIBLE);
                textViewBlackOut.setText("" + movesGameMiddel.getHashMapBord().get(uidRivel).get(24));
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
                textViewWhiteWin.setText("" + movesGameMiddel.getHashMapBord().get(uidRivel).get(25));
                textViewWhiteWin.setVisibility(View.VISIBLE);
            } else {
                imageViewBlackWin.setImageResource(R.drawable.black);
                imageViewBlackWin.setVisibility(View.VISIBLE);
                textViewBlackWin.setText("" + movesGameMiddel.getHashMapBord().get(uidRivel).get(25));
                textViewBlackWin.setVisibility(View.VISIBLE);
            }
        }


    }

    private void continiouBuild() {
        MovesGame movesGameMiddel = PlayActivity2.movesGame;

        if (movesGameMiddel.getType().equals("start")) {
            board.build();
            board.moveDice(imageViewDice1);
            return;
        }
        Stones stones = new Stones();
        stones.setListStonesColor(PlayActivity2.movesGame.getHashMapBord().get(uidMe));
        stones.setListStonesColorRivel(PlayActivity2.movesGame.getHashMapBord().get(uidRivel));
        board.setStones(stones);


        if (movesGameMiddel.getTournUid().equals(uidMe)) {//my tourn
            switch (movesGameMiddel.getType()) {
                case "start1":
                    start1(movesGameMiddel);

                    break;
                case "start2":
                    start2(movesGameMiddel);

                    break;
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
                        board.setImage(movesGameMiddel.getDice2(), imageViewDice2);
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
        } else {
            switch (movesGameMiddel.getType()) {
                case "start1":
                    board.setImage(movesGameMiddel.getDice1(), imageViewDice1);
                    board.moveDice(imageViewDice2);
                    textViewDice.setText("תור השני: שיזרוק מי מתחיל");

                    break;
                case "start2":
                    start2(movesGameMiddel);

                    break;
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
                        board.setImage(movesGameMiddel.getDice2(), imageViewDice2);
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

    private void setBottunExit() {
        Button button = findViewById(R.id.button_exsit_play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!board.conction(BackgammonActivity.contextGame)) {
                    textViewDice.setText("אין קליטה");
                    textViewDice.setError("אין קליטה");
                    textViewDice.requestFocus();
                    return;
                }
                textViewDice.clearFocus();

                if (flagExsit) {
                    Intent intent = new Intent(BackgammonActivity.this, PlayActivity2.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (call != null)
                        call.hangup();
                    textViewDice.setText("הפסדת");
                    MovesGame movesGame3 = new MovesGame();
                    movesGame3.setType("exsit");
                    movesGame3.setInfoTo(board.rivalColor);
                    DatabaseReference databaseReference11 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(uid);
                    databaseReference11.setValue(movesGame3).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DatabaseReference databaseReference9 = FirebaseDatabase.getInstance()
                                    .getReference("Play backgammon").child(uidMe);
                            databaseReference9.removeValue();
                            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                                    .getReference("Play backgammon").child(uidRivel);
                            databaseReference3.removeValue();
                            databaseRef.removeEventListener(valueEventListener);

                            Intent intent = new Intent(BackgammonActivity.this, PlayActivity2.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            }
        });
    }


    private void calling() {
        sinchClient = Sinch.getSinchClientBuilder().
                context(this).
                userId(uidMe).
                applicationKey(PlayActivity2.microphone.getKey())
                .applicationSecret(PlayActivity2.microphone.getSecret())
                .environmentHost(PlayActivity2.microphone.getHostname())
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new CallClientListener() {
            @Override
            public void onIncomingCall(CallClient callClient, Call callStart) {
                if (flagCallBlakStart && color.equals("black")) {
                  //  buttonMic.setImageResource(R.drawable.microphone_on);
                    flagCallBlakStart = false;
                }
                if (flagCall) {
                    buttonMic.setImageResource(R.drawable.microphone_on);

                    call = callStart;
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                } else {
                    call = callStart;
                    call.hangup();
                }
            }
        });
        sinchClient.start();
//        if (getIntent().hasExtra("middelGame")) {
//            call = sinchClient.getCallClient().callUser(uidRivel);
//            call.addCallListener(new SinchCallListener());
//            buttonMic.setImageResource(R.drawable.microphone_on);
//            flagCall = true;
//        }
         if (color.equals("white")||getIntent().hasExtra("middelGame")) {
            new Thread() {
                public void run() {

                    try {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (call == null) {
                                    flagCallBlakStart = false;
                                    call = sinchClient.getCallClient().callUser(uidRivel);
                                    call.addCallListener(new SinchCallListener());
                                    flagCall = true;
                                    //  call.hangup();
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
        new Thread() {
            public void run() {

                try {
                    Thread.sleep(5000);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            buttonMic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!board.conction(BackgammonActivity.this)) {
                                        textViewDice.setText("אין קליטה");
                                        textViewDice.setError("אין קליטה");
                                        textViewDice.requestFocus();
                                        return;
                                    }
                                    textViewDice.clearFocus();

                                    if (flagCall &&call!=null) {
                                        flagCall = false;
                                        buttonMic.setImageResource(R.drawable.microphone_off);
                                        call.hangup();
                                    } else {
                                        if (call == null) {
                                            flagCall = true;
                                            call = sinchClient.getCallClient().callUser(uidRivel);
                                            call.addCallListener(new SinchCallListener());

                                            //  call.hangup();
                                        }
                                    }
                                }
                            });
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call call) {

        }

        @Override
        public void onCallEstablished(Call call) {
            buttonMic.setImageResource(R.drawable.microphone_on);

        }

        @Override
        public void onCallEnded(Call callEnd) {
            call = null;
            callEnd.hangup();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null)
            call.hangup();
        call = null;

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (call == null&&flagCall&& !flagCallBlakStart) {

            call = sinchClient.getCallClient().callUser(uidRivel);
            call.addCallListener(new SinchCallListener());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (call != null)
            call.hangup();
        call = null;
    }
}
