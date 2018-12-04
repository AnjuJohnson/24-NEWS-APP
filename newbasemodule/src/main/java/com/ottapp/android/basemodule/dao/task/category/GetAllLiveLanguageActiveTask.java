package com.ottapp.android.basemodule.dao.task.category;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class GetAllLiveLanguageActiveTask extends AsyncTask<Integer, Void, LiveData<List<LanguageModel>>> {

    private LanguageDataDao assosiationDataDao;

    public GetAllLiveLanguageActiveTask(LanguageDataDao appIntroCardDao) {
        this.assosiationDataDao = appIntroCardDao;
    }

    @Override
    protected LiveData<List<LanguageModel>> doInBackground(Integer... integers) {
        try {
            return assosiationDataDao.getAllLiveLanguage();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
