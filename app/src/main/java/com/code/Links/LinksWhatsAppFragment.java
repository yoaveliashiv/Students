package com.code.Links;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.code.Hikers.FeedbackChat;
import com.code.Hikers.MainActivity3;
import com.code.Hikers.RegisterInformation2;
import com.code.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LinksWhatsAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinksWhatsAppFragment extends Fragment {
    private View viewGroup;
    private ArrayList<LinksToWhatsApp> arrayListLink;
    private ArrayList<LinksToWhatsApp> arrayListLink2;
    private String uid;
    private TextView textViewNameColge;
    private String nameCollegeEnglish = "";
    private String nameCollegeHebrew = "";
    private TextView textViewNoFond;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonSeeAllGroups;
    private ListView listView;
    private LinksToWhatsAppAdapter linksToWhatsAppAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LinksWhatsAppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LinksWhatsAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LinksWhatsAppFragment newInstance(String param1, String param2) {
        LinksWhatsAppFragment fragment = new LinksWhatsAppFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        viewGroup = inflater.inflate(R.layout.fragment_links_whats_app, container, false);
        listView = (ListView) viewGroup.findViewById(R.id.list_view);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        textViewNameColge = viewGroup.findViewById(R.id.textView_name_cologe1);
        textViewNoFond = viewGroup.findViewById(R.id.textView_no_found);
        editTextSearch = viewGroup.findViewById(R.id.editText_search);
        buttonSearch = viewGroup.findViewById(R.id.button_search);
        buttonSeeAllGroups = viewGroup.findViewById(R.id.button_all_groups);
        allLinks();
        buttonSearch.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewNoFond.setVisibility(View.GONE);
                search();
            }
        });
        buttonSeeAllGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linksToWhatsAppAdapter.clear();
                ArrayList<LinksToWhatsApp> arrayList = new ArrayList<>(arrayListLink2);
                linksToWhatsAppAdapter.addAll(arrayList);
                linksToWhatsAppAdapter.notifyDataSetChanged();
                buttonSeeAllGroups.setVisibility(View.GONE);
                textViewNoFond.setVisibility(View.GONE);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogJoinWhatappsGrop(i);
            }
        });

        TextView buttonNewLink = viewGroup.findViewById(R.id.button_new_link);
        buttonNewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNewLink();
            }
        });
        return viewGroup;


    }

    private void dialogNewLink() {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_new_link);
        d.setTitle("Manage");

        d.setCancelable(true);
        final EditText editTextNameGroup = d.findViewById(R.id.editText_name_link);
        final EditText editTextLink = d.findViewById(R.id.editText_new_link);

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
                String link_ = editTextLink.getText().toString();
                if (link_.length() < 4) {
                    editTextLink.setError("הכנס קישור תקין");
                    editTextLink.requestFocus();
                    return;
                }
                String nameLink_ = editTextNameGroup.getText().toString();

                if (nameLink_.length() < 4) {
                    editTextNameGroup.setError("הכנס שם קבוצה תקין");
                    editTextNameGroup.requestFocus();
                    return;
                }
                chakIfExsitAndSend(link_, nameLink_, editTextLink, editTextNameGroup, d);


            }
        });
        d.show();

    }

    private void chakIfExsitAndSend(final String link_, final String nameLink_, final EditText editTextLink, final EditText editTextNameGroup, final Dialog d) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LinksToWhatsApp")
                .child(nameCollegeEnglish);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                   LinksToWhatsApp linksToWhatsAppChak=child.getValue(LinksToWhatsApp.class);

                    if (link_.equals(linksToWhatsAppChak.getLink())) {
                        editTextLink.setError("הקבוצה כבר קיימת בשם: " + child.getKey());
                        editTextLink.requestFocus();
                        return;
                    }
                }
                LinksToWhatsApp link = new LinksToWhatsApp();
                link.setNameGroup(nameLink_);
                link.setLink(link_);
                link.setUidWantNewLink(uid);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NewLinks")
                        .child(nameCollegeEnglish).push();
                link.setKey(databaseReference.getKey());
                databaseReference.setValue(link);
                Toast.makeText(getContext(), "הלינק נשלח בהצלחה ומחכה לאישור המערכת", Toast.LENGTH_LONG).show();
                d.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialogJoinWhatappsGrop(final int i) {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_links);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView textViewJoinGroup = d.findViewById(R.id.textView_go_whatapps);
        TextView textViewFeed = d.findViewById(R.id.feed_block_i);

        TextView textViewCopyLink = d.findViewById(R.id.textView_copy_link);
        TextView textViewMyGroup = d.findViewById(R.id.textView_go_my_group);
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("StartApp");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists() ||snapshot.getValue(Integer.class)!=1) {
                   textViewMyGroup.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
textViewFeed.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
        textViewCopyLink.setVisibility(View.VISIBLE);
        return false;
    }
});
        textViewMyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity3.class);

                String nameGroup1 = arrayListLink.get(i).getNameGroup();
                int size = nameGroup1.indexOf(" ");
                if (size == -1) size = nameGroup1.length();
                String nameGroup2 = nameGroup1.substring(0, size);
                intent.putExtra("flag_serch", nameGroup2);
                startActivity(intent);
            }
        });

        textViewJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String link = arrayListLink.get(i).getLink();
                String url = link;

                intentWhatsapp.setData(Uri.parse(url));
               if(!link.contains("bit.")) intentWhatsapp.setPackage("com.whatsapp");
               try {
                   startActivity(intentWhatsapp);

               }
               catch (Exception E){

               }

            }
        });


        textViewCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1", arrayListLink.get(i).getLink());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "הלינק הועתק", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFeedLink(arrayListLink.get(i));
                d.dismiss();
            }
        });
        d.show();

    }

    private void search() {
        final String groupSearch = editTextSearch.getText().toString();
        if (groupSearch.isEmpty()) {
            editTextSearch.setError("אנא הכנס קבוצה");
            editTextSearch.requestFocus();
            return;
        }
        ArrayList<LinksToWhatsApp> arrayList = new ArrayList<>();

        for (LinksToWhatsApp child : arrayListLink2) {

            if (child.getNameGroup().contains(groupSearch)) {
                arrayList.add(child);
            }
        }
        if (arrayList.size() == 0) {
            textViewNoFond.setVisibility(View.VISIBLE);
        }

        buttonSeeAllGroups.setVisibility(View.VISIBLE);
        linksToWhatsAppAdapter.clear();
        linksToWhatsAppAdapter.addAll(arrayList);
        linksToWhatsAppAdapter.notifyDataSetChanged();
//        arrayListLink = new ArrayList<>();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LinksToWhatsApp");
//
//        final LinksToWhatsAppAdapter linksToWhatsAppAdapter = new LinksToWhatsAppAdapter(getContext(), 0, 0, arrayListLink);
//
//        listView.setAdapter(linksToWhatsAppAdapter);
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (!snapshot.exists())
//                    return;
//                for (DataSnapshot child : snapshot.getChildren()) {
//
//                    LinksToWhatsApp linksToWhatsApp = new LinksToWhatsApp();
//                    String nameGroup = child.getKey();
//                    if (nameGroup.contains(groupSearch)) {
//                        linksToWhatsApp.setNameGroup(nameGroup);
//                        linksToWhatsApp.setLink(child.getValue(String.class));
//                        arrayListLink.add(linksToWhatsApp);
//                    }
//                }
//                if (arrayListLink.size() == 0) {
//                    textViewNoFond.setVisibility(View.VISIBLE);
//                }
//
//                buttonSeeAllGroups.setVisibility(View.VISIBLE);
//                linksToWhatsAppAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void allLinks() {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                .getReference("RegisterInformation2")
                .child(uid);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterInformation2 registerInformation = snapshot.getValue(RegisterInformation2.class);
                nameCollegeEnglish = registerInformation.getNameCollegeEnglish();
                textViewNameColge.setText("מ-" + registerInformation.getNameCollegeHebrew() + " ל- ");
                arrayListLink = new ArrayList<>();
                arrayListLink2 = new ArrayList<>();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("LinksToWhatsApp")
                        .child(registerInformation.getNameCollegeEnglish());

                linksToWhatsAppAdapter = new LinksToWhatsAppAdapter(getContext(), 0, 0, arrayListLink);

                listView.setAdapter(linksToWhatsAppAdapter);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists())
                            return;
                        for (DataSnapshot child : snapshot.getChildren()) {

                            arrayListLink.add(child.getValue(LinksToWhatsApp.class));
                        }
                        arrayListLink2 = new ArrayList<>(arrayListLink);
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

    }

    private void dialogFeedLink(final LinksToWhatsApp linksToWhatsApp) {
        final Dialog d2 = new Dialog(getContext());
        d2.setContentView(R.layout.dialog_feedback_link_dont_work);
        d2.setTitle("Manage");

        d2.setCancelable(true);
        Button buttonYes = d2.findViewById(R.id.buttonYes);
        Button buttonNo = d2.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d2.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackChat feedbackChat = new FeedbackChat();
                feedbackChat.setFeedback("לינק לא תקין");
                feedbackChat.setType("בעיות טכניות");
                feedbackChat.setLinksToWhatsApp(linksToWhatsApp);
                linksToWhatsApp.setNameCollegeEnglish(nameCollegeEnglish);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FeedbackChat")
                        .child("בעיות טכניות").push();
                feedbackChat.setKey(databaseReference.getKey());
                databaseReference.setValue(feedbackChat);
                Toast.makeText(getContext(), "דיווח נשלח בהצלחה", Toast.LENGTH_SHORT).show();
                d2.dismiss();
            }
        });

        d2.show();

    }
}