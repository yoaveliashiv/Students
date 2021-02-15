package com.code.game;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.code.R;
import com.code.chat.FeedbackChat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayActivity2 extends AppCompatActivity {
    private String keyWait;
    private Game game2;
    private MovesGame movesGame4 = null;
    protected static MovesGame movesGame = null;
    private String uid;
    private List<Player> arrayListPlayer;
    private ArrayList<String> arrayListIAmRequests;
    private ListView listViewPlayer;
    private PlayerAdapter arrayAdapterPlayer;
    private List<Player> arrayListGameRequests;
    private ListView listViewGameRequests;
    private PlayerAdapter arrayAdapterGameRequests;
    private TextView textViewGameRequests;
    private Player playerI;
    protected static Microphone microphone;
    private int flagStart = 0;
    private DatabaseReference databaseReferenceGameRequest;
    private DatabaseReference databaseReferencePlayerOnline;
    private DatabaseReference databaseReferenceI;
    private ValueEventListener valueEventListenerOpenGame;
    private ChildEventListener childEventListenerGameRequests;
    private ChildEventListener childEventListenerPlayer;

    private Boolean flagPlayerOut = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
arrayListIAmRequests=new ArrayList<>();
        setContentView(R.layout.activity_play2);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        premisonAodio();

        microphoneSet();

        chakIfInMideelGame();


        super.onCreate(savedInstanceState);

    }

    private void build() {
        arrayListIAmRequests = new ArrayList<>();
        databaseReferenceI = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReferencePlayerOnline = FirebaseDatabase.getInstance()
                .getReference("ListNamePlayerOnlineBackgammonBoard");
        databaseReferenceGameRequest = FirebaseDatabase.getInstance()
                .getReference("GameRequestsׂׂBackgammonBoard").child(uid);


        openGameListener();
        lisnerNamePlayer();
        dialogNameLogin();
        listenerGameRequestsׂׂ();
        onClickListsItemes();


        Button button = findViewById(R.id.button_play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(PlayActivity2.this);
                progressDialog.setMessage("מחפש שחקן..");
                progressDialog.show();
              //  progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setCancelable(true);
progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
    @Override
    public void onCancel(DialogInterface dialogInterface) {
        DatabaseReference databaseReference7 = FirebaseDatabase.getInstance()
                .getReference("Waiting to play backgammon").child(uid);
        databaseReference7.removeValue();
    }
});
//                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        DatabaseReference databaseReference7 = FirebaseDatabase.getInstance()
//                                .getReference("Waiting to play backgammon").child(uid);
//                        databaseReference7.removeValue();
//                    }
//                });

                contiuo(uid, button, progressDialog);
            }
        });
    }

    private void chakIfInMideelGame() {
        DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    build();
                } else {

                    Intent intent = new Intent(PlayActivity2.this, BackgammonActivity.class);
                    intent.putExtra("middelGame", true);
                    try {
                        movesGame = snapshot.getValue(MovesGame.class);
                        flagPlayerOut=false;//onStop
                        startActivity(intent);

                    }

                    catch (DatabaseException e){
                        build();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void microphoneSet() {
        DatabaseReference databaseR = FirebaseDatabase.getInstance().getReference("Microphone");

        databaseR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    microphone = snapshot.getValue(Microphone.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGameListener() {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                .getReference("Play backgammon").child(uid);
        databaseReference2.setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                valueEventListenerOpenGame = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (flagStart == 1) {
                            movesGame4 = snapshot.getValue(MovesGame.class);
                            if (movesGame4.getUidPrimery().equals(uid)) {
                                removeAll();
                                Intent intent = new Intent(PlayActivity2.this, BackgammonActivity.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("color", "white");
                                startActivity(intent);
                            }

                        }

                        flagStart++;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                databaseReferenceI.addValueEventListener(valueEventListenerOpenGame);
            }
        });

    }

    private void onClickListsItemes() {
        listViewPlayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final Player player = arrayListPlayer.get(i);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("GameRequestsׂׂBackgammonBoard")
                        .child(player.getUid()).child(uid);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                                .getReference("GameRequestsׂׂBackgammonBoard")
                                .child(player.getUid()).child(uid);
                        if (snapshot.exists()) {
                            databaseReference1.removeValue();
                            arrayListIAmRequests.remove(player.getUid());
                            view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        } else {

                            textViewGameRequests.setVisibility(View.VISIBLE);
                            databaseReference1.setValue(playerI);
                            arrayListIAmRequests.add(player.getUid());
                            view.setBackgroundColor(getResources().getColor(R.color.colorWarng));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        listViewGameRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Player player = arrayListGameRequests.get(i);
                removeAll();

                MovesGame movesGame2 = new MovesGame();
                HashMap<String, String> hashMapColor = new HashMap<>();
                hashMapColor.put(uid, "black");
                hashMapColor.put(player.getUid(), "white");
                movesGame2.setHashMapColor(hashMapColor);

                HashMap<String, String> hashMapUid = new HashMap<>();
                hashMapUid.put(uid, player.getUid());
                hashMapUid.put(player.getUid(), uid);
                movesGame2.setHashMapUid(hashMapUid);
                movesGame2.setUidPrimery(player.getUid());
                movesGame2.setType("start");
                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
                        .getReference("Play backgammon").child(player.getUid());
                databaseReference5.setValue(movesGame2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference databaseReference12 = FirebaseDatabase.getInstance()
                                .getReference("Play backgammon").child(uid);
                        databaseReference12.setValue(movesGame2);
                        removeAll();
                        Intent intent = new Intent(PlayActivity2.this, BackgammonActivity.class);
                        intent.putExtra("uid", player.getUid());
                        intent.putExtra("color", "black");

                        startActivity(intent);
                    }
                });
            }
        });

    }


    private void listenerGameRequestsׂׂ() {
        arrayListGameRequests = new ArrayList<>();
        listViewGameRequests = findViewById(R.id.list_view_game_requestsׂׂ);
        textViewGameRequests = findViewById(R.id.textViewGameRequestsׂׂ);
        // textViewGameRequests.setVisibility(View.GONE);
        arrayAdapterGameRequests = new PlayerAdapter(PlayActivity2.this,
                0, 0, arrayListGameRequests);
        listViewGameRequests.setAdapter(arrayAdapterGameRequests);

        childEventListenerGameRequests = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Player player = snapshot.getValue(Player.class);
                    arrayListGameRequests.add(snapshot.getValue(Player.class));
                    arrayAdapterGameRequests.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    Player player = snapshot.getValue(Player.class);
//
//                    arrayListGameRequests.remove(player);
//                    arrayAdapterGameRequests.notifyDataSetChanged();
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //          Player player = snapshot.getValue(Player.class);
//textViewGameRequests.setText(snapshot.getKey());
                    for (Player child : arrayListGameRequests) {
                        if (child.getUid().equals(snapshot.getKey())) {
                            arrayListGameRequests.remove(child);
                            break;
                        }
                    }
                    arrayAdapterGameRequests.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    Player player = snapshot.getValue(Player.class);
//
//                    arrayListGameRequests.remove(player);
//                    arrayAdapterGameRequests.notifyDataSetChanged();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        };
        databaseReferenceGameRequest.addChildEventListener(childEventListenerGameRequests);
    }

    private void lisnerNamePlayer() {
        arrayListPlayer = new ArrayList<>();
        listViewPlayer = findViewById(R.id.list_view_player);

        arrayAdapterPlayer = new PlayerAdapter(PlayActivity2.this,
                0, 0, arrayListPlayer);
        listViewPlayer.setAdapter(arrayAdapterPlayer);

        childEventListenerPlayer = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Player player = snapshot.getValue(Player.class);
                    if (!player.getUid().equals(uid)) {
                        arrayListPlayer.add(snapshot.getValue(Player.class));
                        arrayAdapterPlayer.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    arrayListPlayer.remove(snapshot.getValue(Player.class));
//                    arrayAdapterPlayer.notifyDataSetChanged();
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //    Player player = snapshot.getValue(Player.class);

                    for (Player child : arrayListPlayer) {
                        if (child.getUid().equals(snapshot.getKey())) {
                            arrayListPlayer.remove(child);
                            break;
                        }
                    }
                    arrayAdapterPlayer.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    arrayListPlayer.remove(snapshot.getValue(Player.class));
//                    arrayAdapterPlayer.notifyDataSetChanged();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReferencePlayerOnline.addChildEventListener(childEventListenerPlayer);


    }

    private void dialogNameLogin() {
        final Dialog d2 = new Dialog(this);
        d2.setContentView(R.layout.dialog_game_name_player);
        d2.setTitle("Manage");

        d2.setCancelable(false);
        EditText editText = d2.findViewById(R.id.editTextText_name_player);
        Button button = d2.findViewById(R.id.button_name_plyer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (text.isEmpty()) {
                    editText.setError("אנא הכנס שם");
                    editText.requestFocus();
                    return;
                }
                for (Player player : arrayListPlayer) {
                    if (player.getName().equals(text)) {
                        editText.setError("שם כבר קיים, בחר שם חדש");
                        editText.requestFocus();
                        return;
                    }

                }


                playerI = new Player(uid, text);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                        .getReference("ListNamePlayerOnlineBackgammonBoard").child(uid);
                databaseReference1.setValue(playerI);
                d2.dismiss();

            }
        });


        d2.show();
    }


    private void contiuo(String uid, Button button, ProgressDialog progressDialog) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Waiting to play backgammon");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                            .getReference("Waiting to play backgammon").child(uid);
                    Game game = new Game();
                    game.setUid(uid);
                    databaseReference1.setValue(game);

                } else {
                    game2 = new Game();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        game2 = child.getValue(Game.class);
                        break;
                    }
                    DatabaseReference databaseReference7 = FirebaseDatabase.getInstance()
                            .getReference("Waiting to play backgammon").child(game2.getUid());
                    databaseReference7.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            MovesGame movesGame2 = new MovesGame();
                            HashMap<String, String> hashMapColor = new HashMap<>();
                            hashMapColor.put(uid, "black");
                            hashMapColor.put(game2.getUid(), "white");
                            movesGame2.setHashMapColor(hashMapColor);

                            HashMap<String, String> hashMapUid = new HashMap<>();
                            hashMapUid.put(uid, game2.getUid());
                            hashMapUid.put(game2.getUid(), uid);
                            movesGame2.setHashMapUid(hashMapUid);
                            movesGame2.setUidPrimery(game2.getUid());
                            movesGame2.setType("start");
                            DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
                                    .getReference("Play backgammon").child(game2.getUid());
                            databaseReference5.setValue(movesGame2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference databaseReference12 = FirebaseDatabase.getInstance()
                                            .getReference("Play backgammon").child(uid);
                                    databaseReference12.setValue(movesGame2);
                                    button.setText("עובד");
                                    Intent intent = new Intent(PlayActivity2.this, BackgammonActivity.class);
                                    intent.putExtra("uid", game2.getUid());
                                    intent.putExtra("color", "black");

                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void premisonAodio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!canAccessLocation()) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }
        }
    }

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.RECORD_AUDIO,
    };

    private static final int INITIAL_REQUEST = 1337;


// add these lines in your onCreate method


    // implemente these methods
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.RECORD_AUDIO));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!flagPlayerOut) {
            Intent intent = new Intent(PlayActivity2.this, PlayActivity2.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (flagPlayerOut) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference("GameRequestsׂׂBackgammonBoard");
            for (String uidR : arrayListIAmRequests) {
                databaseReference.child(uidR).child(uid).removeValue();
            }
            databaseReferencePlayerOnline.removeEventListener(childEventListenerPlayer);
            databaseReferenceGameRequest.removeEventListener(childEventListenerGameRequests);
            databaseReferenceI.removeEventListener(valueEventListenerOpenGame);
            flagPlayerOut = false;
            databaseReferencePlayerOnline.child(uid).removeValue();
            if (movesGame == null && movesGame4 == null) {
                databaseReferenceI.removeValue();

            }
            DatabaseReference databaseReference7 = FirebaseDatabase.getInstance()
                    .getReference("Waiting to play backgammon").child(uid);
            databaseReference7.removeValue();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (flagPlayerOut) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference("GameRequestsׂׂBackgammonBoard");
            for (String uidR : arrayListIAmRequests) {
                databaseReference.child(uidR).child(uid).removeValue();
            }
            databaseReferencePlayerOnline.removeEventListener(childEventListenerPlayer);
            databaseReferenceGameRequest.removeEventListener(childEventListenerGameRequests);
            databaseReferenceI.removeEventListener(valueEventListenerOpenGame);
            flagPlayerOut = false;
            databaseReferencePlayerOnline.child(uid).removeValue();
            if (movesGame == null && movesGame4 == null) {
                databaseReferenceI.removeValue();

            }
            DatabaseReference databaseReference7 = FirebaseDatabase.getInstance()
                    .getReference("Waiting to play backgammon").child(uid);
            databaseReference7.removeValue();
        }

    }

    private void removeAll() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("GameRequestsׂׂBackgammonBoard");
        for (String uidR : arrayListIAmRequests) {
            databaseReference.child(uidR).child(uid).removeValue();
        }
        databaseReferencePlayerOnline.removeEventListener(childEventListenerPlayer);
        databaseReferenceGameRequest.removeEventListener(childEventListenerGameRequests);
        databaseReferenceI.removeEventListener(valueEventListenerOpenGame);
        databaseReferencePlayerOnline.child(uid).removeValue();
        if (movesGame == null && movesGame4 == null) {
            databaseReferenceI.removeValue();

        }
    }


}