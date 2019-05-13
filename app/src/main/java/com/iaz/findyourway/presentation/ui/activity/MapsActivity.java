package com.iaz.findyourway.presentation.ui.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.iaz.findyourway.R;
import com.iaz.findyourway.domain.entity.PlaceModel;
import com.iaz.findyourway.utility.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final private PlaceModel placeToReturn = new PlaceModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        else {
            returnError();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Places.
        Places.initialize(getApplicationContext(), Constants.MAPS_KEY);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            // Specify the types of place data to return.
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            // Set up a PlaceSelectionListener to handle the response.
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {

                    if (place.getLatLng() != null) {
                        mMap.clear();
                        Log.i("", "Place: " + place.getName() + ", " + place.getId());
                        LatLng location = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        MarkerOptions marker = new MarkerOptions().position(location).title("Click to select:").snippet(place.getName());
                        mMap.addMarker(marker).showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));

                        placeToReturn.setAddress(place.getAddress());
                        placeToReturn.setName(place.getName());
                        placeToReturn.setLatLng(place.getLatLng());
                    } else {
                        returnError();
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.d("onPlaceSelected", status.getStatusMessage());
                    returnError();
                }
            });
        }
        else {
            returnError();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(marker -> {
            Log.d("onInfoWindowClick", "clicked");
            Intent result = new Intent();
            result.putExtra(Constants.BUNDLE_PLACE, placeToReturn);
            setResult(RESULT_OK, result);
            finish();
        });
        mMap.setOnMapClickListener(latLng -> {
            final List<Address> addresses = new ArrayList<>();
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                addresses.addAll(geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1));
            } catch (IOException e) {
                e.printStackTrace();
                returnError();
            }

            mMap.clear();
            MarkerOptions marker = new MarkerOptions().position(latLng).title("Click to select");
            if (!addresses.isEmpty()) {
                marker.snippet(addresses.get(0).getAddressLine(0));

                mMap.addMarker(marker).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(10));

                String address = "";

                if (addresses.get(0).getFeatureName() != null && !addresses.get(0).getFeatureName().isEmpty()
                        && !addresses.get(0).getFeatureName().equals(addresses.get(0).getThoroughfare())
                        && !addresses.get(0).getFeatureName().equals(addresses.get(0).getSubAdminArea())
                        && !addresses.get(0).getFeatureName().equals(addresses.get(0).getPostalCode())
                        && !addresses.get(0).getFeatureName().equals("Unnamed Road"))
                    address = address.concat(addresses.get(0).getFeatureName());

                if (addresses.get(0).getThoroughfare() != null && !addresses.get(0).getThoroughfare().isEmpty()
                        && !addresses.get(0).getThoroughfare().equals(addresses.get(0).getSubAdminArea())
                        && !addresses.get(0).getThoroughfare().equals("Unnamed Road"))
                    address = address.concat(addresses.get(0).getThoroughfare() + ", ");

                if (addresses.get(0).getSubAdminArea() != null && !addresses.get(0).getSubAdminArea().isEmpty())
                    address = address.concat(addresses.get(0).getSubAdminArea() + " - ");

                if (addresses.get(0).getAdminArea() != null && !addresses.get(0).getAdminArea().isEmpty())
                    address = address.concat(addresses.get(0).getAdminArea() + ", ");

                if (addresses.get(0).getCountryCode() != null && !addresses.get(0).getCountryCode().isEmpty())
                    address = address.concat(addresses.get(0).getCountryCode());

                placeToReturn.setAddress(address);
                placeToReturn.setLatLng(latLng);
            } else {
                returnError();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void returnError() {
        Intent result = new Intent();
        setResult(RESULT_FIRST_USER, result);
        finish();
    }
}