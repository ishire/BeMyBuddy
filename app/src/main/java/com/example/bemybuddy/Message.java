package com.example.bemybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Context context;
    private ArrayList<messageobj> chatArrayList = new ArrayList<messageobj>();
    private List<messageobj> getData(){ return chatArrayList;}
    RecyclerView recyclerView;
    messageAdapter adapter;
    private String uid,cid,key,name,eventid;
    private EditText message;
    private Button sendButton;
    DatabaseReference databaseReferenceuser, mdatabaseReferencechat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.messageview);
        uid = mAuth.getCurrentUser().getUid();
        cid = getIntent().getExtras().getString("cid");
        key = getIntent().getExtras().getString("id");
        eventid = getIntent().getExtras().getString("eventid");
        sendButton = (Button) findViewById(R.id.sendButton);
        message = (EditText) findViewById(R.id.message) ;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }


        });
        databaseReferenceuser = FirebaseDatabase.getInstance().getReference().child("Events").child(eventid).child("connections").child("yes").child(uid).child(cid);
        mdatabaseReferencechat = FirebaseDatabase.getInstance().getReference().child("Chat");
        mdatabaseReferencechat = mdatabaseReferencechat.child(cid);

        mdatabaseReferencechat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    //Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                    String message = null;
                    String createdByUser = null;

                    if (dataSnapshot.child("message").getValue() != null ){
                        message = dataSnapshot.child("message").getValue().toString();
                        //Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();

                    }
                    if (dataSnapshot.child("createdByUser").getValue() != null ){

                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                        Toast.makeText(getApplicationContext(),createdByUser,Toast.LENGTH_LONG).show();

                    }
                    if(message != null  && createdByUser != null ){
                        Boolean currentBool = false;
                        if(createdByUser.contains(mAuth.getCurrentUser().getUid())){
                            currentBool = true;



                        }

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setNestedScrollingEnabled(true);
                        adapter = new messageAdapter(getApplicationContext(),chatArrayList);
                        recyclerView.setAdapter(adapter);

                        messageobj messageobj2 = new messageobj(message, currentBool);
                        chatArrayList.add(messageobj2);
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(getApplicationContext(),chatArrayList.get(0).getMessage(), Toast.LENGTH_LONG).show();


                    }
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

        //





        //getMessages();
        //


    }


    private void sendMessage() {
        String sendText = message.getText().toString();
        if (!sendText.isEmpty()){
            DatabaseReference newmessagedb = mdatabaseReferencechat.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser", mAuth.getCurrentUser().getUid());
            newMessage.put("message", sendText);
            newmessagedb.setValue(newMessage);
        }
        message.setText(null);

    }
    private void getcid(){

    }
}
