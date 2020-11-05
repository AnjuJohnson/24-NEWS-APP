package com.tfApp.android.newstv.presenter.fragment;

import android.support.annotation.NonNull;

import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.AssetsDetailsResponseEvent;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;
import com.tfApp.android.newstv.adaptors.PlayedDurationListener;
import com.tfApp.android.newstv.requests.ApiRequests;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayDurationPresenter {


    //service for the played duration
    public void getPlayedDurationService(AssetVideosDataModel playListModel, PlayedDurationListener listener) {
        Call<ResultObject<AssetDetaillsDataModel>> cardCall = RetrofitEngine.getRetrofitEngine()
                .getApiRequests(ApiRequests.class).getPlayedDuration(playListModel.getId(),(int) PreferenceManager.getManager().getUserId());
        cardCall.enqueue(new Callback<ResultObject<AssetDetaillsDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<AssetDetaillsDataModel>> call,
                                   @NonNull Response<ResultObject<AssetDetaillsDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<AssetDetaillsDataModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {


                            List<AssetDetaillsDataModel> detailsList = new ArrayList<>();
                            detailsList.add(data.getData());

                            EventBus.getDefault().post(new AssetsDetailsResponseEvent(data.getData(), data.isRequestStatus(),false));

                    } else {

                            EventBus.getDefault().post(new AssetsDetailsResponseEvent(null, false, true));
                    }
                } else {

                        EventBus.getDefault().post(new AssetsDetailsResponseEvent(null, false, true));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<AssetDetaillsDataModel>> call, @NonNull Throwable t) {
                System.out.println("Error:"+t.getLocalizedMessage());
                    EventBus.getDefault().post(new AssetsDetailsResponseEvent(null, false, true));
            }
        });
    }
}
