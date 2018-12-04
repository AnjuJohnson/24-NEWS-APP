package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

public class UpdateMenuTask extends AsyncTask<MenuDataModel, Void, Boolean> {

    private MenuDataDao menuDao;

    public UpdateMenuTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected Boolean doInBackground(MenuDataModel... models) {
        try {
            menuDao.update(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
