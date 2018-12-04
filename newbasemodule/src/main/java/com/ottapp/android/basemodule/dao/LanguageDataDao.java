package com.ottapp.android.basemodule.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ottapp.android.basemodule.dao.base.BaseDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.models.UserProfileModel;

import java.util.List;

@Dao
public abstract class LanguageDataDao implements BaseDao<LanguageModel> {
    @Query("SELECT * FROM LanguageModel where active = 1 ")
    public abstract List<LanguageModel> getAll();


    @Query("SELECT * FROM LanguageModel WHERE  active=1")
    public abstract LiveData<List<LanguageModel>> getAllLiveLanguage();

    @Query("SELECT count(*) FROM LanguageModel where selected = 1 ")
    public abstract int getAllActiveCount();
}
