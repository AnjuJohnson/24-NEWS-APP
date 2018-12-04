package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

public class GetMenuByIdTask extends AsyncTask<Integer, Void, MenuDataModel> {

    private MenuDataDao menuDao;

    public GetMenuByIdTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected MenuDataModel doInBackground(Integer... ids) {
        try {
            return menuDao.selectByPlayId(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
