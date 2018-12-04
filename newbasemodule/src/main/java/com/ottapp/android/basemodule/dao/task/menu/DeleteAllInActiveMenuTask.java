package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;

public class DeleteAllInActiveMenuTask extends AsyncTask<Void, Void, Boolean> {

    private MenuDataDao menuDao;

    public DeleteAllInActiveMenuTask(MenuDataDao menusDao) {
        this.menuDao = menusDao;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            menuDao.deleteAllInActive();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
        return true;
    }
}
