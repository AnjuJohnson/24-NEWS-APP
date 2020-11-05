package com.tfApp.android.newstv.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.VideoDetailsFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.VideoDetailFragmentIView;

/**
 * Created by George PJ on 22-02-2018.
 */

public class VideoDetailsFragment extends BaseFragment<VideoDetailsFragmentPresenter<VideoDetailFragmentIView>, VideoDetailFragmentIView> implements VideoDetailFragmentIView {


    private View view;
    public static final String LOAD_VIDEO_DATA = "video_data";
    public static final String LOAD_USER_DATA = "user_data";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_details_layout, container, false);
//        ((HolderActivity) getActivity()).hideLogoWithGenres();
        getPresenter().setupAdapter();
        eventListener();

        return view;
    }

    private void eventListener() {
        ImageView playIcon = view.findViewById(R.id.image_play);
        RelativeLayout relativeLayout = view.findViewById(R.id.detail_layout);
        relativeLayout.setOnClickListener(v->getPresenter().onPlayClicked());
    }

    @Override
    public void showCheckInternetDialog() {
        Log.e("Error", "error in loading");
    }

    @Override
    public void showRetryDialog() {

    }

    @Override
    public TextView getViewers() {
        return view.findViewById(R.id.text_viewers);
    }

    @Override
    public TextView getDuration() {
        return view.findViewById(R.id.text_duration);
    }

    @Override
    public TextView getDescription() {
        return view.findViewById(R.id.text_description);
    }

    @Override
    public ImageView getPlayIcon() {
        return view.findViewById(R.id.image_play);
    }

    @Override
    public ImageView getScreenImage() {
        return view.findViewById(R.id.youtube_view);
    }

    @Override
    public TextView getMyList() {
        return view.findViewById(R.id.myList_test);
    }


    @Override
    public String getScreenTag() {
        return "Details Fragment";
    }

    @Override
    protected VideoDetailsFragmentPresenter<VideoDetailFragmentIView> initializePresenter() {
        return new VideoDetailsFragmentPresenter<>(this);
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public RecyclerView getRecyclerView() {
        return view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().destroy();

    }
}
