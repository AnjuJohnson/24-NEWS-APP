package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

public class UpdateAssosiationTask extends AsyncTask<CategoryAssosiationDataModel, Void, Boolean> {

    private CategoryAssosiationDataDao categoryAssosiationDataDao;

    public UpdateAssosiationTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.categoryAssosiationDataDao = assosiationDataDao;
    }

    @Override
    protected Boolean doInBackground(CategoryAssosiationDataModel... models) {
        try {
             categoryAssosiationDataDao.update(models[0]);
             return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
