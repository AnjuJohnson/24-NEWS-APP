package com.ottapp.android.basemodule.services;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.dao.task.asset.GetAllAssetActiveTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAssetIdTask;
import com.ottapp.android.basemodule.dao.task.asset.GetLastUpdatedDateAssetTask;
import com.ottapp.android.basemodule.dao.task.asset.InsertAllAssetTask;
import com.ottapp.android.basemodule.dao.task.category.GetAllLiveCategoryActiveTask;
import com.ottapp.android.basemodule.dao.task.category.GetAllLiveLanguageActiveTask;
import com.ottapp.android.basemodule.dao.task.language.GetAllLanguagesActiveTask;
import com.ottapp.android.basemodule.dao.task.language.GetAllLanguagesTask;
import com.ottapp.android.basemodule.dao.task.language.InsertAllLanguagesTask;
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

public class LanguageService extends BaseService<LanguageModel> {
    private static LanguageService services;
    private LanguageDataDao assetDao;

    private LanguageService() {
        super();
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        assetDao = appDatabase.languageDao();
    }

    @Override
    public void insert(LanguageModel model) {

    }

    @Override
    public void insertAll(List<LanguageModel> models) {
        if (assetDao != null && models != null) {
            //noinspection unchecked
            new InsertAllLanguagesTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, models);
        }
    }

    @Override
    public List<LanguageModel> getAll() {

        if (assetDao != null) {
            try {
                return new GetAllLanguagesTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getAllActive(){
        if (assetDao != null) {
            try {
                return new GetAllLanguagesActiveTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    @Override
    public LiveData<List<LanguageModel>> getAllLive(int id) {
        if (assetDao != null) {
            try {
                return new GetAllLiveLanguageActiveTask(assetDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public LanguageModel getById(int id) {
        return null;
    }

    @Override
    public void deleteAllInValid() {

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

    public static LanguageService getServices() {
        if (services == null) {
            services = new LanguageService();
        }
        return services;
    }



    public void checkUpdate(int version,boolean needReturnEvent) {
        Call<ResultObject<VersionModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getVersionUpdateInfo(version);
        qrDataCall.enqueue(new Callback<ResultObject<VersionModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<VersionModel>> call, @NonNull Response<ResultObject<VersionModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject data = response.body();
                    if (data != null && data.isRequestStatus()) {

                        if (needReturnEvent) {
                            EventBus.getDefault().post(new VersionResponse(null, data.isRequestStatus()));
                        }
                    } else {
                        if (!data.isRequestStatus()&&data.getMessage().equals("No data found")&&needReturnEvent) {
                            EventBus.getDefault().post(new VersionResponse(null, false,false));
                        }
                    }
                } else {
                    if (needReturnEvent) {
                        EventBus.getDefault().post(new VersionResponse(null, false,true));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<VersionModel>> call, @NonNull Throwable t) {
                Log.e("Error", t.getLocalizedMessage());
                if (needReturnEvent) {
                    EventBus.getDefault().post(new VersionResponse(null, false,true));
                }
            }
        });
    }
}
