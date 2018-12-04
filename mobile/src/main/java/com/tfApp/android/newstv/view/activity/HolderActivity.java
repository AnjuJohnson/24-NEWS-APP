package com.tfApp.android.newstv.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.activity.HolderActivityPresenterImpl;
import com.tfApp.android.newstv.presenter.activity.iview.HolderActivityIView;
import com.tfApp.android.newstv.view.fragment.VideoDetailsFragment;
import com.ottapp.android.basemodule.utils.ScreenSwitchEvent;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

public class HolderActivity extends BaseActivity<HolderActivityPresenterImpl<HolderActivityIView>,HolderActivityIView> implements View.OnClickListener, HolderActivityIView {
    private static final int SEARCH_REQUEST = 1234;
    public static final int POS_MORE_SCREE = 24;
    public static final int POS_MORE_SEARCH = 27;
//    private ImageView ic_action_home, ic_action_search;
    private TextView ic_genre,ic_title;
    private HolderActivityPresenterImpl activityPresenter;
   // public ImageView logoImage;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

//        ic_action_search = findViewById(R.id.ic_action_search);
//        ic_action_home = findViewById(R.id.ic_action_home);
        ic_genre = findViewById(R.id.gendre);
        ic_title = findViewById(R.id.title);
//        ic_action_search.setOnClickListener(this);
//        ic_action_home.setOnClickListener(this);
//
//        logoImage = findViewById(R.id.bitryt_logo);
        if (activityPresenter == null)
            activityPresenter = new HolderActivityPresenterImpl(this);
        eventListener();
    }

    private void eventListener() {
        ic_genre.setOnClickListener(v->getPresenter().showGenreFragment());
    }
    public void showLogo(String title){
        ic_title.setVisibility(View.VISIBLE);
        ic_title.setText(title);
        ic_genre.setVisibility(View.VISIBLE);
    }

    public void setGenre(String name){
       ic_genre.setText(name);
    }
//
    public void hideLogoWithGenres(String title){

        ic_title.setVisibility(View.VISIBLE);
        ic_title.setText(title);
        ic_genre.setVisibility(View.GONE);
    }

    public String getTitleTop(){
        return ic_title.getText().toString();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        activityPresenter.setupMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityPresenter.registerEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityPresenter.unregisterEvents();
    }
    public void itemSelection(int posMoreScreen) {
        getPresenter().onItemSelected(posMoreScreen);
    }

    public void itemSelectionMore(int posMoreScreen) {
        getPresenter().onOptionSelected(null,posMoreScreen);
    }
    @Override
    protected HolderActivityPresenterImpl<HolderActivityIView> initializePresenter() {
        return new HolderActivityPresenterImpl<>(this);
    }

    @Override
    public void onClick(View v) {
//        if (v == ic_action_home) {
//            EventBus.getDefault().post(new ScreenSwitchEvent(7));
//        } else if (v == ic_action_search) {
//            startActivityForResult(new Intent(this, SearchActivityScreen.class), SEARCH_REQUEST);
//        }
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy();
        super.onDestroy();
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public View getBottomMenuRL() {
        return findViewById(R.id.rl_menu_bottom);
    }

    @Override
    public ViewPager getViewPager() {
        return null;
    }

    @Override
    public RecyclerView getBottomMenu() {
        RecyclerView recyclerViewMenuBottom = findViewById(R.id.bottom_navigation);
        recyclerViewMenuBottom.setHasFixedSize(true);
        recyclerViewMenuBottom.setLayoutManager(new GridLayoutManager(this, 5));
        return recyclerViewMenuBottom;
    }

    @Override
    public View getTopBar() {
        return findViewById(R.id.top_bar);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        activityPresenter.onBackPressed();
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
//            getSupportFragmentManager().popBackStack();
//        } else {
//           askExitDialog();
//        }
    //    getPresenter().onBackPressed();
//        if (activityPresenter == null || activityPresenter.onBackPressed()) {
//            askExitDialog();
//        }
    }

    private AlertDialog dialog;

    public void askExitDialog() {
        if (dialog != null)
            dialog.cancel();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            super.onBackPressed();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        dialog = builder.create();
        dialog.show();
    }

}
