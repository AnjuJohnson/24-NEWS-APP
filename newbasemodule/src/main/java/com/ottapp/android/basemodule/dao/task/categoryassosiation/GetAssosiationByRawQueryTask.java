package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

public class GetAssosiationByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, CategoryAssosiationDataModel> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public GetAssosiationByRawQueryTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected CategoryAssosiationDataModel doInBackground(SupportSQLiteQuery... queries) {
        try {
            return assosiationDataDao.getByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
