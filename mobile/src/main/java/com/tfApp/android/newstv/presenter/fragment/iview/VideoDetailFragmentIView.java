package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface VideoDetailFragmentIView extends BaseFragmentIView {
    RecyclerView getRecyclerView();

    Bundle getArguments();

    Activity getActivity();

    void setTitle(String title, String color);

    void showCheckInternetDialog();

    void showRetryDialog();

    TextView getViewers();
    TextView getDuration();
    TextView getDescription();
    ImageView getPlayIcon();
    ImageView getScreenImage();
    TextView getMyList();
}
