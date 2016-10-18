package com.pkb149.riddlemania;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginWithEmail extends Activity {
    private EditText email_login;
    private EditText password_login;
    private Button signin_login;
   // private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;


    private void Login(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Invalid Email/Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Sign In Successful",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), loggedInDashboard.class));
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finishAffinity();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_with_email);
        email_login=(EditText) findViewById(R.id.email);
        password_login=(EditText)findViewById(R.id.password);
        signin_login=(Button)findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        signin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login(email_login.getText().toString().trim(),password_login.getText().toString().trim());
            }
        });
    }
}
