package com.iaz.findyourway.domain.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iaz.findyourway.domain.entity.PlaceModel;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class DataConverter {

    @TypeConverter
    public String fromPlacesList(List<PlaceModel> places) {
        if (places == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PlaceModel>>() {}.getType();
        return gson.toJson(places, type);
    }

    @TypeConverter
    public List<PlaceModel> toPlacesList(String placesString) {
        if (placesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PlaceModel>>() {}.getType();
        return gson.fromJson(placesString, type);
    }
 }