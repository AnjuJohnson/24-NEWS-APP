package com.tfApp.android.newstv.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.events.OTPReceivedEvent;
import com.ottapp.android.basemodule.receiver.OTPBroadcastReceiver;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.activity.LoginActivityPresenter;
import com.tfApp.android.newstv.presenter.activity.iview.LoginActivityIView;
import com.tfApp.android.newstv.utils.StaticValues;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by George PJ on 23-04-2018.
 */

public class LoginActivity extends BaseActivity<LoginActivityPresenter<LoginActivityIView>, LoginActivityIView> implements LoginActivityIView {

    private EditText edit_email, edt_phone;
    private LoginActivityPresenter loginActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.edit_email);
        if (StaticValues.emailId != null) {
            edit_email.setText(StaticValues.emailId);
        }

        edt_phone = findViewById(R.id.edit_phone);
        if (StaticValues.mobileNumber != null) {
            edt_phone.setText(StaticValues.mobileNumber);
        }
        MaterialRippleLayout btn_proceed = findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(v -> getPresenter().checkAndProceed());

        receiveSMSForOTP();
        /////changed permissiom
       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            proceedWithPermissionRequest();
        } else {
            receiveSMSForOTP();
        }*/
    }

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 123;

    //sms receiving request permission is declared in the manifest
    private void proceedWithPermissionRequest() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {
                showDeniedForReceiveSMS();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().activityVisible();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getPresenter() != null) {
            getPresenter().activityGoneSleep();
        }
    }

    @Override
    protected LoginActivityPresenter<LoginActivityIView> initializePresenter() {
        return new LoginActivityPresenter<>(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().activityStopped();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().clean();
        }
        if (otpBroadcastReceiver != null) {
            try {
                unregisterReceiver(otpBroadcastReceiver);
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void switchToNextScreen() {
        Intent intent = new Intent(LoginActivity.this, VerifyOtpActivity.class);
        intent.putExtra(VerifyOtpActivity.KEY_OTP_RECEIVED, otp);
        startActivity(intent);
        finish();
    }

    @Override
    public String getEmail() {
        return edit_email.getText().toString();
    }

    @Override
    public void showInvalidEmailError() {
        edit_email.setError("Please enter a valid email");
        edit_email.requestFocus();
    }

    @Override
    public String getPhoneNumber() {
        return edt_phone.getText().toString();
    }

    @Override
    public void showInvalidPhoneError() {
        edt_phone.setError("Please enter a valid mobile number");
        edt_phone.requestFocus();
    }

    private KProgressHUD progressHUD;

    public void toggleProgress(boolean visible) {
        if (visible) {
            if (progressHUD != null) {
                progressHUD.dismiss();
            }
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(3)
                    .setDimAmount(0.5f)
                    .show();
        } else if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
            progressHUD = null;
        }

    }

    @Override
    public void showRetryMessage() {
        if (!isFinishing())
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "Unable to Login Please Try Again", Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    receiveSMSForOTP();
                } else {


                    /////changed
               //     showDeniedForReceiveSMS();
                }
            }
        }
    }


    void showDeniedForReceiveSMS() {
        Toast.makeText(this, R.string.permission_sms_denied, Toast.LENGTH_SHORT).show();
    }

    private OTPBroadcastReceiver otpBroadcastReceiver;

    void receiveSMSForOTP() {
        otpBroadcastReceiver = new OTPBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(999);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(otpBroadcastReceiver, intentFilter);
    }

    private String otp;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onOTPReceivedEvent(OTPReceivedEvent otpReceivedEvent) {
        otp = otpReceivedEvent.getOtp();
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public Context getContext() {
        return this;
    }


}
