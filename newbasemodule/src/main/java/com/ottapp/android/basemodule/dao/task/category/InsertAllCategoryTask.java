package com.ottapp.android.basemodule.dao.task.category;

import android.os.AsyncTask;
import android.util.Log;

import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.VasTagModel;

import java.util.ArrayList;
import java.util.List;

public class InsertAllCategoryTask extends AsyncTask<List<CategoryListDataModel>, Void, Boolean> {

    private CategoryDataDao assetDao;

    public InsertAllCategoryTask(CategoryDataDao assetDao) {
        this.assetDao = assetDao;
    }

    @Override
    protected Boolean doInBackground(List<CategoryListDataModel>... models) {
        try {
            assetDao.insertAll(models[0]);
            for (CategoryListDataModel categoryModel : models[0]) {
                if (categoryModel.getVastTag() != null) {
                    int id = categoryModel.getId();
                    VasTagModel model = categoryModel.getVastTag().get(0);
                     model.setCategoryId(id);
                     List<VasTagModel> vasTagList = new ArrayList<>();
                     vasTagList.add(model);
                     assetDao.insertvasListUnderCategory(vasTagList);
                   // Log.e("LogIds", subIds.toString());
                }


            }
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
