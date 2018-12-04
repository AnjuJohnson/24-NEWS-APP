package com.ottapp.android.basemodule.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.ottapp.android.basemodule.app.BaseApplication;

public class UpdateRequestReceiver extends BroadcastReceiver {

    public static final String UPDATE_ACTION = "com.bitryt.android.flowerstv.APP_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Click", "Received");
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(UPDATE_ACTION)) {
            int notifyId=intent.getIntExtra("N_ID",0);
            if (notifyId>0) {
                String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                if (nMgr != null) {
                    nMgr.cancel(notifyId);
                }
            }
            navigateToPlayStore();
        }
    }

    private void navigateToPlayStore() {
        final String appPackageName = BaseApplication.getApplication().getPackageName(); // getPackageName() from Context or Activity object
        try {
            BaseApplication.getApplication().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            BaseApplication.getApplication().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

}
