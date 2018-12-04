package com.ottapp.android.basemodule.dao.task.category;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;

public class GetCategorysByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, CategoryListDataModel> {

    private CategoryDataDao categoryDao;

    public GetCategorysByRawQueryTask(CategoryDataDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    protected CategoryListDataModel doInBackground(SupportSQLiteQuery... queries) {
        try {
            return categoryDao.getByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
