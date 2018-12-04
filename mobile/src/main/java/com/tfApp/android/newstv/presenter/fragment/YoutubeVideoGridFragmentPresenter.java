package com.tfApp.android.newstv.presenter.fragment;

import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.AutoFitGridLayoutManager;
import com.tfApp.android.newstv.adaptors.OnYoutubeItemSelectionListener;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.adaptors.VerticalRecyclerAdapter;
import com.tfApp.android.newstv.adaptors.YoutubeItemAdapter;
import com.tfApp.android.newstv.adaptors.YoutubeSnap;
import com.tfApp.android.newstv.presenter.fragment.iview.YoutubeVideoGridFragmentIView;
import com.tfApp.android.newstv.utils.ProgressDialog;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.HolderActivity;
import com.tfApp.android.newstv.view.activity.MenuLeftActivity;
import com.tfApp.android.newstv.view.fragment.GenreFragment;
import com.tfApp.android.newstv.view.fragment.VideoDetailsFragment;
import com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.AssetsDetailsResponseEvent;
import com.ottapp.android.basemodule.models.AssetsViewDataModel;
import com.ottapp.android.basemodule.models.CategoryAssetsList;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.MoreItemRequestServiceModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.models.VasTagModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.AssetsMoreListResponse;
import com.ottapp.android.basemodule.services.AssetMenuService;
import com.ottapp.android.basemodule.services.CategoryService;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.DecodeUrl;
import com.ottapp.android.basemodule.utils.ValidatorUrl;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.LOAD_DATA;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.LOAD_LIST;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.TITLE_TEXT;

public class YoutubeVideoGridFragmentPresenter<I extends YoutubeVideoGridFragmentIView> extends BaseFragmentPresenter<I> implements OnYoutubeItemSelectionListener, PlayedDurationListener {

    private List<AssetVideosDataModel> dataModels = new ArrayList<>();
    private AssetVideosDataModel onItemClickVideoData;
    private YoutubeItemAdapter youtubeItemAdapter;
    private int maxLimit = 10;
    private int dataId;
    private boolean loadMore = false;
    private String loadFor;
    private List<AssetVideosDataModel> favouriteList = new ArrayList<>();
    public YoutubeVideoGridFragmentPresenter(I iView) {
        super(iView);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);

        }
    }

    //adapter class to set the data into the recyclerview
    @SuppressWarnings("unchecked")
    public void setupAdapter() {
        getIView().getRecyclerView().setHasFixedSize(true);
        if (getIView().getArguments() != null) {
            String title = getIView().getArguments().getString(TITLE_TEXT);
            Intent intent = getIView().getActivityObj().getIntent();
            getIView().setTitle(title);
           // getIView().getTextView().setVisibility(View.GONE);
           // getIView().getTextView().setText(R.string.common_videos_screen_no_data_hint);
            List<AssetVideosDataModel> list = AssetMenuService.getServices().getAll();
            dataId = getIView().getArguments().getInt(LOAD_DATA);
            loadFor = getIView().getArguments().getString(KEY_LOADER_TYPE);
            if (loadFor == null) {
                //handle no data
               // getIView().getTextView().setVisibility(View.VISIBLE);
                return;
            }
            if (loadFor.equalsIgnoreCase(YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU)) {
                loadMenuAssets(dataId);
            } else if (loadFor.equalsIgnoreCase(YoutubeVideoGridVideoGridFragment.LOADER_TYPE_CATEGORY)) {
                loadCategoryAssets(dataId);
            }
        }
    }

    private void loadCategoryAssets(int categoryId) {

        AssetsViewDataModel viewDataModel = ViewModelProviders.of((FragmentActivity) getIView().getActivityObj()).get(AssetsViewDataModel.class);
//        // Update the cached copy of the words in the adapter.
        viewDataModel.getAllMenus(categoryId).observe(((FragmentActivity) getIView().getActivityObj()), this::setLiveData);
     //   dataModels = AssetMenuService.getServices().getAssetsUnderCategory(categoryId);
        youtubeItemAdapter = new YoutubeItemAdapter(dataModels, this, loadMore);
        getIView().getRecyclerView().setAdapter(youtubeItemAdapter);
        getIView().getRecyclerView().setLayoutManager(new AutoFitGridLayoutManager(getIView().getActivityObj(), getIView().getActivityObj().getResources().getDimensionPixelSize(R.dimen._140sdp)));
        if (youtubeItemAdapter.getItemCount() == 0) {
            //handle no data
       //     getIView().getTextView().setVisibility(View.VISIBLE);
        } else {
       //     getIView().getTextView().setVisibility(View.GONE);
        }
    }

    public void showGenreFragment(){
        GenreFragment genreFragment = new GenreFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
        genreFragment.setArguments(bundle);
        showFragment(genreFragment);

    }

    private void showFragment(Fragment fragment) {
        getIView().getActivityObj().getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    public void loadLiveCategory(int categoryId){
                AssetsViewDataModel viewDataModel = ViewModelProviders.of((FragmentActivity) getIView().getActivityObj()).get(AssetsViewDataModel.class);
//        // Update the cached copy of the words in the adapter.
                viewDataModel.getAllMenus(categoryId).observe(((FragmentActivity) getIView().getActivityObj()), this::setLiveData);
    }
    private void loadMenuAssets(int menuId) {

        List<CategoryAssetsList> data = CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(menuId);
        List<CategoryAssetsList> filteredData = checkFavouriteStatus(data);
        if(!filteredData.isEmpty()) {
            if (filteredData.size() > 1) {
                getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getActivityObj()));
                List<YoutubeSnap> items = new ArrayList<>(1);
                for (CategoryAssetsList playListModel : filteredData) {
                    items.add(new YoutubeSnap(Gravity.START, playListModel));
                }
                VerticalRecyclerAdapter youtubeSnapAdapter = new VerticalRecyclerAdapter(items, false, this);
                getIView().getRecyclerView().setAdapter(youtubeSnapAdapter);
                if (youtubeSnapAdapter.getItemCount() == 0) {
                    //handle no data
                    getIView().getTextView().setVisibility(View.VISIBLE);
                } else {
                    getIView().getTextView().setVisibility(View.GONE);
                }
            } else {
                loadGiidAssets(filteredData.get(0).getAssetVideos());
            }
        }else{
            getIView().getTextView().setVisibility(View.VISIBLE);
        }

    }
    private void loadGiidAssets(List<AssetVideosDataModel> dataModels) {
        dataId = dataModels.get(0).getCategoryId();
//        youtubeItemAdapter = new YoutubeItemAdapter(dataModels, this,loadMore);
//        getIView().getRecyclerView().setAdapter(youtubeItemAdapter);
//        getIView().getRecyclerView().setLayoutManager(new AutoFitGridLayoutManager(getIView().getActivityObj(), getIView().getActivityObj().getResources().getDimensionPixelSize(R.dimen._140sdp)));
//        if (youtubeItemAdapter.getItemCount() == 0) {
//            //handle no data
//          //  getIView().getTextView().setVisibility(View.VISIBLE);
//        } else {
//       //     getIView().getTextView().setVisibility(View.GONE);
//        }
        loadCategoryAssets(dataId);
    }
    private void setLiveData(List<AssetVideosDataModel> dataAssets) {
        dataModels.addAll(dataAssets);
        dataModels = checkFavouriteStatusCategory(dataAssets);
//        if (loadFor.equalsIgnoreCase(YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU)) {
//        if (dataModels != null) {
//            dataModels.clear();
//            dataModels.addAll(dataAssets);
//            dataModels = checkFavouriteStatusCategory(dataModels);
//        }
//        if (youtubeItemAdapter != null)
//            youtubeItemAdapter.notifyDataSetChanged();
        if(getIView()!=null) {
            if (dataModels.isEmpty()) {
                //handle no data
                getIView().getTextView().setVisibility(View.VISIBLE);
            } else {
                getIView().getTextView().setVisibility(View.GONE);
            }
        }

        youtubeItemAdapter.setLiveAssetsData(dataModels);
        if(getIView()!=null) {
            getIView().getRecyclerView().post(new Runnable() {
                @Override
                public void run() {
                    youtubeItemAdapter.notifyDataSetChanged();

                }
            });
        }

    }


    private void getMoreItemsOnScroll(MoreItemRequestServiceModel requestServiceModel) {
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_TYPE_MORE_ITEMS, requestServiceModel);
        EventBus.getDefault().post(repoRequestEvent);
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoreItemsEvent(AssetsMoreListResponse moreEvent) {
        if (moreEvent.isError()) {
            if (getIView() != null) {
                getIView().showRetryDialog();
            }
            return;
        }
        if (moreEvent.isSuccess()) {
            List<AssetVideosDataModel> list = moreEvent.getDatas();
//            System.out.println("beforAdd:");
//            logLargeString(new Gson().toJson(list));
           dataModels.addAll(list);
//            System.out.println("AfterAdd:");
    //        logLargeString(new Gson().toJson(dataModels));
            if(list!=null || !list.isEmpty())
                youtubeItemAdapter.setLiveAssetsData(dataModels);
                maxLimit = moreEvent.getMaxLimit();
                getIView().getRecyclerView().post(new Runnable()
                {
                    @Override
                    public void run() {
                        youtubeItemAdapter.notifyDataSetChanged();

                    }
                });

//            if (loadFor.equalsIgnoreCase(YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU)){
//                loadItemScrollData();
//            }else {
//                loadLiveCategory(dataId);
//            }

        }else{
            getIView().showRetryDialog();
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
    //here we check the data is included in favourite or not
    //compare the favourite list with the data list
    private List<CategoryAssetsList> checkFavouriteStatus(List<CategoryAssetsList> videosdata) {
        for (CategoryAssetsList categoryAssetsList : videosdata) {

            if (categoryAssetsList.getAssetVideos() != null) {
                for (AssetVideosDataModel assetVideosDataModel : categoryAssetsList.getAssetVideos()) {
                    UserFavouritesModel model = UserFavouriteServices.getServices().getUserFavourite(assetVideosDataModel.getId());

                    if (model != null) {
                        assetVideosDataModel.setFavourite(1);
                    } else {
                        assetVideosDataModel.setFavourite(0);
                    }
                }
            }
        }
        return videosdata;
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
    @Override
    public void onItemSelect(AssetVideosDataModel modal) {
//        AssetVideosDataModel modelwithVideoId = setVideoId(modal);
//        new VideoPlayDurationPresenter().getPlayedDurationService(modelwithVideoId, this);
//        onItemClickVideoData = modal;
        VasTagModel vasModel = CategoryService.getServices().getVasUrlById(modal.getCategoryId());
        AssetVideosDataModel modelWithVideoId = setVideoId(modal);
        // new VideoPlayDurationPresenter().getPlayedDurationService(modelWithVideoId, this);

        onItemClickVideoData = modelWithVideoId;
        if(vasModel!=null) {
            onItemClickVideoData.setVasUrl(vasModel.getVastTag());
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoDetailsFragment.LOAD_VIDEO_DATA, onItemClickVideoData);
        bundle.putSerializable(VideoDetailsFragment.LOAD_USER_DATA,
                getUserDetails());
        VideoDetailsFragment fragment = new VideoDetailsFragment();
        fragment.setArguments(bundle);
        showFragment(fragment);
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
    //to get the user details
    private UserProfileModel getUserDetails() {
        UserProfileModel userProfileModel = new UserProfileModel();
        userProfileModel.setId(PreferenceManager.getManager().getUserId());
        userProfileModel.setMobileNumber(PreferenceManager.getManager().getUserMobileNumber());
        userProfileModel.setEmailId(StaticValues.emailId = PreferenceManager.getManager().getUserEmailId());

        return userProfileModel;
    }

    @Override
    public void onMoreItemsClicked(AssetVideosDataModel playListModel) {
        if (getIView() != null && getIView().getActivityObj() != null) {
            CategoryListDataModel categoryModel = CategoryService.getServices().getById(playListModel.getCategoryId());
            Intent intent = getIView().getActivityObj().getIntent();
            intent.putExtra(TITLE_TEXT, categoryModel.getName());
            intent.putExtra(LOAD_DATA, categoryModel.getId());
            ((HolderActivity) getIView().getActivityObj()).itemSelection(MenuLeftActivity.POS_MORE_SCREEN);

        }
    }

    @Override
    public void onMoreItemsNeeded() {
        MoreItemRequestServiceModel requestServiceModel = new MoreItemRequestServiceModel(dataId, maxLimit);
        getMoreItemsOnScroll(requestServiceModel);
    }

    @Override
    public void onFavouriteActionSelected(AssetVideosDataModel youtubeVideoModel, int position) {//converting the asset data into favourite object to store in db
        favouriteServiceRequest(youtubeVideoModel);

    }

    //creating th favourite service request
    private void favouriteServiceRequest(AssetVideosDataModel youtubeVideoModel) {
        FavouriteRequestModel request = new FavouriteRequestModel();
        request.setActive(youtubeVideoModel.getActive());
        request.setAssetId(youtubeVideoModel.getId());
        UserFavouriteServices.getServices().favouriteRequest(request, PreferenceManager.getManager().getUserId(), youtubeVideoModel);

    }

    @Override
    public void showItemClickScreen(AssetVideosDataModel modal) { //go to the youtube player

        onItemClickVideoData.setPlayed_duration(modal.getPlayed_duration());
        if (getIView() != null) {
            if (modal.getAssetTypeId() == StaticValues.youtube_activity_id) {
                Intent intent = new Intent(getIView().getActivityObj(), MediaStreamingLandActivity.class);
                intent.putExtra(MediaStreamingLandActivity.DATA_MEDIA, onItemClickVideoData);
                UserProfileModel data = getUserDetails();
                intent.putExtra(MediaStreamingLandActivity.DATA_USER, data);
                getIView().getActivityObj().startActivity(intent);
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
