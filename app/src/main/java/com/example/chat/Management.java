package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Management extends AppCompatActivity {
    EditText editTextFrom;
    Button buttonDo;
    EditText editTexto;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        editTexto = findViewById(R.id.editTextGrohp_To);
        listView = findViewById(R.id.list_management);
        editTextFrom = findViewById(R.id.editTextGrope_From);
        buttonDo = findViewById(R.id.buttonDo_Grohp);
        editTexto.setVisibility(View.GONE);
        editTextFrom.setVisibility(View.GONE);
        buttonDo.setVisibility(View.GONE);
    }

    private void makeGroup() {
        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTextFrom.setText("אריאל");
        editTexto.setHint("ל-");
        buttonDo.setText("הוסף קבוצה");
        final ArrayList<String> arrayListGroups = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Management.this,
                android.R.layout.simple_list_item_1, arrayListGroups);
        listView.setAdapter(arrayAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayListGroups.add(child.getKey());
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });
        editTextFrom.setText("אריאל");
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("NamesGroups");

                cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String from = editTextFrom.getText().toString();
                        String to = editTexto.getText().toString();
                        Boolean flagExsis = false;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String grohp = child.getKey();
                            if (("" + from + "-" + to).equals(grohp) || to.length() < 3)
                                flagExsis = true;
                            editTexto.setError("הקבוצה קיימת או קצרה");
                            editTexto.requestFocus();
                        }
                        if (!flagExsis) {
                            DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("NamesGroups");
                            cardRef2.child("" + from + "-" + to).setValue("");
                            makeGroup();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void makeLinks() {
        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTextFrom.setText("");
        editTexto.setText("");
        editTextFrom.setHint("שם הקבוצה");
        editTexto.setHint("לינק");
        buttonDo.setText("הוסף לינק");
        final ArrayList<LinksToWhatsApp> arrayListLink = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LinksToWhatsApp");

        final LinksToWhatsAppAdapter linksToWhatsAppAdapter = new LinksToWhatsAppAdapter(Management.this, 0, 0, arrayListLink);

        listView.setAdapter(linksToWhatsAppAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;
                for (DataSnapshot child : snapshot.getChildren()) {
                    LinksToWhatsApp linksToWhatsApp = new LinksToWhatsApp();
                    linksToWhatsApp.setNameGroup(child.getKey());
                    linksToWhatsApp.setLink(child.getValue(String.class));
                    arrayListLink.add(linksToWhatsApp);
                }

                linksToWhatsAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String from = editTextFrom.getText().toString();
                String to = editTexto.getText().toString();


                if (from.length() < 5) {
                    editTexto.setError("הקבוצה  קצרה");
                    editTexto.requestFocus();
                    return;
                }

                DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("LinksToWhatsApp");
                cardRef2.child(from).setValue(to);
                makeLinks();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // if (flagConnected) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_management, menu);
        return true;
        //   }
        //   return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {

            case R.id.mainIconMenu:
                intent2 = new Intent(Management.this, MainActivity3.class);
                startActivity(intent2);
                return true;

            case R.id.groups:
                makeGroup();
                return true;
            case R.id.link_groups:
                makeLinks();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}