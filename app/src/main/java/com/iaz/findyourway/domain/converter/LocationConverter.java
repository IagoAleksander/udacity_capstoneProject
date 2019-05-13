package com.iaz.findyourway.domain.converter;

import com.google.android.gms.maps.model.LatLng;

import androidx.room.TypeConverter;

public class LocationConverter {
    @TypeConverter
    public static LatLng toLatLng(String location) {
        String[] locationSplitted = location.split("/");
        try {
            return new LatLng(Double.parseDouble(locationSplitted[0]), Double.parseDouble(locationSplitted[1]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toLocation(LatLng latLng) {
        return latLng == null ? null : latLng.latitude +"/" +latLng.longitude;
    }
}