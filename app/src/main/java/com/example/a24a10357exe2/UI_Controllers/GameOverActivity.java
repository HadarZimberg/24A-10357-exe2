package com.example.a24a10357exe2.UI_Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.a24a10357exe2.Model.Record;
import com.example.a24a10357exe2.Model.RecordsList;
import com.example.a24a10357exe2.R;
import com.example.a24a10357exe2.Utilities.SharedPreferencesManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class GameOverActivity extends AppCompatActivity {

    public static final String RECORDS_LIST = "RECORDS LIST";
    public static final String KEY_SCORE = "KEY_SCORE";
    private MaterialTextView gameover_LBL_score;
    private MaterialButton gameover_BTN_menu;
    private MaterialButton gameover_BTN_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //Request location access
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        findViews();
        initViews();

        Intent previousScreen = getIntent();
        int score = previousScreen.getIntExtra(KEY_SCORE, 0);
        saveCurrentLocation(score);

        refreshUI(score);
    }

    private void updateRecords(int score, LatLng latLng) {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance();

        // Retrieve the existing list of high scores
        String highScoresJson = sharedPreferencesManager.getString("high_scores", "");
        RecordsList recordsList = new Gson().fromJson(highScoresJson, RecordsList.class);

        if (recordsList == null) // Check if there's no records yet
            recordsList = new RecordsList();

        recordsList.addRecord(new Record().setScore(score).setLocation(latLng)); // Add the new record

        // Save the updated list
        sharedPreferencesManager.putString("high_scores", new Gson().toJson(recordsList));
    }

    private void initViews() {
        gameover_BTN_menu.setOnClickListener(view -> goToHomepage());
        gameover_BTN_records.setOnClickListener(view -> goToRecords());
    }

    private void goToRecords() {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        scoreIntent.putExtra(GameOverActivity.RECORDS_LIST, SharedPreferencesManager.getInstance().getString("high_scores", ""));
        startActivity(scoreIntent);
        finish();
    }

    private void goToHomepage() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }

    private void refreshUI(int score) {
        gameover_LBL_score.setText(score + "$");
    }

    private void findViews() {
        gameover_LBL_score = findViewById(R.id.gameover_LBL_score);
        gameover_BTN_menu = findViewById(R.id.gameover_BTN_menu);
        gameover_BTN_records = findViewById(R.id.gameover_BTN_records);
    }

    public void saveCurrentLocation(int score) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if permissions are granted. Ensure permissions are granted before calling this method.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted
            return;
        }

        // Define a location listener to receive updates
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Save the obtained location to your records
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                LatLng currentLatLng = new LatLng(lat, lon);
                updateRecords(score, currentLatLng); // Add the new record
                locationManager.removeUpdates(this);// Remove location updates
            }
        };

        // Request a single location update from the location provider
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
    }
}