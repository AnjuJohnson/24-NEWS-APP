package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;

public class GetLastUpdatedDateAssosiationTask extends AsyncTask<Void, Void, Long> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public GetLastUpdatedDateAssosiationTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        try {
            return assosiationDataDao.getLastUpdatedTimestamp();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return 0L;
        }
    }
}
