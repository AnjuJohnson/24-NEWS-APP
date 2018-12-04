package com.ottapp.android.basemodule.dao.task.category;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;

public class GetLastUpdatedDateCategoryTask extends AsyncTask<Void, Void, Long> {

    private CategoryDataDao categoryDao;

    public GetLastUpdatedDateCategoryTask(CategoryDataDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        try {
            return categoryDao.getLastUpdatedTimestamp();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return 0L;
        }
    }
}
