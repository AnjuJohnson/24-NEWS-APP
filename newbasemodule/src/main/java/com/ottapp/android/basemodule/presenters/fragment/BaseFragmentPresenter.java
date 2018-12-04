package com.ottapp.android.basemodule.presenters.fragment;

import com.ottapp.android.basemodule.presenters.BasePresenter;
import com.ottapp.android.basemodule.view.iview.fragment.BaseFragmentIView;

public class BaseFragmentPresenter<I extends BaseFragmentIView> extends BasePresenter<I> {
    public BaseFragmentPresenter(I iView) {
        super(iView);
    }
}
