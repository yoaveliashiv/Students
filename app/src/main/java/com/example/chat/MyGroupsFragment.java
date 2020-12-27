package com.example.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private ContactsAdapter contactsAdapter;
    private Message messageContact = new Message();
    private View viewContacts;
    private View viewGroup;


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

        viewContacts = inflater.inflate(R.layout.fragment_my_groups, container, false);
        final ListView listView = (ListView) viewContacts.findViewById(R.id.list_view);
        contactArrayList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(getContext(), 0, 0, contactArrayList);

        listView.setAdapter(contactsAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyGroups");

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                ArrayList<String> arrayList = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {

                    String uidContact = child.getKey();
                    arrayList.add(uidContact);

                }
                addContact(arrayList, uid);

//  contactsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent2 = new Intent(getContext(), GroupChatActivity.class);
                intent2.putExtra("nameGroup",contactArrayList.get(i).getName());
                intent2.putExtra("flagAllGroup",false);
                startActivity(intent2);
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
//                databaseReference.child(uid).child("hitchhikingGroups").setValue(arrayListGroups.get(i));
            }
        });
        // Inflate the layout for this fragment
        return viewContacts;
    }

    private void addContact(final ArrayList<String> uidContact, final String uid) {

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("NamesGroups");
                databaseReference1.child(uidContact.get(0)).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            messageContact = child.getValue(Message.class);

                        }
                        Contact contact = new Contact();
                        contact.setMessage(messageContact);
                        contact.setName(uidContact.get(0));
                        contactArrayList.add(contact);
                        contactsAdapter.notifyDataSetChanged();
                        uidContact.remove(0);
                        if(uidContact.size()>0)addContact(uidContact,uid);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }



}