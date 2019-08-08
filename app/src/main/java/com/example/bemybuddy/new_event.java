package com.example.bemybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class new_event extends AppCompatActivity implements View.OnClickListener {
    Button add_event;
    EditText event_desc;
    EditText event_photo;
    EditText event_date;
    ImageView event_pic;
    EditText event_holders;
    Uri event_uri;
    FirebaseAuth mAuth;
    Event event;
    private String user_id;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eventRef;

    private StorageReference mStorageRef;
    ArrayList<String> joiners;
    private String picurl;
    private String event_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        event_date = (EditText) findViewById(R.id.event_date);
        event_desc = (EditText) findViewById(R.id.event_desc);
        mAuth = FirebaseAuth.getInstance();
        event_holders = (EditText) findViewById(R.id.event_holders);
        ArrayList<String> joiners = new ArrayList<String>();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        add_event = (Button) findViewById(R.id.add_button);
        add_event.setOnClickListener(this);
        event_pic = findViewById(R.id.event_photo);
        event_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT);
                picIntent.setType("image/*");
                startActivityForResult(picIntent,1);
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navView);

        bnv.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                // do nothing because we're already here
                                //Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_LONG).show();
                                Intent intent1;
                                intent1 = new Intent(new_event.this, HomeActivity.class);
                                startActivity(intent1);
                                break;



                            case R.id.inbox:
                                //Toast.makeText(getApplicationContext(), "inbox", Toast.LENGTH_LONG).show();
                                Intent intent3 = new Intent(new_event.this, Inbox.class);
                                startActivity(intent3);
                                break;

                            case R.id.profile:
                                //Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_LONG).show();
                                //Intent intent3;
                                //intent3 = new Intent(Message.this, ProfileActivity.class);
                                //startActivity(intent3);
                                break;

                        }
                        return false;
                    }
                }
        );



    }
    public  void event_adder(){
        final String date = event_date.getText().toString().trim();
        final String desc = event_desc.getText().toString().trim();
        final  String holders = event_holders.getText().toString().trim();
        if (date.isEmpty()){
            event_date.setError("date is required");
            event_date.requestFocus();
        }
        if (desc.isEmpty()){
            event_desc.setError("date is required");
            event_desc.requestFocus();
        }
        if (holders.isEmpty()){
            event_holders.setError("date is required");
            event_holders.requestFocus();
        }
        user_id = mAuth.getCurrentUser().getUid();
        //add evernt 2 database






        //adds photto to database

        mStorageRef = FirebaseStorage.getInstance().getReference().child("event_pics");
        final StorageReference imgfilepath = mStorageRef.child(event_uri.getLastPathSegment());
        imgfilepath.putFile(event_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //profile pic image is the uri
                        picurl = uri.toString();

                        Event new_event= new Event(holders,desc, date, user_id,picurl );
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference("Events");
                        event_id = databaseReference.push().getKey();



                        DatabaseReference eventRef = database.getReference("Events");

                        eventRef.child(event_id).setValue(new_event);
                        //Toast.makeText(getApplicationContext(),picurl, Toast.LENGTH_SHORT).show();
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(mAuth.getCurrentUser().getDisplayName()).setPhotoUri(event_uri).build();
                        mAuth.getCurrentUser().updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"pic uploaded", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"pic failed", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }
                });

            }
        });




        //Toast.makeText(getApplicationContext(),picurl, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri new_Uri =  data.getData();
            event_uri = new_Uri;
            event_pic.setImageURI(event_uri);



        }
    }


    @Override
    public void onClick(View v) {
        if(v == add_event){
            event_adder();

            Intent added_event = new Intent(this,HomeActivity.class);

            startActivity(added_event);
        }
    }
}
