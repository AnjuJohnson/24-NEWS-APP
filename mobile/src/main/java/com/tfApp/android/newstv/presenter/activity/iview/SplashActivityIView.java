package com.tfApp.android.newstv.presenter.activity.iview;


import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;

/**
 * Created by George PJ on 21-02-2018.
 */

public interface SplashActivityIView extends BaseActivityIView {

    void switchToNextScreen(int toScreen);

    void forceUpdateDialog();

    void showCheckInternetDialog();


    void showRetryDialog();
}
