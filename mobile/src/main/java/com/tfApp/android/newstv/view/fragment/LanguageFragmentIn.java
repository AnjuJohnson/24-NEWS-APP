package com.tfApp.android.newstv.view.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ottapp.android.basemodule.models.LanguageViewModel;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.LanguagePresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.LanguageFragmentIView;
import com.tfApp.android.newstv.view.activity.HolderActivity;

public class LanguageFragmentIn extends BaseFragment<LanguagePresenter<LanguageFragmentIView>, LanguageFragmentIView> implements LanguageFragmentIView {
   private  View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_language_in, container, false);
        getPresenter().setApiCallLanguage();
        ((HolderActivity) getActivity()).hideLogoWithGenres("Languages");

        eventListener();

        LanguageViewModel languageViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(LanguageViewModel.class);
        languageViewModel.getAllLanguages().observe((FragmentActivity) getActivity(), languages -> {
            //Update the cached copy of the words in the adapter.
            getPresenter().updateLanguages(languages);
        });
        return view;
    }

    private void eventListener() {
        getButton().setOnClickListener(v->getPresenter().onButtonClick());

        getSkip().setOnClickListener(v->getPresenter().buttonSkip());
    }

    @Override
    public String getScreenTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected LanguagePresenter<LanguageFragmentIView> initializePresenter() {
        return new LanguagePresenter<>(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }



    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public TextView getTextView() {
        return null;
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
        if (!getActivity().isFinishing())
            getActivity().runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Language");
                builder.setMessage("Please Select Atleast One Language");
                builder.setPositiveButton("Ok", (dialog, which) -> {
                    builder.create().dismiss();
                });

                builder.create().show();
            });
    }

    @Override
    public Bundle getArgument() {
        return getArguments();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return view.findViewById(R.id.recyclerView);
    }

    @Override
    public Button getButton() {
        return view.findViewById(R.id.btn_proceed);
    }

    @Override
    public Button getSkip() {
        return view.findViewById(R.id.btn_skip);
    }

    @Override
    public void showNextScreen(String actionLanguageChoice) {
        System.out.println("languageChek:"+actionLanguageChoice);
        if (actionLanguageChoice == null) {
            ((HolderActivity) getActivity()).itemSelection(HolderActivity.POS_MORE_SCREE);
        }else {
            if (actionLanguageChoice.equals("SelectLanguage".trim())) {
                startActivity(new Intent(getActivity(), HolderActivity.class));
                getActivity().finish();
            } else {
                ((HolderActivity) getActivity()).itemSelection(HolderActivity.POS_MORE_SCREE);
            }
        }
    }

}
