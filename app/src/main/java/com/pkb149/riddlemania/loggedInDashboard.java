package com.pkb149.riddlemania;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import java.util.Date;


public class loggedInDashboard extends Activity {
    private int energy;

    //
    SharedPreferences  sharedPref;//= getAppContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor;//= sharedPref.edit();
    TextView energyTextView;//=(TextView)findViewById(R.id.energyTextView);
    //SharedPreferences sharedPref = PreferenceManager
    //        .getDefaultSharedPreferences(this);// try another context once
    //SharedPreferences.Editor editor = sharedPref.edit();

    private Button button1;
    private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_dashboard);
        energyTextView=(TextView)findViewById(R.id.energyTextView);
        sharedPref= this.getSharedPreferences("myData", Context.MODE_PRIVATE);
        editor= sharedPref.edit();
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
                //Long timeOld= sharedPref.getLong("time",-1);
                long totalTime = (currenttime.getTime() - 10000)/1000;// 10000 instead of timeOld
                if(totalTime>3600){
                    //
                    //check server time difference
                    //if true
                    energy=energy+15;
                    editor.putLong("time",currenttime.getTime());// gettime converts time into millis
                    editor.putInt("energy",energy);
                    editor.commit();
                    //update remote db as well
                    energyTextView.setText(Integer.toString(energy));
                }
            }
            else {
                energyTextView.setText(Integer.toString(energy));
            }
        }
        else {
            energy=60;
            Date currenttime= new Date();
            editor.putLong("time",currenttime.getTime());
            editor.putInt("energy",energy);
            editor.commit();
            energyTextView.setText(Integer.toString(energy));
            //update remote db as well
        }
        button2= (Button)findViewById(R.id.gamePlay);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //updateEnergyGamePlay();
                energy=energy-15;
                editor.putInt("energy",energy);
                editor.commit();

                energyTextView.setText(Integer.toString(energy));
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




