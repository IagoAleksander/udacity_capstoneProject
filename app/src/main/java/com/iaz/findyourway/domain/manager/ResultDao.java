package com.iaz.findyourway.domain.manager;

import com.iaz.findyourway.domain.entity.ResultModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

@Dao
public interface ResultDao {

    @Query("SELECT * FROM result")
    LiveData<List<ResultModel>> loadAllResults();

    @Query("SELECT * FROM result")
    Single<List<ResultModel>> loadAllResultsSync();

    @Insert
    void insertResult(ResultModel result);

    @Update
    void updateResult(ResultModel result);

    @Delete
    void deleteResult(ResultModel result);

    @Query("DELETE FROM result")
    void nukeTable();
}
