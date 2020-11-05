package com.tfApp.android.newstv.presenter.fragment;

import android.app.Fragment;
import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.AssetsDetailsResponseEvent;
import com.ottapp.android.basemodule.models.CategoryAssetsList;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.HomeDataModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.models.VasTagModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.repository.responses.HomeMoreModelResponse;
import com.ottapp.android.basemodule.services.CategoryService;
import com.ottapp.android.basemodule.services.GenreService;
import com.ottapp.android.basemodule.services.MenuServices;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.Constants;
import com.ottapp.android.basemodule.utils.DecodeUrl;
import com.ottapp.android.basemodule.utils.FilterVideoId;
import com.ottapp.android.basemodule.utils.ValidatorUrl;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.OnYoutubeItemSelectionListener;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.adaptors.VerticalRecyclerAdapter;
import com.tfApp.android.newstv.adaptors.YoutubeSnap;
import com.tfApp.android.newstv.presenter.fragment.iview.HomeFragmentIView;
import com.tfApp.android.newstv.requests.ApiRequests;
import com.tfApp.android.newstv.utils.ProgressDialog;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.HolderActivity;
import com.tfApp.android.newstv.view.activity.MenuLeftActivity;
import com.tfApp.android.newstv.view.fragment.VideoDetailsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.LOAD_DATA;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.LOAD_LIST;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.TITLE_TEXT;

public class HomeFragmentPresenter<I extends HomeFragmentIView> extends BaseFragmentPresenter<I> implements OnYoutubeItemSelectionListener, PlayedDurationListener {
    private int maxLimit = 10;
    private int i;
    private AssetVideosDataModel onItemClickVideoData;
    private List<CategoryAssetsList> filteredAssetData;
    private VerticalRecyclerAdapter youtubeSnapAdapter;
    String TAG = "test";
    private int defaultMenuId;

    public HomeFragmentPresenter(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        progress_spinner = ProgressDialog.LoadingSpinner(getIView().getActivity(),true);
        //for getting the default menu id from room db
        defaultMenuId = MenuServices.getServices().getDefaultMenu("home");

        GenreModel model = GenreService.getServices().getAllActive();
        if(model!=null){
            ((HolderActivity) getIView().getActivity()).setGenre(model.getName());
        }

    }

    @SuppressWarnings("unchecked")
    private static KProgressHUD progress_spinner;
   // private Dialog progress_spinner;
    //to set the data into the adapter
    public void setupAdapter() {
        progress_spinner.show();
        getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getActivity()));
        getIView().getRecyclerView().setHasFixedSize(true);
        List<CategoryAssetsList> data = null;
        List<YoutubeSnap> items = new ArrayList<>(1);
        if (getIView().getArgument() != null) {
            int menuId=getIView().getArgument().getInt(LOAD_DATA,1);

            data= CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(menuId);
            filteredAssetData = checkFavouriteStatus(data);
            System.out.println("HomePosition"+new Gson().toJson(data));
            String title = getIView().getArgument().getString(TITLE_TEXT);
//            if (title != null)
//                getIView().setTitle(title, "#FFFFFF");



        }
        if (filteredAssetData != null)
            for (CategoryAssetsList categoryAssetsList : filteredAssetData) {
                items.add(new YoutubeSnap(Gravity.START, categoryAssetsList));
            }

        youtubeSnapAdapter = new VerticalRecyclerAdapter(items, true, this);
        if (getIView() != null) {
            getIView().getRecyclerView().setAdapter(youtubeSnapAdapter);






            if((youtubeSnapAdapter!=null)&&(data.size()!=0))  {
                getIView().getTextView().setVisibility(View.GONE);
            }
            else {
                getIView().getTextView().setVisibility(View.VISIBLE);
            }

/*
     if((youtubeSnapAdapter==null)&&(data.size()==0))  {
         getIView().getTextView().setVisibility(View.VISIBLE);
     }
     else {
         getIView().getTextView().setVisibility(View.GONE);
     }*/

           /* if(youtubeSnapAdapter == null){
                getIView().getTextView().setVisibility(View.VISIBLE);
            }else{
                getIView().getTextView().setVisibility(View.GONE);
            }*/
        }




        progress_spinner.dismiss();
    }

    public void updateLiveData(List<CategoryListDataModel> category) {

        List<YoutubeSnap> items = new ArrayList<>(1);
        List<CategoryAssetsList> videoLists = CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(defaultMenuId);
        filteredAssetData = checkFavouriteStatus(videoLists);
//        String title = getIView().getArgument().getString(TITLE_TEXT);
//        if (title != null)
//            getIView().setTitle(title, "#FFFFFF");
        if (filteredAssetData != null)
            for (CategoryAssetsList playListModel : filteredAssetData) {
                items.add(new YoutubeSnap(Gravity.START, playListModel));
            }
        youtubeSnapAdapter.setLiveData(items);
        youtubeSnapAdapter.notifyDataSetChanged();
    }




    public void updateLiveDataAssosiation(List<CategoryAssosiationDataModel> category) {
        List<YoutubeSnap> items = new ArrayList<>(1);
        List<CategoryAssetsList> videoLists = CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(defaultMenuId);
        filteredAssetData = checkFavouriteStatus(videoLists);
//        String title = getIView().getArgument().getString(TITLE_TEXT);
//        if (title != null)
//            getIView().setTitle(title, "#FFFFFF");
        if (filteredAssetData != null)
            for (CategoryAssetsList playListModel : filteredAssetData) {
                items.add(new YoutubeSnap(Gravity.START, playListModel));
            }
        youtubeSnapAdapter.setLiveData(items);
        youtubeSnapAdapter.notifyDataSetChanged();


    }

    public void updateLiveDataAssets(List<AssetVideosDataModel> category) {
        List<YoutubeSnap> items = new ArrayList<>(1);
        List<CategoryAssetsList> videoLists = CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(defaultMenuId);
        //logLargeString(new Gson().toJson(videoLists));
        filteredAssetData = checkFavouriteStatus(videoLists);
//        String title = getIView().getArgument().getString(TITLE_TEXT);
//        if (title != null)
//            getIView().setTitle(title, "#FFFFFF");
        if (filteredAssetData != null)
            for (CategoryAssetsList playListModel : filteredAssetData) {
                items.add(new YoutubeSnap(Gravity.START, playListModel));
            }
        youtubeSnapAdapter.setLiveData(items);
        youtubeSnapAdapter.notifyDataSetChanged();
    }
    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i(TAG, str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i(TAG, str); // continuation
        }
    }
    //compare the favourite list with the data list
    private List<CategoryAssetsList> checkFavouriteStatus(List<CategoryAssetsList> videosdata) {
        if (videosdata!=null)
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

    @Override
    public void onItemSelect(AssetVideosDataModel modal) {

        VasTagModel vasModel = CategoryService.getServices().getVasUrlById(modal.getCategoryId());
        AssetVideosDataModel modelWithVideoId = setVideoId(modal);
       // new VideoPlayDurationPresenter().getPlayedDurationService(modelWithVideoId, this);

        onItemClickVideoData = modelWithVideoId;
        if(vasModel!=null) {
            onItemClickVideoData.setVasUrl(vasModel.getVastTag());
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoDetailsFragment.LOAD_VIDEO_DATA, modelWithVideoId);
        bundle.putSerializable(VideoDetailsFragment.LOAD_USER_DATA,
                getUserDetails());
        VideoDetailsFragment fragment = new VideoDetailsFragment();
        fragment.setArguments(bundle);
        showFragment(fragment);
        ((HolderActivity) getIView().getActivity()).hideLogoWithGenres("Details");
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVideoDetailsEvent(AssetsDetailsResponseEvent responseEvent) {
        UserProfileModel data = getUserDetails();
        if(responseEvent.isError()){
            if (getIView()!=null){
              //  getIView().showRetryDialog();
            }
            return;
        }
        if(responseEvent.isSuccess()){
            AssetDetaillsDataModel assetDetaillsDataModel = responseEvent.getData();
//            onItemClickVideoData.setPlayed_duration(assetDetaillsDataModel.getPlayed_duration());
            Bundle bundle = new Bundle();
            bundle.putSerializable(VideoDetailsFragment.LOAD_VIDEO_DATA, onItemClickVideoData);
            bundle.putSerializable(VideoDetailsFragment.LOAD_USER_DATA,
                    getUserDetails());
            VideoDetailsFragment fragment = new VideoDetailsFragment();
            fragment.setArguments(bundle);
            showFragment(fragment);
           // getIView().switchToYoutubeActivity(onItemClickVideoData,data);
        }else{
            getIView().switchToYoutubeActivity(onItemClickVideoData,data);
        }
    }

    private void showFragment(Fragment fragment) {
        getIView().getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    private AssetVideosDataModel setVideoId(AssetVideosDataModel assetVideosDataModel) {
        FilterVideoId filterVideoId = new FilterVideoId();
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
        progress_spinner.show();
        //getSpecificCategory(playListModel);
        showMoreItemScreens(null, playListModel);
    }

    @Override
    public void onMoreItemsNeeded() {
        Log.e("Home Loading", "Requested " + i++);
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


    //service to get the data based on the category id
    private void getSpecificCategory(AssetVideosDataModel playListModel) {
        Call<ResultObject<HomeDataModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(ApiRequests.class).getSpecificCategory(playListModel.getCategoryId(), Constants.ITEM_MORE_LIMIT, maxLimit);
        qrDataCall.enqueue(new Callback<ResultObject<HomeDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Response<ResultObject<HomeDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<HomeDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {
                        //showMoreItemScreens(data.getData().getAssetList(), playListModel);
                        maxLimit = data.getData().getMaxLimit();

                    } else {
                        getIView().showCheckInternetDialog();
                    }
                } else {
                    getIView().showCheckInternetDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<HomeDataModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage() != null)
                    Log.e("ErrorCategory", t.getLocalizedMessage());
                getIView().showCheckInternetDialog();
            }
        });
    }


    //to navigate to the player
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onHomeMoreEvent(HomeMoreModelResponse versionEvent) {
        if (versionEvent.isError()) {
            if (getIView() != null) {
                getIView().showRetryDialog();
            }
            return;
        }
        if (versionEvent.isSuccess()) {
            //  showMoreItemScreens(versionEvent.getDatas());
        }
    }

    //to navigate into the more items screen
    private void showMoreItemScreens(List<AssetVideosDataModel> playListModel, AssetVideosDataModel listModel) {
        if (getIView() != null) {
            ((HolderActivity) getIView().getActivity()).showLogo("More Videos");
            CategoryListDataModel categoryModel = CategoryService.getServices().getById(listModel.getCategoryId());
            Intent intent = getIView().getActivity().getIntent();
            intent.putExtra(TITLE_TEXT, categoryModel.getName());
            intent.putExtra(LOAD_LIST,(Serializable) playListModel);
            intent.putExtra(LOAD_DATA, categoryModel.getId());
            ((HolderActivity) getIView().getActivity()).itemSelection(MenuLeftActivity.POS_MORE_SCREEN);
            progress_spinner.dismiss();
        }

    }

    public void destroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    //to show the progress dialog
    public void showMoreProgress() {
        getIView().showCheckInternetDialog();
    }
}
