package com.ottapp.android.basemodule.services;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.dao.task.asset.GetVasIdTask;
import com.ottapp.android.basemodule.dao.task.category.DeleteAllInActiveCategoryTask;
import com.ottapp.android.basemodule.dao.task.category.GetAllCategoryActiveTask;
import com.ottapp.android.basemodule.dao.task.category.GetAllLiveCategoryActiveTask;
import com.ottapp.android.basemodule.dao.task.category.GetCategoryByIdTask;
import com.ottapp.android.basemodule.dao.task.category.GetCategoryListByMenuIdTask;
import com.ottapp.android.basemodule.dao.task.category.GetCategorysActiveCountTask;
import com.ottapp.android.basemodule.dao.task.category.GetLastUpdatedDateCategoryTask;
import com.ottapp.android.basemodule.dao.task.category.InsertAllCategoryTask;
import com.ottapp.android.basemodule.dao.task.category.InsertCategoryTask;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryAssetsList;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.HomeDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.models.MoreItemRequestServiceModel;
import com.ottapp.android.basemodule.models.VasTagModel;
import com.ottapp.android.basemodule.repository.responses.CategoryModelResponse;
import com.ottapp.android.basemodule.repository.responses.HomeMoreModelResponse;
import com.ottapp.android.basemodule.services.base.BaseService;
import com.ottapp.android.basemodule.utils.AssetsOrderComparator;
import com.ottapp.android.basemodule.utils.CategoryOrderComparator;
import com.ottapp.android.basemodule.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryService extends BaseService<CategoryListDataModel> {
    private static CategoryService services;
    private CategoryDataDao categoryDao;

    private CategoryService() {
        super();

    }

    public static CategoryService getServices() {
        if (services == null) {
            services = new CategoryService();
        }
        return services;
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        categoryDao = appDatabase.categoryDao();
    }

    @Override
    public void insert(CategoryListDataModel model) {
        if (categoryDao != null && model != null) {
            new InsertCategoryTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
        }
    }

    @Override
    public void insertAll(List<CategoryListDataModel> models) {
        if (categoryDao != null && models != null) {
            //noinspection unchecked
            new InsertAllCategoryTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, models);
        }
        deleteAllInValid();
    }

    @Override
    public List<CategoryListDataModel> getAll() {
        if (categoryDao != null) {
            try {
                return new GetAllCategoryActiveTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public LiveData<List<CategoryListDataModel>> getAllLive(int id) {
        if (categoryDao != null) {
            try {
                return new GetAllLiveCategoryActiveTask(categoryDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public CategoryListDataModel getById(int id) {
        try {
            return new GetCategoryByIdTask(categoryDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public VasTagModel getVasUrlById(int id) {
        try {
            return new GetVasIdTask(categoryDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteAllInValid() {
        if (categoryDao != null) {
            try {
                new DeleteAllInActiveCategoryTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getLastUpdatedTimestamp() {
        if (categoryDao != null) {
            try {
                return new GetLastUpdatedDateCategoryTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
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
            return categoryDao != null && new GetCategorysActiveCountTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get() > 0;
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

    public List<CategoryListDataModel> categoryLists(int menuId) {

        if (categoryDao != null) {
            try {
                return new GetCategoryListByMenuIdTask(categoryDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, menuId).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<CategoryAssetsList> getAssetsUnderCategoryMenuAssociation(int menuId) {
        List<CategoryListDataModel> categoryLists = categoryLists(menuId);
        Collections.sort(categoryLists, new CategoryOrderComparator());
        List<CategoryAssetsList> videoLists = new ArrayList<>(0);
        if (categoryLists != null) {
            for (CategoryListDataModel cm : categoryLists) {
                CategoryAssetsList categoryAssetsList = new CategoryAssetsList();
                List<AssetVideosDataModel> model = AssetMenuService.getServices().getAssetsUnderCategory(cm.getId(), 10);

                if(!model.isEmpty() ) {
                    categoryAssetsList.setCategories(cm);
                    categoryAssetsList.setAssetVideos(model);
                    videoLists.add(categoryAssetsList);
                }
            }
        }

        return videoLists;
    }

    @SuppressLint("NewApi")
    public String getLanguageIds(){
        String ids = null;
        ArrayList<String> idList = new ArrayList<>();
        List<LanguageModel> languages = LanguageService.getServices().getAll();
        for(LanguageModel model : languages){
            if(model.getSelected() == 1){
                idList.add(String.valueOf(model.getId()));
            }
        }
        if(!idList.isEmpty()){
            ids = idList.stream()
                    .collect(Collectors.joining(","));
        }
        return  ids;
    }
    @SuppressLint("NewApi")
    public String getGenreIds(){
        String ids = null;
        ArrayList<String> idList = new ArrayList<>();
        List<GenreModel> languages = GenreService.getServices().getAll();
        for(GenreModel model : languages){
            if(model.getSeleted() == 1){
                idList.add(String.valueOf(model.getId()));
            }
        }
        if(!idList.isEmpty()){
            ids = idList.stream()
                    .collect(Collectors.joining(","));;
        }
        return  ids;
    }
    public void getSpecificCategoryMoreHome(MoreItemRequestServiceModel playListModel, boolean needCallback) {
        String langIds = getLanguageIds();
        String genreIds = getGenreIds();
        Call<ResultObject<HomeDataModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getSpecificCategory(playListModel.getCategoryId(), Constants.ITEM_MORE_LIMIT, playListModel.getMaxLimit(),langIds,genreIds);
        qrDataCall.enqueue(new Callback<ResultObject<HomeDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Response<ResultObject<HomeDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<HomeDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {
                        if (needCallback)
                            EventBus.getDefault().post(new HomeMoreModelResponse(data.getData().getAssetList(), data.isRequestStatus(), data.getData().getMaxLimit()));


                    } else {
                        EventBus.getDefault().post(new HomeMoreModelResponse(null, false, true));
                    }
                } else {
                    EventBus.getDefault().post(new HomeMoreModelResponse(null, false, true));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage() != null)
                    Log.e("ErrorCategory", t.getLocalizedMessage());
                EventBus.getDefault().post(new HomeMoreModelResponse(null, false, true));
            }
        });
    }

    public void getAllUpdatedCategoryModelsFromServer(boolean needCallback) {
        CategoryService.getServices().setRetryRequired(true);
        long lastUpdatedDate = getLastUpdatedTimestamp();
        Call<ResultObject<CategoryListDataModel>> cardCall = RetrofitEngine.getRetrofitEngine()
                .getApiRequests(RequestApi.class).getDynamicCategoryList(lastUpdatedDate);
        cardCall.enqueue(new Callback<ResultObject<CategoryListDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<CategoryListDataModel>> call,
                                   @NonNull Response<ResultObject<CategoryListDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<CategoryListDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {

                        insertAll(data.getList());
                        if (needCallback)
                            EventBus.getDefault().post(new CategoryModelResponse(data.getList(), data.isRequestStatus()));

                    } else {
                        if (needCallback)
                            EventBus.getDefault().post(new CategoryModelResponse(null, false, true));

                    }
                } else {
                    if (needCallback)
                        EventBus.getDefault().post(new CategoryModelResponse(null, false, true));

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<CategoryListDataModel>> call, @NonNull Throwable t) {
                if (needCallback)
                    EventBus.getDefault().post(new CategoryModelResponse(null, false, true));

            }
        });
    }

}

