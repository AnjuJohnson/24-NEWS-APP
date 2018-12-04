package com.ottapp.android.basemodule.services;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.app.BaseApplication;
import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;
import com.ottapp.android.basemodule.dao.task.favourite.DeleteFavouriteTask;
import com.ottapp.android.basemodule.dao.task.favourite.GetAllFavouriteActiveTask;
import com.ottapp.android.basemodule.dao.task.favourite.GetFavouriteByIdTask;
import com.ottapp.android.basemodule.dao.task.favourite.InsertAllFavouriteTask;
import com.ottapp.android.basemodule.dao.task.favourite.InsertFavouriteTask;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.services.base.BaseService;
import com.ottapp.android.basemodule.utils.Constants;
import com.ottapp.android.basemodule.utils.DecodeUrl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFavouriteServices extends BaseService<UserFavouritesModel> {
    private static UserFavouriteServices services;
    private UserFavouriteDataDao userFavouritesDao;
    private List<UserFavouritesModel> list;

    private UserFavouriteServices() {
        AppDatabase db = Room.databaseBuilder(BaseApplication.getApplication(),
                AppDatabase.class, "AppData").build();
        userFavouritesDao = db.userFavouriteDao();
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {

    }

    @Override
    public void insert(UserFavouritesModel model) {
        if (userFavouritesDao != null && model != null) {
            new InsertFavouriteTask(userFavouritesDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,model);
        }
    }

    @Override
    public void insertAll(List<UserFavouritesModel> models) {
        if (userFavouritesDao != null && models != null) {
            //noinspection unchecked
            new InsertAllFavouriteTask(userFavouritesDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,models);
        }
    }

    @Override
    public List<UserFavouritesModel> getAll() {
        if (userFavouritesDao != null) {
            try {
                return new GetAllFavouriteActiveTask(userFavouritesDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public LiveData<List<UserFavouritesModel>> getAllLive(int id) {
        return null;
    }

    @Override
    public UserFavouritesModel getById(int id) {
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

    }

    public static UserFavouriteServices getServices() {
        if (services == null) {
            services = new UserFavouriteServices();
        }
        return services;
    }

    public UserFavouritesModel getUserFavourite(int videoId) {
        try {
            return new GetFavouriteByIdTask(userFavouritesDao).execute(videoId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFavourite(int id){
        new DeleteFavouriteTask(userFavouritesDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,id);
    }


    public UserFavouriteDataDao getUserFavouritesDao() {
        return userFavouritesDao;
    }


    public void favouriteFetchRequest(long userId, int assetId, int offset) {
        Call<ResultObject<UserFavouritesModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).fetchFavouriteVideo(userId,assetId,Constants.ITEM_LIMIT);
        qrDataCall.enqueue(new Callback<ResultObject<UserFavouritesModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<UserFavouritesModel>> call, @NonNull Response<ResultObject<UserFavouritesModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    ResultObject<UserFavouritesModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {
                        list = fetchFavouriteVideos(data.getList());
                        insertAll(list);
                    } else {
                       // getIView().showCheckInternetDialog();
                    }
                } else {
                   // getIView().showCheckInternetDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<UserFavouritesModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage()!=null)
                    Log.e("ErrorCategory", t.getLocalizedMessage());
                //getIView().showCheckInternetDialog();
            }
        });
    }

   public void favouriteRequest(FavouriteRequestModel request, long userId, AssetVideosDataModel youtubeVideoModel) {
        Call<ResultObject<FavouriteRequestModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).setFavouriteVideo(userId,request);
        qrDataCall.enqueue(new Callback<ResultObject<FavouriteRequestModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<FavouriteRequestModel>> call, @NonNull Response<ResultObject<FavouriteRequestModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject data = response.body();
                    if (data != null && data.isRequestStatus()) {
                        saveFavouriteVideos(data.getData(),youtubeVideoModel);
                    } else {
                       // getIView().showCheckInternetDialog();
                    }
                } else {
                   // getIView().showCheckInternetDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<FavouriteRequestModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage()!=null)
                    Log.e("ErrorFavourite", t.getLocalizedMessage());
                //getIView().showCheckInternetDialog();
            }
        });
    }
    public String getVideoId(String url){
        String[] separated = url.split("\\=");
        return separated[1];
    }


    private List<UserFavouritesModel> fetchFavouriteVideos(List<UserFavouritesModel> list){
        if(list!=null){
            for(UserFavouritesModel model:list){
                DecodeUrl urlDecoder = new DecodeUrl();

                String videoIdUrl = urlDecoder.decodeData(model.getHlsUrl());
                String videoId = getVideoId(videoIdUrl);
                model.setVideoId(videoId);
            }
        }
        return list;
    }

    private void saveFavouriteVideos(Object data, AssetVideosDataModel youtubeVideoModel) {

        UserFavouritesModel userFavouritesModel = new UserFavouritesModel();
        if(youtubeVideoModel!=null){

            if(youtubeVideoModel.getFavourite() == 1) {
                if(youtubeVideoModel.getAssetTypeId() ==3) {
                    DecodeUrl urlDecoder = new DecodeUrl();
                    String videoIdUrl = urlDecoder.decodeData(youtubeVideoModel.getHlsUrl());
                    String videoId = getVideoId(videoIdUrl);
                    userFavouritesModel.setVideoId(videoId);
                }


                userFavouritesModel.setFavourite(youtubeVideoModel.getFavourite());

                userFavouritesModel.setActive(youtubeVideoModel.getActive());
                userFavouritesModel.setAssetTypeId(youtubeVideoModel.getAssetTypeId());
                userFavouritesModel.setAuthor(youtubeVideoModel.getAuthor());
                userFavouritesModel.setCategory(youtubeVideoModel.getCategory());
                userFavouritesModel.setCategoryId(youtubeVideoModel.getCategoryId());
                userFavouritesModel.setDescription(youtubeVideoModel.getDescription());
                userFavouritesModel.setDuration(youtubeVideoModel.getDuration());
                userFavouritesModel.setEndDate(youtubeVideoModel.getEndDate());
                userFavouritesModel.setExternalAuthor(youtubeVideoModel.getExternalAuthor());
                userFavouritesModel.setGenreId(youtubeVideoModel.getGenreId());
                userFavouritesModel.setHlsUrl(youtubeVideoModel.getHlsUrl());
                userFavouritesModel.setId(youtubeVideoModel.getId());
                userFavouritesModel.setLanguageId(youtubeVideoModel.getLanguageId());
                userFavouritesModel.setName(youtubeVideoModel.getName());
                userFavouritesModel.setPlayed_duration(youtubeVideoModel.getPlayed_duration());
                userFavouritesModel.setRtmpUrl(youtubeVideoModel.getRtmpUrl());
                userFavouritesModel.setStartDate(youtubeVideoModel.getStartDate());
                userFavouritesModel.setSubcategory(youtubeVideoModel.getSubcategory());
                userFavouritesModel.setThumbnailUrl(youtubeVideoModel.getThumbnailUrl());
                insert(userFavouritesModel);
            }else{
                UserFavouriteServices.getServices().deleteFavourite(youtubeVideoModel.getId());
            }

        }
    }
}
