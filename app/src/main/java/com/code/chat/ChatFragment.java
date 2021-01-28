package com.code.chat;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.code.Hikers.RegisterInformation2;
import com.code.R;
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
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Contact> contactArrayList;
    private String nameContact = "";
    private String imageContact = "";
    private String phoneContact = "";
    private ContactsAdapter contactsAdapter;
    private DatabaseReference reference;
    private ListView listView;
    private String uid;
    private Message messageContact = new Message();
    private View viewContacts;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    private boolean flagNewConcat = false;
    private int  lastSee;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("העמוד נטען..");
        viewContacts = inflater.inflate(R.layout.fragment_chat, container, false);
        viewContacts.setClickable(false);
        listView = (ListView) viewContacts.findViewById(R.id.list_view);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Contacts").child(uid);

        onStart1();
       list();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        return viewContacts;

    }

    private void list() {
        contactArrayList = new ArrayList<>();
        listView.clearDisappearingChildren();
        contactsAdapter = new ContactsAdapter(getContext(), 0, 0, contactArrayList);

        listView.setAdapter(contactsAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");


        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flagNewConcat=true;
                if (!snapshot.exists()) {
                    TextView textView = viewContacts.findViewById(R.id.textView_empty);
                    textView.setVisibility(View.VISIBLE);
                    return;
                }
                progressDialog.show();

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
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                chakIfExistAndBlokeAndSend(i);

            }
        });
    }

    private void dialogDelete(final int i, final View view2) {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_delete_chat);
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
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts")
                        .child(uid).child(contactArrayList.get(i).getUidContacts());
                databaseReference.removeValue();
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                        .getReference("NotificationsIdSeeLast").child(uid).child(contactArrayList.get(i).getUidContacts());//set Notifications
                databaseReference3.removeValue();

                Toast.makeText(getContext(), "השיחה נמחקה בהצלחה", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), MainActivity3.class);

                startActivityForResult(intent, 0);
            }

        });

        d.show();
    }

    private void dialogIsBlocking() {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_is_blocking);
        d.setTitle("Manage");

        d.setCancelable(true);
        Button buttonClose = d.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void addContact(final ArrayList<String> uidContact, final String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation2");
        databaseReference.child(uidContact.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {
                    RegisterInformation2 registerInformationRecive = new RegisterInformation2();
                    registerInformationRecive = snapshot.getValue(RegisterInformation2.class);
                    nameContact = registerInformationRecive.getName();
                    if (nameContact.isEmpty())
                        nameContact = registerInformationRecive.getEmail();
                    phoneContact = registerInformationRecive.getEmail();
                    imageContact = registerInformationRecive.getImageUrl();
                }

                //dataChange(uid, uidContact.get(0));
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Contacts");
                databaseReference1.child(uid).child(uidContact.get(0)).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot child : snapshot.getChildren()) {
                            messageContact = child.getValue(Message.class);

                        }
                        notifications(uidContact, uid);


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

    private void notifications(final ArrayList<String> uidContact, final String uid) {
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                .getReference("NotificationsIdSeeLast").child(uid)
                .child(uidContact.get(0));
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 lastSee = 0;
                if (snapshot.exists()) lastSee = snapshot.getValue(Integer.class);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts")
                        .child(uid).child(uidContact.get(0));
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Contact contact = new Contact();


                        int numNewMessage = (int)snapshot.getChildrenCount() - lastSee;
                        contact.setNotifications(numNewMessage);

                        contact.setPhoneContacts(phoneContact);
                        contact.setImage(imageContact);
                        contact.setMessage(messageContact);
                        contact.setName(nameContact);
                        contact.setUidI(uid);
                        contact.setUidContacts(uidContact.get(0));
                        contactArrayList.add(contact);
                        uidContact.remove(0);
                        if (uidContact.size() == 0) {
                            if (contactArrayList.size() > 1) {
                                Collections.sort(contactArrayList, new ComperatorContact());
                            }
                            contactsAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
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


    public void onStart1() {

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()&&flagNewConcat) {
                    ArrayList arrayList=new ArrayList();
                    String uidContact = snapshot.getKey();
                    arrayList.add(uidContact);
                    addContact(arrayList,uid);

                   // list();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String uidContact = snapshot.getKey();
                    for(Contact contact:contactArrayList){
                        if(contact.getUidContacts().equals(uidContact)) {
                            contactArrayList.remove(contact);
                            break;
                        }
                    }
                    ArrayList arrayList=new ArrayList();
                    arrayList.add(uidContact);
                    addContact(arrayList,uid);

                   // list();
                }
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

    private void chakIfExistAndBlokeAndSend(final int i) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInformation2")
                .child(contactArrayList.get(i).getUidContacts());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chakIfBlockAndSend(i);
                } else {
                    dialogIsBlocking();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chakIfBlockAndSend(final int i) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocked")
                .child(uid).child(contactArrayList.get(i).getPhoneContacts());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dialogIsBlocking();

                } else {

                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("send_message_user_id", uid);
                    intent.putExtra("to_message_user_id", contactArrayList.get(i).getUidContacts());
                    if (contactArrayList.get(i).getMessage().getPhone().equals("הודעת מערכת"))
                        intent.putExtra("flagMengeSend", true);

                    // Toast.makeText(getContext(), uid, Toast.LENGTH_LONG).show();
                    intent.putExtra("num_notifications", contactArrayList.get(i).getNotifications());
                    startActivityForResult(intent, 0);
                    contactArrayList.get(i).setNotifications(0);
                    contactsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}