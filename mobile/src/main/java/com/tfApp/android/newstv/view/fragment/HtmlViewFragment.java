package com.tfApp.android.newstv.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.fragment.HtmlViewerFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.HtmlViewerFragmentIView;
import com.tfApp.android.newstv.view.activity.MenuLeftActivity;

/**
 * Created by George PJ on 22-02-2018.
 */

public class HtmlViewFragment extends BaseFragment<HtmlViewerFragmentPresenter<HtmlViewerFragmentIView>, HtmlViewerFragmentIView> implements HtmlViewerFragmentIView {

    public static final String WEB_URL = "WEB_URL";
    public static final String PAGE_TITLE = "TITLE";
    private KProgressHUD progressHUD;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        if (getArguments() != null) {
            String url = getArguments().getString(WEB_URL, null);
            String title = getArguments().getString(PAGE_TITLE, null);
            setTitle(title != null ? title : getString(R.string.app_name), getString(R.string.screen_title_color));
            if (url != null) {
                //HtmlViewerFragmentPresenter htmlViewerFragmentPresenter = initializePresenter();
                getPresenter().setupAdapter(url);
            }
        } else {
            if (getActivity() != null)
                ((MenuLeftActivity) getActivity()).itemSelection(MenuLeftActivity.POS_DEFAULT);
        }
        return view;
    }



    @Override
    public String getScreenTag() {
        return getArguments() == null ? HtmlViewFragment.class.getSimpleName() : getArguments().getString(PAGE_TITLE, null);
    }

    @Override
    protected HtmlViewerFragmentPresenter<HtmlViewerFragmentIView> initializePresenter() {
        return new HtmlViewerFragmentPresenter<>(this);
    }

    @Override
    public void toggleProgress(boolean visible, boolean cancelable) {

        if (visible) {
            if (progressHUD != null) {
                progressHUD.dismiss();
            }
            if (getActivity() != null)
                progressHUD = KProgressHUD.create(getActivity())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(true)
                        .setAnimationSpeed(3)
                        .setDimAmount(0.5f)
                        .show();
        } else if (progressHUD != null) {
            progressHUD.dismiss();
            progressHUD = null;
        }
    }

    @Override
    public WebView getWebView() {
        return view.findViewById(R.id.web_view);
    }

    @Override
    public void onPause() {
        super.onPause();
        toggleProgress(false, true);
    }
}
