package com.ottapp.android.basemodule.view.base.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.ottapp.android.basemodule.R;
import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;

public abstract class BaseActivity<P extends BaseActivityPresenter<I>, I extends BaseActivityIView> extends AppCompatActivity {

    @Override
    public void finish() {
        overridePendingTransitionExit();
        super.finish();
    }

    private P presenter;

    @Override
    public void startActivity(Intent intent) {
        overridePendingTransitionEnter();
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        overridePendingTransitionEnter();
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getPresenter() != null) {
            getPresenter().onPause();
        }
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public P getPresenter() {
        if (presenter == null)
            presenter = initializePresenter();
        return presenter;
    }

    protected abstract P initializePresenter();
}