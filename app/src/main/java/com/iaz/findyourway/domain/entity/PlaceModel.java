package com.iaz.findyourway.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "place")
public class PlaceModel implements Parcelable {

    @PrimaryKey
    @NonNull
    private LatLng latLng = new LatLng(0,0);
    private String name;
    private String address;
    private boolean checked;

    @Ignore
    public PlaceModel(@NonNull LatLng latLng, String name, String address, boolean checked) {
        this.latLng = latLng;
        this.name = name;
        this.address = address;
        this.checked = checked;
    }

    public String getName() {
        if (name != null)
            return name;
        else
            return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(@NonNull LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public PlaceModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latLng, flags);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
    }

    protected PlaceModel(Parcel in) {
        this.latLng = Objects.requireNonNull(in.readParcelable(LatLng.class.getClassLoader()));
        this.name = in.readString();
        this.address = in.readString();
        this.checked = in.readByte() != 0;
    }

    public static final Creator<PlaceModel> CREATOR = new Creator<PlaceModel>() {
        @Override
        public PlaceModel createFromParcel(Parcel source) {
            return new PlaceModel(source);
        }

        @Override
        public PlaceModel[] newArray(int size) {
            return new PlaceModel[size];
        }
    };
}
