package com.tfApp.android.newstv.presenter.activity.iview;

import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;

/**
 * Created by George PJ on 23-04-2018.
 */

public interface LoginActivityIView extends BaseActivityIView {
    void switchToNextScreen();


    String getEmail();

    void showInvalidEmailError();

    String getPhoneNumber();

    void showInvalidPhoneError();

    void toggleProgress(boolean b);

    void showRetryMessage();
}
