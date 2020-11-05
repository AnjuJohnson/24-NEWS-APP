package com.tfApp.android.newstv.presenter.fragment;

import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ottapp.android.basemodule.app.GlideApp;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.AssetsDetailsResponseEvent;
import com.ottapp.android.basemodule.models.CategoryAssetsList;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.services.CategoryService;
import com.ottapp.android.basemodule.services.MenuServices;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.DecodeUrl;
import com.ottapp.android.basemodule.utils.FilterVideoId;
import com.ottapp.android.basemodule.utils.ValidatorUrl;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.OnYoutubeItemSelectionListener;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.adaptors.VerticalRecyclerAdapter;
import com.tfApp.android.newstv.adaptors.YoutubeSnap;
import com.tfApp.android.newstv.presenter.fragment.iview.VideoDetailFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.HolderActivity;
import com.tfApp.android.newstv.view.activity.MediaStreamingActivityExoPlayer;
import com.tfApp.android.newstv.view.activity.MenuLeftActivity;
import com.tfApp.android.newstv.view.fragment.VideoDetailsFragment;
import com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tfApp.android.newstv.view.fragment.InfoFragment.TITLE_TEXT;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.LOAD_DATA;
import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.LOAD_LIST;

public class VideoDetailsFragmentPresenter<I extends VideoDetailFragmentIView> extends BaseFragmentPresenter<I> implements OnYoutubeItemSelectionListener, PlayedDurationListener {
    private int maxLimit = 10;
    private int i;
    private AssetVideosDataModel onItemClickVideoData;
    private UserProfileModel userProfileModel;
    private List<CategoryAssetsList> filteredAssetData;
    private VerticalRecyclerAdapter youtubeSnapAdapter;
    String TAG = "test";
    private RequestOptions ro;
    private int defaultMenuId;

    public VideoDetailsFragmentPresenter(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        defaultMenuId = MenuServices.getServices().getDefaultMenu("home");
    }

    @SuppressWarnings("unchecked")

    //to set the data into the adapter
    public void setupAdapter() {
        if (getIView().getArguments() != null) {
            AssetVideosDataModel assetVideosDataModel = (AssetVideosDataModel) getIView().getArguments().getSerializable(VideoDetailsFragment.LOAD_VIDEO_DATA);
            onItemClickVideoData = assetVideosDataModel;
            userProfileModel = (UserProfileModel) getIView().getArguments().getSerializable(VideoDetailsFragment.LOAD_USER_DATA);
            String title = getIView().getArguments().getString(TITLE_TEXT);
            if (title != null)
                getIView().setTitle(title, "#FFFFFF");

            setTheDetailsIntoScreen(assetVideosDataModel);
        }
//
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVideoDetailsEvent(AssetsDetailsResponseEvent responseEvent) {
        if(responseEvent.isError()){
            if (getIView()!=null){
                getIView().showRetryDialog();
            }
            return;
        }
        if(responseEvent.isSuccess()){
            AssetDetaillsDataModel assetDetaillsDataModel = responseEvent.getData();

                onItemClickVideoData.setPlayed_duration(assetDetaillsDataModel.getPlayed_duration());
               // setTheDetailsIntoScreen(assetDetaillsDataModel);

        }
    }

    private void setTheDetailsIntoScreen(AssetVideosDataModel assetDetaillsDataModel) {

        if(getIView()!=null){
            List<CategoryAssetsList> categoryAssetsListsAll = new ArrayList<>();
            List<YoutubeSnap> items = new ArrayList<>(1);
            if (!getIView().getActivity().isFinishing())
                getIView().getActivity().runOnUiThread(() -> {
                    ro = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.thumb_default)
                            .error(R.drawable.thumb_default)
                            .diskCacheStrategy(DiskCacheStrategy.DATA) // because file name is always same
                            .skipMemoryCache(false);
                    GlideApp.with(getIView().getScreenImage()).load(assetDetaillsDataModel.getThumbnailUrl()).apply(ro).into(getIView().getScreenImage());
                  //  getIView().getViewers().setText(String.valueOf(assetDetaillsDataModel.getNoOfViews()));
                  //  String videoDuration = timeConversion(assetDetaillsDataModel.getDuration());
                    getIView().getDuration().setText("1h : 57 m");
                    getIView().getDescription().setText(assetDetaillsDataModel.getDescription());
                    if(assetDetaillsDataModel.getFavourite() == 1){
                       getIView().getMyList().setTextColor(Color.RED);
                    }

                   List<CategoryAssetsList> data =  CategoryService.getServices().getAssetsUnderCategoryMenuAssociation(defaultMenuId);

                    filteredAssetData = checkFavouriteStatus(data);
                   for(CategoryAssetsList categoryAssetsList :filteredAssetData){
                       if(categoryAssetsList.getCategories().getId() == onItemClickVideoData.getCategoryId()){
                           CategoryAssetsList assetsList = new CategoryAssetsList();
                           assetsList.setAssetVideos(categoryAssetsList.getAssetVideos());
                           assetsList.setCategories(categoryAssetsList.getCategories());
                           categoryAssetsListsAll.add(assetsList);
                       }
                   }
                    getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getActivity()));
                    for (CategoryAssetsList categoryAssetsList : categoryAssetsListsAll) {
                        items.add(new YoutubeSnap(Gravity.START, categoryAssetsList));
                    }
                    youtubeSnapAdapter = new VerticalRecyclerAdapter(items, true, this);
                    if (getIView() != null) {
                        getIView().getRecyclerView().setAdapter(youtubeSnapAdapter);
                    }
                });
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
    public void onPlayClicked(){
        if(onItemClickVideoData.getAssetTypeId() == 3) {
            Intent intent = new Intent(getIView().getActivity(), MediaStreamingLandActivity.class);
            intent.putExtra(MediaStreamingLandActivity.DATA_MEDIA, onItemClickVideoData);

            intent.putExtra(MediaStreamingLandActivity.DATA_USER, userProfileModel);
            getIView().getActivity().startActivity(intent);
        }else{
            Intent intent = new Intent(getIView().getActivity(), MediaStreamingActivityExoPlayer.class);
            intent.putExtra(MediaStreamingActivityExoPlayer.DATA_MEDIA, onItemClickVideoData);
            getIView().getActivity().startActivity(intent);
        }
    }

    private static String timeConversion(int totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        return hours + " h " + minutes + " m " + seconds + " s";
    }

    private void getAssetDetails(AssetVideosDataModel assetVideosDataModel) {
//        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_ASSETS_DETAILS, assetVideosDataModel);
//        EventBus.getDefault().post(repoRequestEvent);
        new VideoPlayDurationPresenter().getPlayedDurationService(assetVideosDataModel, this);
    }
    public void destroy(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onItemSelect(AssetVideosDataModel modal) {
        onItemClickVideoData =  setVideoId(modal);
//        new VideoPlayDurationPresenter().getPlayedDurationService(modal, this);
        setTheDetailsIntoScreen(onItemClickVideoData);

   //    setTheDetailsIntoScreen(modal);
//        System.out.println("assetsDEtails:"+ new Gson().toJson(modal));


       // getAssetDetails(onItemClickVideoData);
    }
//    private void showFragment(Fragment fragment) {
//        getIView().getActivity().getFragmentManager().beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();
//    }
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
    public void onMoreItemsClicked(AssetVideosDataModel modal) {
        ((HolderActivity) getIView().getActivity()).showLogo("More Videos");
        CategoryListDataModel categoryModel = CategoryService.getServices().getById(modal.getCategoryId());
        Intent intent = getIView().getActivity().getIntent();
        intent.putExtra(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, categoryModel.getName());
        intent.putExtra(LOAD_LIST,(Serializable) modal);
        intent.putExtra(LOAD_DATA, categoryModel.getId());
        ((HolderActivity) getIView().getActivity()).itemSelection(MenuLeftActivity.POS_MORE_SCREEN);
    }

    @Override
    public void onMoreItemsNeeded() {

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

        getAssetDetails(onItemClickVideoData);

    }

    @Override
    public void showMoreProgress() {

    }
}
