package com.ottapp.android.basemodule.dao.base;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<T> data);

    @Update
    void update(T data);

    @Delete
    void delete(T data);

    @RawQuery
    T getByRawQuery(SupportSQLiteQuery query);


    @RawQuery
    List<T> getAllByRawQuery(SupportSQLiteQuery query);

}
