package com.pkb149.riddlemania;

import android.os.AsyncTask;

public class AsyncTaskRunner extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        return "abc";
    }
}