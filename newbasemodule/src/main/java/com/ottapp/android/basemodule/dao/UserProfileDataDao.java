package com.ottapp.android.basemodule.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.UserProfileModel;

import java.util.List;

@Dao
public abstract class UserProfileDataDao implements BaseDao<UserProfileModel> {
    @Query("SELECT * FROM UserProfileModel ")
    public abstract List<UserProfileModel> getAll();

    @Query("SELECT count(*) FROM UserProfileModel ")
    public abstract int getAllActiveCount();
}
