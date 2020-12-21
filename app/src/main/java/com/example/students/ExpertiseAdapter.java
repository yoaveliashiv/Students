package com.example.students;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.example.R;

public class ExpertiseAdapter extends ArrayAdapter<String> {

    private ImageView ivProduct;
    private Context context;
    private List<String> listExpertise;
    private boolean go = false;
    private View view;
    private boolean flagSee = false;


    public ExpertiseAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);

        this.flagSee = false;
        this.context = context;
        this.listExpertise = objects;

    }

    public ExpertiseAdapter(Context context, int resource, int textViewResourceId, List<String> objects, boolean flagSee) {
        super(context, resource, textViewResourceId);
        this.flagSee = flagSee;
        this.context = context;
        this.listExpertise = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.expertise_list, parent, false);

        TextView expertise = (TextView) view.findViewById(R.id.textViewExpertise);
        expertise.setText("מקצוע: "+listExpertise.get(position));

        return view;
    }


}