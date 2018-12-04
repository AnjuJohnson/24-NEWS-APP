package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;

public class GetLastUpdatedDateMenuTask extends AsyncTask<Void, Void, Long> {

    private MenuDataDao menuDao;

    public GetLastUpdatedDateMenuTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        try {
            return menuDao.getLastUpdatedTimestamp();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return 0L;
        }
    }
}
