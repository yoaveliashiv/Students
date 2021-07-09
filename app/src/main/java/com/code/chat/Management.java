package com.code.chat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.code.R;
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
    String blockPhone = "";

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
        if (getIntent().hasExtra("blockPhone")) {
            blockPhone = getIntent().getExtras().getString("blockPhone");
            blockMenge();/**/
        }
        if (getIntent().hasExtra("flagPrice")) {
            nameCollege = getIntent().getExtras().getString("flagPrice");
            nameHebrow("flagPrice");
        }
        if (getIntent().hasExtra("flagGroups")) {
            nameCollege = getIntent().getExtras().getString("flagGroups");
            nameHebrow("flagGroups");
        }
        if (getIntent().hasExtra("flagLinks")) {
            nameCollege = getIntent().getExtras().getString("flagLinks");
            nameHebrow("flagLinks");
        }


        getSupportActionBar().setTitle(nameCollege);

    }

    private void nameHebrow(final String fun) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("NamesColleges").child(nameCollege);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameCollegeHebrow = snapshot.getValue(String.class);
                if (fun.equals("flagPrice"))
                    priceGroups();
                if (fun.equals("flagGroups"))
                    makeGroup();
                if (fun.equals("flagLinks"))
                    makeLinks();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void priceGroups() {
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
                    continuePriceGroups();
                }
            });
        } else
            continuePriceGroups();

    }

    private void continuePriceGroups() {
        editTextDate.setVisibility(View.GONE);

        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTexto.setText("");
        editTexto.setHint("מחיר");
        editTextFrom.setHint("שם קבוצה");
        editTextFrom.setText("");
        buttonDo.setText("הוסף מחיר");
        final ArrayList<String> arrayListGroups = new ArrayList<>();
        final ArrayList<PriceDrive> groups = new ArrayList<>();


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Management.this,
                android.R.layout.simple_list_item_1, arrayListGroups);
        listView.setAdapter(arrayAdapter);

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("GroupsPriceOffer")
                .child(nameCollege);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child2 : snapshot.getChildren()) {
                    String nameGrop = child2.getKey();
                    groups.add(new PriceDrive());
                    arrayListGroups.add("                          שם קבוצה:" + nameGrop);
                    for (DataSnapshot child : child2.getChildren()) {
                        PriceDrive priceDrive = child.getValue(PriceDrive.class);
                        arrayListGroups.add(nameGrop + "  " + priceDrive.getPrice());
                        groups.add(priceDrive);
                    }
                }
                arrayListGroups.add("                            עד כאן הצעות");
                arrayAdapter.notifyDataSetChanged();

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("GroupsPrice");
                databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists())
                            return;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            arrayListGroups.add(child.getKey() + ": " + child.getValue(String.class));
                        }
                        arrayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < groups.size())
                    dialogPrice(i, groups);


            }
        });
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editTextFrom.getText().toString();
                String price = editTexto.getText().toString();
                if (name.isEmpty() || price.isEmpty()) {
                    editTexto.setError("אנא הכנס פרטים תקינים");
                    editTexto.requestFocus();
                    return;
                }
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("GroupsPrice")
                        .child(name);
                databaseReference3.setValue(price).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management.this, "מחיר נוסף בהצלחה", Toast.LENGTH_SHORT).show();
                        continuePriceGroups();
                    }
                });
            }

        });

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
        final ArrayList<Group> groups = new ArrayList<>();


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Management.this,
                android.R.layout.simple_list_item_1, arrayListGroups);
        listView.setAdapter(arrayAdapter);

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("NewGroups")
                .child(nameCollege);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Group group = child.getValue(Group.class);
                    arrayListGroups.add(group.getName());
                    groups.add(group);
                }
                arrayListGroups.add("      עד כאן הצעות של קבוצות");
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("NamesGroups").child(nameCollege);
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
                if (i < groups.size())
                    dialogJoinGrop(i, groups);


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
        editTextFrom.setText(nameCollegeHebrow + "-");
        editTexto.setText("");
        editTextFrom.setHint("שם הקבוצה");
        editTexto.setHint("לינק");
        buttonDo.setText("הוסף לינק");
        final ArrayList<LinksToWhatsApp> arrayListLink = new ArrayList<>();


        final LinksToWhatsAppAdapter linksToWhatsAppAdapter = new LinksToWhatsAppAdapter(Management.this, 0, 0, arrayListLink);

        listView.setAdapter(linksToWhatsAppAdapter);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("NewLinks")
                .child(nameCollege);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayListLink.add(child.getValue(LinksToWhatsApp.class));
                }
                LinksToWhatsApp linksToWhatsApp = new LinksToWhatsApp();
                linksToWhatsApp.setNameGroup("                            עד כאן לינקים שהציעו");
                arrayListLink.add(linksToWhatsApp);
                linksToWhatsAppAdapter.notifyDataSetChanged();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("LinksToWhatsApp").child(nameCollege);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists())
                            return;
                        for (DataSnapshot child : snapshot.getChildren()) {

                            arrayListLink.add(child.getValue(LinksToWhatsApp.class));
                        }

                        linksToWhatsAppAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogJoinWhatappsGrop(i, arrayListLink);

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
                LinksToWhatsApp linksToWhatsApp=new LinksToWhatsApp();
                linksToWhatsApp.setNameGroup(from);
                linksToWhatsApp.setLink(to);
                linksToWhatsApp.setNameCollegeEnglish(nameCollege);
                cardRef2.child(from).setValue(linksToWhatsApp);
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

            case R.id.mainIconMenuManga:
                intent2 = new Intent(Management.this, Management.class);
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
            case R.id.price_drive:
                priceGroups();
                return true;
            case R.id.feedback:
                intent2 = new Intent(Management.this, Management2Feedbacks.class);
                startActivity(intent2);
                return true;
            case R.id.verison:
                notificationsVerison();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void notificationsVerison() {
        editTextDate.setVisibility(View.GONE);
        spinnerNameColeg.setVisibility(View.GONE);
        editTexto.setVisibility(View.VISIBLE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTextFrom.setText("");
        editTextFrom.setHint("גרסאת התראה שחייב להוריד");
        editTexto.setHint("גרסאת התראה שלא חייב להוריד");

        buttonDo.setText("הפעל התראה");
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verison1mast = editTextFrom.getText().toString();
                String verison2noMast  = editTexto.getText().toString();

                if (verison1mast.length() > 4 || verison2noMast.length() > 4||(!verison1mast.isEmpty() && !verison2noMast.isEmpty())
                ||(verison1mast.isEmpty()  && verison2noMast.isEmpty())) {
                    editTextFrom.setError("אנא הכנס מספר תקין");
                    editTextFrom.requestFocus();
                    return;
                }
                if (verison1mast.isEmpty()){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                            .getReference("VersionNotifications");
                    databaseReference.setValue(verison2noMast+" 2").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            buttonDo.setText("גרסא שונתה בהצלחה");
                        }
                    });
                }
                if (verison2noMast.isEmpty()){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                            .getReference("VersionNotifications");
                    databaseReference.setValue(verison1mast+" 1").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            buttonDo.setText("גרסא שונתה בהצלחה");
                        }
                    });
                }


            }
        });
    }
    private void makeVerison() {
        editTextDate.setVisibility(View.GONE);
        spinnerNameColeg.setVisibility(View.GONE);
        editTexto.setVisibility(View.GONE);
        editTextFrom.setVisibility(View.VISIBLE);
        buttonDo.setVisibility(View.VISIBLE);
        editTextFrom.setText("");
        editTextFrom.setHint("גרסא");
        buttonDo.setText("שנה גרסא");
        buttonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verison = editTextFrom.getText().toString();
                if (verison.length() > 4||verison.length() <1) {
                    editTextFrom.setError("אנא הכנס מספר תקין");
                    editTextFrom.requestFocus();
                    return;
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("Version");
                databaseReference.setValue(verison).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        buttonDo.setText("גרסא שונתה בהצלחה");
                    }
                });

            }
        });
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
        if (!blockPhone.isEmpty()) {
            editTextFrom.setText(blockPhone);
        }
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


    private void dialogJoinWhatappsGrop(final int i, final ArrayList<LinksToWhatsApp> arrayListLink) {
        final Dialog d = new Dialog(Management.this);
        d.setContentView(R.layout.dialog_links);
        d.setTitle("Manage");

        d.setCancelable(true);
        final TextView textViewJoinGroup = d.findViewById(R.id.textView_go_whatapps);
        TextView textViewFeed = d.findViewById(R.id.feed_block_i);
        textViewFeed.setVisibility(View.GONE);
        final TextView textViewCopyLink = d.findViewById(R.id.textView_copy_link);
        TextView textViewMyGroup = d.findViewById(R.id.textView_go_my_group);
        textViewMyGroup.setText("מחק הצעה");
        editTextFrom.setText(arrayListLink.get(i).getNameGroup());
        editTexto.setText(arrayListLink.get(i).getLink());

        textViewJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String link = arrayListLink.get(i).getLink();
                String url = link;
                intentWhatsapp.setData(Uri.parse(url));
                // intentWhatsapp.setPackage("com.whatsapp");
                try {
                    startActivity(intentWhatsapp);
                } catch (RuntimeException e) {
                    textViewJoinGroup.setText("קישור לא תקין");
                    textViewJoinGroup.setTextColor(getResources().getColor(R.color.colorWarng));//TODO:set text color
                }

            }
        });


        textViewCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) Management.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListLink.get(i).getLink());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Management.this, "הלינק הועתק", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewMyGroup.setOnClickListener(new View.OnClickListener() {//delete
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NewLinks")
                        .child(nameCollege).child(arrayListLink.get(i).getKey());
                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management.this, "הצעת לינק נמחקה בהצלחה", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                        makeLinks();
                    }
                });
            }
        });
        d.show();

    }

    private void dialogJoinGrop(final int i, final ArrayList<Group> arrayListGroup) {
        final Dialog d = new Dialog(Management.this);
        d.setContentView(R.layout.dialog_links);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView textViewJoinGroup = d.findViewById(R.id.textView_go_whatapps);
        textViewJoinGroup.setVisibility(View.GONE);
        TextView textViewFeed = d.findViewById(R.id.feed_block_i);
        textViewFeed.setVisibility(View.GONE);
        final TextView textViewCopyLink = d.findViewById(R.id.textView_copy_link);
        TextView textViewMyGroup = d.findViewById(R.id.textView_go_my_group);
        textViewMyGroup.setText("מחק הצעה");
        String nameGroup = arrayListGroup.get(i).getName();
        editTextFrom.setText(nameGroup.substring(0, nameGroup.indexOf("-")));
        String to = nameGroup.substring(nameGroup.indexOf("-") + 1, nameGroup.length());
//        String newTo = "";
//        if(to.charAt(0)==' ') {
//            for (int j = 0; j < to.length(); j++) {
//                if (to.charAt(j) != ' ')
//                    newTo += to.charAt(j);
//            }
//        }
        editTexto.setText("" + to);


        textViewCopyLink.setText("העתק שם קבוצה");

        textViewCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) Management.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListGroup.get(i).getName());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Management.this, "הלינק הועתק", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewMyGroup.setOnClickListener(new View.OnClickListener() {//delete
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NewGroups")
                        .child(nameCollege).child(arrayListGroup.get(i).getKey());
                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management.this, "הצעת הקבוצה נמחקה בהצלחה", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                        makeGroup();
                    }
                });
            }
        });
        d.show();

    }

    private void dialogPrice(final int i, final ArrayList<PriceDrive> arrayListGroup) {
        final Dialog d = new Dialog(Management.this);
        d.setContentView(R.layout.dialog_links);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView textViewJoinGroup = d.findViewById(R.id.textView_go_whatapps);
        textViewJoinGroup.setVisibility(View.GONE);

        TextView textViewFeed = d.findViewById(R.id.feed_block_i);
        textViewFeed.setVisibility(View.GONE);

        final TextView textViewCopyLink = d.findViewById(R.id.textView_copy_link);
        TextView textViewMyGroup = d.findViewById(R.id.textView_go_my_group);

        textViewMyGroup.setText("מחק הצעות מקבוצה זו");
        editTextFrom.setText(arrayListGroup.get(i).getNameGroup());

        editTexto.setText(arrayListGroup.get(i).getPrice());


        textViewCopyLink.setText("העתק שם קבוצה");

        textViewCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) Management.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListGroup.get(i).getNameGroup());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Management.this, "הלינק הועתק", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewMyGroup.setOnClickListener(new View.OnClickListener() {//delete
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupsPriceOffer")
                        .child(nameCollege).child(arrayListGroup.get(i).getNameGroup());
                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Management.this, "הצעות המחירים נמחקו בהצלחה", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                        priceGroups();
                    }
                });
            }
        });
        d.show();

    }
}