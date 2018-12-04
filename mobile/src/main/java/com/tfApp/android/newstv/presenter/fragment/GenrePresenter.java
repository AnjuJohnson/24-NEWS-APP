package com.tfApp.android.newstv.presenter.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.tfApp.android.newstv.adaptors.GenreAdapter;
import com.tfApp.android.newstv.adaptors.OnGentreSelectionListener;
import com.tfApp.android.newstv.presenter.fragment.iview.GenreFragmentIView;
import com.tfApp.android.newstv.view.activity.HolderActivity;
import com.google.gson.Gson;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.GenreResponseEvent;
import com.ottapp.android.basemodule.services.AssetMenuService;
import com.ottapp.android.basemodule.services.GenreService;
import com.ottapp.android.basemodule.services.LanguageService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class GenrePresenter<I extends GenreFragmentIView> extends BaseFragmentPresenter<I> implements OnGentreSelectionListener {
private boolean languageActive;
private GenreAdapter genreAdapter;
private String actionLanguageChoice;
    public GenrePresenter(I iView) {
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

    public void buttonClose(){
        getIView().showNextScreen(actionLanguageChoice);
    }

    public void onButtonClick(List<GenreModel> modal){
     if(modal!=null) {

         for (GenreModel model : modal) {
             if (model.getSeleted() == 1) {
                 languageActive = true;
                 break;
             } else {
                 languageActive = false;
             }
         }
         if (languageActive) {
             GenreService.getServices().insertAll(modal);
             getIView().showNextScreen(actionLanguageChoice);
         } else {
             getIView().showRetryDialog();
         }
     }else{
         getIView().showNextScreen(actionLanguageChoice);
     }
     // System.out.println("GenreSelected:"+ new Gson().toJson(languages));
    }

    public void onSkipClick(){
        getIView().showNextScreen(actionLanguageChoice);
    }

    private void checkValidations() {

        int count = LanguageService.getServices().getAllActive();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGenreEvent(GenreResponseEvent languageEvent) {
        if(languageEvent.isError()){
            if (getIView()!=null){
                getIView().showCheckInternetDialog();
            }
            return;
        }

        if(languageEvent.isSuccess()){
            List<GenreModel> languages = languageEvent.getDatas();
            setupAdapter(languages);
        }else{
            if(getIView()!=null) {
                getIView().showCheckInternetDialog();
            }
        }
    }

    private void setupAdapter(List<GenreModel> genres) {
GenreModel genreModel = new GenreModel();
genreModel.setSeleted(-1);
genreModel.setName("Select All");
genreModel.setActive(1);
genreModel.setId(0);
genres.add(0,genreModel);
     //   List<GenreModel> languageList = GenreService.getServices().getAll();
        if(genres!=null ||!genres.isEmpty())
        for(GenreModel dbModel : genres){
            for(GenreModel model : genres){
                if(dbModel.getId() == model.getId()){
                    if(dbModel.getSeleted() == 1){
                        model.setSeleted(1);
                    }
                }
            }
        }

        genreAdapter = new GenreAdapter(genres, this);
        getIView().getRecyclerView().post(new Runnable()
        {
            @Override
            public void run() {
                getIView().getRecyclerView().setAdapter(genreAdapter);
                getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getActivityObj()));
                getIView().getButton().setVisibility(View.VISIBLE);
              //  getIView().getRecyclerView().setLayoutManager(new AutoFitGridLayoutManager(getIView().getActivityObj(), getIView().getActivityObj().getResources().getDimensionPixelSize(R.dimen._140sdp)));
            }
        });

    }

    public void updateLanguages(List<GenreModel> languages){
       Boolean status = AssetMenuService.getServices().deleteAllAssets();
       if(status){
           AssetMenuService.getServices().getAllUpdatedAssetsRelatedToMenuFromServer(true);
       }

    }

    public void setApiCallLanguage(){
        getIView().getButton().setVisibility(View.INVISIBLE);
        if(getIView()!=null){
            getIView().setTitle("Genre");
        }
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_GENRE_DETAILS,null);
        EventBus.getDefault().post(repoRequestEvent);
    }
    public void onDestroy(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onItemSelect(List<GenreModel> modal, int position) {


        for(int i = 0;i<modal.size();i++){
            if(i == position){
                if(modal.get(i).getName().equals("Select All".trim())){
                    GenreService.getServices().deleteAllInValid();
                    ((HolderActivity) getIView().getActivityObj()).setGenre("All Genre");
                    getIView().showNextScreen(actionLanguageChoice);
                }else{
                    modal.get(i).setSeleted(1);
                }

            }else{
                modal.get(i).setSeleted(-1);
            }
        }
        onButtonClick(modal);
//        onButtonClick();
//        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_LANGUAGE_DETAILS,null);
//        EventBus.getDefault().post(repoRequestEvent);
    }
}
