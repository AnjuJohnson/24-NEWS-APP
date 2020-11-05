package com.tfApp.android.newstv.view.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.activity.SplashscreenActivityPresenter;
import com.tfApp.android.newstv.presenter.activity.iview.SplashActivityIView;
import io.fabric.sdk.android.Fabric;


/**
 * Created by George PJ on 21-02-2018.
 */

public class SplashActivity extends BaseActivity<SplashscreenActivityPresenter<SplashActivityIView>, SplashActivityIView> implements SplashActivityIView {

    private AlertDialog.Builder builder;

    // private SplashActivityPresenter getPresenter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
     //   Fabric.with(this, new Crashlytics());
        int notifyId = getIntent().getIntExtra("N_ID", 0);
        if (notifyId > 0) {
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nMgr != null) {
                nMgr.cancel(notifyId);
            }
        }
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //getPresenter()=initializePresenter();
        getPresenter().startBackgroundJob();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().destroy();
    }

    @Override
    protected SplashscreenActivityPresenter<SplashActivityIView> initializePresenter() {
        return new SplashscreenActivityPresenter<>(this);
    }

    @Override
    public void switchToNextScreen(int toScreen) {
        if (toScreen == SplashscreenActivityPresenter.TO_HOME) {
            startActivity(new Intent(SplashActivity.this, HolderActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void forceUpdateDialog() {
        if (!isFinishing())
            runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("24News Update");
                builder.setMessage("Please update application to enjoy new experience");
                builder.setPositiveButton("Update", (dialog, which) -> {
                    navigateToPlayStore();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    finish();
                });
                builder.create().show();
            });
    }

    @Override
    public void showCheckInternetDialog() {
        Log.e("Error", "error in loading");
    }

    @Override
    public void showRetryDialog() {
        if (!isFinishing())
            runOnUiThread(() -> {
                builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("24 News ");
                builder.setMessage("Something went wrong!please retry");
                builder.setPositiveButton("Retry", (dialog, which) -> {
                    getPresenter().startBackgroundJob();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    finish();
                });
                builder.create().show();
            });

    }


    //to navigate into play store
    private void navigateToPlayStore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
