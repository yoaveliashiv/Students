package com.example.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Contact> contactArrayList;
    private String nameContact;
    private String imageContact;
    ContactsAdapter contactsAdapter;
    private Message messageContact = new Message();
    private View viewContacts;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();

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
        viewContacts = inflater.inflate(R.layout.fragment_chat, container, false);
        final ListView listView = (ListView) viewContacts.findViewById(R.id.list_view);
        contactArrayList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(getContext(), 0, 0, contactArrayList);

        listView.setAdapter(contactsAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    return;
                }
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
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("send_message_user_id", uid);
                intent.putExtra("to_message_user_id", contactArrayList.get(i).getUid());

                startActivityForResult(intent, 0);
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
//                databaseReference.child(uid).child("hitchhikingGroups").setValue(arrayListGroups.get(i));
            }
        });
        // Inflate the layout for this fragment
        return viewContacts;
    }

    private void addContact(final ArrayList<String> uidContact, final String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference.child(uidContact.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                RegisterInformation2 registerInformationRecive = new RegisterInformation2();
                registerInformationRecive = snapshot.getValue(RegisterInformation2.class);
                nameContact = registerInformationRecive.getName();
                if (nameContact.isEmpty())
                    nameContact = registerInformationRecive.getEmail();

                imageContact = registerInformationRecive.getImageUrl();

                //dataChange(uid, uidContact.get(0));
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Contacts");
                databaseReference1.child(uid).child(uidContact.get(0)).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            messageContact = child.getValue(Message.class);

                        }
                        Contact contact = new Contact();
                        contact.setImage(imageContact);
                        contact.setMessage(messageContact);
                        contact.setName(nameContact);
                        contact.setUid(uidContact.get(0));
                        contactArrayList.add(contact);
                        contactsAdapter.notifyDataSetChanged();
                        uidContact.remove(0);
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

}