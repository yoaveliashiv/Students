package com.code.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class PlayActivity extends AppCompatActivity {
    private String keyWait;
    private Game game2;
private int flagStart=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Button button = findViewById(R.id.button_play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);
//
//                startActivity(intent);
//                return;
                ProgressDialog progressDialog=new ProgressDialog(PlayActivity.this);
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
                                            ValueEventListener valueEventListener=new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(flagStart==1) {
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
                                    DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
                                            .getReference("Play backgammon").child(game2.getUid());
                                    databaseReference5.setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
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
        });
    }
}