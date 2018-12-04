package com.ottapp.android.basemodule.services;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.app.BaseApplication;
import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.dao.task.categoryassosiation.DeleteAllInAssosiationTask;
import com.ottapp.android.basemodule.dao.task.categoryassosiation.GetAllAssosiationTask;
import com.ottapp.android.basemodule.dao.task.categoryassosiation.GetAllLiveAssosiationActiveTask;
import com.ottapp.android.basemodule.dao.task.categoryassosiation.GetAssosiationCountTask;
import com.ottapp.android.basemodule.dao.task.categoryassosiation.GetLastUpdatedDateAssosiationTask;
import com.ottapp.android.basemodule.dao.task.categoryassosiation.InsertAllAssosiationTask;
import com.ottapp.android.basemodule.dao.task.categoryassosiation.InsertAssosiationTask;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;
import com.ottapp.android.basemodule.repository.responses.CategoryMenuAssociationModelResponse;
import com.ottapp.android.basemodule.services.base.BaseService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAssociationsService extends BaseService<CategoryAssosiationDataModel> {
    private static CategoryAssociationsService services;
    private CategoryAssosiationDataDao categoryDao;

    private CategoryAssociationsService() {

        super();
        AppDatabase db = Room.databaseBuilder(BaseApplication.getApplication(),
                AppDatabase.class, "AppData").build();
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        categoryDao = appDatabase.categoryAssociationDab();
    }

    @Override
    public void insert(CategoryAssosiationDataModel model) {
        if (categoryDao != null && model != null) {
            new InsertAssosiationTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
        }
    }

    @Override
    public void insertAll(List<CategoryAssosiationDataModel> models) {
        if (categoryDao != null && models != null) {
            //noinspection unchecked
            new InsertAllAssosiationTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, models);
        }
    }

    @Override
    public List<CategoryAssosiationDataModel> getAll() {
        List<CategoryAssosiationDataModel> categoryModels = new ArrayList<>(0);
        if (categoryDao != null) {
            try {
                categoryModels = new GetAllAssosiationTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (categoryModels == null) {
                categoryModels = new ArrayList<>(0);
            }
        }
        return categoryModels;
    }

    @Override
    public LiveData<List<CategoryAssosiationDataModel>> getAllLive(int id) {
        if (categoryDao != null) {
            try {
                return new GetAllLiveAssosiationActiveTask(categoryDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public CategoryAssosiationDataModel getById(int id) {
        return null;
    }

    @Override
    public void deleteAllInValid() {
        if (categoryDao != null) {
            new DeleteAllInAssosiationTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public long getLastUpdatedTimestamp() {
        if (categoryDao != null) {
            try {
                return new GetLastUpdatedDateAssosiationTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean isPresent() {
        try {
            return categoryDao != null && new GetAssosiationCountTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get() > 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void destroy() {
        synchronized (this) {
            services = null;
        }
    }

    public static CategoryAssociationsService getServices() {
        if (services == null) {
            services = new CategoryAssociationsService();
        }
        return services;
    }


    public void getAllUpdatedCategoryMenuAssociationModelsFromServer(boolean needCallback) {
        CategoryAssociationsService.getServices().setRetryRequired(true);
        long lastUpdatedDate = getLastUpdatedTimestamp();
        Call<ResultObject<CategoryAssosiationDataModel>> cardCall = RetrofitEngine.getRetrofitEngine()
                .getApiRequests(RequestApi.class).getDynamicCategoryAssosiation(lastUpdatedDate);
        cardCall.enqueue(new Callback<ResultObject<CategoryAssosiationDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<CategoryAssosiationDataModel>> call,
                                   @NonNull Response<ResultObject<CategoryAssosiationDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<CategoryAssosiationDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {
                        if ( categoryDao!= null) {
                            System.out.println("categoryAssosiationList:"+new Gson().toJson(data.getList()));
                            new InsertAllAssosiationTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data.getList());
                            deleteAllInValid();
                        }
                        if (needCallback)
                            EventBus.getDefault().post(new CategoryMenuAssociationModelResponse(data.getList(), data.isRequestStatus()));

                    } else {
                        if (needCallback)
                            EventBus.getDefault().post(new CategoryMenuAssociationModelResponse(null, false,true));

                    }
                } else {
                    if (needCallback)
                        EventBus.getDefault().post(new CategoryMenuAssociationModelResponse(null, false,true));

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<CategoryAssosiationDataModel>> call, @NonNull Throwable t) {
                if (needCallback)
                    EventBus.getDefault().post(new CategoryMenuAssociationModelResponse(null, false,true));

            }
        });
    }
}






