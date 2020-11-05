package com.tfApp.android.newstv.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.ToolbarFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.ToolbarFragmentIView;
import com.tfApp.android.newstv.view.activity.HolderActivity;

/**
 * Created by George PJ on 22-02-2018.
 */

public class ToolbarFragment extends BaseFragment<ToolbarFragmentPresenter<ToolbarFragmentIView>, ToolbarFragmentIView> implements ToolbarFragmentIView {


    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_holder, container, false);
        ((HolderActivity) getActivity()).hideLogoWithGenres("Coming Soon");
        getPresenter().setupMenu();
        return view;
    }




    @Override
    public String getScreenTag() {
        return getString(R.string.app_name);
    }

    @Override
    protected ToolbarFragmentPresenter<ToolbarFragmentIView> initializePresenter() {
        return new ToolbarFragmentPresenter<>(this);
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View getBottomMenuRL() {
        return null;
    }

    @Override
    public ViewPager getViewPager() {
        return null;
    }

    @Override
    public RecyclerView getBottomMenu() {
        return view.findViewById(R.id.bottom_navigation);
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return null;
    }

    @Override
    public View getTopBar() {
        return null;
    }
}
