package com.ottapp.android.basemodule.app;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ottapp.android.basemodule.repository.RepoEngine;
import com.viewpagerindicator.BuildConfig;

import java.lang.ref.WeakReference;

public abstract class BaseApplication extends Application {
    private static WeakReference<BaseApplication> appContextReference;
    private AppDatabase db;

    public static BaseApplication getApplication() {
        return appContextReference.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContextReference = new WeakReference<>(this);
        RepoEngine.getRepoEngine().init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public abstract String deviceType();

    public abstract int getAppVersion();

    public AppDatabase getDatabase() {
        if (db == null)

            db = Room.databaseBuilder(BaseApplication.getApplication(),
                    AppDatabase.class, AppDatabase.DB_NAME).fallbackToDestructiveMigration().build();
        return db;
    }
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users "
                    + " ADD COLUMN last_update INTEGER");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("DROP TABLE IF EXISTS UserFavourites");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {


            }


    };



    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users "
                    + " ADD COLUMN last_update INTEGER");
        }
    };
}
