package com.ottapp.android.basemodule.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.UserFavouritesModel;

import java.util.List;

@Dao
public abstract class UserFavouriteDataDao implements BaseDao<UserFavouritesModel> {
    @Query("SELECT * FROM UserFavouritesModel WHERE active=1")
    public abstract List<UserFavouritesModel> getAll();

    @Query("DELETE FROM UserFavouritesModel WHERE id = :favId")
    public abstract void deleteById(long favId);

    @Query("SELECT count(*) FROM UserFavouritesModel where active=1")
    public abstract int getAllActiveCount();

    @Query("SELECT * FROM UserFavouritesModel WHERE id = :favId")
    public abstract UserFavouritesModel getFavouriteModel(int favId);
}
