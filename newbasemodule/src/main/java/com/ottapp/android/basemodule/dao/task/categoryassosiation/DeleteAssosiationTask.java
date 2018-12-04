package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

public class DeleteAssosiationTask extends AsyncTask<CategoryAssosiationDataModel, Void, Boolean> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public DeleteAssosiationTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected Boolean doInBackground(CategoryAssosiationDataModel... models) {
        try {
             assosiationDataDao.delete(models[0]);
             return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
