package com.tfApp.android.newstv.adaptors;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;

public interface PlayedDurationListener {
    void showItemClickScreen(AssetVideosDataModel listModel);

    void showMoreProgress();
}
