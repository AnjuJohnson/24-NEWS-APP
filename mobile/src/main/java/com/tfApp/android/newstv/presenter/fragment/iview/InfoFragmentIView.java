package com.tfApp.android.newstv.presenter.fragment.iview;

import android.app.Activity;
import android.widget.TextView;

import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public interface InfoFragmentIView extends BaseFragmentIView {

    TextView getModel();
    TextView getVersion();
    TextView getBuild();
    TextView getSeriel();
    void setTitle(String title);
    TextView getAccount();
    TextView getAppVersion();
    Activity getActivity();


}
