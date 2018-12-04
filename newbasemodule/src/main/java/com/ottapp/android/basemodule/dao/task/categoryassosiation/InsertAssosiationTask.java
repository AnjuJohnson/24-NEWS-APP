package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

public class InsertAssosiationTask extends AsyncTask<CategoryAssosiationDataModel, Void, Boolean> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public InsertAssosiationTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected Boolean doInBackground(CategoryAssosiationDataModel... models) {
        try {
             assosiationDataDao.insert(models[0]);
             return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
