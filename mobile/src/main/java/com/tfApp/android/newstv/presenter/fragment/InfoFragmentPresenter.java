package com.tfApp.android.newstv.presenter.fragment;

import android.annotation.SuppressLint;
import android.os.Build;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.iview.InfoFragmentIView;
import com.tfApp.android.newstv.view.fragment.InfoFragment;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import static com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment.TITLE_TEXT;

public class InfoFragmentPresenter<I extends InfoFragmentIView> extends BaseFragmentPresenter<I> {
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    public InfoFragmentPresenter(I iView) {
        super(iView);
    }

    @SuppressLint("SetTextI18n")
    public void setScreen() {
        if(getIView()!=null) {
            getIView().setTitle(InfoFragment.TITLE_TEXT);
            getIView().getModel().setText(getIView().getActivity().getString(R.string.device_model)+"    : "+"  " + Build.MODEL);
            getIView().getBuild().setText(getIView().getActivity().getString(R.string.device_build)+"      : "+"  " + Build.FINGERPRINT);
            getIView().getVersion().setText(getIView().getActivity().getString(R.string.device_version)+"  : "+"  " + Build.VERSION.RELEASE + ", " + getIView().getActivity().getString(R.string.os_api)+ "  " + Build.VERSION.SDK_INT);
            getIView().getAccount().setText(PreferenceManager.getManager().getUserEmailId());
            getIView().getAppVersion().setText(getIView().getActivity().getString(R.string.app_version)+" : "+" " + "1.6");
            getIView().getSeriel().setText(getIView().getActivity().getString(R.string.device_seriel)+"     : "+"  " + android.os.Build.SERIAL);
        }
    }

}
