package com.ott.android.flowerstv.app;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ottapp.android.basemodule.app.BaseApplication;
import com.ottapp.android.basemodule.events.ActivateFCMEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.fabric.sdk.android.Fabric;

public abstract class BaseCouplingApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onFcmActivationRequest(ActivateFCMEvent activateFCMEvent) {
        Log.e("FCM ", "Enable command received");
        FirebaseMessaging.getInstance().setAutoInitEnabled(activateFCMEvent.isActivateRequest());
        Log.e("FCM ", "Enable status " + FirebaseMessaging.getInstance().isAutoInitEnabled());
    }
}
