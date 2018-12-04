package com.ottapp.android.basemodule.dao.task.language;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class GetAllLanguagesTask extends AsyncTask<Void, Void, List<LanguageModel>> {

    private LanguageDataDao appIntroCardDao;

    public GetAllLanguagesTask(LanguageDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected List<LanguageModel> doInBackground(Void... voids) {
        try {
            return appIntroCardDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
