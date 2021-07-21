package com.code.Chat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.code.Hikers.MainActivity3;
import com.code.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

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
    private ArrayList<Contact> contactArrayList2;

    private String nameContact;
    private String imageContact;
    private GroupsAdapter contactsAdapter;
    private Message messageContact = new Message();
    private View viewContacts;
    private TextView textViewNoFond;
    private String groupSearch;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonSeeAllGroups;
    private String uid;
    private boolean flagStart1 = false;

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayListName2;

    private ArrayList<String> arrayListNameCologeEnglish;
    private ArrayList<String> arrayListNameCologeEnglish2;

    private ProgressDialog progressDialog;
    private boolean wait=false;

    public MyGroupsFragment() {

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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("העמוד נטען..");

        viewContacts = inflater.inflate(R.layout.fragment_my_groups, container, false);


        listView = (ListView) viewContacts.findViewById(R.id.list_view);

        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("StartApp");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() &&snapshot.getValue(Integer.class)==1) {
                    TextView textView =viewContacts.findViewById(R.id.textViewOpenSoon);
                    textView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        secrchId();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        seeAllMyGroups();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(Color.parseColor("#FFACE8EF"));
                dialogDelete(i, view);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(), GroupChatActivity.class);
                intent.putExtra("nameGroup", contactArrayList.get(i).getName());
                intent.putExtra("flagAllGroup", false);
                intent.putExtra("num_notifications", contactArrayList.get(i).getNotifications());
                intent.putExtra("flagNameCologeEnglish", contactArrayList.get(i).getNameCollegeEnglish());

                startActivity(intent);
                contactArrayList.get(i).setNotifications(0);
                contactsAdapter.notifyDataSetChanged();
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
        contactArrayList = new ArrayList<>();
        contactArrayList2 = new ArrayList<>();

        contactsAdapter = new GroupsAdapter(getContext(), 0, 0, contactArrayList);
        listView.clearDisappearingChildren();
        listView.setAdapter(contactsAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyGroups");

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    TextView textView = viewContacts.findViewById(R.id.textView_empty);
                   // textView.setVisibility(View.VISIBLE);
                    return;
                }
                progressDialog.show();
                arrayList = new ArrayList<>();
                arrayListName2=new ArrayList<>();
                arrayListNameCologeEnglish = new ArrayList<>();
                arrayListNameCologeEnglish2 = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {

                    String nameGroup = child.getKey();
                    arrayList.add(nameGroup);
                    arrayListName2.add(nameGroup);
                    arrayListNameCologeEnglish.add(child.getValue(String.class));
                    arrayListNameCologeEnglish2.add(child.getValue(String.class));

                }
                onStart1();
                if (arrayList.size() > 0) {
                    buttonSearch.setVisibility(View.VISIBLE);

                    addContact(uid);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void secrchId() {
        textViewNoFond = viewContacts.findViewById(R.id.textView_no_found);
        editTextSearch = viewContacts.findViewById(R.id.editText_search);
        buttonSearch = viewContacts.findViewById(R.id.button_search);
        buttonSearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView.setVisibility(View.VISIBLE);
                return false;
            }
        });
        buttonSeeAllGroups = viewContacts.findViewById(R.id.button_all_groups);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonSearch.getText().equals("חיפוש")) {
                    editTextSearch.setVisibility(View.VISIBLE);
                    buttonSearch.setText("חפש");
                } else if (buttonSearch.getText().equals("חפש"))

                    search();

            }
        });
        buttonSeeAllGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsAdapter.clear();
                ArrayList<Contact> arrayList = new ArrayList<>(contactArrayList2);
                contactsAdapter.addAll(arrayList);
                contactsAdapter.notifyDataSetChanged();
                textViewNoFond.setVisibility(View.GONE);
                editTextSearch.setVisibility(View.GONE);
                buttonSeeAllGroups.setVisibility(View.GONE);
                buttonSearch.setText("חיפוש");
            }
        });


    }


    private void addContact(final String uid) {

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("NamesGroups");
        //  if(arrayList.size()<1 ||arrayListNameCologeEnglish.size()<1)return;
        databaseReference1.child(arrayListNameCologeEnglish.get(0)).child(arrayList.get(0)).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    messageContact = new Message();
                }
                for (DataSnapshot child : snapshot.getChildren()) {
                    messageContact = child.getValue(Message.class);

                }

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("NotificationsIdSeeLast");
                databaseReference1.child(uid).child(arrayList.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Contact contact = new Contact();
                        int lastSee = 0;
                        if (snapshot.exists()) lastSee = snapshot.getValue(Integer.class);
                        int numNewMessage = messageContact.getId() - lastSee;
                        contact.setNotifications(numNewMessage);

                        contact.setMessage(messageContact);
                        contact.setName(arrayList.get(0));
                        contact.setUidI(uid);
                        //contactsAdapter.notifyDataSetChanged();
                        contact.setNameCollegeEnglish(arrayListNameCologeEnglish.get(0));
                        contactArrayList.add(contact);

                        arrayListNameCologeEnglish.remove(0);
                        arrayList.remove(0);
                        if (arrayList.size() == 0) {
                            contactArrayList2 = new ArrayList<>(contactArrayList);
                            if (contactArrayList.size() > 1)
                                Collections.sort(contactArrayList, new ComperatorContact());

                            contactsAdapter.notifyDataSetChanged();
                            getView().setClickable(true);
                            progressDialog.dismiss();
                            flagStart1 = true;
                        }

                        if (arrayList.size() > 0) addContact(uid);
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

    public void onStart1() {
        ArrayList < DatabaseReference> arrayListRe=new ArrayList<>();

        for (int i=0;i<arrayListName2.size();i++) {
            arrayListRe.add(FirebaseDatabase.getInstance().getReference("NamesGroups"));
        }
        for (int i=0;i<arrayListName2.size();i++) {
            final String nameGroup2 = arrayListName2.get(i);

            arrayListRe.get(i).child(arrayListNameCologeEnglish2.get(i)).child(nameGroup2)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (!snapshot.exists())
                                return;
//                    else{
//                        buttonSearch.setText(snapshot.toString());
//                       // return;
//                    }
                            final ArrayList<String> arrayListTemp= new ArrayList<>();
                            final ArrayList<String> arrayListName2Temp=arrayListName2;

                            ArrayList<String> arrayListNameCologeEnglishTemp=new ArrayList<>();
                            ArrayList<String> arrayListNameCologeEnglish2Temp;

                            Message temp=new Message();
                            temp = snapshot.getValue(Message.class);

                            final Message  messageContact1=temp;
                            Contact contact1=new Contact();
                            String NameCologeEnglishTemp1="";
                            if (snapshot.exists() && flagStart1) {
                                for (Contact contact : contactArrayList) {
                                    if (contact.getName().equals(nameGroup2)) {
                                        NameCologeEnglishTemp1=contact.getNameCollegeEnglish();
                                        contact1=contact;
                                  //buttonSearch.setText("lklk");
                                        break;
                                    }
                                }
                                final Contact contact2=contact1;
                                final     String NameCologeEnglishTemp2=NameCologeEnglishTemp1;
                                // arrayList = new ArrayList<>();
                                arrayListTemp.add(nameGroup2);
                                //addContact(uid);
                                addContact2(arrayListTemp,messageContact1,NameCologeEnglishTemp2,contact2);



                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    if (snapshot.exists() && flagStart1) {
//                        if (snapshot.exists()) {
//                            arrayListNameCologeEnglish = new ArrayList<>();
//                            for (Contact contact : contactArrayList) {
//                                if (contact.getName().equals(nameGroup2)) {
//                                    arrayListNameCologeEnglish.add(contact.getNameCollegeEnglish());
//                                    contactArrayList.remove(contact);
//                                    break;
//                                }
//                            }
//                            arrayList = new ArrayList<>();
//                            arrayList.add(nameGroup2);
//                            addContact(uid);
//
//
//                        }
//                    }
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void addContact2(ArrayList<String> arrayListTemp, Message messageContact1, String nameCologeEnglishTemp2, Contact contact2) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("NotificationsIdSeeLast");
        databaseReference1.child(uid).child(arrayListTemp.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int lastSee = 0;
                if (snapshot.exists())
                    lastSee = snapshot.getValue(Integer.class);
                int numNewMessage = messageContact1.getId() - lastSee;
                contact2.setNotifications(numNewMessage);

                contact2.setMessage(messageContact1);
                contact2.setName(arrayListTemp.get(0));
                contact2.setUidI(uid);
                //contactsAdapter.notifyDataSetChanged();
                contact2.setNameCollegeEnglish(nameCologeEnglishTemp2);
                // contactArrayList.add(contact);

                arrayListTemp.remove(0);
                if (arrayListTemp.size() == 0) {
                    contactArrayList2 = new ArrayList<>(contactArrayList);
                    if (contactArrayList.size() > 1)
                        Collections.sort(contactArrayList, new ComperatorContact());

                    contactsAdapter.notifyDataSetChanged();
                    getView().setClickable(true);
                    progressDialog.dismiss();
                }

//                if (arrayListTemp.size() > 0)
//                    addContact2(arrayListTemp,messageContact1,nameCologeEnglishTemp2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void search() {
        groupSearch = editTextSearch.getText().toString();
        if (groupSearch.isEmpty()) {
            editTextSearch.setError("אנא הכנס קבוצה");
            editTextSearch.requestFocus();
            return;
        }
        ArrayList<Contact> arrayList = new ArrayList<>();
        for (Contact contact : contactArrayList2) {
            if (contact.getName().contains(groupSearch)) {
                arrayList.add(contact);
            }
        }
        contactsAdapter.clear();
        contactsAdapter.addAll(arrayList);
        contactsAdapter.notifyDataSetChanged();

        buttonSeeAllGroups.setVisibility(View.VISIBLE);
        if (arrayList.size() == 0) {
            textViewNoFond.setVisibility(View.VISIBLE);
        } else textViewNoFond.setVisibility(View.GONE);

    }

    private void dialogDelete(final int i, final View view2) {
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
        Button buttonBlokeDialog = d.findViewById(R.id.button_aprove_terms);
        Button buttonNoBloke = d.findViewById(R.id.button_exit_app);
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

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                        .getReference("NotificationsIdSeeLast").child(uid).child(contactArrayList.get(i).getName());//set Notifications
                databaseReference3.removeValue();

                Toast.makeText(getContext(), "יצאת מהקבוצה בהצלחה", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), MainActivity3.class);
                intent.putExtra("flagPage", 1);

                startActivityForResult(intent, 0);
            }

        });

        d.show();
    }

//  6
}