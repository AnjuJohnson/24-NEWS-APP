package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

import java.util.List;

public class InsertAllAssosiationTask extends AsyncTask<List<CategoryAssosiationDataModel>, Void, Boolean> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public InsertAllAssosiationTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected Boolean doInBackground(List<CategoryAssosiationDataModel>... models) {
        try {
            assosiationDataDao.insertAll(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
