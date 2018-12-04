package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface FavouriteFragmentIView extends BaseFragmentIView {

    RecyclerView getRecyclerView();

    TextView getTextView();

    void setTitle(String title);

    Bundle getArguments();

    Activity getActivity();

    SwipeRefreshLayout getSwipeView();

    void switchToYoutubeActivity(AssetVideosDataModel onItemClickVideoData, UserProfileModel data);
}
