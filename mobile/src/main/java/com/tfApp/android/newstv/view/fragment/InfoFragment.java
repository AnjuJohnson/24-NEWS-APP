package com.tfApp.android.newstv.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.InfoFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.InfoFragmentIView;
import com.tfApp.android.newstv.view.activity.HolderActivity;

public class InfoFragment extends BaseFragment<InfoFragmentPresenter<InfoFragmentIView>, InfoFragmentIView> implements InfoFragmentIView {
    public static final String TITLE_TEXT = "Settings";

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_fragment_layout, container, false);

        ((HolderActivity) getActivity()).hideLogoWithGenres("Settings");
        getPresenter().setScreen();
        //retrieve a reference to an instance of TelephonyManager
        return view;
    }
    @Override
    public String getScreenTag() {
        return null;
    }

    @Override
    protected InfoFragmentPresenter<InfoFragmentIView> initializePresenter() {
        return new InfoFragmentPresenter<>(this);
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public TextView getModel() {
        return view.findViewById(R.id.textViewModel);
    }

    @Override
    public TextView getVersion() {
        return view.findViewById(R.id.textViewVersion);
    }

    @Override
    public TextView getBuild() {
        return view.findViewById(R.id.textViewBuild);
    }

    @Override
    public TextView getSeriel() {
        return view.findViewById(R.id.textViewSeriel);
    }

    @Override
    public void setTitle(String title) {
        setTitle(title, getString(R.string.screen_title_color));
    }

    @Override
    public TextView getAccount() {
        return view.findViewById(R.id.uEmail);
    }

    @Override
    public TextView getAppVersion() {
        return view.findViewById(R.id.app_version);
    }

    }
