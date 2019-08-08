package com.example.bemybuddy;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Cards card[];
    private ArrayAdapter arrayAdapter;
    private int i;
    private FloatingActionButton addEvent;
    FirebaseAuth mAuth;
    ListView listView;
    List<Cards> cardsList;
    private  DatabaseReference eventdb;
    TextView key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addEvent = (FloatingActionButton) findViewById(R.id.add_event);
        addEvent.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        eventdb =  FirebaseDatabase.getInstance().getReference().child("Events");
        key = (TextView) findViewById(R.id.key);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navView);

        bnv.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        //Toast.makeText(getApplicationContext(), "menu", Toast.LENGTH_LONG).show();


                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                // do nothing because we're already here
                                //




                            case R.id.inbox:
                                Toast.makeText(getApplicationContext(), "i", Toast.LENGTH_LONG).show();
                                Intent intent3 = new Intent(HomeActivity.this, Inbox.class);
                                startActivity(intent3);
                                //break;

                            case R.id.profile:
                                //Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_LONG).show();
                                //Intent intent3;
                                //intent3 = new Intent(Message.this, ProfileActivity.class);
                                //tartActivity(intent3);
                                //break;

                        }
                        return false;
                    }
                }
        );








        cardsList = new ArrayList<Cards>();

        /*al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");
        */

        arrayAdapter = new arrayAdapter(this, R.layout.item, cardsList );

        final DatabaseReference eventdb= FirebaseDatabase.getInstance().getReference().child("Events");
        eventdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //String user_id = mAuth.getCurrentUser().getUid();
                if((dataSnapshot.exists() && !dataSnapshot.child("connections").child("no").hasChild(mAuth.getUid()) && !dataSnapshot.child("connections").child("yes").hasChild(mAuth.getUid()) )){
                    //al.add(dataSnapshot.child("email").getValue().toString());

                    //Toast.makeText(HomeActivity.this, dataSnapshot.child("date").getValue().toString(),Toast.LENGTH_LONG).show();
                    String name = dataSnapshot.child("user").getValue().toString();
                    String date = dataSnapshot.child("date").getValue().toString();
                    String desc = dataSnapshot.child("event_descrp").getValue().toString();
                    String user_id = dataSnapshot.child("userId").getValue().toString();
                    String picUrl = dataSnapshot.child("imgUri").getValue().toString();


                    Cards new_card = new Cards(user_id,name,desc,date,picUrl);

                    cardsList.add(new_card);





                    arrayAdapter.notifyDataSetChanged();








                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SwipeFlingAdapterView flingContainer =  (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(HomeActivity.this, "left!",Toast.LENGTH_LONG).show();
                Cards curr_card = (Cards) dataObject;
                String userId = curr_card.getUserId();
                String name = curr_card.getName();
                String date = curr_card.getDate();
                String desc = curr_card.getDate();

                //when u swipe left it changes the connection of the current user id and the event tht was selected to no!!!
                //Toast.makeText(HomeActivity.this, curr_card.getUserId(),Toast.LENGTH_LONG).show();
                //eventdb.child(curr_card.getUserId()).child("connections").child("no").child(mAuth.getUid()).setValue(true);
                eventdb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String key = snapshot.getKey();

                            //Toast.makeText(HomeActivity.this, key,Toast.LENGTH_LONG).show();

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(HomeActivity.this, "Right!",Toast.LENGTH_LONG).show();
                Cards curr_card = (Cards) dataObject;
                String userId = curr_card.getUserId();
                //eventdb.child(curr_card.getUserId()).child("connections").child("yes").child(mAuth.getUid()).setValue(true);
                SharedPreferences sharedPreferences;

                eventdb.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            key.setText(snapshot.getKey());

                            //

                            //DatabaseReference matchdb = eventdb.child(key).child("connections").child("yes");

                            //eventdb.child(key).child("connections").child("yes").child(mAuth.getUid()).child("chatid").setValue(chatKey);

                            //Toast.makeText(HomeActivity.this, key,Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(getApplicationContext(), key.getText(),Toast.LENGTH_LONG).show();
                        String chatKey = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                        eventdb.child(key.getText().toString()).child("connections").child("yes").child(mAuth.getUid()).child("chatid").setValue(chatKey);





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            private void addApproved(String key) {
                eventdb.child(key);

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });



        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Cards curr_card = (Cards) dataObject;



            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == addEvent){
            Intent event = new Intent(HomeActivity.this, new_event.class);
            startActivity(event);

        }

    }
}
