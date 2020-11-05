package com.tfApp.android.newstv.view.fragment;


import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.SearchFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.SearchFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.MediaStreamingActivityExoPlayer;

/**
 * Created by George PJ on 22-02-2018.
 */

public class SearchVideoGridFragment extends BaseFragment<SearchFragmentPresenter<SearchFragmentIView>, SearchFragmentIView> implements SearchFragmentIView {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_youtube_common, container, false);
        getPresenter().setupAdapter();
        return view;
    }


    @Override
    public String getScreenTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected SearchFragmentPresenter<SearchFragmentIView> initializePresenter() {
        return new SearchFragmentPresenter<>(this);
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
    public ProgressBar getProgressView() {
        return view.findViewById(R.id.pb_loading);
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
