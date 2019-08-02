package com.example.bemybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards> {
    Context context;

    public arrayAdapter(Context context, int id, List<Cards> items) {
        super(context,id,items);
    }

    public View getView(int pos, View v, ViewGroup parent){
        Cards card_item = getItem(pos);
        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }
        TextView name = (TextView) v.findViewById(R.id.holders);
        TextView descp = (TextView) v.findViewById(R.id.desc);
        TextView date = (TextView) v.findViewById(R.id.date);
        ImageView img =( ImageView )v.findViewById(R.id.event_card_pic);
        name.setText(card_item.getName());
        date.setText(card_item.getDate());
        descp.setText(card_item.getDescp());
        //img.setImageResource(R.mipmap.ic_launcher_round);

        Glide.with(getContext()).load(card_item.getImgUrl()).into(img);

        return  v;




    }

}
