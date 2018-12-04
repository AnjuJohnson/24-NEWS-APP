package com.tfApp.android.newstv.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetsDetailsResponseEvent;
import com.ottapp.android.basemodule.models.BannerModel;
import com.ottapp.android.basemodule.repository.responses.BannerModelResponse;
import com.ottapp.android.basemodule.services.UserProfileService;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.HomeFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.HomeFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.HolderActivity;
import com.tfApp.android.newstv.view.activity.MediaStreamingActivityExoPlayer;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.AssetsViewDataModel;
import com.ottapp.android.basemodule.models.CategoryMenuAssociationViewModel;
import com.ottapp.android.basemodule.models.CategoryViewModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.services.AssetMenuService;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by George PJ on 22-02-2018.
 */

public class HomeFragment extends BaseFragment<HomeFragmentPresenter<HomeFragmentIView>, HomeFragmentIView> implements HomeFragmentIView {


    private View view;
    private BannerSlider bannerSlider;
    private List<String> banner;
    private List<Banner> banners;
    private Timer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home_header, container, false);
        ((HolderActivity) getActivity()).showLogo("Home");
        bannerSlider = view.findViewById(R.id.banner_slider1);
        banners = new ArrayList<>();
        UserProfileService.getInstance().getAllUpdatedBannersFromServer(true);
        banner = new ArrayList<>();
//        banner.add("https://s3.ap-south-1.amazonaws.com/24news.bitryt.com/Banners/banner1.jpg");
//        banner.add("https://s3.ap-south-1.amazonaws.com/24news.bitryt.com/Banners/banner2.jpg");
//        banner.add("https://s3.ap-south-1.amazonaws.com/24news.bitryt.com/Banners/banner3.jpg");
//        banner.add("https://s3.ap-south-1.amazonaws.com/24news.bitryt.com/Banners/banner4.jpeg");
//        banner.add("https://s3.ap-south-1.amazonaws.com/24news.bitryt.com/Banners/banner5.jpg");
//        for (int i = 0; i<banner.size();i++){
//           // if(banners.size()<3)
//            banners.add(new RemoteBanner(banner.get(i)));
//        }
//        bannerSlider.setBanners(banners);
//        startAutoScrolling();
        getPresenter().setupAdapter();

        //for live data
        CategoryViewModel categoryViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(CategoryViewModel.class);
        CategoryMenuAssociationViewModel categoryMenuAssociationViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(CategoryMenuAssociationViewModel.class);
        categoryViewModel.getAllCategory().observe((FragmentActivity) getActivity(), category -> {
            //Update the cached copy of the words in the adapter.
            getPresenter().updateLiveData(category);
        });

        categoryMenuAssociationViewModel.getAllCategory().observe((FragmentActivity) getActivity(), categoryMenuAssociation -> {
            //Update the cached copy of the words in the adapter.
            getPresenter().updateLiveDataAssosiation(categoryMenuAssociation);
        });

        AssetsViewDataModel viewDataModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(AssetsViewDataModel.class);
        viewDataModel.getAllAssets().observe(((FragmentActivity) getActivity()), data -> {
            //Update the cached copy of the words in the adapter.
            getPresenter().updateLiveDataAssets(data);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void startAutoScrolling() {

        stopAutoScrolling();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(getActivity()!=null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int position = (bannerSlider.getCurrentSlidePosition()+ 1) % banner.size();
                            bannerSlider.setCurrentSlide(position);
                        }
                    });

            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, 3000, 3000);
    }

    public void stopAutoScrolling() {
        Log.i("TAG", "stop auto scroll of viewpager");
        if (timer != null)
            timer.cancel();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVideoDetailsEvent(BannerModelResponse responseEvent) {

        if(responseEvent.isError()){

        }
        if(responseEvent.isSuccess()){
        List<BannerModel>bannersList =responseEvent.getDatas();
            for (int i = 0; i<bannersList.size();i++){
                // if(banners.size()<3)

                banner.add(bannersList.get(i).getBannerUrl());
            }

            for (int i = 0; i<banner.size();i++){
                // if(banners.size()<3)
                banners.add(new RemoteBanner(banner.get(i)));
            }
            bannerSlider.setBanners(banners);
            startAutoScrolling();
//
        }else{

        }
    }

    @Override
    public Bundle getArgument() {
        return getArguments();
    }

    @Override
    public void showCheckInternetDialog() {
        Log.e("Error", "error in loading");
    }

    @Override
    public void showRetryDialog() {
        if (!getActivity().isFinishing())
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), " Please Try Again", Toast.LENGTH_LONG).show());
    }

    @Override
    public TextView getTextView() {
        return view.findViewById(R.id.tv_no_data_found);
    }

    @Override
    public void switchToYoutubeActivity(AssetVideosDataModel onItemClickVideoData, UserProfileModel data) {

            if (onItemClickVideoData.getAssetTypeId() == StaticValues.youtube_activity_id) {
                Intent intent = new Intent(getActivity(), MediaStreamingLandActivity.class);
                intent.putExtra(MediaStreamingLandActivity.DATA_MEDIA, onItemClickVideoData);

                intent.putExtra(MediaStreamingLandActivity.DATA_USER, data);
                getActivity().startActivity(intent);
            }else {
                Intent intent = new Intent(getView().getContext(), MediaStreamingActivityExoPlayer.class);
                intent.putExtra(MediaStreamingActivityExoPlayer.DATA_MEDIA, onItemClickVideoData);
                getView().getContext().startActivity(intent);
            }
    }


    @Override
    public String getScreenTag() {
        return "HomeFragment";
    }

    @Override
    protected HomeFragmentPresenter<HomeFragmentIView> initializePresenter() {
        return new HomeFragmentPresenter<>(this);
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public RecyclerView getRecyclerView() {
        return view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().destroy();
    }
}
