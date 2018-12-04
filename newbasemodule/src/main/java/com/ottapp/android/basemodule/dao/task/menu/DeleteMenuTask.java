package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

public class DeleteMenuTask extends AsyncTask<MenuDataModel, Void, Boolean> {

    private MenuDataDao menuDao;

    public DeleteMenuTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected Boolean doInBackground(MenuDataModel... models) {
        try {
            menuDao.delete(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
