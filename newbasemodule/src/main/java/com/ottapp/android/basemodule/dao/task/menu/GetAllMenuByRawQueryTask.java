package com.ottapp.android.basemodule.dao.task.menu;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

public class GetAllMenuByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, List<MenuDataModel>> {

    private MenuDataDao menuDao;

    public GetAllMenuByRawQueryTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected List<MenuDataModel> doInBackground(SupportSQLiteQuery... queries) {
        try {
            return menuDao.getAllByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
