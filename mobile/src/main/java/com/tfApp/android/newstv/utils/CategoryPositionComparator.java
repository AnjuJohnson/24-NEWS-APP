package com.tfApp.android.newstv.utils;

import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.Comparator;

public class CategoryPositionComparator implements Comparator<CategoryListDataModel> {
    @Override
    public int compare(CategoryListDataModel o1, CategoryListDataModel o2) {
        return  Integer.compare(o1.getPositionInMenuScreen(), o2.getPositionInMenuScreen());
    }
}