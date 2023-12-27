package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class LocationSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        // Sample list of locations
        String[] locations = {"Scarborough", "Delhi", "London", "Seoul", "Paris"};

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);

        // Get a reference to the ListView
        ListView listView = findViewById(R.id.listView);

        // Set the adapter on the ListView
        listView.setAdapter(adapter);

        // Set item click listener to handle location selection
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = (String) parent.getItemAtPosition(position);

                // Pass the selected location to the next activity
                Intent intent = new Intent(LocationSelectionActivity.this, WeatherInfoActivity.class);
                intent.putExtra("selectedLocation", selectedLocation);
                startActivity(intent);
            }
        });
    }
}

