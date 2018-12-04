package com.ottapp.android.basemodule.services;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.dao.GenreDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.dao.task.asset.DeleteAssetTask;
import com.ottapp.android.basemodule.dao.task.category.GetAllLiveLanguageActiveTask;
import com.ottapp.android.basemodule.dao.task.genre.DeleteAllInActiveGenreTask;
import com.ottapp.android.basemodule.dao.task.genre.GetAllGenreActiveTask;
import com.ottapp.android.basemodule.dao.task.genre.GetAllGenreTask;
import com.ottapp.android.basemodule.dao.task.genre.GetAllLiveGenreActiveTask;
import com.ottapp.android.basemodule.dao.task.genre.InsertAllGenreTask;
import com.ottapp.android.basemodule.dao.task.language.GetAllLanguagesActiveTask;
import com.ottapp.android.basemodule.dao.task.language.GetAllLanguagesTask;
import com.ottapp.android.basemodule.dao.task.language.InsertAllLanguagesTask;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.models.VersionModel;
import com.ottapp.android.basemodule.repository.responses.VersionResponse;
import com.ottapp.android.basemodule.services.base.BaseService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreService extends BaseService<GenreModel> {
    private static GenreService services;
    private GenreDataDao assetDao;

    private GenreService() {
        super();
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        assetDao = appDatabase.genreDataDao();
    }

    @Override
    public void insert(GenreModel model) {

    }

    @Override
    public void insertAll(List<GenreModel> models) {
        if (assetDao != null && models != null) {
            //noinspection unchecked
            new InsertAllGenreTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, models);
        }
    }

    @Override
    public List<GenreModel> getAll() {

        if (assetDao != null) {
            try {
                return new GetAllGenreTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public GenreModel getAllActive(){
        if (assetDao != null) {
            try {
                return new GetAllGenreActiveTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    public LiveData<List<GenreModel>> getAllLive(int id) {
        if (assetDao != null) {
            try {
                return new GetAllLiveGenreActiveTask(assetDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public GenreModel getById(int id) {
        return null;
    }

    @Override
    public void deleteAllInValid() {
        if (assetDao != null) {
            try {
                new DeleteAllInActiveGenreTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();

            } catch (InterruptedException e) {
                e.printStackTrace();

            } catch (ExecutionException e) {
                e.printStackTrace();

            }
        }

    }

    @Override
    public long getLastUpdatedTimestamp() {

        return 0;
    }

    @Override
    public boolean isPresent() {

        return false;
    }

    @Override
    public void destroy() {
        synchronized (this) {
            services = null;
        }
    }

    public static GenreService getServices() {
        if (services == null) {
            services = new GenreService();
        }
        return services;
    }

}
