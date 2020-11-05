package com.ottapp.android.basemodule.services;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.dao.task.menu.DeleteAllInActiveMenuTask;
import com.ottapp.android.basemodule.dao.task.menu.GetAllLiveMenuActiveTask;
import com.ottapp.android.basemodule.dao.task.menu.GetAllMenuActiveTask;
import com.ottapp.android.basemodule.dao.task.menu.GetDefaultMenuTask;
import com.ottapp.android.basemodule.dao.task.menu.GetLastUpdatedDateMenuTask;
import com.ottapp.android.basemodule.dao.task.menu.GetMenuByIdTask;
import com.ottapp.android.basemodule.dao.task.menu.GetMenusActiveCountTask;
import com.ottapp.android.basemodule.dao.task.menu.InsertAllMenuTask;
import com.ottapp.android.basemodule.dao.task.menu.InsertMenuTask;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.repository.responses.MenuModelResponse;
import com.ottapp.android.basemodule.services.base.BaseService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuServices extends BaseService<MenuDataModel>{
    private static MenuServices services;
    private MenuDataDao menuDao;

    private MenuServices() {
       super();


    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        menuDao = appDatabase.menuDao();
    }

    @Override
    public void insert(MenuDataModel model) {
        if (menuDao != null && model != null) {
            new InsertMenuTask(menuDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,model);
        }
    }

    @Override
    public void insertAll(List<MenuDataModel> models) {
        if (menuDao != null && models != null) {
            //noinspection unchecked
            new InsertAllMenuTask(menuDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,models);
        }
        deleteAllInValid();
    }

    @Override
    public List<MenuDataModel> getAll() {
        if (menuDao != null) {
            try {
                return new GetAllMenuActiveTask(menuDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Integer getDefaultMenu(String home) {
        if (menuDao != null) {
            try {
                return new GetDefaultMenuTask(menuDao).execute(home).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public LiveData<List<MenuDataModel>> getAllLive(int id) {
        if (menuDao != null) {
            try {
                return new GetAllLiveMenuActiveTask(menuDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public MenuDataModel getById(int id) {
        if (menuDao != null) {
            try {
                return new GetMenuByIdTask(menuDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void deleteAllInValid() {
        if (menuDao != null) {
            try {
                new DeleteAllInActiveMenuTask(menuDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getLastUpdatedTimestamp() {
        if (menuDao != null) {
            try {
                return new GetLastUpdatedDateMenuTask(menuDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
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
            return menuDao != null && new GetMenusActiveCountTask(menuDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get() > 0;
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

    public static MenuServices getServices() {
        if (services == null) {
            services = new MenuServices();
        }
        return services;
    }
    public void getAllUpdatedMenuModelsFromServer(boolean callbackNeeded) {
        long lastUpdatedDate = getLastUpdatedTimestamp();
        Call<ResultObject<MenuDataModel>> cardCall = RetrofitEngine.getRetrofitEngine()
                .getApiRequests(RequestApi.class).getDynamicMenuList(lastUpdatedDate);
        cardCall.enqueue(new Callback<ResultObject<MenuDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<MenuDataModel>> call,
                                   @NonNull Response<ResultObject<MenuDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<MenuDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {
                        insertAll(data.getList());
                        if (callbackNeeded)
                            EventBus.getDefault().post(new MenuModelResponse(data.getList(), data.isRequestStatus()));
                    } else {
                        if (callbackNeeded)
                            EventBus.getDefault().post(new MenuModelResponse(null, false,true));

                    }
                } else {
                    if (callbackNeeded)
                        EventBus.getDefault().post(new MenuModelResponse(null, false,true));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<MenuDataModel>> call, @NonNull Throwable t) {
                if (callbackNeeded)
                    EventBus.getDefault().post(new MenuModelResponse(null, false,true));

            }
        });
    }

}
