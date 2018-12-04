package com.tfApp.android.newstv.presenter.activity.iview;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;

public interface HolderActivityIView extends BaseActivityIView {
    View getBottomMenuRL();

    ViewPager getViewPager();

    RecyclerView getBottomMenu();

    FragmentManager getSupportFragmentManager();

    View getTopBar();

    Activity getActivity();
}
