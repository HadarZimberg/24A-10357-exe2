package com.example.a24a10357exe2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.a24a10357exe2.Interfaces.RecordCallBack;
import com.example.a24a10357exe2.Model.Record;
import com.example.a24a10357exe2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>  {

    private Context context;
    private ArrayList<Record> records;
    private RecordCallBack recordCallback;

    public RecordAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
    }

    public RecordAdapter setRecordCallback(RecordCallBack recordCallback) {
        this.recordCallback = recordCallback;
        return this;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = getItem(position);
        holder.record_TXT_place.setText("#" + record.getPlace());
        holder.record_TXT_score.setText(record.getScore() + "$");
    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    private Record getItem(int position) {
        return records.get(position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        private TextView record_TXT_place;
        private TextView record_TXT_score;
        private FloatingActionButton record_FAB_location;
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record_TXT_place = itemView.findViewById(R.id.record_TXT_place);
            record_TXT_score = itemView.findViewById(R.id.record_TXT_score);
            record_FAB_location = itemView.findViewById(R.id.record_FAB_location);

            record_FAB_location.setOnClickListener(v ->
            {
                if (recordCallback != null) {
                    recordCallback.locationButtonClicked(getItem(getAdapterPosition()));
                }
            });
        }
    }
}
