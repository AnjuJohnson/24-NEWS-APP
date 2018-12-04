package com.tfApp.android.newstv.presenter.activity.iview;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.ottapp.android.basemodule.models.FlowersViewModel;
import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

public interface LeftMenuActivityIView extends BaseActivityIView {

    SlidingRootNav slidingRootNav();

    RecyclerView getRecyclerView();

    int color(@ColorRes int res);

    void showCheckInternetDialog();

    Activity activity();

    FlowersViewModel getViewModel();


    ImageView getRootView();

    ImageView getMenuRootView();

    SlidingRootNav getSlidingRootNav();

    void finish();
}
