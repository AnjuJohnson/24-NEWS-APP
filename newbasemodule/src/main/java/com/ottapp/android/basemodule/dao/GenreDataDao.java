package com.ottapp.android.basemodule.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

@Dao
public abstract class GenreDataDao implements BaseDao<GenreModel> {
    @Query("SELECT * FROM GenreModel where active = 1 ")
    public abstract List<GenreModel> getAll();


    @Query("SELECT * FROM GenreModel WHERE  active=1")
    public abstract LiveData<List<GenreModel>> getAllLiveGenre();

    @Query("SELECT * FROM GenreModel where seleted = 1 ")
    public abstract GenreModel getAllActiveCount();

    @Query("DELETE FROM GenreModel ")
    public abstract void deleteAllInActive();
}
