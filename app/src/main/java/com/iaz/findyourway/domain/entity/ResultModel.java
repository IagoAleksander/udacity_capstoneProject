package com.iaz.findyourway.domain.entity;

import com.iaz.findyourway.domain.converter.DataConverter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "result")
public class ResultModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    private List<PlaceModel> places = new ArrayList<>();
    private String totalDistance;
    private boolean isChecked;

    @TypeConverters(DataConverter.class)
    public List<PlaceModel> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceModel> places) {
        this.places = places;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public ResultModel() {
    }

    @Ignore
    public ResultModel(List<PlaceModel> places, String totalDistance) {
        this.places = places;
        this.totalDistance = totalDistance;
    }

}
