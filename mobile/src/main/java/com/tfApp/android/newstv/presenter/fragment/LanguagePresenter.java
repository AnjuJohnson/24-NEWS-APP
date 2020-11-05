package com.tfApp.android.newstv.presenter.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.LanguageResponseEvent;
import com.ottapp.android.basemodule.services.AssetMenuService;
import com.ottapp.android.basemodule.services.LanguageService;
import com.tfApp.android.newstv.adaptors.LanguageAdapter;
import com.tfApp.android.newstv.adaptors.OnLanguageSelectionListener;
import com.tfApp.android.newstv.presenter.fragment.iview.LanguageFragmentIView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class LanguagePresenter<I extends LanguageFragmentIView> extends BaseFragmentPresenter<I> implements OnLanguageSelectionListener {
private boolean languageActive;
private LanguageAdapter languageAdapter;
private String actionLanguageChoice;
    public LanguagePresenter(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Bundle extras = getIView().getActivityObj().getIntent().getExtras();
        if (extras != null) {
            actionLanguageChoice = extras.getString("set_language");
            //The key argument here must match that used in the other activity

        }
    }

    public void onButtonClick(){

      List<LanguageModel> languages = languageAdapter.getSelectedLanguages();

      for(LanguageModel model: languages){
          if(model.getSelected() == 1){
              languageActive = true;
              break;
          }else{
              languageActive = false;
          }
      }
      if(languageActive) {
          LanguageService.getServices().insertAll(languages);
          checkValidations();
          getIView().showNextScreen(actionLanguageChoice);
      }else{
          getIView().showRetryDialog();
      }

      System.out.println("languageSelected:"+ new Gson().toJson(languages));
    }

    public void buttonSkip(){

        getIView().showNextScreen(actionLanguageChoice);
    }

    private void checkValidations() {

        int count = LanguageService.getServices().getAllActive();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onLanguageEvent(LanguageResponseEvent languageEvent) {
        if(languageEvent.isError()){
            if (getIView()!=null){
                getIView().showCheckInternetDialog();
            }
            return;
        }

        if(languageEvent.isSuccess()){
            List<LanguageModel> languages = languageEvent.getDatas();
            setupAdapter(languages);
        }else{
            if(getIView()!=null) {
                getIView().showCheckInternetDialog();
            }
        }

    }

    private void setupAdapter(List<LanguageModel> languages) {

        List<LanguageModel> languageList = LanguageService.getServices().getAll();
        if(languageList!=null ||!languageList.isEmpty())
        for(LanguageModel dbModel : languageList){
            for(LanguageModel model : languages){
                if(dbModel.getId() == model.getId()){
                    if(dbModel.getSelected() == 1){
                        model.setSelected(1);
                    }
                }
            }
        }
        languageAdapter = new LanguageAdapter(languages, this);
        getIView().getRecyclerView().post(new Runnable()
        {
            @Override
            public void run() {
                getIView().getRecyclerView().setAdapter(languageAdapter);
                getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getActivityObj()));
                getIView().getButton().setVisibility(View.VISIBLE);
              //  getIView().getRecyclerView().setLayoutManager(new AutoFitGridLayoutManager(getIView().getActivityObj(), getIView().getActivityObj().getResources().getDimensionPixelSize(R.dimen._140sdp)));
            }
        });

    }

    public void updateLanguages(List<LanguageModel> languages){
       Boolean status = AssetMenuService.getServices().deleteAllAssets();
       if(status){
           AssetMenuService.getServices().getAllUpdatedAssetsRelatedToMenuFromServer(true);
       }

    }

    public void setApiCallLanguage(){
        getIView().getButton().setVisibility(View.GONE);
        if(getIView()!=null){
            getIView().setTitle("Languages");
        }
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_LANGUAGE_DETAILS,null);
        EventBus.getDefault().post(repoRequestEvent);
    }
    public void onDestroy(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onItemSelect(LanguageModel modal) {
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_LANGUAGE_DETAILS,null);
        EventBus.getDefault().post(repoRequestEvent);
    }
}
