package com.tfApp.android.newstv.presenter.fragment;

import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.AutoFitGridLayoutManager;
import com.tfApp.android.newstv.adaptors.OnYoutubeItemSelectionListener;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.adaptors.YoutubeItemAdapter;
import com.tfApp.android.newstv.presenter.fragment.iview.FavouriteFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.AssetsDetailsResponseEvent;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.DecodeUrl;
import com.ottapp.android.basemodule.utils.ValidatorUrl;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragmentPresenter<I extends FavouriteFragmentIView> extends BaseFragmentPresenter<I> implements OnYoutubeItemSelectionListener, SwipeRefreshLayout.OnRefreshListener,PlayedDurationListener {

    private List<AssetVideosDataModel> data = new ArrayList<>();
    private YoutubeItemAdapter youtubeItemAdapter;
    private AssetVideosDataModel onItemClickVideoData;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    public FavouriteFragmentPresenter(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);

        }
    }

    //to set the data into the adapter
    public void setupAdapter(boolean loading) {
        getIView().getRecyclerView().setHasFixedSize(true);
        getIView().setTitle(getIView().getActivity().getString(R.string.screen_title_favorites));
        getIView().getTextView().setVisibility(!loading ? View.VISIBLE : View.INVISIBLE);
        getIView().getTextView().setText(R.string.favourite_videos_screen_no_data_hint);

        youtubeItemAdapter = new YoutubeItemAdapter(data, this, false);
        getIView().getRecyclerView().setAdapter(youtubeItemAdapter);
        getIView().getRecyclerView().setLayoutManager(new AutoFitGridLayoutManager(getIView().getActivity(), getIView().getActivity().getResources().getDimensionPixelSize(R.dimen._140sdp)));

    }

    //compare with the list of favorite videos from db
    public void getFavoriteVideos(){

        List<UserFavouritesModel> favouritesData = UserFavouriteServices.getServices().getAll();

        if(favouritesData != null && !favouritesData.isEmpty())
            for(UserFavouritesModel favourites : favouritesData){
                AssetVideosDataModel assetVideosDataModel = new AssetVideosDataModel();
                assetVideosDataModel.setVideoId(favourites.getVideoId());
                assetVideosDataModel.setFavourite(favourites.getFavourite());
                assetVideosDataModel.setActive(favourites.getActive());
                assetVideosDataModel.setAssetTypeId(favourites.getAssetTypeId());
                assetVideosDataModel.setAuthor(favourites.getAuthor());
                assetVideosDataModel.setCategory(favourites.getCategory());
                assetVideosDataModel.setCategoryId(favourites.getCategoryId());
                assetVideosDataModel.setDescription(favourites.getDescription());
                assetVideosDataModel.setDuration(favourites.getDuration());
                assetVideosDataModel.setEndDate(favourites.getEndDate());
                assetVideosDataModel.setExternalAuthor(favourites.getExternalAuthor());
                assetVideosDataModel.setGenreId(favourites.getGenreId());
                assetVideosDataModel.setHlsUrl(favourites.getHlsUrl());
                assetVideosDataModel.setId(favourites.getId());
                assetVideosDataModel.setLanguageId(favourites.getLanguageId());
                assetVideosDataModel.setName(favourites.getName());
                assetVideosDataModel.setPlayed_duration(favourites.getPlayed_duration());
                assetVideosDataModel.setRtmpUrl(favourites.getRtmpUrl());
                assetVideosDataModel.setStartDate(favourites.getStartDate());
                assetVideosDataModel.setSubcategory(favourites.getSubcategory());
                assetVideosDataModel.setThumbnailUrl(favourites.getThumbnailUrl());

                data.add(assetVideosDataModel);
            }
         data = checkFavouriteStatusCategory(data);

         if(data!= null && !data.isEmpty()){
             setupAdapter(false);
             getIView().getTextView().setVisibility(View.GONE);
         }else{
             if (getIView() != null)
                     //handle no data
             getIView().getTextView().setVisibility(View.VISIBLE);
             setupAdapter(false);
         }

    }
    private List<AssetVideosDataModel> checkFavouriteStatusCategory(List<AssetVideosDataModel> videosdata) {
        for (AssetVideosDataModel assetVideosDataModel : videosdata) {
            UserFavouritesModel model = UserFavouriteServices.getServices().getUserFavourite(assetVideosDataModel.getId());
            if (model != null) {
                assetVideosDataModel.setFavourite(1);
            } else {
                assetVideosDataModel.setFavourite(0);
            }
        }
        return videosdata;
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
    public void onItemSelect(AssetVideosDataModel modal) {
        AssetVideosDataModel modelWithVideoId = setVideoId(modal);
        new VideoPlayDurationPresenter().getPlayedDurationService(modelWithVideoId,this);

        onItemClickVideoData = modal;
    }


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
    @Override
    public void onMoreItemsClicked(AssetVideosDataModel modal) {

    }

    @Override
    public void onMoreItemsNeeded() {

    }

    @Override
    public void onFavouriteActionSelected(AssetVideosDataModel youtubeVideoModel, int position) {

        if ( data != null) {
            data.remove(position);
            youtubeItemAdapter.notifyDataSetChanged();
        }
        if (getIView() != null)
            if (data == null || data.isEmpty()) {
                //handle no data
                getIView().getTextView().setVisibility(View.VISIBLE);
            } else {
                getIView().getTextView().setVisibility(View.GONE);
            }
        favouriteServiceRequest(youtubeVideoModel);
    }
    //creating th favourite service request
    private void favouriteServiceRequest(AssetVideosDataModel youtubeVideoModel) {
        FavouriteRequestModel request = new FavouriteRequestModel();
        request.setActive(youtubeVideoModel.getFavourite());
        request.setAssetId(youtubeVideoModel.getId());
        UserFavouriteServices.getServices().favouriteRequest(request, PreferenceManager.getManager().getUserId(),youtubeVideoModel);

    }

    @Override
    public void onRefresh() {

        setupAdapter(true);
    }
    private UserProfileModel getUserDetails() {
        UserProfileModel userProfileModel = new UserProfileModel();
        userProfileModel.setId(PreferenceManager.getManager().getUserId());
        userProfileModel.setMobileNumber(PreferenceManager.getManager().getUserMobileNumber());
        userProfileModel.setEmailId(StaticValues.emailId = PreferenceManager.getManager().getUserEmailId());

        return userProfileModel;
    }
    @Override
    public void showItemClickScreen(AssetVideosDataModel modal) { //to navigate into the other screen
        onItemClickVideoData.setPlayed_duration(modal.getPlayed_duration());
        if (getIView() != null) {
            if(modal.getAssetTypeId() == StaticValues.youtube_activity_id) {
                Intent intent = new Intent(getIView().getActivity(), MediaStreamingLandActivity.class);
                intent.putExtra(MediaStreamingLandActivity.DATA_MEDIA, onItemClickVideoData);
                UserProfileModel data = getUserDetails();
                intent.putExtra(MediaStreamingLandActivity.DATA_USER, data);
                getIView().getActivity().startActivity(intent);
            }
        }
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
