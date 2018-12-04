package com.tfApp.android.newstv.presenter.fragment;

import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.tfApp.android.newstv.adaptors.ComingSoonItemAdapter;
import com.tfApp.android.newstv.adaptors.OnYoutubeItemSelectionListener;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.adaptors.VerticalRecyclerAdapter;
import com.tfApp.android.newstv.adaptors.YoutubeSnap;
import com.tfApp.android.newstv.presenter.fragment.iview.ComingSoonFragmentIView;
import com.tfApp.android.newstv.utils.FilterVideoId;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.MenuLeftActivity;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryAssetsList;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.MoreItemRequestServiceModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.AssetsMoreListResponse;
import com.ottapp.android.basemodule.services.CategoryService;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.DecodeUrl;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.LOAD_DATA;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.TITLE_TEXT;

public class ComingSoonFragmentPresenter<I extends ComingSoonFragmentIView> extends BaseFragmentPresenter<I> implements OnYoutubeItemSelectionListener, PlayedDurationListener {

    private List<AssetVideosDataModel> dataModels = new ArrayList<>();
    private AssetVideosDataModel onItemClickVideoData;
    private ComingSoonItemAdapter youtubeItemAdapter;
    private int maxLimit = 10;
    private int dataId;
    private List<AssetVideosDataModel> favouriteList = new ArrayList<>();

    public ComingSoonFragmentPresenter(I iView) {
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
            getIView().setTitle(title);
            dataId = getIView().getArguments().getInt(LOAD_DATA);
            String loadFor = getIView().getArguments().getString(KEY_LOADER_TYPE);
            if (loadFor == null) {
                //handle no data
                getIView().getTextView().setVisibility(View.VISIBLE);
                return;
            }
//            if (loadFor.equalsIgnoreCase(YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU)) {
//                loadMenuAssets(dataId);
//            } else if (loadFor.equalsIgnoreCase(YoutubeVideoGridVideoGridFragment.LOADER_TYPE_CATEGORY)) {
                loadCategoryAssets(dataId);
          //  }
        }
    }

    private void loadCategoryAssets(int menuId) {
        List<CategoryAssetsList> data = CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(menuId);
        if(!data.isEmpty()) {
        dataModels = data.get(0).getAssetVideos();

            youtubeItemAdapter = new ComingSoonItemAdapter(dataModels, this);
            getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getActivityObj()));
            getIView().getRecyclerView().setAdapter(youtubeItemAdapter);
            getIView().getTextView().setVisibility(View.GONE);

        }else{
            getIView().getTextView().setVisibility(View.VISIBLE);
        }
    }

    private void loadMenuAssets(int menuId) {
        List<CategoryAssetsList> data = CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(menuId);
        List<CategoryAssetsList> filteredData = checkFavouriteStatus(data);
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

    }

    private void setLiveData(List<AssetVideosDataModel> dataAssets) {

        if (dataModels != null) {
            dataModels.clear();
            dataModels.addAll(dataAssets);
            dataModels = checkFavouriteStatusCategory(dataModels);
        }else{
            dataModels.addAll(dataAssets);
        }

        if (youtubeItemAdapter != null)
            youtubeItemAdapter.notifyDataSetChanged();
        if(getIView()!=null) {
            if (youtubeItemAdapter.getItemCount() == 0) {
                //handle no data
                getIView().getTextView().setVisibility(View.VISIBLE);
            } else {
                getIView().getTextView().setVisibility(View.GONE);
            }
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
            maxLimit = moreEvent.getMaxLimit();
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
        DecodeUrl urlDecoder = new DecodeUrl();
        FilterVideoId filterVideoId = new FilterVideoId();
        String videoIdUrl = urlDecoder.decodeData(modal.getHlsUrl());
        String videoId = filterVideoId.getVideoId(videoIdUrl);
        modal.setVideoId(videoId);
        new VideoPlayDurationPresenter().getPlayedDurationService(modal, this);
        onItemClickVideoData = modal;
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
            ((MenuLeftActivity) getIView().getActivityObj()).itemSelection(MenuLeftActivity.POS_MORE_SCREEN);

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
