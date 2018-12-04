package com.ottapp.android.basemodule.dao.task.category;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.List;

public class GetCategoryListByMenuIdTask extends AsyncTask<Integer, Void, List<CategoryListDataModel>> {

    private CategoryDataDao categoryDao;

    public GetCategoryListByMenuIdTask(CategoryDataDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    protected List<CategoryListDataModel> doInBackground(Integer... ids) {
        try {
            return categoryDao.getAllCategoryUnderMenuId(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
