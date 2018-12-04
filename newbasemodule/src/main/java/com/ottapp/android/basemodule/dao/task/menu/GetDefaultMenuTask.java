package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

public class GetDefaultMenuTask extends AsyncTask<String, Void, Integer> {

    private MenuDataDao menuDao;

    public GetDefaultMenuTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected Integer doInBackground(String... ids) {
        try {
            return menuDao.selectDefaultMenuId(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
