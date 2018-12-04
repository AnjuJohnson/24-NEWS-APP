package com.ottapp.android.basemodule.heartbeat.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.heartbeat.iView.HeartbeatTimerInterface;
import com.ottapp.android.basemodule.models.HeartBeatModel;
import com.ottapp.android.basemodule.models.HeartBeatStartServiceModel;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HeartBeatTimerHelper {

    private TimerTask timerTask;
    private Timer timer;
    private HeartbeatTimerInterface heartbeatTimerInterface;

    //constructor created
    public HeartBeatTimerHelper(HeartbeatTimerInterface heartbeatTimerInterface) {
        this.heartbeatTimerInterface = heartbeatTimerInterface;
    }

    //perform the task based on the time 30sec
    public void startTimer(){

                timerTask = new TimerTask() {
                    @Override
                    public void run() {

                        if (heartbeatTimerInterface != null)

                            createHeartBeatRequest(heartbeatTimerInterface.getCurrentTime());
                    }
                };
                timer = new Timer();

                timer.scheduleAtFixedRate(timerTask,0,30000);


    }

    //creating request for the heartbeat service
    private void createHeartBeatRequest(int currenttime) {

        HeartBeatModel model = new HeartBeatModel();

        model.setAssetId(heartbeatTimerInterface.assetData().getId());
        model.setBitrate(480);
        model.setUserId(heartbeatTimerInterface.getUserProfileModel().getId());
        model.setDeviceId(1);
        model.setActive(heartbeatTimerInterface.activeStatus());
        model.setPlayed_duration(currenttime);
        model.setCreatedBy(heartbeatTimerInterface.assetData().getAuthor());
        model.setModifiedBy(heartbeatTimerInterface.assetData().getAuthor());

       getHeartBeatService(model);

    }

    //to clear the timer data
    public void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    //request for heatbeat start ervice
    public void heartBeatStartServiceRequest(){

        HeartBeatStartServiceModel model = new HeartBeatStartServiceModel();

        model.setActive(heartbeatTimerInterface.activeStatus());
        model.setAssetDuration(heartbeatTimerInterface.assetData().getDuration());
        model.setAssetId(heartbeatTimerInterface.assetData().getId());
        model.setAssetName(heartbeatTimerInterface.assetData().getName());
        model.setAssetTitle(heartbeatTimerInterface.assetData().getName());
        model.setAssetUrl(heartbeatTimerInterface.assetData().getHlsUrl());
        model.setDeviceId(1);
        model.setUserId((int)heartbeatTimerInterface.getUserProfileModel().getId());
        model.setEmailId(heartbeatTimerInterface.getUserProfileModel().getEmailId());
        model.setMobileNumber(heartbeatTimerInterface.getUserProfileModel().getMobileNumber());
        model.setModifiedBy((int)heartbeatTimerInterface.getUserProfileModel().getId());
        model.setCreatedBy((int)heartbeatTimerInterface.getUserProfileModel().getId());
        getHeartBeatStartService(model);

    }

    //heartbeat start service
   private void getHeartBeatStartService(HeartBeatStartServiceModel model){
        Call<ResultObject<HeartBeatStartServiceModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getHeartBeatStart(model);
        qrDataCall.enqueue(new Callback<ResultObject<HeartBeatStartServiceModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<HeartBeatStartServiceModel>> call, @NonNull Response<ResultObject<HeartBeatStartServiceModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<HeartBeatStartServiceModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {

                    } else {
                       heartbeatTimerInterface.showProgress();
                    }
                } else {
                    heartbeatTimerInterface.showProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<HeartBeatStartServiceModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage()!=null)
                    Log.e("ErrorCategory", t.getLocalizedMessage());
                heartbeatTimerInterface.showProgress();
            }
        });
    }



    //heartbeat service
    private void getHeartBeatService(HeartBeatModel model){

        Call<ResultObject<HeartBeatModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getHeartBeat(model);
        qrDataCall.enqueue(new Callback<ResultObject<HeartBeatModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResultObject<HeartBeatModel>> call, @NonNull Response<ResultObject<HeartBeatModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<HeartBeatModel> data = response.body();
                    if (data != null && data.isRequestStatus()) {

                    } else {
                        heartbeatTimerInterface.showProgress();
                    }
                } else {
                    heartbeatTimerInterface.showProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<HeartBeatModel>> call, @NonNull Throwable t) {
                if (t.getLocalizedMessage()!=null)
                    Log.e("ErrorHeartBeat", t.getLocalizedMessage());
                heartbeatTimerInterface.showProgress();
            }
        });
    }

}
