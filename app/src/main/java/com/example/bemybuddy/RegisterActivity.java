package com.example.bemybuddy;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button reg_button;
    private EditText fname;
    private EditText lname;
    private  EditText reg_email;
    private  EditText reg_pass;
    private EditText reg_desp;
    private FirebaseAuth mAuth;
    ImageView profilepic;
    Uri imgUri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef;
    private StorageReference mStorageRef;
    static int REQUESTCODE = 1 ;
    private String user_id;
    private Object Bitmap;
    User user;
    String picurl;
    Uri pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        reg_email = (EditText) findViewById(R.id.emailreg);
        reg_pass =  (EditText) findViewById(R.id.passreg);
        reg_button = (Button) findViewById(R.id.reg_button);
        reg_desp = (EditText) findViewById(R.id.reg_desp) ;
        reg_button.setOnClickListener(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();




        mStorageRef = FirebaseStorage.getInstance().getReference();

        profilepic = findViewById(R.id.regUserPhoto) ;
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opens the user gallery
                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT);
                picIntent.setType("image/*");
                startActivityForResult(picIntent,REQUESTCODE);
            }
        });







    }
    public void register(){
        final String user_email = reg_email.getText().toString().trim();
        String password = reg_pass.getText().toString().trim();
        final String first_name = fname.getText().toString().trim();
        final String last_name = lname.getText().toString().trim();
        final String descrip = reg_desp.getText().toString().trim();

        // check for first name
        if (first_name.isEmpty()) {
            fname.setError("First name is required");
            fname.requestFocus();
            return;
        }

        // check for last name
        if (last_name.isEmpty()) {
            lname.setError("Last name is required");
            lname.requestFocus();
            return;
        }



        //checks if user email and password is empty and makes sure they are not.
        if (user_email.isEmpty()){
            reg_email.setError("Email is required");
            reg_email.requestFocus();
            return;
        }

        //checks if it is a correct email format.
        if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            reg_email.setError("Please enter a valid email!");
            reg_email.requestFocus();
            return;

        }
        //makes sure password is at least 6 letters long
        if (password.length() < 6){
            reg_pass.setError("Password must be at least 6 letters long!");
            reg_pass.requestFocus();
            return;
        }

        //https://www.youtube.com/watch?v=WP5yBHYxaiw

        final Map user_info = new HashMap();
        /*
        if (imgUri != null){
            Bitmap bitmap = null;
            mStorageRef = FirebaseStorage.getInstance().getReference().child("pro_pics");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream compress = new ByteArrayOutputStream();
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 20, compress);
            byte [] data = compress.toByteArray();
            UploadTask uploadTask = mStorageRef.putBytes(data);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    Map newimg = new HashMap();
                    newimg.put("profileuri",uriTask.toString());
                    userRef.updateChildren(newimg);

                }
            });

        }
        else {
            finish();
        }
        */
        mStorageRef = FirebaseStorage.getInstance().getReference().child("pro_pics");
        final StorageReference imgfilepath = mStorageRef.child(imgUri.getLastPathSegment());
        imgfilepath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //Toast.makeText(getApplicationContext(),pic.toString(), Toast.LENGTH_LONG).show();


                imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        picurl = uri.toString();
                        Toast.makeText(getApplicationContext(),picurl, Toast.LENGTH_LONG).show();

                        //profile pic image is the uri
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(first_name).setPhotoUri(imgUri).build();
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

        mAuth.createUserWithEmailAndPassword(user_email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User Registered!", Toast.LENGTH_LONG).show();
                    //picurl = pic.toString();
                    User user = new User(first_name,last_name,user_email,descrip,picurl);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    Toast.makeText(getApplicationContext(),picurl, Toast.LENGTH_LONG).show();


                    user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference userRef = database.getReference("Users").child(user_id);

                    userRef.setValue(user);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Register Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == Activity.RESULT_OK){
            final Uri user_Uri =  data.getData();
            imgUri = user_Uri;

            profilepic.setImageURI(imgUri);



        }
    }

    @Override
    public void onClick(View v) {
        if (v == reg_button){
            register();

            Toast.makeText(this,"User Registered!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));

        }

    }
}
