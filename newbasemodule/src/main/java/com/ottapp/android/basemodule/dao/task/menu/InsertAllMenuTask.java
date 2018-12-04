package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

public class InsertAllMenuTask extends AsyncTask<List<MenuDataModel>, Void, Boolean> {

    private MenuDataDao assetDao;

    public InsertAllMenuTask(MenuDataDao assetDao) {
        this.assetDao = assetDao;
    }

    @Override
    protected Boolean doInBackground(List<MenuDataModel>... models) {
        try {
            assetDao.insertAll(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
