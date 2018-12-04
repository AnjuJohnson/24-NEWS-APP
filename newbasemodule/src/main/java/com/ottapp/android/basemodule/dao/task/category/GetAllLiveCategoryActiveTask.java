package com.ottapp.android.basemodule.dao.task.category;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.List;

public class GetAllLiveCategoryActiveTask extends AsyncTask<Integer, Void, LiveData<List<CategoryListDataModel>>> {

    private CategoryDataDao assosiationDataDao;

    public GetAllLiveCategoryActiveTask(CategoryDataDao appIntroCardDao) {
        this.assosiationDataDao = appIntroCardDao;
    }

    @Override
    protected LiveData<List<CategoryListDataModel>> doInBackground(Integer... integers) {
        try {
            return assosiationDataDao.getAllLiveCategory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
