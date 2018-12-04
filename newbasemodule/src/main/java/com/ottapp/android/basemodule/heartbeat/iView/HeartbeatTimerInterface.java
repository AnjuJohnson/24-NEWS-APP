package com.ottapp.android.basemodule.heartbeat.iView;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;

public interface HeartbeatTimerInterface {
    int getCurrentTime();

    AssetVideosDataModel assetData();

    int activeStatus();

    UserProfileModel getUserProfileModel();

    void showProgress();
}
