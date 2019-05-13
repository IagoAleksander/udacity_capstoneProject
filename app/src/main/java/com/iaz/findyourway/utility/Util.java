package com.iaz.findyourway.utility;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.iaz.findyourway.R;
import com.iaz.findyourway.domain.entity.PlaceModel;

import java.util.ArrayList;

public class Util {

    public static ArrayList<PlaceModel> convertArray(ArrayList<String> result, ArrayList<PlaceModel> places) {
        ArrayList<PlaceModel> sortedPlaces = new ArrayList<>();
        for (String item : result) {
            switch (item) {
                case "A":
                    sortedPlaces.add(places.get(0));
                    break;
                case "B":
                    sortedPlaces.add(places.get(1));
                    break;
                case "C":
                    sortedPlaces.add(places.get(2));
                    break;
                case "D":
                    sortedPlaces.add(places.get(3));
                    break;
                case "E":
                    sortedPlaces.add(places.get(4));
                    break;
                case "F":
                    sortedPlaces.add(places.get(5));
                    break;
                case "G":
                    sortedPlaces.add(places.get(6));
                    break;
                case "H":
                    sortedPlaces.add(places.get(7));
                    break;
                case "I":
                    sortedPlaces.add(places.get(8));
                    break;
                case "J":
                    sortedPlaces.add(places.get(9));
                    break;
            }
        }
        return sortedPlaces;
    }

    public static void throwErrorMessage(Context context) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, context.getString(R.string.general_error), Toast.LENGTH_SHORT).show());
    }
}
