package com.example.myapplication.activities;

import android.os.AsyncTask;

import com.example.myapplication.NetworkUtils;

public class WeatherTask extends AsyncTask<String, Void, String> {

    private OnTaskCompleted listener;

    public WeatherTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        // Perform the API request and return the response
        return NetworkUtils.fetchData(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Notify the listener with the result
        if (listener != null) {
            listener.onTaskCompleted(result);
        }
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
}
