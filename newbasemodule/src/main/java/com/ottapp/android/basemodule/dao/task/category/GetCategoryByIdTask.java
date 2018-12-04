package com.ottapp.android.basemodule.dao.task.category;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;

public class GetCategoryByIdTask extends AsyncTask<Integer, Void, CategoryListDataModel> {

    private CategoryDataDao categoryDao;

    public GetCategoryByIdTask(CategoryDataDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    protected CategoryListDataModel doInBackground(Integer... ids) {
        try {
            return categoryDao.selectByPlayId(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
