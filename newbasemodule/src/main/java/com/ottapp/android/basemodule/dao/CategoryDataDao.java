package com.ottapp.android.basemodule.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.VasTagModel;

import java.util.List;

@Dao
public abstract class CategoryDataDao implements BaseDao<CategoryListDataModel> {
    @Query("SELECT * FROM CategoryListDataModel WHERE active =1 ORDER BY sortOrder ASC")
    public abstract List<CategoryListDataModel> getAll();

    @Query("SELECT * FROM CategoryListDataModel WHERE id = :playId and active=1")
    public abstract CategoryListDataModel selectByPlayId(long playId);

    @Query("DELETE FROM CategoryListDataModel where active!=1")
    public abstract void deleteAllInActive();

    @Query("SELECT count(*) FROM CategoryListDataModel where active=1")
    public abstract int getAllActiveCount();

    @Query("SELECT * from CategoryListDataModel where id in (select m.categoryId from CategoryAssosiationDataModel m where m.menuId=:menuId )")
    public abstract List<CategoryListDataModel> getAllCategoryUnderMenuId(int menuId);

    @Query("SELECT max(updatedDate) FROM CategoryListDataModel where active=1")
    public abstract long getLastUpdatedTimestamp();

    @Query("SELECT * FROM CategoryListDataModel WHERE  active=1")
    public abstract LiveData<List<CategoryListDataModel>> getAllLiveCategory();

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    public abstract List<Long> insertvasListUnderCategory(List<VasTagModel> list);

    @Query("SELECT * FROM VasTagModel WHERE categoryId = :Id and active=1")
    public abstract VasTagModel selectVasUrlId(long Id);

}
