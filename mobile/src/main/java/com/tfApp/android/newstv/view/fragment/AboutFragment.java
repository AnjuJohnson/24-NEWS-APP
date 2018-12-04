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

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.AboutFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.YoutubeVideoGridFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.AboutFragmentIView;
import com.tfApp.android.newstv.presenter.fragment.iview.YoutubeVideoGridFragmentIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.MediaStreamingActivityExoPlayer;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;

/**
 * Created by George PJ on 22-02-2018.
 */

public class AboutFragment extends BaseFragment<AboutFragmentPresenter<AboutFragmentIView>, AboutFragmentIView> implements AboutFragmentIView {
    public static final String PAGE_TITLE = "TITLE";
    public static final String PAGE_ACTION = "ACTION";
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);

        getPresenter().setupAdapter();

        return view;
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
    protected AboutFragmentPresenter<AboutFragmentIView> initializePresenter() {
        return new AboutFragmentPresenter<>(this);
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public TextView getTextView() {
        return view.findViewById(R.id.about_text);
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
    public Bundle getArgument() {
        return getArguments();
    }

}
