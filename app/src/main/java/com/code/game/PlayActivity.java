package com.code.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Button button = findViewById(R.id.button_play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);

                startActivity(intent);
                return;
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
//                        .getReference("Waiting to play backgammon");
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (!snapshot.exists()) {
//                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
//                                    .getReference("Play backgammon").child(uid);
//                            databaseReference2.setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
//                                            .getReference("Waiting to play backgammon").push();
//                                    keyWait = databaseReference1.getKey();
//                                    databaseReference.setValue(uid).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
//                                                    .getReference("Play backgammon").child(uid);
//                                            databaseReference3.addChildEventListener(new ChildEventListener() {
//                                                @Override
//                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                                }
//
//                                                @Override
//                                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                                    Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);
//                                                    intent.putExtra("uid", uid);
//                                                    startActivity(intent);
//                                                }
//
//                                                @Override
//                                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                                                }
//
//                                                @Override
//                                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            });
//
//                        } else {
//                            for (DataSnapshot child : snapshot.getChildren()) {
//                                final String palyerUid = child.getValue(String.class);
//                                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
//                                        .getReference("Play backgammon").child(palyerUid);
//                                databaseReference5.setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Intent intent = new Intent(PlayActivity.this, BackgammonActivity.class);
//                                        intent.putExtra("uid", palyerUid);
//                                        startActivity(intent);
//                                    }
//                                });
//
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
            }
        });
    }
}