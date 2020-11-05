package com.tfApp.android.newstv.presenter.fragment;

import android.annotation.SuppressLint;
import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.bitryt.com.youtubedataapi.background.OnLoadingCompletedListener;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.AssetsDetailsResponseEvent;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.HomeDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.services.AssetMenuService;
import com.ottapp.android.basemodule.services.GenreService;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.DecodeUrl;
import com.ottapp.android.basemodule.utils.ValidatorUrl;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.AutoFitGridLayoutManager;
import com.tfApp.android.newstv.adaptors.OnYoutubeItemSelectionListener;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.adaptors.YoutubeItemAdapter;
import com.tfApp.android.newstv.presenter.fragment.iview.SearchFragmentIView;
import com.tfApp.android.newstv.utils.ProgressDialog;
import com.tfApp.android.newstv.utils.StaticValues;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragmentPresenter<I extends SearchFragmentIView> extends BaseFragmentPresenter<I> implements OnYoutubeItemSelectionListener, OnLoadingCompletedListener<AssetVideosDataModel>, PlayedDurationListener {

    private List<AssetVideosDataModel> data = new ArrayList<>();
    private YoutubeItemAdapter youtubeItemAdapter;
    //private YoutubeSearchModel searchModel;
    private Handler handler;
    private int maxLimit = 0;
    private int itemLimit = 10;
    private String query;
    private AssetVideosDataModel onItemClickVideoData;
    private KProgressHUD progress_spinner;



    public SearchFragmentPresenter(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        progress_spinner = ProgressDialog.LoadingSpinner(getIView().getActivity(),true);
    }

    @SuppressWarnings("unchecked")
    public void setupAdapter() {
        progress_spinner.show();

        handler = new Handler();
        getIView().getRecyclerView().setHasFixedSize(true);
        getIView().setTitle(getIView().getActivity().getString(R.string.screen_title_favorites));

        getIView().getTextView().setVisibility(View.VISIBLE);
        getIView().getTextView().setText(R.string.search_videos_screen_no_data_hint);
        getIView().getProgressView().setVisibility(View.GONE);

        youtubeItemAdapter = new YoutubeItemAdapter(data, this, false);
        getIView().getRecyclerView().setAdapter(youtubeItemAdapter);
        getIView().getRecyclerView().setLayoutManager(new AutoFitGridLayoutManager(getIView().getActivity(), getIView().getActivity().getResources().getDimensionPixelSize(R.dimen._140sdp)));
        progress_spinner.dismiss();
    }

    public void startQuery(String query) {
        this.query = query;
        if (query == null || query.isEmpty()) {
            // searchModel = null;
            data.clear();
            itemLimit = 10;
            if (handler != null)
                handler.post(() -> {
                    youtubeItemAdapter.notifyDataSetChanged();
                    if (data.isEmpty()) {
                        //handle no data
                        itemLimit =10;
                        getIView().getTextView().setVisibility(View.VISIBLE);
                    } else {
                        getIView().getTextView().setVisibility(View.GONE);
                    }
                });

        } else {
            getSearchItems(query);
        }

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
    //   // service to get the data based on the category id
    private void getSearchItems(String query) {
        String langIds = AssetMenuService.getServices().getLanguageIds();
        String genreIds = getGenreIds();
        Call<ResultObject<HomeDataModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getSearchDataMore(0, itemLimit, maxLimit, query,langIds,genreIds);
        qrDataCall.enqueue(new Callback<ResultObject<HomeDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Response<ResultObject<HomeDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<HomeDataModel> responseData = response.body();
                    if (responseData != null && responseData.isRequestStatus()) {

                        maxLimit = responseData.getData().getMaxLimit();
                        itemLimit=0;
                        progress_spinner.show();
                       showMoreItems(responseData.getData().getAssetList());
                       // setVideoId(responseData.getData().getAssetList());

                    } else {
                       data.clear();
                       showListWithNocontent();
                    }
                } else {
                  //  data.clear();
                    showListWithNocontent();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage() != null)
                    Log.e("ErrorCategory", t.getLocalizedMessage());
                progress_spinner.dismiss();

            }
        });
    }


    private void showListWithNocontent(){
        if (data.isEmpty()) {
            //handle no data
            itemLimit = 10;
            getIView().getTextView().setVisibility(View.VISIBLE);
        } else {
            getIView().getTextView().setVisibility(View.GONE);
        }
        progress_spinner.dismiss();
    }

    private void loadMoreData(List<AssetVideosDataModel> assetList){
      data.addAll(assetList);
      if(data == null || data.isEmpty()){
          getIView().getTextView().setVisibility(View.GONE);
      }else{
          getIView().getTextView().setVisibility(View.VISIBLE);
      }
        youtubeItemAdapter.setLiveAssetsData(data);
        getIView().getRecyclerView().post(new Runnable()
        {
            @Override
            public void run() {
                youtubeItemAdapter.notifyDataSetChanged();

            }
        });
        progress_spinner.dismiss();
    }
//    private void setVideoId(List<AssetVideosDataModel> list) {
//        if(!list.isEmpty()) {
//            FilterVideoId filterVideoId = new FilterVideoId();
//            DecodeUrl urlDecoder = new DecodeUrl();
//            ValidatorUrl validatorUrl = new ValidatorUrl();
//            List<AssetVideosDataModel> menuVideoList = new ArrayList<>();
//            for (AssetVideosDataModel assetVideosDataModel : list) {
//                if (assetVideosDataModel.getAssetTypeId() == 3)
//                    if (assetVideosDataModel.getHlsUrl() != null) {
//                        if (validatorUrl.isValidUrl(assetVideosDataModel.getHlsUrl())) {
//                            String videoIdUrl = urlDecoder.decodeData(assetVideosDataModel.getHlsUrl());
//                            String videoId = filterVideoId.getVideoId(videoIdUrl);
//                            assetVideosDataModel.setVideoId(videoId);
//                            menuVideoList.add(assetVideosDataModel);
//                        }
//                    } else {
//                        if (validatorUrl.isValidUrl(assetVideosDataModel.getRtmpUrl())) {
//                            String videoIdUrl = urlDecoder.decodeData(assetVideosDataModel.getRtmpUrl());
//                            String videoId = filterVideoId.getVideoId(videoIdUrl);
//                            assetVideosDataModel.setVideoId(videoId);
//                            menuVideoList.add(assetVideosDataModel);
//                        }
//                    }
//
//            }
//            showMoreItems(menuVideoList);
//        }
//        progress_spinner.dismiss();
//    }

    private AssetVideosDataModel setVideoId(AssetVideosDataModel assetVideosDataModel) {
        com.ottapp.android.basemodule.utils.FilterVideoId filterVideoId = new com.ottapp.android.basemodule.utils.FilterVideoId();
        DecodeUrl urlDecoder = new DecodeUrl();
        ValidatorUrl validatorUrl = new ValidatorUrl();

        if (assetVideosDataModel.getAssetTypeId() == 3)
            if (assetVideosDataModel.getHlsUrl() != null) {
                if (validatorUrl.isValidUrl(assetVideosDataModel.getHlsUrl())) {
                    String videoIdUrl = urlDecoder.decodeData(assetVideosDataModel.getHlsUrl());
                    String videoId = filterVideoId.getVideoId(videoIdUrl);
                    assetVideosDataModel.setVideoId(videoId);

                }
            } else {
                if (validatorUrl.isValidUrl(assetVideosDataModel.getRtmpUrl())) {
                    String videoIdUrl = urlDecoder.decodeData(assetVideosDataModel.getRtmpUrl());
                    String videoId = filterVideoId.getVideoId(videoIdUrl);
                    assetVideosDataModel.setVideoId(videoId);

                }
            }
        return assetVideosDataModel;
    }

    private void showMoreItems(List<AssetVideosDataModel> playListModel) {

        if (getIView() != null) {
            if (playListModel.isEmpty() || playListModel.size() == 0) {

                data.clear();
                itemLimit = 10;
                getIView().getTextView().setVisibility(View.VISIBLE);
            } else {
                data.clear();
                data.addAll(playListModel);
                getIView().getTextView().setVisibility(View.GONE);

            }
            youtubeItemAdapter.setLiveAssetsData(data);
            getIView().getRecyclerView().post(new Runnable()
            {
                @Override
                public void run() {
                    youtubeItemAdapter.notifyDataSetChanged();

                }
            });
            progress_spinner.dismiss();
        }
    }
    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("TAG", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("TAG", str); // continuation
        }
    }
    @Override
    public void onLoadingCompleted(List<AssetVideosDataModel> result, boolean loadMoreResult) {

    }

    @Override
    public void onLoadingStarted() {

    }

    @Override
    public void onItemSelect(AssetVideosDataModel modal) {
        AssetVideosDataModel assetModel = setVideoId(modal);
        new VideoPlayDurationPresenter().getPlayedDurationService(assetModel, this);

        onItemClickVideoData = assetModel;
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVideoDetailsEvent(AssetsDetailsResponseEvent responseEvent) {
        UserProfileModel data = getUserDetails();
        if(responseEvent.isError()){
            if (getIView()!=null){
                //getIView().showRetryDialog();
            }
            return;
        }
        if(responseEvent.isSuccess()){
            AssetDetaillsDataModel assetDetaillsDataModel = responseEvent.getData();
            onItemClickVideoData.setPlayed_duration(assetDetaillsDataModel.getPlayed_duration());
            getIView().switchToYoutubeActivity(onItemClickVideoData,data);
        }else{
            getIView().switchToYoutubeActivity(onItemClickVideoData,data);
        }
    }
    @Override
    public void onMoreItemsClicked(AssetVideosDataModel modal) {

    }

    @Override
    public void onMoreItemsNeeded() {

            getSearchItems(query);
            itemLimit = 0;
    }

    @Override
    public void onFavouriteActionSelected(AssetVideosDataModel youtubeVideoModel, int position) {
        favouriteServiceRequest(youtubeVideoModel);
    }
    private void favouriteServiceRequest(AssetVideosDataModel youtubeVideoModel) {
        FavouriteRequestModel request = new FavouriteRequestModel();
        request.setActive(youtubeVideoModel.getFavourite());
        request.setAssetId(youtubeVideoModel.getId());
        UserFavouriteServices.getServices().favouriteRequest(request, PreferenceManager.getManager().getUserId(), youtubeVideoModel);
    }
    @Override
    public void showItemClickScreen(AssetVideosDataModel modal) {
        onItemClickVideoData.setPlayed_duration(modal.getPlayed_duration());
        if (getIView() != null) {
            if (modal.getAssetTypeId() == StaticValues.youtube_activity_id) {
                Intent intent = new Intent(getIView().getActivity(), MediaStreamingLandActivity.class);
                intent.putExtra(MediaStreamingLandActivity.DATA_MEDIA, onItemClickVideoData);
                UserProfileModel data = getUserDetails();
                intent.putExtra(MediaStreamingLandActivity.DATA_USER, data);
                getIView().getActivity().startActivity(intent);
            }
        }
    }

    //to get the user details
    private UserProfileModel getUserDetails() {
        UserProfileModel userProfileModel = new UserProfileModel();
        userProfileModel.setId(PreferenceManager.getManager().getUserId());
        userProfileModel.setMobileNumber(PreferenceManager.getManager().getUserMobileNumber());
        userProfileModel.setEmailId(StaticValues.emailId = PreferenceManager.getManager().getUserEmailId());

        return userProfileModel;
    }

    public void onDestroy() {
        if (getIView() != null) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }

    @Override
    public void showMoreProgress() {

    }
}
