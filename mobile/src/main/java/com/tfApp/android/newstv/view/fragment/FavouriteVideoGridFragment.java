package com.tfApp.android.newstv.view.fragment;


import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.FavouriteFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.FavouriteFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.HolderActivity;
import com.tfApp.android.newstv.view.activity.MediaStreamingActivityExoPlayer;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;

/**
 * Created by George PJ on 22-02-2018.
 */

public class FavouriteVideoGridFragment extends BaseFragment<FavouriteFragmentPresenter<FavouriteFragmentIView>, FavouriteFragmentIView> implements FavouriteFragmentIView {

    private View view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ((HolderActivity) getActivity()).hideLogoWithGenres("Favourites");
        getPresenter().getFavoriteVideos();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
        return view;
    }


    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public String getScreenTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected FavouriteFragmentPresenter<FavouriteFragmentIView> initializePresenter() {
        return new FavouriteFragmentPresenter<>(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public RecyclerView getRecyclerView() {
        return view.findViewById(R.id.recyclerView);
    }

    @Override
    public TextView getTextView() {
        return view.findViewById(R.id.tv_no_data_found);
    }

    @Override
    public void setTitle(String title) {
        setTitle(title, getString(R.string.screen_title_color));
    }

    @Override
    public SwipeRefreshLayout getSwipeView() {
//        SwipeRefreshLayout swipeView = view.findViewById(R.id.lv_swipe);
//        swipeView.setOnRefreshListener(getPresenter());
//        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
//                Color.RED, Color.CYAN);
//        swipeView.setDistanceToTriggerSync(20);// in dips
//        swipeView.setSize(SwipeRefreshLayout.DEFAULT);
//        swipeView.setRefreshing(true);
        return null;
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


}
