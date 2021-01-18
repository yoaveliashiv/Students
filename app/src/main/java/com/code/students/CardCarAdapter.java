package com.code.students;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.code.R;

import java.util.List;


public class CardCarAdapter extends ArrayAdapter<CardTutor> {

    private ImageView ivProduct;
    private Context context;
    private List<CardTutor> objects;
    private CardTutor temp;
    private boolean go = false;
    private View view;
    private boolean flagSee=false;
    int flag=-1;


    public CardCarAdapter(Context context, int resource, int textViewResourceId, List<CardTutor> objects) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = false;
        this.context = context;
        this.objects = objects;

    }

    public CardCarAdapter(Context context, int resource, int textViewResourceId, List<CardTutor> objects, boolean flagSee) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = flagSee;
        this.context = context;
        this.objects = objects;

    }

    public CardCarAdapter(Context context, int resource, int textViewResourceId, List<CardTutor> objects, boolean flagSee,int flagMange) {
        super(context, resource, textViewResourceId, objects);
        this.flagSee = flagSee;
        this.context = context;
        this.objects = objects;
        this.flag=flagMange;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.activity_card_list, parent, false);

        ivProduct = (ImageView) view.findViewById(R.id.imageViewListCard);
        temp = objects.get(position);
        String url = "";
        if (temp.getImageViewArrayListName().size() > 0) {
            url = temp.getImageViewArrayListName().get(0);
            Glide.with(view)
                    .load(url)
                    .into(ivProduct);

        }
        TextView phone = (TextView) view.findViewById(R.id.phoneListCard);

        TextView permission = (TextView) view.findViewById(R.id.textViewPermission);
        TextView price = (TextView) view.findViewById(R.id.priceDayListCard);
        TextView area = (TextView) view.findViewById(R.id.areaListCard);
        TextView remark = (TextView) view.findViewById(R.id.remarksListCard);
        TextView typeCar = (TextView) view.findViewById(R.id.typeCarListCard);
        TextView seeMe = (TextView) view.findViewById(R.id.textViewSeeMe);
        ImageView imageSee = (ImageView) view.findViewById(R.id.imageViewSee);

        if(flagSee){
    seeMe.setText(""+temp.getSeeCard());
    seeMe.setVisibility(View.VISIBLE);
    imageSee.setVisibility(View.VISIBLE);
}
        if (temp.getPermissionToPublish() == 1) permission.setVisibility(View.GONE);
        else if (temp.getPermissionToPublish() == 0) {
            permission.setText("הכרטיס ממתין לאישור");
            permission.setVisibility(View.VISIBLE);
        } else if (temp.getPermissionToPublish() == 2) {
            permission.setText("הכרטיס סורב");
            permission.setVisibility(View.VISIBLE);
        }
        phone.setText("פלאפון: " + temp.getPhone());
        price.setText("מחיר ליום: " + temp.getPriceToLesson());
        area.setText("אזור " + temp.getArea() + ": " + temp.getCity());

        if (!temp.getRemarks().equals(""))
            remark.setText("הערות: " + temp.getRemarks());
        else remark.setText("");

        ListView lv1 = (ListView) view.findViewById(R.id.listViewChat);
        ExpertiseAdapter expertiseAdapter = new ExpertiseAdapter(context, 0, 0, temp.getArrayListExpertise());
        lv1.setAdapter(expertiseAdapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (flag==0){
                   Intent intent = new Intent(context, MainActivityRegisterTutor.class);
                   intent.putExtra("position", position);
                   MainActivity1.pass(intent, objects.get(position));
                   intent.putExtra("flagEdit", true);
                   ((Activity) context).startActivityForResult(intent,0);
               }
               else  if (flag==1){
                   Intent intent = new Intent(context, MainActivityRegisterTutor.class);
                   intent.putExtra("position", position);
                   MainActivity1.pass(intent, objects.get(position));
                   intent.putExtra("flagManagement", true);
                   intent.putExtra("flagEdit", false);
                   intent.putExtra("flagMain", true);
                   ((Activity) context).startActivityForResult(intent,0);
               }
               else  if (flag==2){
                   Intent intent = new Intent(context, MainActivityCardView.class);
                   intent.putExtra("position", position);
                   MainActivity1.pass(intent, objects.get(position));
                   intent.putExtra("flagMain", true);
                   ((Activity) context).startActivityForResult(intent,0);
               }

            }
        });
        return view;
    }


}