package com.tfApp.android.newstv.utils;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.services.CategoryAssociationsService;
import com.ottapp.android.basemodule.services.CategoryService;
import com.tfApp.android.newstv.models.BrandKeyResponseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by George PJ on 23-02-2018.
 */
@SuppressWarnings("unused")
public final class DataStore {

    private static DataStore instance;

    private Map<String, List<CategoryListDataModel>> screenMapping = new HashMap<String, List<CategoryListDataModel>>();
    private List<CategoryListDataModel> categoryList;
    private List<MenuDataModel> menuList;
    private List<CategoryAssosiationDataModel> categoryAssosiationList;
    private BrandKeyResponseModel brandKeyResponseModel;
    private List<AssetVideosDataModel> menuVideos;



    private int maxLimit;
    private String TAG ="datastore";

    private DataStore() {
    }

    public void clearData(){
        screenMapping.clear();
        menuList = null;
        categoryList = null;
        categoryAssosiationList = null;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static DataStore getInstance() {
        if (instance == null)
            instance = new DataStore();
        return instance;
    }


    public BrandKeyResponseModel getBrandKeyResponseModel() {
        return brandKeyResponseModel;
    }

    public void setBrandKeyResponseModel(BrandKeyResponseModel brandKeyResponseModel) {

        this.brandKeyResponseModel = brandKeyResponseModel;
    }

    public List<MenuDataModel> getDynamicMenuList() {
        return menuList;
    }
    public void setDynamicMenuList(List<MenuDataModel> menuList){

     this.menuList = menuList;
    }
    public List<CategoryListDataModel> getCategoryList() {
        return categoryList;
    }
    public List<CategoryAssosiationDataModel> getCategoryAssosiationList() {
        return categoryAssosiationList;
    }

    public void setCategoryAssosiationList(List<CategoryAssosiationDataModel> categoryAssosiationList) {

        this.categoryAssosiationList = categoryAssosiationList;
    }


    public void setCategoryList(List<CategoryListDataModel> categoryList) {

        this.categoryList = categoryList;
    }
    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    private int getCategoryById(Integer id) {
        List<CategoryListDataModel> allCategories = CategoryService.getServices().getAll();
        for (CategoryListDataModel categoryModel : allCategories
                ) {
            if (categoryModel.getId() == id) {
                return categoryModel.getId();
            }
        }
        return 0;
    }

    public void setMenuVideoData(List<AssetVideosDataModel> menuVideos){
     this.menuVideos = menuVideos;
    }


    public List<AssetVideosDataModel> getMenuVideos(){
       return menuVideos;
    }

    public List<String> allPlayListIds() {

        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.

            }
        };
        List<String> list = new ArrayList<>();

        List<CategoryListDataModel> categoryModels = CategoryService.getServices().getAll();
        if (categoryModels != null)
            for (CategoryListDataModel categoryModel : categoryModels) {
                list.add(String.valueOf(categoryModel.getId()));
            }

        return list;
    }



    public String allPlayListNameById(String id) {

        List<CategoryListDataModel> categoryModels = CategoryService.getServices().getAll();
        if (categoryModels != null)
            for (CategoryListDataModel categoryModel : categoryModels) {
                if (String.valueOf(categoryModel.getId()).equalsIgnoreCase(id)) {
                    return categoryModel.getName();
                }
            }
        return null;
    }



    private Map<String, List<CategoryListDataModel>> getScreenPlayListMappingV2() {
        if (screenMapping.isEmpty()) {

           List<CategoryAssosiationDataModel> associations = CategoryAssociationsService.getServices().getAll();

            for (CategoryAssosiationDataModel categoryAssociation : associations) {
                if (screenMapping.containsKey(categoryAssociation.getMenuId())) {
                    CategoryListDataModel categoryModel = getCategoryModelNewInstanceById(categoryAssociation.getCategoryId());
                    if (categoryModel != null) {
                        categoryModel.setPositionInMenuScreen(categoryAssociation.getSortOrder());
                        screenMapping.get(categoryAssociation.getMenuId()).add(categoryModel);
                    }
                } else {
                    List<CategoryListDataModel> list = new ArrayList<>(1);
                    CategoryListDataModel categoryModel = getCategoryModelNewInstanceById(categoryAssociation.getCategoryId());
                    if (categoryModel != null) {
                        categoryModel.setPositionInMenuScreen(categoryAssociation.getSortOrder());
                        list.add(categoryModel);
                        screenMapping.put(String.valueOf(categoryAssociation.getMenuId()), list);
                    }
                }
            }
            Set<String> keys = screenMapping.keySet();
            for (String key : keys) {
                Collections.sort(screenMapping.get(key), new CategoryPositionComparator());
            }
        }
        return screenMapping;
    }
    /**
     * Just to hold position in in screens
     *
     */
    private CategoryListDataModel getCategoryModelNewInstanceById(int id) {
        List<CategoryListDataModel> categoryModels = CategoryService.getServices().getAll();
        for (CategoryListDataModel categoryModel : categoryModels
                ) {
            if (categoryModel.getId() == id) {
                return  categoryModel;
            }
        }
        return null;
    }


}