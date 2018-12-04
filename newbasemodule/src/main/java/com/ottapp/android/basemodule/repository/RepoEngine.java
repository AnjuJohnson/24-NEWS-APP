package com.ottapp.android.basemodule.repository;

import com.ottapp.android.basemodule.app.BaseApplication;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.ConfirmOtp;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.models.MoreItemRequestServiceModel;
import com.ottapp.android.basemodule.models.RequestOtp;
import com.ottapp.android.basemodule.models.UserDetailsModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.repository.responses.AssetsModelResponse;
import com.ottapp.android.basemodule.repository.responses.CategoryMenuAssociationModelResponse;
import com.ottapp.android.basemodule.repository.responses.CategoryModelResponse;
import com.ottapp.android.basemodule.repository.responses.MenuModelResponse;
import com.ottapp.android.basemodule.repository.responses.UserProfileResponse;
import com.ottapp.android.basemodule.services.AssetMenuService;
import com.ottapp.android.basemodule.services.CategoryAssociationsService;
import com.ottapp.android.basemodule.services.CategoryService;
import com.ottapp.android.basemodule.services.MenuServices;
import com.ottapp.android.basemodule.services.UserProfileService;
import com.ottapp.android.basemodule.services.VersionService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public final class RepoEngine {
    private static RepoEngine repoEngine;

    private RepoEngine() {
    }

    public static RepoEngine getRepoEngine() {
        if (repoEngine == null)
            repoEngine = new RepoEngine();
        return repoEngine;
    }

    public void init() {
        repoEngine.registerEventListener();
    }

    private void registerEventListener() {
        if (!EventBus.getDefault().isRegistered(repoEngine)) {
            EventBus.getDefault().register(repoEngine);
        }
    }

    @SuppressWarnings("unused")
    public void destroy() {
        unRegisterEventListener();
    }

    private void unRegisterEventListener() {
        if (EventBus.getDefault().isRegistered(repoEngine)) {
            EventBus.getDefault().unregister(repoEngine);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(RepoRequestEvent repoRequestEvent) {
        switch (repoRequestEvent.getRequestType()) {
            case RepoRequestType.REQUEST_TYPE_LOAD_APP_CATEGORY_ASSOSIATION_OFFLINE:
                List<CategoryAssosiationDataModel> assosiationModels = CategoryAssociationsService.getServices().getAll();
                EventBus.getDefault().post(new CategoryMenuAssociationModelResponse(assosiationModels, true));
                CategoryAssociationsService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_APP_CATEGORY_ASSOSIATION_ONLINE:
                CategoryAssociationsService.getServices().getAllUpdatedCategoryMenuAssociationModelsFromServer(true);
                CategoryAssociationsService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_APP_CATEGORY_ASSOSIATION_HYBRID:
                assosiationModels = CategoryAssociationsService.getServices().getAll();
                EventBus.getDefault().post(new CategoryMenuAssociationModelResponse(assosiationModels, true));
                if (assosiationModels == null || assosiationModels.isEmpty()) {
                    CategoryAssociationsService.getServices().getAllUpdatedCategoryMenuAssociationModelsFromServer(true);
                }
                CategoryAssociationsService.getServices().destroy();
                break;

            case RepoRequestType.REQUEST_TYPE_LOAD_CATEGORIES_OFFLINE:
                List<CategoryListDataModel> models = CategoryService.getServices().getAll();
                EventBus.getDefault().post(new CategoryModelResponse(models, true));
                CategoryService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_CATEGORIES_ONLINE:
                CategoryService.getServices().getAllUpdatedCategoryModelsFromServer(true);
                CategoryService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_CATEGORIES_HYBRID:
                models = CategoryService.getServices().getAll();
                EventBus.getDefault().post(new CategoryModelResponse(models, true));
                if (models == null || models.isEmpty()) {
                    CategoryService.getServices().getAllUpdatedCategoryModelsFromServer(true);
                }
                CategoryService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_MENUS_OFFLINE:
                List<MenuDataModel> menus = MenuServices.getServices().getAll();
                EventBus.getDefault().post(new MenuModelResponse(menus, true));
                MenuServices.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_MENUS_ONLINE:
                MenuServices.getServices().getAllUpdatedMenuModelsFromServer(true);
                MenuServices.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_MENUS_HYBRID:
                menus = MenuServices.getServices().getAll();
                EventBus.getDefault().post(new MenuModelResponse(menus, true));
                if (menus == null || menus.isEmpty()) {
                    MenuServices.getServices().getAllUpdatedMenuModelsFromServer(true);
                }
                MenuServices.getServices().destroy();
                break;

//            case RepoRequestType.REQUEST_TYPE_LOAD_BRANDING_OFFLINE:
//               // List<BrandingModelResponse> branding = BrandingModelService.getInstance().getAll();
//               // EventBus.getDefault().post(new BrandingModelResponse(branding, true));
//               // BrandingModelService.getInstance().destroy();
//                break;
//            case RepoRequestType.REQUEST_TYPE_LOAD_BRANDING_ONLINE:
//                BrandingModelService.getInstance().getAllUpdatedBrandingModelsFromServer(true);
//                BrandingModelService.getInstance().destroy();
//                break;
//            case RepoRequestType.REQUEST_TYPE_LOAD_BRANDING_HYBRID:
//                branding = BrandingModelService.getInstance().getAll();
//                EventBus.getDefault().post(new BrandingModelResponse(branding, true));
//                if (branding == null || branding.isEmpty()) {
//                    BrandingModelService.getInstance().getAllUpdatedBrandingModelsFromServer(true);
//                }
//                BrandingModelService.getInstance().destroy();
//                break;

            case RepoRequestType.REQUEST_TYPE_LOAD_ASSETS_BY_MENU_OFFLINE:
                List<AssetVideosDataModel> assetInMenu = AssetMenuService.getServices().getAll();
                EventBus.getDefault().post(new AssetsModelResponse(assetInMenu, true));
                AssetMenuService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_ASSETS_BY_MENU_ONLINE:
                AssetMenuService.getServices().getAllUpdatedAssetsRelatedToMenuFromServer(true);
                AssetMenuService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_LOAD_ASSETS_BY_MENU_HYBRID:
                assetInMenu = AssetMenuService.getServices().getAll();
                EventBus.getDefault().post(new AssetsModelResponse(assetInMenu, true));
                if (assetInMenu == null || assetInMenu.isEmpty()) {
                    AssetMenuService.getServices().getAllUpdatedAssetsRelatedToMenuFromServer(true);
                }
                AssetMenuService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_VERSION_CHECK:
                int version = BaseApplication.getApplication().getAppVersion();
                VersionService.getServices().checkUpdate(version,true);
                VersionService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_USER_PROFILE:
                UserProfileService.
                        getInstance().generateOtpRequest((RequestOtp)repoRequestEvent.getData(),true);
                UserProfileService.getInstance().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_USER_PROFILE_OFFLINE:
                List<UserProfileModel> UserProfileModel = UserProfileService.getInstance().getAll();
                EventBus.getDefault().post(new UserProfileResponse(UserProfileModel, true));

                UserProfileService.getInstance().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_OTP_VERIFY:
                UserProfileService.
                        getInstance().generateOtpRequest((ConfirmOtp)repoRequestEvent.getData(),true);
                UserProfileService.getInstance().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_MORE_ITEMS:
                AssetMenuService.
                        getServices().getSpecificCategory((MoreItemRequestServiceModel)repoRequestEvent.getData(),true);
                AssetMenuService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_TYPE_MORE_ITEMS_HOME:
                CategoryService.
                        getServices().getSpecificCategoryMoreHome((MoreItemRequestServiceModel) repoRequestEvent.getData(),true);
                CategoryService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_ASSETS_UPDATION:
                AssetMenuService.getServices().getAllCategoryIds(true);
                AssetMenuService.getServices().destroy();
                break;
            case RepoRequestType.REQUEST_USER_DETAILS:
                UserProfileService.
                        getInstance().generateUserDetailsRequest((UserDetailsModel)repoRequestEvent.getData(),true);
                UserProfileService.getInstance().destroy();
                break;

            case RepoRequestType.REQUEST_ABOUT_DETAILS:
                UserProfileService.
                        getInstance().generateAboutDetailsRequest(true);
                UserProfileService.getInstance().destroy();
                break;
            case RepoRequestType.REQUEST_LANGUAGE_DETAILS:
                UserProfileService.
                        getInstance().languageDetailsRequest(true);
                UserProfileService.getInstance().destroy();
                break;
            case RepoRequestType.REQUEST_LANGUAGE_SELECTION:
                UserProfileService.
                        getInstance().languageDetailsRequest(true);
                UserProfileService.getInstance().destroy();
                break;

            case RepoRequestType.REQUEST_GENRE_DETAILS:
                UserProfileService.
                        getInstance().genreDetailsRequest(true);
                UserProfileService.getInstance().destroy();
                break;


        }
    }

}
