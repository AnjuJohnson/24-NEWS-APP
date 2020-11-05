package com.tfApp.android.newstv.presenter.activity;


import android.util.Log;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.models.RequestOtp;
import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.GenerateOtpResponse;
import com.tfApp.android.newstv.presenter.activity.iview.LoginActivityIView;
import com.tfApp.android.newstv.utils.ProgressDialog;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.utils.Validations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by George PJ on 23-04-2018.
 */

public class LoginActivityPresenter<I extends LoginActivityIView> extends BaseActivityPresenter<I> {

    private KProgressHUD progress_spinner;
    public LoginActivityPresenter(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }


    public void clean() {
        super.release();
        releaseResources();
    }

    private void releaseResources() {

    }

    public void activityVisible() {

    }

    public void activityGoneSleep() {

    }

    public void activityStopped() {

    }


    //for the validation checking
    public void checkAndProceed() {
        progress_spinner = ProgressDialog.LoadingSpinner(getIView().getContext(),true);
        progress_spinner.show();
        Validations validations = new Validations();
        String email = getIView().getEmail();
        if (validations.isEditTextContainEmail(email)) {
            String phoneNumber = getIView().getPhoneNumber();
            if (validations.validatePhone(phoneNumber)) {
                StaticValues.mobileNumber = phoneNumber;
                StaticValues.emailId = email;
                generateOtpRequest();
            } else {
                getIView().showInvalidPhoneError();
                progress_spinner.dismiss();
            }
        } else {
            getIView().showInvalidEmailError();
            progress_spinner.dismiss();
        }

    }


    //to call the otp request
    private void generateOtpRequest() {

        RequestOtp requestOtp = new RequestOtp(StaticValues.mobileNumber, StaticValues.emailId, StaticValues.device, StaticValues.model, StaticValues.version);
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_TYPE_USER_PROFILE, requestOtp);
        EventBus.getDefault().post(repoRequestEvent);

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onOtepGenerated(GenerateOtpResponse generateOtpResponse) {
    if(generateOtpResponse.isSuccess()){
        if(getIView()!=null){
            getIView().switchToNextScreen();
            progress_spinner.dismiss();
        }
    }else{
        if(getIView()!=null){
            progress_spinner.dismiss();
            getIView().showRetryMessage();

        }
    }
    }

    private void showMessage(String s) {

        if (getIView() != null)
            Toast.makeText(getIView().getContext(), s, Toast.LENGTH_SHORT).show();
        Log.e("Error", s + "");
    }


    public void destroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
