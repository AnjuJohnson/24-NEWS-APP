package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

import java.util.List;

public class GetAssosiationTask extends AsyncTask<Integer, Void, List<CategoryAssosiationDataModel>> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public GetAssosiationTask(CategoryAssosiationDataDao assosiationDataDao) {
        this.assosiationDataDao = assosiationDataDao;
    }

    @Override
    protected List<CategoryAssosiationDataModel> doInBackground(Integer... ids) {
        try {
            return assosiationDataDao.selectByPlayId(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
