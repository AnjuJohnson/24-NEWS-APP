package com.ottapp.android.basemodule.dao.task.menu;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.MenuDataDao;
import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

public class GetAllMenuActiveTask extends AsyncTask<Void, Void, List<MenuDataModel>> {

    private MenuDataDao menuDao;

    public GetAllMenuActiveTask(MenuDataDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    protected List<MenuDataModel> doInBackground(Void... voids) {
        try {
            return menuDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
