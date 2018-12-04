package com.ottapp.android.basemodule.dao.task.category;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;

public class DeleteAllInActiveCategoryTask extends AsyncTask<Void, Void, Boolean> {

    private CategoryDataDao categoryDao;

    public DeleteAllInActiveCategoryTask(CategoryDataDao categorysDao) {
        this.categoryDao = categorysDao;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            categoryDao.deleteAllInActive();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
        return true;
    }
}
