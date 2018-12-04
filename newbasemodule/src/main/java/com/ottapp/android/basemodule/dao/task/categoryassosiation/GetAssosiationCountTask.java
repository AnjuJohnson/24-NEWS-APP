package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;

public class GetAssosiationCountTask extends AsyncTask<Void, Void, Integer> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public GetAssosiationCountTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return assosiationDataDao.getAllActiveCount();
    }
}
