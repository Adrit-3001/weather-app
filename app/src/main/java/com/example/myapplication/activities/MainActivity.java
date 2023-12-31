package com.example.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView1;
    private VideoView videoView2;
    private VideoView videoView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// hase
        // Initialize VideoViews
        videoView1 = findViewById(R.id.videoView1);
        videoView2 = findViewById(R.id.videoView2);
        videoView3 = findViewById(R.id.videoView3);

        // Set the path of the video files
        String videoPath1 = "android.resource://" + getPackageName() + "/" + R.raw.cloudy_video_2160p;
        String videoPath2 = "android.resource://" + getPackageName() + "/" + R.raw.rain_video_1080p;
        String videoPath3 = "android.resource://" + getPackageName() + "/" + R.raw.snow_video_1080p;

        // Set the URIs of the videos
        Uri videoUri1 = Uri.parse(videoPath1);
        Uri videoUri2 = Uri.parse(videoPath2);
        Uri videoUri3 = Uri.parse(videoPath3);

        // Set the video URIs to the VideoViews
        videoView1.setVideoURI(videoUri1);
        videoView2.setVideoURI(videoUri2);
        videoView3.setVideoURI(videoUri3);

        // Start playing the videos
        videoView1.start();
        videoView2.start();
        videoView3.start();

        // Set volume to 0 to mute the videos
        videoView1.setOnPreparedListener(mediaPlayer -> mediaPlayer.setVolume(0f, 0f));
        videoView2.setOnPreparedListener(mediaPlayer -> mediaPlayer.setVolume(0f, 0f));
        videoView3.setOnPreparedListener(mediaPlayer -> mediaPlayer.setVolume(0f, 0f));

        // Set a looping listener to restart the videos when they reach the end
        videoView1.setOnCompletionListener(mediaPlayer -> mediaPlayer.seekTo(0));
        videoView2.setOnCompletionListener(mediaPlayer -> mediaPlayer.seekTo(0));
        videoView3.setOnCompletionListener(mediaPlayer -> mediaPlayer.seekTo(0));

        // Set the "Select Location" button click listener
        Button selectLocationButton = findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectLocationActivity();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the videos when the activity is not visible
        videoView1.pause();
        videoView2.pause();
        videoView3.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume playing the videos when the activity is visible again
        videoView1.start();
        videoView2.start();
        videoView3.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop and release the video resources when the activity is destroyed
        videoView1.stopPlayback();
        videoView2.stopPlayback();
        videoView3.stopPlayback();
    }

    private void openSelectLocationActivity() {
        Intent intent = new Intent(MainActivity.this, LocationSelectionActivity.class);
        startActivity(intent);
    }
}
