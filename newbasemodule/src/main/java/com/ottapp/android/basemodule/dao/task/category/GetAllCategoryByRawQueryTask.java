package com.ottapp.android.basemodule.dao.task.category;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.List;

public class GetAllCategoryByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, List<CategoryListDataModel>> {

    private CategoryDataDao categoryDao;

    public GetAllCategoryByRawQueryTask(CategoryDataDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    protected List<CategoryListDataModel> doInBackground(SupportSQLiteQuery... queries) {
        try {
            return categoryDao.getAllByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }

    }
}
