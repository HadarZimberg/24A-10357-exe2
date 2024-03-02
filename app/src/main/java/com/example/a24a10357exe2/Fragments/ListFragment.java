package com.example.a24a10357exe2.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a24a10357exe2.Adapter.RecordAdapter;
import com.example.a24a10357exe2.Interfaces.LocationCallBack;
import com.example.a24a10357exe2.Interfaces.RecordCallBack;
import com.example.a24a10357exe2.Model.RecordsList;
import com.example.a24a10357exe2.R;
import com.example.a24a10357exe2.UI_Controllers.GameOverActivity;
import com.example.a24a10357exe2.Utilities.SharedPreferencesManager;
import com.example.a24a10357exe2.Model.Record;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class ListFragment extends Fragment {

    private RecyclerView score_LST_records;
    private LocationCallBack locationCallBack;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Retrieve the list of records from the intent:
        String recordsJson = getActivity().getIntent().getStringExtra(GameOverActivity.RECORDS_LIST);
        RecordsList recordsList = new Gson().fromJson(recordsJson, RecordsList.class);

        findViews(view);
        initViews(view, recordsList);

        return view;
    }

    private void initViews(View view, RecordsList recordsList) {
        // Retrieve the list of records from SharedPreferences
        String highScoresJson = SharedPreferencesManager.getInstance().getString("high_scores", "");
        recordsList = new Gson().fromJson(highScoresJson, RecordsList.class);
        if (recordsList == null) // If no records are found
            recordsList = new RecordsList();

        RecordAdapter recordAdapter = new RecordAdapter(getContext(), recordsList.getRecordsList());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        score_LST_records.setLayoutManager(linearLayoutManager);
        score_LST_records.setAdapter(recordAdapter);

        recordAdapter.setRecordCallback(new RecordCallBack() {
            @Override
            public void locationButtonClicked(Record record) {
                score_LST_records.getAdapter().notifyDataSetChanged();
                itemClicked(record.getLocation());
            }
        });
    }

    private void findViews(View view) {
        score_LST_records = view.findViewById(R.id.score_LST_records);
    }

    public void setCallback(LocationCallBack locationCallBack) {
        this.locationCallBack = locationCallBack;
    }

    private void itemClicked(LatLng latLng) { //Get current location
        if (locationCallBack != null)
            locationCallBack.recordClicked(latLng);
    }
}