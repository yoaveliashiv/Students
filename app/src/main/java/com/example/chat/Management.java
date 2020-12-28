package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Hikers.MainActivityManagementCardsApprov2;
import com.example.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Management extends AppCompatActivity {
    EditText editTextFrom;
    Button buttonDo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        makeGroup();

    }

    private void makeGroup() {
        editTextFrom = findViewById(R.id.editTextGropeFrom);
        editTextFrom.setText("אריאל");
        Button buttonDo=findViewById(R.id.buttonDoGrohp);
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("NamesGroups");

                cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        EditText editTexto = findViewById(R.id.editTextGrohpTo);
                        String from=editTextFrom.getText().toString();
                        String to=editTexto.getText().toString();
                        Boolean flagExsis=false;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String grohp=child.getKey();
                            if (("" + from + "-" + to).equals(grohp)||to.length()<3)
                                flagExsis=true;
                            editTexto.setError("הקבוצה קיימת או קצרה");
                            editTexto.requestFocus();
                        }
                        if(!flagExsis) {
                            DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("NamesGroups");
                            cardRef2.child("" + from + "-" + to).setValue("");
                            Intent intent = new Intent(Management.this, Management.class);
                            startActivityForResult(intent, 0);
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