package com.code.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.code.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PlayActivity extends AppCompatActivity {
    private String keyWait;
    private Game game2;
    protected static MovesGame movesGame;
    private int flagStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Button button = findViewById(R.id.button_play);
        premisonAodio();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);
//
//                startActivity(intent);
//                return;
                ProgressDialog progressDialog = new ProgressDialog(PlayActivity.this);
                progressDialog.setMessage("מחפש שחקן..");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                    }
                });
                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                });
                //     progressDialog.setCanceledOnTouchOutside(true);
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//serchIfInMiddelGame
                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
                        .getReference("Play backgammon").child(uid);
                databaseReference5.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            contiuo(uid, button, progressDialog);
                        } else {
                            Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);
                            intent.putExtra("middelGame", true);
                            movesGame = snapshot.getValue(MovesGame.class);
                            startActivity(intent);
                            progressDialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }




    private void contiuo(String uid, Button button, ProgressDialog progressDialog) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Waiting to play backgammon");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                            .getReference("Play backgammon").child(uid);
                    databaseReference2.setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                                    .getReference("Waiting to play backgammon").push();
                            keyWait = databaseReference1.getKey();
                            Game game = new Game();
                            game.setKey(keyWait);
                            game.setUid(uid);
                            databaseReference1.setValue(game).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                                            .getReference("Play backgammon").child(uid);
                                    ValueEventListener valueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (flagStart == 1) {
                                                button.setText("עובד");

                                                Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);
                                                intent.putExtra("uid", uid);
                                                intent.putExtra("color", "white");
                                                startActivity(intent);
                                                progressDialog.dismiss();

                                            }
                                            flagStart++;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };
                                    databaseReference3.addValueEventListener(valueEventListener);

                                }
                            });
                        }
                    });

                } else {
                    game2 = new Game();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        game2 = child.getValue(Game.class);
                        break;
                    }
                    DatabaseReference databaseReference7 = FirebaseDatabase.getInstance()
                            .getReference("Waiting to play backgammon").child(game2.getKey());
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
                                    Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!canAccessLocation()) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }
        }
    }
    private static final String[] INITIAL_PERMS={
            Manifest.permission.RECORD_AUDIO,
    };

    private static final int INITIAL_REQUEST=1337;



// add these lines in your onCreate method



    // implemente these methods
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.RECORD_AUDIO));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
}