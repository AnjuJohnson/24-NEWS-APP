package com.ottapp.android.basemodule.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

import java.util.List;

@Dao
public abstract class CategoryAssosiationDataDao implements BaseDao<CategoryAssosiationDataModel> {
    @Query("SELECT * FROM CategoryAssosiationDataModel WHERE active =1")
    public abstract List<CategoryAssosiationDataModel> getAll();

    @Query("SELECT * FROM CategoryAssosiationDataModel WHERE categoryId = :playId and active=1")
    public abstract List<CategoryAssosiationDataModel> selectByPlayId(long playId);

    @Query("DELETE FROM CategoryAssosiationDataModel where active!=1")
    public abstract void deleteAllInActive();

    @Query("SELECT count(*) FROM CategoryAssosiationDataModel where active=1")
    public abstract int getAllActiveCount();

    @Query("SELECT max(updatedDate) FROM CategoryAssosiationDataModel where active=1")
    public abstract long getLastUpdatedTimestamp();

    @Query("SELECT * FROM CategoryAssosiationDataModel WHERE active=1")
    public abstract LiveData<List<CategoryAssosiationDataModel>> getAllLiveAssosiations();
}
