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
        View viewGroup = inflater.inflate(R.layout.fragment_links_whats_app, container, false);
        ListView listView = (ListView) viewGroup.findViewById(R.id.list_view_link);
        // String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<LinksToWhatsApp> arrayListLink = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LinksToWhatsApp");

        final LinksToWhatsAppAdapter linksToWhatsAppAdapter = new LinksToWhatsAppAdapter(getContext(), 0, 0, arrayListLink);

        listView.setAdapter(linksToWhatsAppAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayListLink.add(child.getValue(LinksToWhatsApp.class));
                }

                linksToWhatsAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return viewGroup;

    }}