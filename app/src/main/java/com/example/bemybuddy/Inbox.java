package com.example.bemybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Inbox extends AppCompatActivity {
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Context context;
    private ArrayList<User> userArrayList = new ArrayList<User>();
    RecyclerView recyclerView;
    InboxAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.inboxview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {





                if(dataSnapshot.exists() ){

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final Event new_event = snapshot.getValue(Event.class);

                        if(new_event.getUserId().contains(mAuth.getCurrentUser().getUid())){

                            if(snapshot.child("connections").hasChild("yes")){
                                String key = snapshot.child("connections").child("yes").getValue().toString();
                                final String keyer = key.substring(1,29);
                                //Toast.makeText(getApplicationContext(), keyer,Toast.LENGTH_LONG).show();



                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                            User newuser = dataSnapshot1.getValue(User.class);
                                            //Toast.makeText(getApplicationContext(), dataSnapshot1.getKey(),Toast.LENGTH_LONG).show();
                                            if(dataSnapshot1.getKey().contains(keyer)){
                                                //Toast.makeText(getApplicationContext(), dataSnapshot1.getKey(),Toast.LENGTH_LONG).show();
                                                 User user = new User(newuser.getFname(),newuser.getLname(),newuser.getEmail(),newuser.getDescp(),newuser.getPicUrl());


                                                userArrayList.add(user);

                                                adapter = new InboxAdapter(getApplicationContext(), userArrayList);
                                                recyclerView.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "approved",Toast.LENGTH_LONG).show();
                        }






                    }

                }

                //Toast.makeText(getApplicationContext(), userArrayList.get(0).toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });





    }
}
