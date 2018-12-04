package com.tfApp.android.newstv.utils;

import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.Comparator;

public class MenuPositionComparator implements Comparator<MenuDataModel> {
    @Override
    public int compare(MenuDataModel o1, MenuDataModel o2) {
        return  Integer.compare(o1.getSortOrder(), o2.getSortOrder());
    }
}