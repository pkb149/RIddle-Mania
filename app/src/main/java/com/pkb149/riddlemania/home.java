package com.pkb149.riddlemania;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class home extends Activity {
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Toast.makeText(getApplicationContext(),"LoggedIn",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), loggedInDashboard.class));
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Log in",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }

        /*
        listner to get current user
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){

                    Toast.makeText(getApplicationContext(),"Please Login to Proceed",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), loggedInDashboard.class));
                    Toast.makeText(getApplicationContext(),"Already Logged in",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };*/

    }
}
