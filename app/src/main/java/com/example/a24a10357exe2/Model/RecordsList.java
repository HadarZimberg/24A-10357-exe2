package com.example.a24a10357exe2.Model;

import java.util.ArrayList;

public class RecordsList {
    private ArrayList<Record> recordsList = new ArrayList<>();

    public RecordsList() {
    }

    public ArrayList<Record> getRecordsList() {
        return this.recordsList;
    }

    public RecordsList setRecordsList(ArrayList<Record> recordsList) {
        this.recordsList = recordsList;
        return this;
    }

    public RecordsList addRecord(Record record) { //Adds a new record in the appropriate place
        int index = 0;
        while (index < recordsList.size() && record.getScore() <= recordsList.get(index).getScore())
            index++;
        recordsList.add(index, record);
        setPlaces();
        return this;
    }

    private void setPlaces() { //Sorts the records' places by size
        int index = 0;
        while (index < recordsList.size()) {
            recordsList.get(index).setPlace(index + 1);
            index++;
        }
    }

    @Override
    public String toString() {
        return "{" + recordsList + "}";
    }

}
