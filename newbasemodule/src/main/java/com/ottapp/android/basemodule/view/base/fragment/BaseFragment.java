package com.ottapp.android.basemodule.view.base.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ottapp.android.basemodule.app.BaseApplication;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

/**
 * Created by George PJ on 23-02-2018.
 */

public abstract class BaseFragment<P extends BaseFragmentPresenter<I>, I extends BaseFragmentIView> extends Fragment {
    private Bundle bundle;
    private P presenter;

    public abstract String getScreenTag();

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getPresenter() != null) {
            getPresenter().onPause();
        }
    }

    public P getPresenter() {
        if (presenter == null)
            presenter = initializePresenter();
        return presenter;
    }

    protected abstract P initializePresenter();

    public void setTitle(int title, String colorHex) {
        setTitle(getString(title), colorHex);
    }

    public void setTitle(String title, String colorHex) {
        if (colorHex == null || colorHex.isEmpty()) {
            if (getActivity() != null)
                getActivity().setTitle(title);
        } else {
            if (getActivity() != null)
                getActivity().setTitle(Html.fromHtml("<font color=\"" + colorHex + "\">" + title + "</font>"));
        }
    }
}
