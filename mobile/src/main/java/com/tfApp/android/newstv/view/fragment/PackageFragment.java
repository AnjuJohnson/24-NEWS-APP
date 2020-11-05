package com.tfApp.android.newstv.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.PackageFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.PackageFragmentIView;
import com.tfApp.android.newstv.view.activity.HolderActivity;

/**
 * Created by George PJ on 22-02-2018.
 */

public class PackageFragment extends BaseFragment<PackageFragmentPresenter<PackageFragmentIView>, PackageFragmentIView> implements PackageFragmentIView {

    private View view;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comingsoon, container, false);
        ((HolderActivity) getActivity()).hideLogoWithGenres("Packages");
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
    protected PackageFragmentPresenter<PackageFragmentIView> initializePresenter() {
        return new PackageFragmentPresenter<>(this);
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

    @Override
    public void showAlerts(String packageName) {
        if (!getActivity().isFinishing())
            getActivity().runOnUiThread(() -> {
                 builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Bitryt ");
                builder.setMessage("To confirm that you wish to be subscribed to "+ packageName+","+ " please click the below button");
                builder.setPositiveButton("SubScribe", (dialog, which) -> {
                    Toast.makeText(getContext(),"Congratulations! You are now subscribed to  "+packageName,Toast.LENGTH_SHORT).show();
                    if (packageName != null) {
                        ((HolderActivity) getActivity()).itemSelection(HolderActivity.POS_MORE_SCREE);
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                   builder.create().dismiss();
                });
                builder.create().show();
            });

    }
}
