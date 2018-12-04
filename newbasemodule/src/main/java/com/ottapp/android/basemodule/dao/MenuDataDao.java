package com.ottapp.android.basemodule.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

@Dao
public abstract class MenuDataDao implements BaseDao<MenuDataModel> {
//    @Query("SELECT * FROM MenuDataModel WHERE active =1")
//    public abstract List<MenuDataModel> getAll();

    @Query("SELECT id FROM MenuDataModel WHERE `action` = :data and active=1")
    public abstract Integer selectDefaultMenuId(String data);

    @Query("SELECT * FROM MenuDataModel WHERE id = :playId and active=1")
    public abstract MenuDataModel selectByPlayId(long playId);

    @Query("DELETE FROM MenuDataModel where active!=1")
    public abstract void deleteAllInActive();

    @Query("SELECT count(*) FROM MenuDataModel where active=1")
    public abstract int getAllActiveCount();

    @Query("SELECT distinct mm.id, mm.active, mm.name, mm.updatedDate, mm.sortOrder,mm.iconUrl, mm.partnerId, mm.updatedBy, mm.tableId ,mm.`action` from MenuDataModel mm inner join CategoryAssosiationDataModel cma on mm.id = cma.menuId or mm.`action` IS NOT NULL and mm.active = 1")
     public abstract LiveData<List<MenuDataModel>> getAllLiveMenu();
//     @Query("SELECT * FROM MenuDataModel WHERE active =1")
//     public abstract LiveData<List<MenuDataModel>> getAllLiveMenu();
    @Query("SELECT max(updatedDate) FROM MenuDataModel where active=1")
    public abstract long getLastUpdatedTimestamp();

    @Query("SELECT distinct mm.id, mm.active, mm.name, mm.updatedDate, mm.sortOrder,mm.iconUrl, mm.partnerId, mm.updatedBy, mm.tableId, mm.`action`  from MenuDataModel mm inner join CategoryAssosiationDataModel cma on mm.id = cma.menuId or mm.`action` IS NOT NULL and mm.active = 1")
    public abstract List<MenuDataModel>getAll();

}
