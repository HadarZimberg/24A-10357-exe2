package com.example.a24a10357exe2.UI_Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.a24a10357exe2.R;
import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final long SLOW = 1000;
    private static final long FAST = 400;
    private MaterialButton menu_BTN_start;
    private MaterialButton menu_BTN_records;
    private RadioGroup menu_RG_speed;
    private RadioGroup menu_RG_mode;
    private RadioButton menu_RB_slow;
    private RadioButton menu_RB_fast;
    private RadioButton menu_RB_buttons;
    private RadioButton menu_RB_tilt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Request location access
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        findViews();
        initViews();
    }

    private void initViews() {
        menu_BTN_start.setOnClickListener(view -> startGameActivity());
        menu_BTN_records.setOnClickListener(view -> scoreActivity());
    }

    private void scoreActivity() {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        startActivity(scoreIntent);
        finish();
    }

    private void findViews() {
        menu_BTN_start = findViewById(R.id.menu_BTN_start);
        menu_BTN_records = findViewById(R.id.menu_BTN_records);
        menu_RG_speed = findViewById(R.id.menu_RG_speed);
        menu_RB_slow = findViewById(R.id.menu_RB_slow);
        menu_RB_fast = findViewById(R.id.menu_RB_fast);
        menu_RG_mode = findViewById(R.id.menu_RG_mode);
        menu_RB_buttons = findViewById(R.id.menu_RB_buttons);
        menu_RB_tilt = findViewById(R.id.menu_RB_tilt);
    }

    private void startGameActivity() {
        Intent playIntent = new Intent(this, MainActivity.class);

        //Go to the next screen according to the settings the player has chosen
        playIntent.putExtra(MainActivity.KEY_SPEED, speed());
        playIntent.putExtra(MainActivity.KEY_MODE, isButtonsMode());

        startActivity(playIntent);
        finish();
    }

    private boolean isButtonsMode() {
        return menu_RB_buttons.isChecked();
    }

    private long speed() { //Sets the speed according to the selected button
        if (menu_RB_slow.isChecked())
            return SLOW;
        else
            return FAST;
    }
}