package com.example.chat;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.example.Hikers.RegisterInformation2;
import com.example.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGroupsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Contact> contactArrayList;
    private String nameContact;
    private String imageContact;
    private GroupsAdapter contactsAdapter;
    private Message messageContact = new Message();
    private View viewContacts;
    private TextView textViewNoFond;
    private TextView textViewFrom;
    boolean flagSerch = false;
    private String groupSearch;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonSeeAllGroups;
    private String uid;
    private ListView listView;
    public MyGroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyGroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyGroupsFragment newInstance(String param1, String param2) {
        MyGroupsFragment fragment = new MyGroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewContacts = inflater.inflate(R.layout.fragment_my_groups, container, false);
        listView = (ListView) viewContacts.findViewById(R.id.list_view);
        contactArrayList = new ArrayList<>();
        contactsAdapter = new GroupsAdapter(getContext(), 0, 0, contactArrayList);
        secrchId();
        listView.setAdapter(contactsAdapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyGroups");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        seeAllMyGroups();
listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        view.setBackgroundColor(Color.parseColor("#FFACE8EF"));
        dialogDelete(i,view);
        return true;
    }
});
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent2 = new Intent(getContext(), GroupChatActivity.class);
                intent2.putExtra("nameGroup", contactArrayList.get(i).getName());
                intent2.putExtra("flagAllGroup", false);
                startActivity(intent2);
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
//                databaseReference.child(uid).child("hitchhikingGroups").setValue(arrayListGroups.get(i));
            }
        });
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        return viewContacts;
    }

    private void seeAllMyGroups() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyGroups");

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                ArrayList<String> arrayList = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {

                    String uidContact = child.getKey();
                    if (flagSerch) {
                        if (uidContact.contains(groupSearch))
                            arrayList.add(uidContact);
                    } else {
                        arrayList.add(uidContact);
                    }

                }
                if (flagSerch) {
                    if (arrayList.size() == 0) {
                        textViewNoFond.setVisibility(View.VISIBLE);
                        contactsAdapter.notifyDataSetChanged();
                    } else textViewNoFond.setVisibility(View.GONE);
                }
                if (arrayList.size() > 0){
                    buttonSearch.setVisibility(View.VISIBLE);
                    addContact(arrayList, uid);
                }

//  contactsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void secrchId() {
        textViewFrom = viewContacts.findViewById(R.id.textView_from);
        textViewNoFond = viewContacts.findViewById(R.id.textView_no_found);
        editTextSearch = viewContacts.findViewById(R.id.editText_search);
        buttonSearch = viewContacts.findViewById(R.id.button_search);
        buttonSeeAllGroups = viewContacts.findViewById(R.id.button_all_groups);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonSearch.getText().equals("חיפוש")) {
                    editTextSearch.setVisibility(View.VISIBLE);
                    buttonSeeAllGroups.setVisibility(View.VISIBLE);
                    buttonSearch.setText("חפש");
                    textViewFrom.setVisibility(View.VISIBLE);
                } else if (buttonSearch.getText().equals("חפש"))

                    search();

            }
        });
        buttonSeeAllGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsAdapter.clear();
                flagSerch = false;
                seeAllMyGroups();
                textViewNoFond.setVisibility(View.GONE);
                editTextSearch.setVisibility(View.GONE);
                buttonSeeAllGroups.setVisibility(View.GONE);
                buttonSearch.setText("חיפוש");
                textViewFrom.setVisibility(View.GONE);
            }
        });


    }


    private void addContact(final ArrayList<String> uidContact, final String uid) {

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("NamesGroups");
        databaseReference1.child(uidContact.get(0)).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    messageContact = new Message();
                }
                for (DataSnapshot child : snapshot.getChildren()) {
                    messageContact = child.getValue(Message.class);

                }

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("NotificationsIdSeeLast");
                databaseReference1.child(uid).child(uidContact.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Contact contact = new Contact();
                        int lastSee = 0;
                        if (snapshot.exists()) lastSee = snapshot.getValue(Integer.class);
                        int numNewMessage = messageContact.getId() - lastSee;
                        contact.setNotifications(numNewMessage);

                        contact.setMessage(messageContact);
                        contact.setName(uidContact.get(0));
                        contact.setUidI(uid);
                        //contactsAdapter.notifyDataSetChanged();

                        contactArrayList.add(contact);
                        uidContact.remove(0);
                        if (uidContact.size() == 0) {
                            if (contactArrayList.size() > 1)
                                Collections.sort(contactArrayList, new ComperatorContact());

                            contactsAdapter.notifyDataSetChanged();

                        }

                        if (uidContact.size() > 0) addContact(uidContact, uid);
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

    }

    //    public void onStart1() {
//
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    list();
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    private void search() {
        groupSearch = editTextSearch.getText().toString();
        if (groupSearch.isEmpty()) {
            editTextSearch.setError("אנא הכנס קבוצה");
            editTextSearch.requestFocus();
            return;
        }
        buttonSeeAllGroups.setVisibility(View.VISIBLE);
        contactsAdapter.clear();
        flagSerch = true;
        seeAllMyGroups();

    }
    private void dialogDelete(final int i,final View view2) {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_delete_my_group);
        d.setTitle("Manage");
        d.setCancelable(true);
        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                view2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

            }
        });
        Button buttonBlokeDialog = d.findViewById(R.id.button_delete_dialog);
        Button buttonNoBloke = d.findViewById(R.id.button_exit_delete);
        buttonNoBloke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                d.cancel();
                return;
            }
        });
        buttonBlokeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyGroups")
                        .child(uid).child(contactArrayList.get(i).getName());
                databaseReference.removeValue();

                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups details")
                        .child(contactArrayList.get(i).getName()).child(uid);
                databaseReference2.removeValue();

                Toast.makeText(getContext(), "יצאת מהקבוצה בהצלחה", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), MainActivity3.class);

                startActivityForResult(intent, 0);
            }

        });

        d.show();
    }

}