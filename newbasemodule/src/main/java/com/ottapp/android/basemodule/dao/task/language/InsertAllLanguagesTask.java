package com.ottapp.android.basemodule.dao.task.language;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class InsertAllLanguagesTask extends AsyncTask<List<LanguageModel>, Void, Boolean> {

    private LanguageDataDao appIntroCardDao;

    public InsertAllLanguagesTask(LanguageDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(List<LanguageModel>... models) {
        try {
          List<Long> ids=  appIntroCardDao.insertAll(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
