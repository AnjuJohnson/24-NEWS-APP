package com.ottapp.android.basemodule.utils;

import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.Comparator;

public class CategoryOrderComparator implements Comparator<CategoryListDataModel> {
    @Override
    public int compare(CategoryListDataModel o1, CategoryListDataModel o2) {
        return  Integer.compare(o1.getSortOrder(), o2.getSortOrder());
    }
}