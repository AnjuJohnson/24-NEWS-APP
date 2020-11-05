package com.tfApp.android.newstv.presenter.fragment;

import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.PackageModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.PackageAdapter;
import com.tfApp.android.newstv.adaptors.PackageItemSelectionListener;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.presenter.fragment.iview.PackageFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class PackageFragmentPresenter<I extends PackageFragmentIView> extends BaseFragmentPresenter<I> implements PackageItemSelectionListener, PlayedDurationListener {

    private List<AssetVideosDataModel> dataModels = new ArrayList<>();
    private AssetVideosDataModel onItemClickVideoData;
    private PackageAdapter youtubeItemAdapter;
    private int maxLimit = 10;
    private int dataId;
    private List<PackageModel> packgeList = new ArrayList<>();

    public PackageFragmentPresenter(I iView) {
        super(iView);

    }

    //adapter class to set the data into the recyclerview
    @SuppressWarnings("unchecked")
    public void setupAdapter() {
        PackageModel packageModel = new PackageModel();
        packageModel.setPackageName("Gold Package");
        packageModel.setPrice(499);
        packageModel.setColorCode(R.drawable.package_card1);
        packgeList.add(packageModel);

        packageModel = new PackageModel();
        packageModel.setPackageName("Silver Package");
        packageModel.setPrice(299);
        packageModel.setColorCode(R.drawable.package_card2);
        packgeList.add(packageModel);

        packageModel = new PackageModel();
        packageModel.setPackageName("Platinum Package");
        packageModel.setPrice(199);
        packageModel.setColorCode(R.drawable.package_card3);
        packgeList.add(packageModel);

        youtubeItemAdapter = new PackageAdapter(packgeList, this);
        getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getActivityObj()));
        getIView().getRecyclerView().setAdapter(youtubeItemAdapter);
        getIView().getTextView().setVisibility(View.GONE);


    }


    @Override
    public void onItemSelect(PackageModel modal) {
       getIView().showAlerts(modal.getPackageName());
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
    public void onMoreItemsClicked(PackageModel playListModel) {
        if (getIView() != null && getIView().getActivityObj() != null) {

        }
    }

    @Override
    public void onMoreItemsNeeded() {

    }

    @Override
    public void onFavouriteActionSelected(PackageModel youtubeVideoModel, int position) {//converting the asset data into favourite object to store in db


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
