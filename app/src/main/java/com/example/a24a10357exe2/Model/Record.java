package com.example.a24a10357exe2.Model;

import com.google.android.gms.maps.model.LatLng;

public class Record {
    private int place = 1;
    private int score = 0;
    private LatLng location = new LatLng(0,0);

    public Record() {
    }

    public int getPlace() {
        return place;
    }

    public Record setPlace(int place) {
        this.place = place;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public LatLng getLocation() {
        return location;
    }

    public Record setLocation(LatLng location) {
        this.location = location;
        return this;
    }
}
