package com.tfApp.android.newstv.presenter.activity;

import android.bitryt.com.youtubedataapi.background.OnLoadingCompletedListener;

import com.tfApp.android.newstv.presenter.activity.iview.SplashActivityIView;
import com.tfApp.android.newstv.utils.StaticValues;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserDetailsModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.ottapp.android.basemodule.repository.responses.AssetsModelResponse;
import com.ottapp.android.basemodule.repository.responses.CategoryMenuAssociationModelResponse;
import com.ottapp.android.basemodule.repository.responses.CategoryModelResponse;
import com.ottapp.android.basemodule.repository.responses.MenuModelResponse;
import com.ottapp.android.basemodule.repository.responses.VersionResponse;
import com.ottapp.android.basemodule.services.AssetMenuService;
import com.ottapp.android.basemodule.services.CategoryAssociationsService;
import com.ottapp.android.basemodule.services.CategoryService;
import com.ottapp.android.basemodule.services.MenuServices;
import com.ottapp.android.basemodule.services.UserProfileService;
import com.ottapp.android.basemodule.utils.Constants;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashscreenActivityPresenter<I extends SplashActivityIView> extends BaseActivityPresenter<I> implements OnLoadingCompletedListener<AssetVideosDataModel> {

    private static final int TO_LOGIN = 10;
    public static final int TO_HOME = 20;
    private long JOB_START_TIME;

    private TimerTask timerTask;
    private Timer timer;
    private boolean isLoaded;
    private boolean flag_menu_synced,
            flag_menu_asset_synced, flag_branding_synced, flag_category_menu_synced, flag_category_synced;

    public SplashscreenActivityPresenter(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void releaseResources() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    //for the background operations
    public void startBackgroundJob() {

        JOB_START_TIME = System.currentTimeMillis();
        if (getIView() != null)
            checkAppVersion();
    }

    //here we call the version service
    private void checkAppVersion() {
        AssetMenuService.getServices().deleteLimitById();
        RepoRequestEvent repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.REQUEST_TYPE_VERSION_CHECK,null);
        EventBus.getDefault().post(repoRequestEvent);

//        if(!PreferenceManager.getManager().getDbStatus()) {
//            boolean b = getIView().getContext().deleteDatabase(AppDatabase.DB_NAME);
//            PreferenceManager.getManager().setDbStatus(true);
//            System.out.println("statusDb"+ b);
//        }

    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVersionEvent(VersionResponse versionEvent) {
        if(versionEvent.isError()){
            if (getIView()!=null){
                getIView().showRetryDialog();
            }
            return;
        }

     if(versionEvent.isSuccess()){
         checkHeaderData();
     }else{
            if(getIView()!=null) {
                getIView().forceUpdateDialog();
            }
     }

    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMenuEvent(MenuModelResponse menuResponseEvent) {
        if(menuResponseEvent.isError()){
            if (getIView()!=null){
                getIView().showRetryDialog();
            }
            return;
        }
        if(menuResponseEvent.isSuccess()){

            checkHeaderData();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAssetsEvent(AssetsModelResponse assetsResponseEvent) {
        if(assetsResponseEvent.isError()){
            if (getIView()!=null){
                getIView().showRetryDialog();
            }
            return;
        }
        if(assetsResponseEvent.isSuccess()){
            AssetMenuService.getServices().setRetryRequired(false);
            checkHeaderData();
        }else{
            AssetMenuService.getServices().setRetryRequired(false);
            checkHeaderData();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onCategoryEvent(CategoryModelResponse categoryResponseEvent) {
        if(categoryResponseEvent.isError()){
            if (getIView()!=null){
                getIView().showRetryDialog();
            }
            return;
        }
        if(categoryResponseEvent.isSuccess()){
            CategoryService.getServices().setRetryRequired(false);
            checkHeaderData();
        }else{
            CategoryService.getServices().setRetryRequired(false);
            checkHeaderData();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onCategoryEvent(CategoryMenuAssociationModelResponse categoryResponseEvent) {
        if(categoryResponseEvent.isError()){
            if (getIView()!=null){
                getIView().showRetryDialog();
            }
            return;
        }
        if(categoryResponseEvent.isSuccess()){
            checkHeaderData();
            CategoryAssociationsService.getServices().setRetryRequired(false);
        }else{
            CategoryAssociationsService.getServices().setRetryRequired(false);
            checkHeaderData();
        }
    }

    //for the background service calls when app launches
    private void checkHeaderData() {
        RepoRequestEvent repoRequestEvent;
        long userId = PreferenceManager.getManager().getUserId();
        String userEmail = PreferenceManager.getManager().getUserEmailId();
        String mobile = PreferenceManager.getManager().getUserMobileNumber();
        if(userId==0 && userEmail!=null &&mobile!=null){

            UserDetailsModel request = new UserDetailsModel();
            request.setEmailId(userEmail);
            request.setMobileNumber(mobile);
            repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                    REQUEST_USER_DETAILS, request);
            EventBus.getDefault().post(repoRequestEvent);
        }

            if (MenuServices.getServices().isPresent() ) {
                if (!flag_menu_synced)
                    MenuServices.getServices().getAllUpdatedMenuModelsFromServer(false);
                flag_menu_synced = true;
                if (AssetMenuService.getServices().isPresent() ||!AssetMenuService.getServices().isRetryRequired() ) {
                    if (!flag_menu_asset_synced)
                        AssetMenuService.getServices().getAllUpdatedAssetsRelatedToMenuFromServer(false);
                    flag_menu_asset_synced = true;
                 //   if (BrandingModelService.getInstance().isPresent() || !BrandingModelService.getInstance().isRetryRequired()) {
                        if ( !CategoryService.getServices().isRetryRequired()) {
                            if (!flag_category_synced)
                                CategoryService.getServices().getAllUpdatedCategoryModelsFromServer(false);
                            flag_category_synced = true;
                            if (!CategoryAssociationsService.getServices().isRetryRequired()) {
                                if (!flag_category_menu_synced)
                                    CategoryAssociationsService.getServices().getAllUpdatedCategoryMenuAssociationModelsFromServer(false);
                                flag_category_menu_synced = true;
                                AssetMenuService.getServices().setRetryRequired(true);
                                CategoryAssociationsService.getServices().setRetryRequired(true);
                                CategoryService.getServices().setRetryRequired(true);
                                if (userEmail != null && !userEmail.trim().isEmpty()) {
                                    StaticValues.mobileNumber = PreferenceManager.getManager().getUserMobileNumber();
                                    StaticValues.emailId = PreferenceManager.getManager().getUserEmailId();
                                    StaticValues.userId = PreferenceManager.getManager().getUserId();
                                    UserProfileModel model = new UserProfileModel();
                                    model.setEmailId(userEmail);
                                    model.setId(userId);
                                    model.setMobileNumber(mobile);
                                    //UserProfileService.getInstance().insert(model);
                                    notifyDataSyncCompleted(TO_HOME);
                                } else {
                                    notifyDataSyncCompleted(TO_LOGIN);
                                }

                               // BrandingModelService.getInstance().setRetryRequired(true);
                                return;
                            } else {
                                repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                                        REQUEST_TYPE_LOAD_APP_CATEGORY_ASSOSIATION_ONLINE, null);
                            }
                        } else {
                            repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                                    REQUEST_TYPE_LOAD_CATEGORIES_ONLINE, null);
                        }

                    } else {
                        repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                                REQUEST_TYPE_LOAD_ASSETS_BY_MENU_ONLINE, null);
                    }
                } else {
                repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                        REQUEST_TYPE_LOAD_MENUS_ONLINE, null);
              }



        EventBus.getDefault().post(repoRequestEvent);
    }
    private void notifyDataSyncCompleted(int to) {
        long JOB_FINISHED_TIME = System.currentTimeMillis();
        long diff_time = JOB_FINISHED_TIME - JOB_START_TIME;
        long MIN_WAIT_TIME = 2000;
        long wait_time = MIN_WAIT_TIME - diff_time;
        if (wait_time > 0) {
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    if (getIView() != null)
                        getIView().switchToNextScreen(to);
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, wait_time);
        } else {
            if (getIView() != null)
                getIView().switchToNextScreen(to);
        }

    }
    @Override
    public void onLoadingCompleted(List<AssetVideosDataModel> result, boolean loadMoreResult) {

    }

    @Override
    public void onLoadingStarted() {

    }

    public void destroy(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
