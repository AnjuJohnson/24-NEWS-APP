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
import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.task.asset.DeleteAllInActiveAssetTask;
import com.ottapp.android.basemodule.dao.task.asset.DeleteAssetTask;
import com.ottapp.android.basemodule.dao.task.asset.DeleteByLimitActiveAssetTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAllAssetActiveTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAllLiveAssetActiveTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAllLiveAssetsTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAssetActiveCountTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAssetByCategoryIds;
import com.ottapp.android.basemodule.dao.task.asset.GetAssetByIdTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAssetBySelectedCategory;
import com.ottapp.android.basemodule.dao.task.asset.GetAssetIdTask;
import com.ottapp.android.basemodule.dao.task.asset.GetAssetObjectByIdTask;
import com.ottapp.android.basemodule.dao.task.asset.GetLastUpdatedDateAssetTask;
import com.ottapp.android.basemodule.dao.task.asset.GetLastUpdatedDateByIdAssetTask;
import com.ottapp.android.basemodule.dao.task.asset.InsertAllAssetTask;
import com.ottapp.android.basemodule.dao.task.asset.InsertAssetTask;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.HomeDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.models.MoreItemRequestServiceModel;
import com.ottapp.android.basemodule.repository.responses.AssetsModelResponse;
import com.ottapp.android.basemodule.repository.responses.AssetsMoreListResponse;
import com.ottapp.android.basemodule.services.base.BaseService;
import com.ottapp.android.basemodule.utils.Constants;
import com.ottapp.android.basemodule.utils.DecodeUrl;
import com.ottapp.android.basemodule.utils.FilterVideoId;
import com.ottapp.android.basemodule.utils.ValidatorUrl;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetMenuService extends BaseService<AssetVideosDataModel> {

    private static AssetMenuService services;
    private AssetDataDao assetDao;


    private AssetMenuService() {
        super();
    }

    public static AssetMenuService getServices() {
        if (services == null) {
            services = new AssetMenuService();
        }
        return services;
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        assetDao = appDatabase.assetDao();
    }

    @Override
    public void insert(AssetVideosDataModel model) {
        if (assetDao != null && model != null) {
            new InsertAssetTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
        }
        //deleteAllInValid();
    }

    @Override
    public void insertAll(List<AssetVideosDataModel> models) {
        if (assetDao != null && models != null) {
            //noinspection unchecked
            new InsertAllAssetTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, models);
        }
        deleteAllInValid();
    }

    @Override
    public List<AssetVideosDataModel> getAll() {
        if (assetDao != null) {
            try {
                return new GetAllAssetActiveTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public LiveData<List<AssetVideosDataModel>> getAllLive(int id) {
        if (assetDao != null) {
            try {
                return new GetAllLiveAssetActiveTask(assetDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public LiveData<List<AssetVideosDataModel>> getAllLiveScroll(int id) {
        if (assetDao != null) {
            try {
                return new GetAllLiveAssetActiveTask(assetDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public LiveData<List<AssetVideosDataModel>> getAllLiveAssets() {
        if (assetDao != null) {
            try {
                return new GetAllLiveAssetsTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Boolean deleteAllAssets(){
        if (assetDao != null) {
            try {
            Boolean status =  new DeleteAssetTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            return status;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    public void deleteLimitById(){
        if (assetDao != null) {
            try {
                new DeleteByLimitActiveAssetTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public AssetVideosDataModel getById(int id) {
        if (assetDao != null) {
            try {
                return new GetAssetObjectByIdTask(assetDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void getAllCategoryIds(boolean needReturn) {
        List<CategoryListDataModel> catogoryList = CategoryService.getServices().getAll();
        if (catogoryList != null && !catogoryList.isEmpty()) {
            for (CategoryListDataModel categoryListDataModel : catogoryList) {
                int categoryId = categoryListDataModel.getId();
                long lastUpdatedDate = getLastUpdatedDateById(categoryId);
                int assetId = getAssetId(categoryId);
//                if (assetId != 0) {
                    getAllUpdatedAssetsonRefresh(categoryId, assetId, lastUpdatedDate, needReturn);
              //  }

            }
        }
    }

    public int getAssetId(int id) {
        if (assetDao != null) {
            try {
                return new GetAssetIdTask(assetDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public void deleteAllInValid() {
        if (assetDao != null) {
            try {
                new DeleteAllInActiveAssetTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getLastUpdatedTimestamp() {
        if (assetDao != null) {
            try {
                return new GetLastUpdatedDateAssetTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }


    public long getLastUpdatedDateById(int id) {
        if (assetDao != null) {
            try {
                return new GetLastUpdatedDateByIdAssetTask(assetDao).execute(id).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0;
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
                  .collect(Collectors.joining(","));;
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

    @Override
    public boolean isPresent() {
        try {
            return assetDao != null && new GetAssetActiveCountTask(assetDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get() > 0;
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

    public List<AssetVideosDataModel> getAssetsUnderCategory(int categoryId,int limit) {
        try {
            return new GetAssetByCategoryIds(assetDao,categoryId,limit).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<AssetVideosDataModel> getAssetsUnderCategory(int categoryId) {
        try {
            return new GetAssetBySelectedCategory(assetDao,categoryId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    //service to get the data based on the category id
    public void getSpecificCategory(MoreItemRequestServiceModel data, boolean needReturnEvent) {
        String langIds = getLanguageIds();
        String genreIds = getGenreIds();
        Call<ResultObject<HomeDataModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getSpecificCategory(data.getCategoryId(), Constants.ITEM_MORE_LIMIT, data.getMaxLimit(),langIds,genreIds);
        qrDataCall.enqueue(new Callback<ResultObject<HomeDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Response<ResultObject<HomeDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<HomeDataModel> responseData = response.body();
                    if (responseData.getData().getAssetList() != null && responseData.isRequestStatus()) {
                       // insertAll(responseData.getData().getAssetList());
                        logLargeString(new Gson().toJson(responseData.getData().getAssetList()));
                        if (needReturnEvent) {
                            EventBus.getDefault().post(new AssetsMoreListResponse(responseData.getData().getAssetList(), responseData.isRequestStatus(), responseData.getData().getMaxLimit()));
                        }


                    } else {
                        if (needReturnEvent) {
                            EventBus.getDefault().post(new AssetsMoreListResponse(null, false, true));
                        }

                    }
                } else {
                    if (needReturnEvent) {
                        EventBus.getDefault().post(new AssetsMoreListResponse(null, false, true));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage() != null)
                    Log.e("ErrorCategory", t.getLocalizedMessage());
                EventBus.getDefault().post(new AssetsModelResponse(null, false, true));
            }
        });
    }

    public void getAllUpdatedAssetsRelatedToMenuFromServer(boolean needReturnEvent) {
        AssetMenuService.getServices().setRetryRequired(true);
        long lastUpdatedDate = getLastUpdatedTimestamp();
        String langIds = getLanguageIds();
        String genreIds = getGenreIds();
        System.out.println("genreIds:"+genreIds);
        Call<ResultObject<HomeDataModel>> cardCall = RetrofitEngine.getRetrofitEngine()
                .getApiRequests(RequestApi.class).getMenuVideos(Constants.ITEM_MORE_LIMIT, Constants.ITEM_LIMIT_ASSET,langIds,genreIds);
        cardCall.enqueue(new Callback<ResultObject<HomeDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<HomeDataModel>> call,
                                   @NonNull Response<ResultObject<HomeDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<HomeDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {


                        insertAll(data.getData().getAssetList());
                  //      setVideoId(data.getData().getAssetList(), 0);

                        if (needReturnEvent) {
                            EventBus.getDefault().post(new AssetsModelResponse(data.getData().getAssetList(), data.isRequestStatus()));
                        }
                    } else {
                        if (needReturnEvent) {
                            EventBus.getDefault().post(new AssetsModelResponse(null, false, true));
                        }
                    }
                } else {
                    if (needReturnEvent) {
                        EventBus.getDefault().post(new AssetsModelResponse(null, false, true));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Throwable t) {
                if (needReturnEvent) {
                    Log.e("ErrorCategory", t.getLocalizedMessage());
                    EventBus.getDefault().post(new AssetsModelResponse(null, false, true));
                }
            }
        });
    }




    public void getAllUpdatedAssetsonRefresh(int categoryId, int assetId, long lastUpdatedDate, boolean needReturnEvent) {
        // long lastUpdatedDate = getLastUpdatedTimestamp();
        String langIds = getLanguageIds();
        System.out.println("languageIds:"+langIds);
        Call<ResultObject<AssetVideosDataModel>> cardCall = RetrofitEngine.getRetrofitEngine()
//                .getApiRequests(RequestApi.class).updateMenuVideos(categoryId, assetId, lastUpdatedDate);
                .getApiRequests(RequestApi.class).updateMenuVideos(categoryId,lastUpdatedDate,langIds);
        cardCall.enqueue(new Callback<ResultObject<AssetVideosDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<AssetVideosDataModel>> call,
                                   @NonNull Response<ResultObject<AssetVideosDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<AssetVideosDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {
                        insertAll(data.getList());

                        if (needReturnEvent) {
                            //  EventBus.getDefault().post(new AssetsModelResponse(data.getData(), data.isRequestStatus()));
                        }
                    } else {
                        if (needReturnEvent) {
                            EventBus.getDefault().post(new AssetsModelResponse(null, false, true));
                        }
                    }
                } else {
                    if (needReturnEvent) {
                        EventBus.getDefault().post(new AssetsModelResponse(null, false, true));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<AssetVideosDataModel>> call, @NonNull Throwable t) {
                if (needReturnEvent) {
                    EventBus.getDefault().post(new AssetsModelResponse(null, false, true));
                }
            }
        });
    }

    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("TAG", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("TAG", str); // continuation
        }
    }
}
