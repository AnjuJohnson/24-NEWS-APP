package com.ottapp.android.basemodule.dao.task.category;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.List;

public class GetAllCategoryActiveTask extends AsyncTask<Void, Void, List<CategoryListDataModel>> {

    private CategoryDataDao categoryDao;

    public GetAllCategoryActiveTask(CategoryDataDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    protected List<CategoryListDataModel> doInBackground(Void... voids) {
        try {
            return categoryDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
