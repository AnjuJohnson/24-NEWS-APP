package com.tfApp.android.newstv.presenter.fragment;

import android.text.Html;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ottapp.android.basemodule.models.AboutUsModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.AboutUsResponseEvent;
import com.tfApp.android.newstv.adaptors.YoutubeItemAdapter;
import com.tfApp.android.newstv.presenter.fragment.iview.AboutFragmentIView;
import com.tfApp.android.newstv.utils.ProgressDialog;
import com.tfApp.android.newstv.view.fragment.AboutFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AboutFragmentPresenter<I extends AboutFragmentIView> extends BaseFragmentPresenter<I>  {

    private List<AssetVideosDataModel> dataModels = new ArrayList<>();
    private AssetVideosDataModel onItemClickVideoData;
    private YoutubeItemAdapter youtubeItemAdapter;
    private int maxLimit = 10;
    private int dataId;
    private List<AssetVideosDataModel> favouriteList = new ArrayList<>();
    private  KProgressHUD progress_spinner;
    private String actionName;
    public AboutFragmentPresenter(I iView) {
        super(iView);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);

        }
        progress_spinner = ProgressDialog.LoadingSpinner(getIView().getActivityObj(),true);
    }

    //adapter class to set the data into the recyclerview
    @SuppressWarnings("unchecked")
    public void setupAdapter() {
        progress_spinner.show();
        if (getIView() != null) {
            String title = getIView().getArgument().getString(AboutFragment.PAGE_TITLE);
            actionName = getIView().getArgument().getString(AboutFragment.PAGE_ACTION);
            getIView().setTitle(title);
            RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_ABOUT_DETAILS,null);
            EventBus.getDefault().post(repoRequestEvent);
//            dataId = getIView().getArgument().getInt(LOAD_DATA);

        }
        progress_spinner.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void AboutUsEvent(AboutUsResponseEvent event) {
        if (event.isError()) {
            if (getIView() != null) {
                getIView().showRetryDialog();
            }
            return;
        }
        if (event.isSuccess()) {
            setText(event.getData());

        }else{
            getIView().showRetryDialog();
        }
        progress_spinner.dismiss();
    }
    private void setText(AboutUsModel data){
        getIView().getActivityObj().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(actionName.equals("about")) {

                    getIView().getTextView().setText(Html.fromHtml("<![CDATA["+data.getAboutUs()+"]]>"));;
                }else if(actionName.equals("terms")){
                    getIView().getTextView().setText(Html.fromHtml(data.getTerms()));
                }
            }
        });
    }
    public void onDestroy(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);

        }
    }
}
