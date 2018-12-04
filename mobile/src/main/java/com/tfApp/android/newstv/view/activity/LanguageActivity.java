package com.tfApp.android.newstv.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.balysv.materialripple.MaterialRippleLayout;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.activity.LoginActivityPresenter;
import com.tfApp.android.newstv.presenter.activity.iview.LoginActivityIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.fragment.LanguageFragment;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.events.OTPReceivedEvent;
import com.ottapp.android.basemodule.receiver.OTPBroadcastReceiver;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by George PJ on 23-04-2018.
 */

public class LanguageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AlertDialog dialog;
    Activity activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bottom_menu_bar)));
        activity.setTitle(R.string.languages_check);
        switchToNextScreen();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void switchToNextScreen() {
        LanguageFragment fragment = new LanguageFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setMessage("Do you really want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog = builder.create();
            dialog.dismiss();
        });
        //   builder.setNegativeButton("Cancel", (dialog, which) -> adapter.setSelected(prev_pos));


        try {
            dialog.show();
        } catch (Exception ignored) {
            //bad window token happened
//                getIView().finish();
            dialog = builder.create();
            dialog.dismiss();
        }
    }

}
