package com.tfApp.android.newstv.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.ComingSoonFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.ComingSoonFragmentIView;
import com.tfApp.android.newstv.view.activity.HolderActivity;

/**
 * Created by George PJ on 22-02-2018.
 */

public class ComingSoonFragment extends BaseFragment<ComingSoonFragmentPresenter<ComingSoonFragmentIView>, ComingSoonFragmentIView> implements ComingSoonFragmentIView {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comingsoon, container, false);
        ((HolderActivity) getActivity()).hideLogoWithGenres("Coming Soon");
        getPresenter().setupAdapter();

        return view;
    }


    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public String getScreenTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected ComingSoonFragmentPresenter<ComingSoonFragmentIView> initializePresenter() {
        return new ComingSoonFragmentPresenter<>(this);
    }


    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public RecyclerView getRecyclerView() {
        return view.findViewById(R.id.recyclerView);
    }

    @Override
    public TextView getTextView() {
        return view.findViewById(R.id.tv_no_data_found);
    }

    @Override
    public void setTitle(String title) {
        setTitle(title, getString(R.string.screen_title_color));
    }

    @Override
    public void showCheckInternetDialog() {

    }

    @Override
    public Activity getActivityObj() {
        return getActivity();
    }

    @Override
    public void showRetryDialog() {

    }


}
