package com.ottapp.android.basemodule.presenters.activity;

import com.ottapp.android.basemodule.presenters.BasePresenter;
import com.ottapp.android.basemodule.view.iview.activity.BaseActivityIView;

public abstract class BaseActivityPresenter<I extends BaseActivityIView> extends BasePresenter<I> {
    public BaseActivityPresenter(I iView) {
        super(iView);
    }
}
