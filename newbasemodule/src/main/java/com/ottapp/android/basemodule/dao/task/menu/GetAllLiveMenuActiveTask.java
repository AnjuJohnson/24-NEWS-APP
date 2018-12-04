package com.ottapp.android.basemodule.dao.task.menu;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

public class GetAllLiveMenuActiveTask extends AsyncTask<Void, Void, LiveData<List<MenuDataModel>>> {

    private com.ottapp.android.basemodule.dao.MenuDataDao MenuDataDao;

    public GetAllLiveMenuActiveTask(com.ottapp.android.basemodule.dao.MenuDataDao appIntroCardDao) {
        this.MenuDataDao = appIntroCardDao;
    }

    @Override
    protected LiveData<List<MenuDataModel>> doInBackground(Void... integers) {
        try {
            return MenuDataDao.getAllLiveMenu();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
