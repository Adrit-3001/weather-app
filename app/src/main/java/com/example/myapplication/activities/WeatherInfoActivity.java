//package com.example.myapplication.activities;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.myapplication.BuildConfig;
//import com.example.myapplication.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class WeatherInfoActivity extends AppCompatActivity {
//
//    private static final String OPEN_WEATHER_MAP_API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_weather_info);
//
//        // Retrieve the selected location from the Intent
//        String selectedLocation = getIntent().getStringExtra("selectedLocation");
//
//        // Display the selected location in a TextView
//        TextView locationTextView = findViewById(R.id.locationTextView);
//        locationTextView.setText(selectedLocation);
//
//        // Fetch and display weather information for the selected location
//        fetchWeatherInfo(selectedLocation);
//    }
//
//    private void fetchWeatherInfo(String location) {
//        // Construct the API URL with the location and API key
//        String apiUrl = BASE_URL + "?q=" + location + "&appid=" + OPEN_WEATHER_MAP_API_KEY;
//
//        // You can use a networking library like Retrofit or Volley for making API requests.
//        // Here, I'll use a simple AsyncTask for demonstration purposes.
//        new WeatherInfoTask().execute(apiUrl);
//    }
//
//    private class WeatherInfoTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            // Perform the API request and return the response
//            return NetworkUtils.fetchData(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            if (result != null) {
//                try {
//                    // Parse the JSON response
//                    JSONObject jsonObject = new JSONObject(result);
//
//                    // Extract relevant weather information
//                    String weatherDescription = jsonObject.getJSONArray("weather")
//                            .getJSONObject(0).getString("description");
//
//                    // Display weather information in a TextView
//                    TextView weatherTextView = findViewById(R.id.weatherTextView);
//                    weatherTextView.setText("Weather: " + weatherDescription);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                // Handle error in fetching data
//                Toast.makeText(WeatherInfoActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}
//
// WeatherInfoActivity.java
package com.example.myapplication.activities;
import com.example.myapplication.NetworkUtils;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class WeatherInfoActivity extends AppCompatActivity {

    private static final String OPEN_WEATHER_MAP_API_KEY = "4ca602083748b203b642802697a476c5";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        // Retrieve the selected location from the Intent
        String selectedLocation = getIntent().getStringExtra("selectedLocation");

        // Display the selected location in a TextView
        TextView locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(selectedLocation);

        // Fetch and display weather information for the selected location
        fetchWeatherInfo(selectedLocation);
    }

    private void fetchWeatherInfo(String location) {
        // Construct the API URL with the location and API key
        String apiUrl = BASE_URL + "?q=" + location + "&appid=" + OPEN_WEATHER_MAP_API_KEY;

        // You can use a networking library like Retrofit or Volley for making API requests.
        // Here, I'll use a simple AsyncTask for demonstration purposes.
        try {
            String result = new WeatherInfoTask().execute(apiUrl).get();
            if (result != null) {
                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(result);

                // Extract relevant weather information
                String weatherDescription = jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("description");

                // Display weather information in a TextView
                TextView weatherTextView = findViewById(R.id.weatherTextView);
                weatherTextView.setText("Weather: " + weatherDescription);

            } else {
                // Handle error in fetching data
                Toast.makeText(this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class WeatherInfoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Perform the API request and return the response
            return NetworkUtils.fetchData(params[0]);
        }
    }
}
