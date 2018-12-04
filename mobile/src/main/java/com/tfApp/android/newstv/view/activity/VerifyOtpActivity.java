package com.tfApp.android.newstv.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.activity.VerifyOtpActivityPresenter;
import com.tfApp.android.newstv.presenter.activity.iview.VerifyOtpActivityIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.events.OTPReceivedEvent;
import com.ottapp.android.basemodule.receiver.OTPBroadcastReceiver;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by George PJ on 23-04-2018.
 */
public class VerifyOtpActivity extends BaseActivity<VerifyOtpActivityPresenter<VerifyOtpActivityIView>, VerifyOtpActivityIView> implements VerifyOtpActivityIView, View.OnClickListener {

    public static String KEY_OTP_RECEIVED = "KEY_OTP";
    private EditText edit_otp;
    private TextView tv_resend;
    private ImageView btn_edit;
    private KProgressHUD progressHUD;

    //private VerifyOtpActivityPresenter getPresenter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_otp);
        //getPresenter()=initializePresenter();
        TextView tv_mobile = findViewById(R.id.tv_mobile);
        tv_mobile.setText(StaticValues.mobileNumber);
        tv_resend = findViewById(R.id.tv_resend);
        tv_resend.setPaintFlags(tv_resend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_resend.setOnClickListener(this);
        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        edit_otp = findViewById(R.id.edit_otp);
        MaterialRippleLayout btn_proceed = findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(v -> getPresenter().checkAndProceed());
        String otp = getIntent().getStringExtra(KEY_OTP_RECEIVED);
        if (otp != null) {
            edit_otp.setText(otp);
            btn_proceed.performClick();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            receiveSMSForOTP();
        }

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
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
    protected VerifyOtpActivityPresenter<VerifyOtpActivityIView> initializePresenter() {
        return new VerifyOtpActivityPresenter<>(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().activityStopped();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOTPReceivedEvent(OTPReceivedEvent otpReceivedEvent) {
        if (getPresenter() != null) {
            EditText editText = findViewById(R.id.edit_otp);
            if (editText != null) {
                editText.setText(otpReceivedEvent.getOtp());
                getPresenter().checkAndProceed();
            }
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
        getPresenter().destroy();
    }

    @Override
    public void switchToNextScreen() {
        Intent intent = new Intent(VerifyOtpActivity.this,LanguageActivity.class);
        intent.putExtra("set_language","SelectLanguage");
        startActivity(intent);
        finish();
//        startActivity(new Intent(VerifyOtpActivity.this, MenuLeftActivity.class));
//        finish();
    }

    @Override
    public String getOtp() {
        return edit_otp.getText().toString();
    }

    @Override
    public void showInvalidOtpError() {
        edit_otp.setError("Invalid OTP");
        edit_otp.requestFocus();
    }

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
                    Toast.makeText(VerifyOtpActivity.this, "Unable to Login Please Try Again", Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_edit) {
            startActivity(new Intent(VerifyOtpActivity.this, LoginActivity.class));
            finish();
        } else if (v == tv_resend) {
            getPresenter().resendOtp();
        }
    }

    private OTPBroadcastReceiver otpBroadcastReceiver;

    void receiveSMSForOTP() {
        otpBroadcastReceiver = new OTPBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(999);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(otpBroadcastReceiver, intentFilter);
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public Context getContext() {
        return this;
    }


}
