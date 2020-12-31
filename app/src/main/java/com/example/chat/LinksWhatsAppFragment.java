package com.example.chat;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.example.R;
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
    private   View viewGroup;
    private ArrayList<LinksToWhatsApp> arrayListLink;
    private TextView textViewNoFond;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonSeeAllGroups;
    private ListView listView;
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
        textViewNoFond = viewGroup.findViewById(R.id.textView_no_found);
        editTextSearch = viewGroup.findViewById(R.id.editText_search);
        buttonSearch = viewGroup.findViewById(R.id.button_search);
        buttonSeeAllGroups = viewGroup.findViewById(R.id.button_all_groups);
       allLinks();
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        buttonSeeAllGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allLinks();
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
        // Inflate the layout for this fragment
        return viewGroup;

    }

    private void dialogJoinWhatappsGrop(final int i) {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_links);
        d.setTitle("Manage");

        d.setCancelable(true);
        TextView textViewJoinGroup = d.findViewById(R.id.textView_go_whatapps);
        TextView textViewFeed = d.findViewById(R.id.textView_feed);

        TextView textViewCopyLink = d.findViewById(R.id.textView_copy_link);

        textViewJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String link =arrayListLink.get(i).getLink();
                String url = link;
                intentWhatsapp.setData(Uri.parse(url));
               // intentWhatsapp.setPackage("com.whatsapp");
                startActivity(intentWhatsapp);

            }
        });


        textViewCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("1",  arrayListLink.get(i).getLink());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "הלינק הועתק", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        textViewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "העמוד בבניה", Toast.LENGTH_SHORT).show();
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
        arrayListLink = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LinksToWhatsApp");

        final LinksToWhatsAppAdapter linksToWhatsAppAdapter = new LinksToWhatsAppAdapter(getContext(), 0, 0, arrayListLink);

        listView.setAdapter(linksToWhatsAppAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;
                for (DataSnapshot child : snapshot.getChildren()) {

                    LinksToWhatsApp linksToWhatsApp = new LinksToWhatsApp();
                    String nameGroup=child.getKey();
                    if(nameGroup.contains(groupSearch)) {
                        linksToWhatsApp.setNameGroup(nameGroup);
                        linksToWhatsApp.setLink(child.getValue(String.class));
                        arrayListLink.add(linksToWhatsApp);
                    }
                }
                if (arrayListLink.size() == 0) {
                    textViewNoFond.setVisibility(View.VISIBLE);
                }

                buttonSeeAllGroups.setVisibility(View.VISIBLE);
                linksToWhatsAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void allLinks() {
         arrayListLink = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LinksToWhatsApp");

        final LinksToWhatsAppAdapter linksToWhatsAppAdapter = new LinksToWhatsAppAdapter(getContext(), 0, 0, arrayListLink);

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
    }
}