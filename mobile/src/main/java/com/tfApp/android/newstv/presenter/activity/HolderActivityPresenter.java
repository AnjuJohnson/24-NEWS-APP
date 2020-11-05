package com.tfApp.android.newstv.presenter.activity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.tfApp.android.newstv.presenter.activity.iview.HolderActivityIView;

import org.greenrobot.eventbus.EventBus;

public class HolderActivityPresenter extends BaseActivityPresenter<HolderActivityIView> {
    private static final long ANIMATION_SPEED = 500;
    private HolderActivityIView activityIView;

    public HolderActivityPresenter(HolderActivityIView iView) {
        super(iView);
        activityIView=iView;
    }


    protected void slideToAbove() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                5.1f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(ANIMATION_SPEED);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        activityIView.getBottomMenuRL().setVisibility(View.VISIBLE);
        activityIView.getBottomMenuRL().startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (activityIView.getBottomMenuRL() != null)
                    activityIView.getBottomMenuRL().clearAnimation();
            }
        });

    }




    protected void slideToBottom() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);

        slide.setDuration(ANIMATION_SPEED);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);

        activityIView.getBottomMenuRL().startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (activityIView.getBottomMenuRL() != null) {
                    activityIView.getBottomMenuRL().clearAnimation();
                    activityIView.getBottomMenuRL().setVisibility(View.GONE);
                }
            }

        });

    }


    protected void showAll() {
//        if (activityIView.getBottomMenuRL().getVisibility() != View.VISIBLE)
//            activityIView.getBottomMenuRL().setVisibility(View.VISIBLE);
//        if (activityIView.getTopBar().getVisibility() != View.VISIBLE)
//            activityIView.getTopBar().setVisibility(View.VISIBLE);
    }

    protected void hideAll() {
    }

    public void registerEvents() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    public void unregisterEvents() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
