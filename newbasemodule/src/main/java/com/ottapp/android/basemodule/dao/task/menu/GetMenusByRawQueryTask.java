package com.ottapp.android.basemodule.dao.task.menu;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

public class GetMenusByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, MenuDataModel> {

    private MenuDataDao menuDao;

    public GetMenusByRawQueryTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected MenuDataModel doInBackground(SupportSQLiteQuery... queries) {
        try {
            return menuDao.getByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
