package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface HomeFragmentIView extends BaseFragmentIView {
    RecyclerView getRecyclerView();

    Bundle getArgument();

    Activity getActivity();

    void setTitle(String title, String color);

    void showCheckInternetDialog();

    void showRetryDialog();

    TextView getTextView();

    void switchToYoutubeActivity(AssetVideosDataModel onItemClickVideoData, UserProfileModel data);
}
