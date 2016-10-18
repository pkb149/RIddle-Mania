package com.pkb149.riddlemania;

import android.app.Activity;
import android.content.Intent;
import android.media.effect.Effect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUpWithEmail extends Activity {
    private EditText name;
    private EditText email;
    private EditText password;

    private Button button;
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up_with_email);
        // Things to Do
        // validate email format
        // check if email already exists
        // name , email and password are mendatory fields

        //Sign UP, if everything is fine. and display a Toast and take user to a new activity
        name= (EditText)findViewById(R.id.name_signup);
        email=(EditText)findViewById(R.id.email_signup);
        password=(EditText)findViewById(R.id.password_signup);
        button=(Button)findViewById(R.id.signup_signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Name is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if(email.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Email is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Password is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(email.getText().toString().trim()))
                {
                    Toast.makeText(getApplicationContext(),"Email Format is Invalid", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent= new Intent(getApplicationContext(), signUpWithEmailIntermediate.class);
                    intent.putExtra("email", email.getText().toString().trim());
                    intent.putExtra("password", password.getText().toString().trim());
                    intent.putExtra("name", name.getText().toString().trim());
                    startActivity(intent);

                }
            }
        });




    }
}
