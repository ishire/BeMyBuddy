package com.example.bemybuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Context context;
    ArrayList<User> userArrayList;



    public class InboxViewHolder extends RecyclerView.ViewHolder {
        public ImageButton accept;
        public  ImageView pPic;
        public TextView acceptedName;
        public ImageButton reject;

        public InboxViewHolder(View itemView) {

            super(itemView);
            accept = (ImageButton) itemView.findViewById(R.id.accept);
            reject = (ImageButton) itemView.findViewById(R.id.reject);
            pPic = (ImageView) itemView.findViewById(R.id.acceptedPic);
            acceptedName = (TextView) itemView.findViewById(R.id.accepted_name);
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");


        }
    }

    public InboxAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;

    }

    @Override
    public InboxViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_item, parent, false);
        return new InboxViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InboxViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        mAuth = FirebaseAuth.getInstance();

        final User user = userArrayList.get(position);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        holder.acceptedName.setText(userArrayList.get(position).getFname());
        String img = userArrayList.get(position).getPicUrl();
        Picasso.get().load(img).into(holder.pPic);






        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user1 = userArrayList.get(position);
                        User selected = dataSnapshot.getValue(User.class);
                        //Toast.makeText(context, "approved",Toast.LENGTH_LONG).show();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            //Toast.makeText(context, snapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                            if(snapshot.child("connections").hasChild("yes")){
                                String key = snapshot.child("connections").child("yes").getValue().toString();

                                final String keyer = key.substring(1,29);

                                //Toast.makeText(context,snapshot.child("connections").child("yes").child(keyer).child("chatid").getValue().toString(),Toast.LENGTH_LONG).show();
                                //
                                Intent message = new Intent(context,Message.class);
                                Bundle b = new Bundle();
                                b.putString("eventid",snapshot.getKey());
                                b.putString("id", keyer);
                                b.putString("username", userArrayList.get(position).getEmail());
                                b.putString("fname", userArrayList.get(position).getFname());
                                b.putString("pic", userArrayList.get(position).getPicUrl());
                                //tToast.makeText(context,snapshot.child("connections").child("yes").child(keyer).child("chatid").getValue().toString(),Toast.LENGTH_LONG).show();
                                String chatid = snapshot.child("connections").child("yes").child(keyer).child("chatid").getValue().toString();
                                b.putString("cid",chatid);
                                message.putExtras( b);
                                context.startActivity(message);


                            }

                        }



                        notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent declined = new Intent(context,HomeActivity.class);
                context.startActivity(declined);

            }
        });




    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


}
