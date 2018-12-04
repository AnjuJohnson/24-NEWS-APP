package com.tfApp.android.newstv.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.tfApp.android.newstv.R;
import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressDialog {
    private static KProgressHUD progressHUD;

//    public static Dialog LoadingSpinner(Context mContext){
//        Dialog pd = new Dialog(mContext, android.R.style.Theme_Black);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.aux_progress_spinner, null);
//        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
//        pd.setContentView(view);
//        return pd;
//    }

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
