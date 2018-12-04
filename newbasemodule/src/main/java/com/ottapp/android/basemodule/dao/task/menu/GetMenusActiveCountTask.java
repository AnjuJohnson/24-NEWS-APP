package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;

public class GetMenusActiveCountTask extends AsyncTask<Void, Void, Long> {

    private MenuDataDao menuDao;

    public GetMenusActiveCountTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        return (long) menuDao.getAllActiveCount();
    }
}
