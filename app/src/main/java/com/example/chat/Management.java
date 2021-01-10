package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Management extends AppCompatActivity {
    EditText editTextFrom;
    Button buttonDo;
    EditText editTexto;
    Button editTextDate;

    ListView listView;
    Spinner spinnerNameColeg;
    String nameCollege = "";
    String nameCollegeHebrow = "";
    String dateBlock = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        editTexto = findViewById(R.id.editTextGrohp_To);
        listView = findViewById(R.id.list_management);
        editTextFrom = findViewById(R.id.editTextGrope_From);
        buttonDo = findViewById(R.id.buttonDo_Grohp);
        spinnerNameColeg = findViewById(R.id.spinner_name_cologe);
        editTextDate = findViewById(R.id.editTextGrohp_cose);
        spinnerNameColeg.setVisibility(View.GONE);
        editTexto.setVisibility(View.GONE);
        editTextFrom.setVisibility(View.GONE);
        editTextDate.setVisibility(View.GONE);

        buttonDo.setVisibility(View.GONE);

        setSpinnerNaemeColge();

    }

    private void makeGroup() {
        if (nameCollege.isEmpty()) {
            spinnerNameColeg.setVisibility(View.VISIBLE);
            buttonDo.setText("אישור");
            buttonDo.setVisibility(View.VISIBLE);

            buttonDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nameCollege.isEmpty() || nameCollege.equals("בחר אוניברסיטה")) {

                        return;
                    }
                    continueMakeGroup();
                }
            });
        } else
            continueMakeGroup();

    }

    private void continueMakeGroup() {
        editTextDate.setVisibility(View.GONE);

        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTexto.setText("");

        editTextFrom.setText("אריאל");
        editTexto.setHint("ל-");
        buttonDo.setText("הוסף קבוצה");
        final ArrayList<String> arrayListGroups = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("NamesGroups").child(nameCollege);

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
        editTextFrom.setText(nameCollegeHebrow);
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cardRef = FirebaseDatabase.getInstance()
                        .getReference("NamesGroups").child(nameCollege);

                cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String from = editTextFrom.getText().toString();
                        String to = editTexto.getText().toString();
                        Boolean flagExsis = false;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String grohp = child.getKey();
                            if (("" + from + "-" + to).equals(grohp) || to.length() < 3) {
                                flagExsis = true;
                                editTexto.setError("הקבוצה קיימת או קצרה");
                                editTexto.requestFocus();
                            }
                        }
                        if (!flagExsis) {
                            DatabaseReference cardRef2 = FirebaseDatabase.getInstance()
                                    .getReference("NamesGroups").child(nameCollege);
                            cardRef2.child("" + from + "-" + to).setValue("");
                            Toast.makeText(Management.this, "קבוצה נוספה בהצלחה", Toast.LENGTH_SHORT).show();
                            continueMakeGroup();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void setSpinnerNaemeColge() {

        final ArrayList<String> arrayListNameSpinnerHebrow = new ArrayList<>();
        final ArrayList<String> arrayListNameSpinnerEnglish = new ArrayList<>();
        arrayListNameSpinnerEnglish.add("בחר אוניברסיטה");
        arrayListNameSpinnerHebrow.add("בחר אוניברסיטה");

        final ArrayAdapter<String> nameClogeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayListNameSpinnerHebrow);
        spinnerNameColeg.setAdapter(nameClogeAdapter);
        spinnerNameColeg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nameCollege = arrayListNameSpinnerEnglish.get(i);
                nameCollegeHebrow = arrayListNameSpinnerHebrow.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("NamesColleges");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayListNameSpinnerEnglish.add(child.getKey());
                    arrayListNameSpinnerHebrow.add(child.getValue(String.class));

                }
                nameClogeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void makeLinks() {
        if (nameCollege.isEmpty()) {

            spinnerNameColeg.setVisibility(View.VISIBLE);
            buttonDo.setText("אישור");
            buttonDo.setVisibility(View.VISIBLE);

            buttonDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nameCollege.isEmpty() || nameCollege.equals("בחר אוניברסיטה")) {

                        return;
                    }
                    continueMakeLinks();
                }
            });
        } else
            continueMakeLinks();

    }

    private void continueMakeLinks() {
        editTextDate.setVisibility(View.GONE);

        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTextFrom.setText("");
        editTexto.setText("");
        editTextFrom.setHint("שם הקבוצה");
        editTexto.setHint("לינק");
        buttonDo.setText("הוסף לינק");
        final ArrayList<LinksToWhatsApp> arrayListLink = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("LinksToWhatsApp").child(nameCollege);

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

                DatabaseReference cardRef2 = FirebaseDatabase.getInstance()
                        .getReference("LinksToWhatsApp").child(nameCollege);
                cardRef2.child(from).setValue(to);
                Toast.makeText(Management.this, "לינק נוסף בהצלחה", Toast.LENGTH_SHORT).show();

                continueMakeLinks();


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
            case R.id.new_cologe:
                newCologe();
                return true;
            case R.id.groups:
                makeGroup();
                return true;
            case R.id.link_groups:
                makeLinks();
                return true;
            case R.id.to_block:
                blockMenge();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void blockMenge() {
        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTextDate.setVisibility(View.VISIBLE);
        editTextFrom.setText("");
        editTexto.setText("");
        editTexto.setHint("סיבה");
        editTextFrom.setHint("מספר פלאפון");
        buttonDo.setText("חסום");
        setDate();
        final ArrayList<String> arrayListBlocked = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Blocked");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Management.this,
                android.R.layout.simple_list_item_1, arrayListBlocked);
        listView.setAdapter(arrayAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Blocked blocked = child.getValue(Blocked.class);
                    arrayListBlocked.add(blocked.getPhone() + ": " + blocked.getDate()
                            + " - " + blocked.getCause_of_blockage());
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
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = editTextFrom.getText().toString();
                String date = editTextDate.getText().toString();
                String cose = editTexto.getText().toString();
                if (phone.length() < 9) {
                    editTextFrom.setError("פלאפון קצר מדי");
                    editTextFrom.requestFocus();
                    return;
                }
                if (date.length() < 6) {
                    editTextDate.setError("תאריך קצר מדי");
                    editTextDate.requestFocus();
                    return;
                }
                Blocked blocked = new Blocked();
                blocked.setDate(date);
                blocked.setPhone(phone);
                blocked.setCause_of_blockage(cose);
                DatabaseReference cardRef2 = FirebaseDatabase.getInstance()
                        .getReference("Blocked").child(phone);
                cardRef2.setValue(blocked).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management.this, "חסימה בוצעה בהצלחה", Toast.LENGTH_SHORT).show();

                        blockMenge();
                    }
                });
            }


        });

    }

    private void newCologe() {
        editTextDate.setVisibility(View.GONE);

        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTextFrom.setText("");
        editTexto.setText("");
        editTexto.setHint("אוניברסיטה באנגלית");
        editTextFrom.setHint("אוניברסיטה בעברית");
        buttonDo.setText("הוסף אוניברסיטה");
        final ArrayList<String> arrayListColge = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NamesColleges");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Management.this,
                android.R.layout.simple_list_item_1, arrayListColge);
        listView.setAdapter(arrayAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayListColge.add(child.getKey() + " - " + child.getValue(String.class));
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
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameHebrow = editTextFrom.getText().toString();
                String nameEnglish = editTexto.getText().toString();
                if (nameEnglish.length() < 3) {
                    editTexto.setError("שם קצר מדי");
                    editTexto.requestFocus();
                    return;
                }
                if (nameHebrow.length() < 3) {
                    editTextFrom.setError("שם קצר מדי");
                    editTextFrom.requestFocus();
                    return;
                }

                DatabaseReference cardRef2 = FirebaseDatabase.getInstance()
                        .getReference("NamesColleges").child(nameEnglish);
                cardRef2.setValue(nameHebrow).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management.this, "אוניברסיטה נוספה בהצלחה", Toast.LENGTH_SHORT).show();

                        newCologe();
                    }
                });
            }


        });
    }

    private void setDate() {

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        // MaterialDatePicker.Builder builder=MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("בחר תאריך");
        final MaterialDatePicker materialDatePicker = builder.build();

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair selectedDates = (Pair) materialDatePicker.getSelection();
//              then obtain the startDate & endDate from the range
                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//              assigned variables
                Date startDate = rangeDate.first;
//              Format the dates in ur desired display mode

                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//              Display it by setText
                dateBlock = simpleFormat.format(startDate).toString();


               editTextDate.setText(dateBlock);
            }
        });



    }

}