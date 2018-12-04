package com.ottapp.android.basemodule.dao.task.user;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.UserProfileDataDao;
import com.ottapp.android.basemodule.models.UserProfileModel;

import java.util.List;

public class GetAllUserActiveTask extends AsyncTask<Void, Void, List<UserProfileModel>> {

    private UserProfileDataDao userDao;

    public GetAllUserActiveTask(UserProfileDataDao appIntroCardDao) {
        this.userDao = appIntroCardDao;
    }

    @Override
    protected List<UserProfileModel> doInBackground(Void... voids) {
        try {
            return userDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
