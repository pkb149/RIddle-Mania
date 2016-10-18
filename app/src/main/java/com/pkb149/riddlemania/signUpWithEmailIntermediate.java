package com.pkb149.riddlemania;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUpWithEmailIntermediate extends Activity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up_with_email_intermediate);
        Bundle bundle = getIntent().getExtras();
        final String email = bundle.getString("email");
        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
        final String password= bundle.getString("password");
        final String name= bundle.getString("name");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), signUpWithEmail.class));
                            finish();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Sign Up Successful",
                                    Toast.LENGTH_SHORT).show();
                            //add Name to database
                            String UID= email.replace("@","");
                            UID= UID.replace(".","");
                            mDatabase.child("users").child(UID).child("Name").setValue(name);
                            startActivity(new Intent(getApplicationContext(), loggedInDashboard.class));
                            finishAffinity();
                        }
                    }
                });
    }
}
