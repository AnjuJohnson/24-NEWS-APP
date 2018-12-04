package com.ottapp.android.basemodule.presenters;

import com.ottapp.android.basemodule.view.iview.BaseIView;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<I extends BaseIView> {
    private WeakReference<I> iWeakReference;
    private boolean visible;

    public BasePresenter(I iView) {
        iWeakReference = new WeakReference<>(iView);
    }

    public I getIView() {
        if (iWeakReference == null) return null;
        return iWeakReference.get();
    }

    public void release() {
        if (iWeakReference != null)
            iWeakReference.clear();
        iWeakReference = null;
    }

    public void onPause() {
        visible = false;
    }

    public void onResume() {
        visible = true;
    }

    public final boolean isVisible() {
        return visible;
    }
}
