package com.iaz.findyourway.domain.manager;

import com.iaz.findyourway.domain.entity.PlaceModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM place")
    LiveData<List<PlaceModel>> loadAllPlaces();

    @Insert
    void insertPlace(PlaceModel place);

    @Update
    void updatePlace(PlaceModel place);

    @Delete
    void deletePlace(PlaceModel place);

    @Query("DELETE FROM place")
    void nukeTable();
}
