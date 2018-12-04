package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

import java.util.List;

public class GetAllAssosiationTask extends AsyncTask<Void, Void, List<CategoryAssosiationDataModel>> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public GetAllAssosiationTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected List<CategoryAssosiationDataModel> doInBackground(Void... voids) {
        try {
            return assosiationDataDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
