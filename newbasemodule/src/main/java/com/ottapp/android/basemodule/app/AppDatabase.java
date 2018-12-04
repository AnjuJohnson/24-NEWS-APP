package com.ottapp.android.basemodule.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ottapp.android.basemodule.R;
import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.dao.GenreDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;
import com.ottapp.android.basemodule.dao.UserProfileDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.models.VasTagModel;

@Database(entities = {UserProfileModel.class, UserFavouritesModel.class, CategoryListDataModel.class, MenuDataModel.class, CategoryAssosiationDataModel.class, AssetVideosDataModel.class, VasTagModel.class, LanguageModel.class, GenreModel.class}, version = 4)

public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = BaseApplication.getApplication().getString(R.string.db_name);

    public abstract UserFavouriteDataDao userFavouriteDao();

    public abstract CategoryDataDao categoryDao();

    public abstract MenuDataDao menuDao();

    public abstract CategoryAssosiationDataDao categoryAssociationDab();

    public abstract AssetDataDao assetDao();

    public abstract LanguageDataDao languageDao();

    public abstract GenreDataDao genreDataDao();

    public abstract UserProfileDataDao profileModelDao();
}