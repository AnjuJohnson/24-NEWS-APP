package com.ottapp.android.basemodule.dao.task.category;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;

public class GetCategorysActiveCountTask extends AsyncTask<Void, Void, Integer> {

    private CategoryDataDao categoryDao;

    public GetCategorysActiveCountTask(CategoryDataDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return categoryDao.getAllActiveCount();
    }
}
