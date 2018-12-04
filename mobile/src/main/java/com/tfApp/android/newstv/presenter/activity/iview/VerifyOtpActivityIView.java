package com.tfApp.android.newstv.presenter.activity.iview;

import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;

/**
 * Created by George PJ on 23-04-2018.
 */

public interface VerifyOtpActivityIView extends BaseActivityIView {
    void switchToNextScreen();

    void finish();

    String getOtp();

    void showInvalidOtpError();


    void toggleProgress(boolean b);

    void showRetryMessage();
}
