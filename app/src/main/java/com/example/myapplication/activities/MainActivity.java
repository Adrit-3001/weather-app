// MainActivity.java
package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById(R.id.selectLocationButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openSelectLocationActivity();
//            }
//        });
        Button selectLocationButton = findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openSelectLocationActivity() {
        Intent intent = new Intent(this, LocationSelectionActivity.class);
        startActivity(intent);
    }
}
