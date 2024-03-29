package com.example.bemybuddy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button signin;

    private EditText pass;

    private  EditText email;
    private TextView txtsignup;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = (Button) findViewById(R.id.logbutton);
        pass = (EditText) findViewById(R.id.loginpass);
        email = (EditText) findViewById(R.id.loginemail);

        txtsignup = (TextView) findViewById(R.id.regtextView);
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(this);
        txtsignup.setOnClickListener(this);
    }
    private void signIn(){

        String user_email = email.getText().toString().trim();
        String password = pass.getText().toString().trim();
        if (user_email.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (password.isEmpty()){
            pass.setError("Pass is required");
            pass.requestFocus();
            return;
        }
        //checks if it is a correct email format.
        if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            email.setError("Please enter a valid email!");
            email.requestFocus();
            return;

        }
        //makes sure password is atleast 6 letters long
        if (password.length() < 6) {
            pass.setError("Password must be at least 6 letters long!");
            pass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(user_email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    //intent.putExtras(new Bundle());
                    //Bundle bundle = intent.getExtras();
                    //bundle.putString("user_email", email.getText().toString().trim());

                    //intent.putExtras(bundle);
                    //CLOSE ALL OPEN ACTIVITES.
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == signin){
            signIn();
        }
        if (v == txtsignup){
            startActivity(new Intent(this,RegisterActivity.class));
        }

    }
}
