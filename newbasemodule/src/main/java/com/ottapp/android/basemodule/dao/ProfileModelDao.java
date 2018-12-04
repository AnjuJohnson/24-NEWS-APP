package com.ottapp.android.basemodule.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.models.UserProfileModel;

import java.util.List;

@Dao
public interface ProfileModelDao {
    @Query("SELECT * FROM UserProfileModel")
    List<UserProfileModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserProfileModel> userProfileModels);

    @Query("SELECT * FROM UserProfileModel")
    LiveData<List<UserProfileModel>> getAllLive();
}
