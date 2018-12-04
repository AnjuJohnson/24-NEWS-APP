package com.ottapp.android.basemodule.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

@Dao
public abstract class AssetDataDao implements BaseDao<AssetVideosDataModel> {
    @Query("SELECT * FROM AssetVideosDataModel WHERE active =1")
    public abstract List<AssetVideosDataModel> getAll();
    @Query("SELECT * FROM AssetVideosDataModel")
    public abstract List<AssetVideosDataModel> getAll_();

    @Query("SELECT * FROM AssetVideosDataModel WHERE categoryId = :playId and active=1")
    public abstract List<AssetVideosDataModel> selectByCategory(long playId);

    @Query("SELECT * FROM AssetVideosDataModel WHERE categoryId = :categoryId and active=1 limit :limit ")
    public abstract List<AssetVideosDataModel> selectByCategoriesWithLimit(int categoryId, int limit);

    @Query("SELECT * FROM AssetVideosDataModel WHERE categoryId = :categoryId and active=1")
    public abstract List<AssetVideosDataModel> selectByCategoriesWithOutLimit(int categoryId);

    @Query("SELECT * FROM AssetVideosDataModel WHERE categoryId = :categoryId and active=1")
    public abstract List<AssetVideosDataModel> selectByCategoriesId(int categoryId);



    @Query("SELECT max(id) FROM AssetVideosDataModel WHERE categoryId = :playId and active=1")
    public abstract int selectByAssetId(long playId);

    @Query("SELECT count(*) FROM AssetVideosDataModel  WHERE categoryId = :id and active=1")
    public abstract int getAssetsCount(long id);


    @Query("DELETE FROM AssetVideosDataModel where active!=1")
    public abstract void deleteAllInActive();

    @Query("DELETE FROM AssetVideosDataModel ")
    public abstract void deleteAllAssets();

    @Query("SELECT count(*) FROM AssetVideosDataModel where active=1")
    public abstract int getAllActiveCount();

    @Query("SELECT * FROM AssetVideosDataModel WHERE categoryId = :playId and active=1")
    public abstract LiveData<List<AssetVideosDataModel>> getAllLiveAssets(long playId);

    @Query("SELECT * FROM AssetVideosDataModel WHERE  active=1")
    public abstract LiveData<List<AssetVideosDataModel>> getAllAssetsLive();

    @Query("SELECT max(modifiedDate) FROM AssetVideosDataModel where active=1")
    public abstract long getLastUpdatedTimestamp();

    @Query("SELECT max(modifiedDate) FROM AssetVideosDataModel  WHERE categoryId = :id and active=1")
    public abstract long getLastUpdatedTimestampById(long id);

    @Query("SELECT * FROM AssetVideosDataModel WHERE id=:id and active=1")
    public abstract AssetVideosDataModel getById(int id);

    @Query("DELETE FROM AssetVideosDataModel WHERE id NOT IN (SELECT id FROM AssetVideosDataModel ORDER BY modifiedDate DESC LIMIT 100)")
    public abstract void deleteLimitById();

//    @Query("SELECT max() FROM AssetVideosDataModel where active=1")
//    public abstract long getLastUpdatedTimestamp();
}
