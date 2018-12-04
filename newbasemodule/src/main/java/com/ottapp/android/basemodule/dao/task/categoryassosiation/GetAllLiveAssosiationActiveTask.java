package com.ottapp.android.basemodule.dao.task.categoryassosiation;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryAssosiationDataDao;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

import java.util.List;

public class GetAllLiveAssosiationActiveTask extends AsyncTask<Integer, Void, LiveData<List<CategoryAssosiationDataModel>>> {

    private CategoryAssosiationDataDao assosiationDataDao;

    public GetAllLiveAssosiationActiveTask(CategoryAssosiationDataDao appIntroCardDao) {
        this.assosiationDataDao = appIntroCardDao;
    }

    @Override
    protected LiveData<List<CategoryAssosiationDataModel>> doInBackground(Integer... integers) {
        try {
            return assosiationDataDao.getAllLiveAssosiations();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
