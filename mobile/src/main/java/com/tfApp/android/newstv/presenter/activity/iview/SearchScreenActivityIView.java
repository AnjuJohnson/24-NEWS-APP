package com.tfApp.android.newstv.presenter.activity.iview;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.SearchView;

import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;

public interface SearchScreenActivityIView extends BaseActivityIView {

    SearchView getSearchView();
    ImageView getVoiceSearchView();
    ImageView getRootView();
    Activity getActivity();

}
