package com.tfApp.android.newstv.presenter.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.tfApp.android.newstv.presenter.fragment.iview.HtmlViewerFragmentIView;

public class HtmlViewerFragmentPresenter<I extends HtmlViewerFragmentIView> extends BaseFragmentPresenter<I> {
    public HtmlViewerFragmentPresenter(I iView) {
        super(iView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("unchecked")
    public void setupAdapter(String url) {
        getIView().getWebView().getSettings().setJavaScriptEnabled(true);
        getIView().getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getIView().getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (getIView() != null)
                    getIView().toggleProgress(true, false);
            }

            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (getIView() != null)
                    getIView().toggleProgress(false, true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (getIView() != null)
                    getIView().toggleProgress(false, true);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (getIView() != null)
                    getIView().toggleProgress(false, true);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if (getIView() != null)
                    getIView().toggleProgress(false, true);
            }
        });
        getIView().getWebView().loadUrl(url);
    }

}
