package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;

public class DeleteAllInAssosiationTask extends AsyncTask<Void, Void, Boolean> {

    private CategoryAssosiationDataDao assosiationDao;

    public DeleteAllInAssosiationTask(CategoryAssosiationDataDao assosiationDao) {
        this.assosiationDao = assosiationDao;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            assosiationDao.deleteAllInActive();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
        return true;
    }
}
