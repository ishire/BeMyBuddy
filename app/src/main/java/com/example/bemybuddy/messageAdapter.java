package com.example.bemybuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.messageViewHolder> {
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Context context;
    ArrayList<messageobj> chatArrayList;



    public class messageViewHolder extends RecyclerView.ViewHolder {


        public TextView message;

        public LinearLayout layout;


        public messageViewHolder(View itemView) {

            super(itemView);

            message = (TextView) itemView.findViewById(R.id.newmessage);
            layout = (LinearLayout) itemView.findViewById(R.id.container) ;


            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");


        }
    }

    public messageAdapter(Context context, ArrayList<messageobj> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;

    }

    @Override
    public messageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);

        return new messageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(messageViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        mAuth = FirebaseAuth.getInstance();
        Toast.makeText(context,chatArrayList.get(position).getMessage(),Toast.LENGTH_LONG).show();

        holder.message.setText(chatArrayList.get(position).getMessage());
        if (chatArrayList.get(position).getCurrentBool()){
            Toast.makeText(context,chatArrayList.get(position).getMessage(),Toast.LENGTH_LONG).show();
            holder.message.setGravity(Gravity.END);
            holder.message.setTextColor(context.getColor(R.color.cuteyerro));
            holder.layout.setBackgroundColor(context.getColor(R.color.deeppurple));


        }

        else {
            holder.message.setGravity(Gravity.START);
            holder. layout.setBackgroundColor(context.getColor(R.color.mainColor));

            holder.message.setTextColor(context.getColor(R.color.cuteyerro));

        }












    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }


}
