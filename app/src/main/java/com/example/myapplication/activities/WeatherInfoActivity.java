//package com.example.myapplication.activities;//// WeatherInfoActivity.java
//
//import java.util.TimeZone;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.myapplication.NetworkUtils;
//import com.example.myapplication.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Locale;
//import java.util.concurrent.ExecutionException;
//
//public class WeatherInfoActivity extends AppCompatActivity {
//
//    private static final String OPEN_WEATHER_MAP_API_KEY = "4ca602083748b203b642802697a476c5";
//    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
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
//        try {
//            String result = new WeatherInfoTask().execute(apiUrl).get();
//            if (result != null) {
//                // Parse the JSON response
//                JSONObject jsonObject = new JSONObject(result);
//
//                // Extract relevant weather information
//                String weatherDescription = jsonObject.getJSONArray("weather")
//                        .getJSONObject(0).getString("description");
//
//                // Extract temperature information
//                JSONObject main = jsonObject.getJSONObject("main");
//                double temperature = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", main.getDouble("temp") - 273.15));
//
//                // Extract sunset and sunrise time NOT NEEDED FOR NOW
////                JSONObject sys = jsonObject.getJSONObject("sys");
////                long sunriseTime = sys.getLong("sunrise");
////                long sunsetTime = sys.getLong("sunset");
//
//                // Display weather information in TextViews
//                TextView weatherTextView = findViewById(R.id.weatherTextView);
//                weatherTextView.setText("Weather: " + weatherDescription);
//
//                TextView temperatureTextView = findViewById(R.id.temperatureTextView);
//                temperatureTextView.setText("Temperature: " + temperature + " °C");
//
////                TextView sunriseTextView = findViewById(R.id.sunriseTextView);
////                sunriseTextView.setText("Sunrise: " + formatTime(sunriseTime, jsonObject.getString("timezone")));
////
////                TextView sunsetTextView = findViewById(R.id.sunsetTextView);
////                sunsetTextView.setText("Sunset: " + formatTime(sunsetTime, jsonObject.getString("timezone")));
//
//            } else {
//                // Handle error in fetching data
//                Toast.makeText(this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
//            }
//        } catch (JSONException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static class WeatherInfoTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            // Perform the API request and return the response
//            return NetworkUtils.fetchData(params[0]);
//        }
//    }
//
//    private String formatTime(long timestamp, String timeZone) {
//        // Convert timestamp to a readable time format in the specified time zone
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
//        return sdf.format(new Date(timestamp * 1000));
//    }
//}
package com.example.myapplication.activities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class WeatherInfoActivity extends AppCompatActivity {

    private static final String OPEN_WEATHER_MAP_API_KEY = "4ca602083748b203b642802697a476c5";
    private static final String BASE_URL_CURRENT = "https://api.openweathermap.org/data/2.5/weather";
    private static final String BASE_URL_HOURLY = "https://api.openweathermap.org/data/2.5/forecast";
    private RecyclerView recyclerView;
    private HourlyTemperatureAdapter hourlyTemperatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);


        // Retrieve the selected location from the Intent
        String selectedLocation = getIntent().getStringExtra("selectedLocation");

        // Display the selected location in a TextView
        TextView locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(selectedLocation);

        // Initialize the adapter
        hourlyTemperatureAdapter = new HourlyTemperatureAdapter();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hourlyTemperatureAdapter);

        // Fetch and display current weather information for the selected location
        new WeatherInfoTask(true, hourlyTemperatureAdapter).execute(selectedLocation);

        // Fetch and display hourly forecast information for the selected location
        new WeatherInfoTask(false, hourlyTemperatureAdapter).execute(selectedLocation);
    }

    private class WeatherInfoTask extends AsyncTask<String, Void, String> {
        private boolean isCurrent;
        private HourlyTemperatureAdapter adapter;

        public WeatherInfoTask(boolean isCurrent, HourlyTemperatureAdapter adapter) {
            this.isCurrent = isCurrent;
            this.adapter = adapter;
        }

        @Override
        protected String doInBackground(String... params) {
            String apiUrl;
            if (isCurrent) {
                apiUrl = BASE_URL_CURRENT + "?q=" + params[0] + "&appid=" + OPEN_WEATHER_MAP_API_KEY;
            } else {
                apiUrl = BASE_URL_HOURLY + "?q=" + params[0] + "&appid=" + OPEN_WEATHER_MAP_API_KEY;
            }
            // Perform the API request and return the response
            return NetworkUtils.fetchData(apiUrl);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    if (isCurrent) {
                        // Parse the JSON response for current weather
                        JSONObject jsonObject = new JSONObject(result);

                        // Extract relevant weather information
                        String weatherDescription = jsonObject.getJSONArray("weather")
                                .getJSONObject(0).getString("description");

                        // Extract temperature information
                        JSONObject main = jsonObject.getJSONObject("main");
                        double temperature = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", main.getDouble("temp") - 273.15));

                        // Display current weather information in TextViews
                        TextView weatherTextView = findViewById(R.id.weatherTextView);
                        weatherTextView.setText("Weather: " + weatherDescription);

                        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
                        temperatureTextView.setText("Temperature: " + temperature + " °C");
                    } else {
                        // Parse the JSON response for hourly temperature
                        JSONObject jsonObject = new JSONObject(result);

                        // Extract hourly temperature information
                        JSONArray hourlyArray = jsonObject.getJSONArray("list");
                        List<HourlyTemperature> hourlyTemperatureList = parseHourlyData(hourlyArray);

                        // Update the adapter with the new data
                        adapter.setHourlyTemperatureList(hourlyTemperatureList);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle exceptions more gracefully, e.g., show a Toast or log an error.
                }
            } else {
                // Handle error in fetching data
                Toast.makeText(WeatherInfoActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<HourlyTemperature> parseHourlyData(JSONArray hourlyArray) throws JSONException {
        // Parse the hourly data and create a list of HourlyTemperature objects
        List<HourlyTemperature> hourlyTemperatureList = new ArrayList<>();
        for (int i = 0; i < hourlyArray.length(); i++) {
            JSONObject hourObject = hourlyArray.getJSONObject(i);
            String timestamp = hourObject.getString("dt_txt");
            double temperature = hourObject.getJSONObject("main").getDouble("temp") - 273.15;

            hourlyTemperatureList.add(new HourlyTemperature(timestamp, temperature));
        }
        return hourlyTemperatureList;
    }

    private static class HourlyTemperatureAdapter extends RecyclerView.Adapter<HourlyTemperatureAdapter.ViewHolder> {

        private List<HourlyTemperature> hourlyTemperatureList = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_temperature, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HourlyTemperature hourlyTemperature = hourlyTemperatureList.get(position);
            holder.timestampTextView.setText(hourlyTemperature.getTimestamp());
            holder.temperatureTextView.setText(String.format(Locale.getDefault(), "%.2f °C", hourlyTemperature.getTemperature()));
        }

        @Override
        public int getItemCount() {
            return hourlyTemperatureList.size();
        }

        public void setHourlyTemperatureList(List<HourlyTemperature> list) {
            hourlyTemperatureList = list;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView timestampTextView;
            TextView temperatureTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                timestampTextView = itemView.findViewById(R.id.timestampTextView);
                temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            }
        }
    }

    private static class HourlyTemperature {
        private final String timestamp;
        private final double temperature;

        public HourlyTemperature(String timestamp, double temperature) {
            this.timestamp = timestamp;
            this.temperature = temperature;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public double getTemperature() {
            return temperature;
        }
    }
}
