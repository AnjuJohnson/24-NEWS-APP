package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

import java.util.List;

public class GetAllAssosiationByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, List<CategoryAssosiationDataModel>> {

    private CategoryAssosiationDataDao categoryAssosiationDataDao;

    public GetAllAssosiationByRawQueryTask(CategoryAssosiationDataDao dataDao) {
        this.categoryAssosiationDataDao = dataDao;
    }

    @Override
    protected List<CategoryAssosiationDataModel> doInBackground(SupportSQLiteQuery... queries) {
        try {
            return categoryAssosiationDataDao.getAllByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }

    }
}
