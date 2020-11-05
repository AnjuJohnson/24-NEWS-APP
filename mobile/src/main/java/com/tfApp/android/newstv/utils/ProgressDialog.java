package com.tfApp.android.newstv.utils;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressDialog {
    private static KProgressHUD progressHUD;

    public static KProgressHUD LoadingSpinner(Context mContext,boolean visible) {
        if (visible) {
            if (progressHUD != null) {
                progressHUD.dismiss();
            }
            progressHUD = KProgressHUD.create(mContext)
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
        return progressHUD;
    }
    }
