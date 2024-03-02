package com.example.a24a10357exe2.UI_Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.a24a10357exe2.Interfaces.LocationCallBack;
import com.example.a24a10357exe2.R;
import com.example.a24a10357exe2.Fragments.ListFragment;
import com.example.a24a10357exe2.Fragments.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

public class ScoreActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private MapFragment mapFragment;
    private FrameLayout score_FRAME_list;
    private FrameLayout score_FRAME_map;
    private MaterialButton score_BTN_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, renderer -> {});

        findViews();

        score_BTN_home.setOnClickListener(view -> backToHomepage());

        listFragment = new ListFragment();
        listFragment.setCallback(new LocationCallBack() {
            @Override
            public void recordClicked(LatLng latLng) {
                mapFragment.updateMapLocation(latLng);
            }
        });
        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_map, mapFragment).commit();
    }

    private void backToHomepage() {
        Intent homeIntent = new Intent(this, MenuActivity.class);
        startActivity(homeIntent);
        finish();
    }

    private void findViews() {
        score_FRAME_list = findViewById(R.id.score_FRAME_list);
        score_FRAME_map = findViewById(R.id.score_FRAME_map);
        score_BTN_home = findViewById(R.id.score_BTN_home);
    }

}