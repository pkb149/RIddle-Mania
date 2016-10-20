package com.pkb149.riddlemania;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class loggedInDashboard extends Activity {
    private static int energy;
    DatabaseReference ref;

    SharedPreferences  sharedPref;//= getAppContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor;//= sharedPref.edit();
    TextView energyTextView;//=(TextView)findViewById(R.id.energyTextView);
    TextView test;
    FirebaseUser user;// = FirebaseAuth.getInstance().getCurrentUser();
    //SharedPreferences sharedPref = PreferenceManager
    //        .getDefaultSharedPreferences(this);// try another context once
    //SharedPreferences.Editor editor = sharedPref.edit();

    private Button button1;
    private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String email;
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_dashboard);
        email = user.getEmail();
        String temp= email.replace("@","");
        final String UID= temp.replace(".","");
        energyTextView=(TextView)findViewById(R.id.energyTextView);
        sharedPref= this.getSharedPreferences("myData", Context.MODE_PRIVATE);
        editor= sharedPref.edit();
        test=(TextView) findViewById(R.id.test);
        //String s=ref.child("users").child("pkb149hotmailcom").child("energy").toString();
        //test.setText(s);
        //Map a= ServerValue.TIMESTAMP;
        //String b= a.get(".sv").toString();// getting timestamp instead of actual value.
        //long c =Long.parseLong(b);//addListenerForSingleValueEvent//addListenerForSingleValueEvent
        button1= (Button)findViewById(R.id.signOut);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finishAffinity();
            }
        });

        //AsyncTaskRunner runner = new AsyncTaskRunner();
        //runner.execute();
        // id will be -1 if energy key is not present
        //get energy back from Async task

        energy = sharedPref.getInt("energy", -1);
        if(energy !=-1) {
            // energy key is already present, so we have got real energy value
            // now implement business logic
            if(energy<60)
            {
                // fetch time from local device
                Date currenttime= new Date();
                //System.currentTimeMillis();

                Long timeOld= sharedPref.getLong("time",-1);
                long totalTime = (currenttime.getTime() - timeOld)/1000;// 10000 instead of timeOld
                Toast.makeText(getApplicationContext(),
                        "You will get 15 unit energy after :"+(3600-totalTime)/60+" mins & "+(3600-totalTime)%60+" secs"
                        ,Toast.LENGTH_LONG).show();

                if(totalTime>36){
                    //update energy 15 per hour by xaluation difference in time from server

                    //check server time difference
                    //if true
                   // Map l= ServerValue.TIMES


                    ref.child("users").child(UID).child("Time_New").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final long time_new= dataSnapshot.getValue(long.class);
                            ref.child("users").child(UID).child("Time_Old").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    long time_old=dataSnapshot.getValue(long.class);
                                    long diff=(time_new-time_old)/60000;
                                    if(diff>240){
                                        energy=60;
                                    }
                                    else if(diff>180){
                                        int temp=energy+45;
                                        if(temp>60){
                                            energy=60;
                                        }
                                        else {
                                            energy = temp;
                                        }
                                    }
                                    else if(diff>120){
                                        int temp=energy+30;
                                        if(temp>60){
                                            energy=60;
                                        }
                                        else {
                                            energy = temp;
                                        }
                                    }
                                    else if(diff>60){
                                        int temp=energy+15;
                                        if(temp>60){
                                            energy=60;
                                        }
                                        else {
                                            energy = temp;
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"wait or buy energy", Toast.LENGTH_LONG);
                                    }
                                    editor.putLong("time",System.currentTimeMillis());
                                    editor.putInt("energy",energy);
                                    editor.apply();
                                    ref.child("users").child(UID).child("energy").setValue(energy);
                                    //ref.child("users").child(UID).child("Time_New").setValue(ServerValue.TIMESTAMP);
                                    ref.child("users").child(UID).child("Time_Old").setValue(ServerValue.TIMESTAMP);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    //update remote db as well
                    }
            }
            else{
                Long timeOld= sharedPref.getLong("time",-1);
                long totalTime = (System.currentTimeMillis() - timeOld)/1000;// 10000 instead of timeOld
                Toast.makeText(getApplicationContext(),"You will get 15 unit energy after :"+(3600-totalTime)/60+" mins & "+(3600-totalTime)%60+" secs" ,Toast.LENGTH_LONG).show();
            }
        }
        else {
            ref.child("users").child(UID).child("energy").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Object value= snapshot.getValue(Object.class);

                    if(value==null){
                        Toast.makeText(getApplicationContext(),"NULL",Toast.LENGTH_SHORT).show();
                        energy=60;
                        Date currenttime= new Date();
                        editor.putLong("time",currenttime.getTime());
                        editor.putInt("energy",energy);
                        editor.apply();
                        if (user != null) {
                            ref.child("users").child(UID).child("energy").setValue(energy);
                           // ref.child("users").child(UID).child("Time_New").setValue(ServerValue.TIMESTAMP);
                            ref.child("users").child(UID).child("Time_Old").setValue(ServerValue.TIMESTAMP);
                        }
                    }
                    else{// if user in installing app again, but his account is already  in firebase db
                        //update time new using ServerValue.TIMESTAMP
                        ref.child("users").child(UID).child("Time_New").setValue(ServerValue.TIMESTAMP);
                        //energy=(int)(long)value;
                        ref.child("users").child(UID).child("Time_New").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final long time_new= dataSnapshot.getValue(long.class);
                                ref.child("users").child(UID).child("Time_Old").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        long time_old=dataSnapshot.getValue(long.class);
                                        long diff=(time_new-time_old)/60000;
                                        if(diff>240){
                                            energy=60;
                                        }
                                        else if(diff>180){
                                            int temp=energy+45;
                                            if(temp>60){
                                                energy=60;
                                            }
                                            else {
                                                energy = temp;
                                            }
                                        }
                                        else if(diff>120){
                                            int temp=energy+30;
                                            if(temp>60){
                                                energy=60;
                                            }
                                            else {
                                                energy = temp;
                                            }
                                        }
                                        else if(diff>60){
                                            int temp=energy+15;
                                            if(temp>60){
                                                energy=60;
                                            }
                                            else {
                                                energy = temp;
                                            }
                                        }
                                        editor.putLong("time",System.currentTimeMillis());
                                        editor.putInt("energy",energy);
                                        editor.apply();
                                        ref.child("users").child(UID).child("energy").setValue(energy);
                                        //ref.child("users").child(UID).child("Time_New").setValue(ServerValue.TIMESTAMP);
                                        ref.child("users").child(UID).child("Time_Old").setValue(ServerValue.TIMESTAMP);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        // read the timestamp from server and update energy
                    }
                    test.setText(String.valueOf(value));
                    energyTextView.setText(String.valueOf(energy));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        energyTextView.setText(String.valueOf(energy));
        button2= (Button)findViewById(R.id.gamePlay);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(energy>14) {
                    energy = energy - 15;
                    ref.child("users").child(UID).child("energy").setValue(energy);
                    editor.putInt("energy", energy);
                    editor.apply();
                    energyTextView.setText(String.valueOf(energy));
                }
                else{
                    Toast.makeText(getApplicationContext(),"energy is "+energy, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //@Override
   /* protected void onResume(){
        super.onResume();
        int energy = sharedPref.getInt("energy", -1);// id will be -1 if energy key is not present
            if (energy < 60) {
                // fetch time from local device
                Date currenttime = new Date();
                //long millis = date.getTime();
                Long timeOld = sharedPref.getLong("time", -1);
                long totalTime = (currenttime.getTime() - timeOld) / 1000;
                if (totalTime > 3600) {
                    //
                    //check server time difference
                    //if true
                    energy = energy + 15;
                    editor.putLong("time", currenttime.getTime());
                    editor.putInt("energy", energy);
                    editor.commit();
                    //update remote db as well
                }
            }

    }*/

    /*class AsyncTaskRunner extends AsyncTask<String , String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            energy = sharedPref.getInt("energy", -1);

            return energy;
        }

        protected int onPostExecute( int energy) {
            Toast.makeText(getApplicationContext(),"Async",Toast.LENGTH_SHORT).show();
            return energy;

        }
    }*/


}




