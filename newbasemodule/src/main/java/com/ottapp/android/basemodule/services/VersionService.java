package com.ottapp.android.basemodule.services;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.VersionModel;
import com.ottapp.android.basemodule.repository.responses.VersionResponse;
import com.ottapp.android.basemodule.services.base.BaseService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionService extends BaseService<VersionModel> {
    private static VersionService services;
    private AssetDataDao assetDao;

    private VersionService() {
        super();
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        assetDao = appDatabase.assetDao();
    }

    @Override
    public void insert(VersionModel model) {

    }

    @Override
    public void insertAll(List<VersionModel> models) {

    }

    @Override
    public List<VersionModel> getAll() {

        return null;
    }

    @Override
    public LiveData<List<VersionModel>> getAllLive(int id) {
        return null;
    }

    @Override
    public VersionModel getById(int id) {
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

    public static VersionService getServices() {
        if (services == null) {
            services = new VersionService();
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
