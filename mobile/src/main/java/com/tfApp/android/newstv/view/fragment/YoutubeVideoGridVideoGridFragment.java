package com.tfApp.android.newstv.view.fragment;


import android.app.Activity;
import android.bitryt.com.youtubedataapi.activity.MediaStreamingLandActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.YoutubeVideoGridFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.YoutubeVideoGridFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.MediaStreamingActivityExoPlayer;

/**
 * Created by George PJ on 22-02-2018.
 */

public class YoutubeVideoGridVideoGridFragment extends BaseFragment<YoutubeVideoGridFragmentPresenter<YoutubeVideoGridFragmentIView>, YoutubeVideoGridFragmentIView> implements YoutubeVideoGridFragmentIView {
    public static final String LOAD_DATA = "data_id";
    public static final String LOAD_LIST = "data_list";
    public static final String KEY_LOADER_TYPE = "loader_type_key";
    public static final String LOADER_TYPE_MENU = "loader_type_menu";
    public static final String LOADER_TYPE_CATEGORY = "loader_type_category";
    public static final String TITLE_TEXT = "title_text";
    private View view;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_youtube_common, container, false);
        //((HolderActivity) getActivity()).showLogo();
        getPresenter().setupAdapter();
        eventListener();

        return view;
    }

    private void eventListener() {
        getGenre().setOnClickListener(v->getPresenter().showGenreFragment());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    public String getScreenTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected YoutubeVideoGridFragmentPresenter<YoutubeVideoGridFragmentIView> initializePresenter() {
        return new YoutubeVideoGridFragmentPresenter<>(this);
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
    public TextView getGenre() {
        return view.findViewById(R.id.gendre);
    }

    @Override
    public void setTitle(String title) {
        setTitle(title, getString(R.string.screen_title_color));
    }

    @Override
    public void showCheckInternetDialog() {

    }

    @Override
    public Activity getActivityObj() {
        return getActivity();
    }

    @Override
    public void showRetryDialog() {
        if (!getActivity().isFinishing())
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), " Please Try Again", Toast.LENGTH_LONG).show());
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
