package com.example.chat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllGroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllGroupsFragment extends Fragment {
    private TextView textViewNoFond;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonSeeAllGroups;
    private ListView listView;
    private View viewGroup;
    private String uid;
    private ArrayList<String> arrayListGroups;
    private ArrayList<String> arrayListGroups2;
    private TextView textViewNameColeg;
    private String flagSearch;
    private ArrayAdapter<String> arrayAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllGroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllGroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllGroupsFragment newInstance(String param1, String param2) {
        AllGroupsFragment fragment = new AllGroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        flagSearch = MainActivity3.search;
        viewGroup = inflater.inflate(R.layout.fragment_all_groups, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listView = (ListView) viewGroup.findViewById(R.id.list_view);
        textViewNoFond = viewGroup.findViewById(R.id.textView_no_found);
        editTextSearch = viewGroup.findViewById(R.id.editText_search);
        textViewNameColeg = viewGroup.findViewById(R.id.textView_name_cologe1);
        buttonSearch = viewGroup.findViewById(R.id.button_search);
        buttonSeeAllGroups = viewGroup.findViewById(R.id.button_all_groups);
        allGroups();

        // Inflate the layout for this fragment
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        buttonSeeAllGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayAdapter.clear();
                ArrayList<String> arrayList = new ArrayList<>(arrayListGroups2);
                arrayAdapter.addAll(arrayList);
                arrayAdapter.notifyDataSetChanged();
                buttonSeeAllGroups.setVisibility(View.GONE);
                textViewNoFond.setVisibility(View.GONE);
            }
        });
        TextView buttonNewLink = viewGroup.findViewById(R.id.button_new_link);
        buttonNewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNewGroup();
            }
        });
        return viewGroup;


    }

    private void allGroups() {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                .getReference("RegisterInformation2")
                .child(uid);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterInformation2 registerInformation = snapshot.getValue(RegisterInformation2.class);
                textViewNameColeg.setText("מ-" + registerInformation.getNameCollegeHebrew() + " ל- ");
                arrayListGroups = new ArrayList<>();
                arrayListGroups2 = new ArrayList<>();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("NamesGroups").child(registerInformation.getNameCollegeEnglish());

                arrayAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1, arrayListGroups);
                listView.setAdapter(arrayAdapter);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            arrayListGroups.add(child.getKey());
                            arrayListGroups2.add(child.getKey());
                        }

                        arrayAdapter.notifyDataSetChanged();
                        if(!flagSearch.isEmpty()){
                            search();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent2 = new Intent(getContext(), GroupChatActivity.class);
                        intent2.putExtra("nameGroup", arrayListGroups.get(i));
                        intent2.putExtra("flagAllGroup", true);
                        startActivity(intent2);
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
//                databaseReference.child(uid).child("hitchhikingGroups").setValue(arrayListGroups.get(i));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void search() {
        final String groupSearch;
        if(flagSearch.isEmpty()) {
             groupSearch = editTextSearch.getText().toString();
        }
        else{
           groupSearch = flagSearch;

        }
        if (groupSearch.isEmpty()) {
            editTextSearch.setError("אנא הכנס קבוצה");
            editTextSearch.requestFocus();
            return;
        }
        ArrayList<String> arrayListSerch = new ArrayList<>();

        for (String name : arrayListGroups2) {
            if (name.contains(groupSearch))
                arrayListSerch.add(name);

        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayListSerch);
        arrayAdapter.notifyDataSetChanged();
        buttonSeeAllGroups.setVisibility(View.VISIBLE);
        if (arrayListSerch.size() == 0) {
            textViewNoFond.setVisibility(View.VISIBLE);
        }
//        final ArrayList<String> arrayListGroups = new ArrayList<>();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NamesGroups");
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_list_item_1, arrayListGroups);
//        listView.setAdapter(arrayAdapter);
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    String nameGroup = child.getKey();
//                    if (nameGroup.contains(groupSearch))
//                        arrayListGroups.add(nameGroup);
//                }
//                if (arrayListGroups.size() == 0) {
//                    textViewNoFond.setVisibility(View.VISIBLE);
//                }
//
//                arrayAdapter.notifyDataSetChanged();
//                buttonSeeAllGroups.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent2 = new Intent(getContext(), GroupChatActivity.class);
//                intent2.putExtra("nameGroup", arrayListGroups.get(i));
//                intent2.putExtra("flagAllGroup", true);
//                startActivity(intent2);
//
//            }
//        });

    }

    private void dialogNewGroup() {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_new_group);
        d.setTitle("Manage");

        d.setCancelable(true);
        final EditText editTextNameGroup = d.findViewById(R.id.editText_name_link);

        Button buttonSend = d.findViewById(R.id.button_send_new_link_dialog);

        Button buttonClose = d.findViewById(R.id.button_close_window_new);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameLink_ = editTextNameGroup.getText().toString();

                if (nameLink_.length() < 4) {
                    editTextNameGroup.setError("הכנס שם קבוצה תקין");
                    editTextNameGroup.requestFocus();
                    return;
                }
                if (!nameLink_.contains("אריאל") && !nameLink_.toLowerCase().contains("ariel")) {
                    editTextNameGroup.setError("הכנס שם קבוצה תקין,לדוגמא אריאל-אשקלון");
                    editTextNameGroup.requestFocus();
                    return;
                }
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NewGroups")
                        .child(uid).push();
                databaseReference.setValue(nameLink_);
                Toast.makeText(getContext(), "ההצעה נשלחה בהצלחה ומחכה לאישור המערכת", Toast.LENGTH_LONG).show();
                d.dismiss();

            }
        });
        d.show();

    }
}