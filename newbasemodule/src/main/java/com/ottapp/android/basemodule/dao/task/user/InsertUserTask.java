package com.ottapp.android.basemodule.dao.task.user;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.UserProfileDataDao;
import com.ottapp.android.basemodule.models.UserProfileModel;

public class InsertUserTask extends AsyncTask<UserProfileModel, Void, Boolean> {

    UserProfileDataDao userDao;

    public InsertUserTask(UserProfileDataDao appIntroCardDao) {
        this.userDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(UserProfileModel... models) {
        try {
            userDao.insert(models[0]);
             return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
