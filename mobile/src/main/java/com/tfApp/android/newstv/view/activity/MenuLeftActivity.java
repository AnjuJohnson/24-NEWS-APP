package com.tfApp.android.newstv.view.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.activity.LeftMenuActivityPresenter;
import com.tfApp.android.newstv.presenter.activity.iview.LeftMenuActivityIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.ottapp.android.basemodule.models.FlowersViewModel;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;

/**
 * Created by George on 25.03.2017.
 */

public class MenuLeftActivity extends BaseActivity<LeftMenuActivityPresenter<LeftMenuActivityIView>, LeftMenuActivityIView> implements View.OnClickListener, LeftMenuActivityIView {

    public static final int POS_MORE_SCREEN = 25;
    public static final int POS_MORE_SCREE = 24;
    //private static final int POS_AR_SHOW = 0;
    private static final int POS_HOME = 24;
    private static final String KEY_POS = "LAST_POS";
    public static int POS_DEFAULT = POS_HOME;

    private FlowersViewModel flowersViewModel;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_menu);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //leftMenuActivityPresenter=initializePresenter();
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withRootViewScale(0.9f)
                .withDragDistancePx(getResources().getDimensionPixelSize(R.dimen._160sdp))
                .withRootViewElevation(5) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        TextView tv_name = slidingRootNav.getLayout().findViewById(R.id.tv_name);
        tv_name.setText(Html.fromHtml("<b>Hi," + "</b><br>" + StaticValues.emailId));
        findTextViewTitle(getTitle().toString());

        getPresenter().setAdapter();
        getPresenter().onCreate();
        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt(KEY_POS, -1);
            if (pos > -1) {
                if (pos == 25) {
                    getPresenter().onItemSelected(pos, null, null);
                } else
                    getPresenter().adapterSetSelected(pos);
            }
        }
        flowersViewModel = ViewModelProviders.of(this).get(FlowersViewModel.class);
        flowersViewModel.getAllMenus().observe(this, words -> {
            // Update the cached copy of the words in the adapter.

            getPresenter().updateLiveData(words);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_POS, getPresenter().getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().clear();
        }
    }

    private void findTextViewTitle(String text) {

        Window window = getWindow();
        View decor = window.getDecorView();

        ArrayList<View> views = new ArrayList<>();
        decor.findViewsWithText(views, text, View.FIND_VIEWS_WITH_TEXT);

        for (View view : views) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setCompoundDrawablePadding(this.getContext().getResources().getDimensionPixelSize(R.dimen._5sdp));
                textView.getViewTreeObserver()
                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                Drawable img = MenuLeftActivity.this.getContext().getResources().getDrawable(
                                        R.drawable.b_logo);
                                img.setBounds(0, 0, (img.getIntrinsicWidth() * textView.getMeasuredHeight() / img.getIntrinsicHeight()), textView.getMeasuredHeight());
                                textView.setCompoundDrawables(img, null, null, null);
                                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getPresenter() != null) {
            getPresenter().onBackPressed();
        }
    }

    public void itemSelection(int posMoreScreen) {
        getPresenter().onItemSelected(posMoreScreen, null, null);
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (slidingRootNav != null && slidingRootNav.isMenuOpened())
            slidingRootNav.closeMenu(false);
//        if (DataStore.getInstance().getDynamicPlayListModel() == null ||
//                DataStore.getInstance().getDynamicPlayListModel().getMenuArray() == null ||
//                DataStore.getInstance().getDynamicPlayListModel().getMenuArray().isEmpty()) {
//            startActivity(new Intent(MenuLeftActivity.this, SplashActivity.class));
//        if ( DataStore.getInstance().getDynamicMenuList() == null ){
////                DataStore.getInstance().getCategoryList().isEmpty()||
////                DataStore.getInstance().getCategoryAssosiationList().isEmpty()||
////                DataStore.getInstance().getCategoryAssosiationList().isEmpty()) {
//            startActivity(new Intent(MenuLeftActivity.this, SplashActivity.class));
     //       finish();
     //   }
    }

    @Override
    protected LeftMenuActivityPresenter<LeftMenuActivityIView> initializePresenter() {
        return new LeftMenuActivityPresenter<>(this);
    }

    @Override
    public SlidingRootNav slidingRootNav() {
        return slidingRootNav;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return findViewById(R.id.list);
    }

    @ColorInt
    public int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void showCheckInternetDialog() {

    }


    @Override
    public Activity activity() {
        return this;
    }

    @Override
    public FlowersViewModel getViewModel() {
        return flowersViewModel;
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public ImageView getRootView() {
        return findViewById(R.id.window_background);
    }

    @Override
    public ImageView getMenuRootView() {

        return findViewById(R.id.menu_background);
    }

    @Override
    public SlidingRootNav getSlidingRootNav() {
        return slidingRootNav;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(MenuLeftActivity.this, SearchActivityScreen.class);
            startActivity(intent);
        }
        return false;
    }
}