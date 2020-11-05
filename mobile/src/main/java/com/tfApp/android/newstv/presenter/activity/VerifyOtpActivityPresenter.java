package com.tfApp.android.newstv.presenter.activity;


import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.models.ConfirmOtp;
import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.FcmInitResponse;
import com.ottapp.android.basemodule.repository.responses.GenerateConfirmOtpResponse;
import com.ottapp.android.basemodule.repository.responses.GenerateOtpResponse;
import com.ottapp.android.basemodule.services.UserFavouriteServices;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;
import com.tfApp.android.newstv.presenter.activity.iview.VerifyOtpActivityIView;
import com.tfApp.android.newstv.utils.ProgressDialog;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.utils.Validations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by George PJ on 23-04-2018.
 */

@SuppressWarnings("unused")
public class VerifyOtpActivityPresenter<I extends VerifyOtpActivityIView> extends BaseActivityPresenter<I> {

    private KProgressHUD progress_spinner;
    public VerifyOtpActivityPresenter(I iView) {
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


    //otp verification
    public void checkAndProceed() {
        progress_spinner = ProgressDialog.LoadingSpinner(getIView().getContext(),true);
        progress_spinner.show();
        Validations validations = new Validations();

        String otp = getIView().getOtp();
        if (validations.isValidString(otp) && otp.trim().length() == 5) {
            verifyOtpRequest(otp);
        } else {
            getIView().showInvalidOtpError();
            progress_spinner.dismiss();
        }
    }

    //request call to check the otp confirmation
    private void verifyOtpRequest(String otp) {
        ConfirmOtp confirmOtp = new ConfirmOtp(StaticValues.mobileNumber, StaticValues.emailId,otp);
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_TYPE_OTP_VERIFY, confirmOtp);
        EventBus.getDefault().post(repoRequestEvent);

    }
    private void checkFavouriteAssetRequest() {
        UserFavouriteServices.getServices().favouriteFetchRequest(PreferenceManager.getManager().getUserId(),3,0);
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onOtepGenerated(GenerateOtpResponse generateOtpResponse) {
        if(generateOtpResponse.isSuccess()){
            if(getIView()!=null){

            }
        }else{
            if(getIView()!=null){
                getIView().showRetryMessage();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onOtpConfirm(GenerateConfirmOtpResponse generateOtpResponse) {
        if(generateOtpResponse.isSuccess()){
            if(getIView()!=null){
                progress_spinner.dismiss();
               EventBus.getDefault().post(new FcmInitResponse(true));

                getIView().switchToNextScreen();
                checkFavouriteAssetRequest();

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
    }

    //for resend otp call
    public void resendOtp() {
        com.ottapp.android.basemodule.models.RequestOtp requestOtp = new com.ottapp.android.basemodule.models.RequestOtp(StaticValues.mobileNumber, StaticValues.emailId);
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_TYPE_USER_PROFILE, requestOtp);
        EventBus.getDefault().post(repoRequestEvent);
    }

    public void destroy(){
        if (EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().unregister(this);
        }
    }
}
