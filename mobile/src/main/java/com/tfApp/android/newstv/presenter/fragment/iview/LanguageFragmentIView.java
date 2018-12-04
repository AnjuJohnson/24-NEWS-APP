package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface LanguageFragmentIView extends BaseFragmentIView {



    TextView getTextView();

    void setTitle(String title);

    void showCheckInternetDialog();

    Activity getActivityObj();

    void showRetryDialog();

    Bundle getArgument();

    RecyclerView getRecyclerView();

    Button getButton();

    Button getSkip();

    void showNextScreen(String actionLanguageChoice);




}
