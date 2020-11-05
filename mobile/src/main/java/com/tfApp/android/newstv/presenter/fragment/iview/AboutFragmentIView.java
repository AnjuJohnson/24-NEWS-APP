package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface AboutFragmentIView extends BaseFragmentIView {



    TextView getTextView();

    void setTitle(String title);

    void showCheckInternetDialog();

    Activity getActivityObj();

    void showRetryDialog();

    Bundle getArgument();




}
