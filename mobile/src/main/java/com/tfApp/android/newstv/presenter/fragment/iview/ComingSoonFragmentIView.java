package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface ComingSoonFragmentIView extends BaseFragmentIView {

    RecyclerView getRecyclerView();

    TextView getTextView();

    void setTitle(String title);

    Bundle getArguments();

    void showCheckInternetDialog();
    Activity getActivityObj();

    void showRetryDialog();
}
