package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface SearchFragmentIView extends BaseFragmentIView {

    RecyclerView getRecyclerView();

    TextView getTextView();

    void setTitle(String title);

    Bundle getArguments();

    Activity getActivity();


    ProgressBar getProgressView();

    void switchToYoutubeActivity(AssetVideosDataModel onItemClickVideoData, UserProfileModel data);
}
