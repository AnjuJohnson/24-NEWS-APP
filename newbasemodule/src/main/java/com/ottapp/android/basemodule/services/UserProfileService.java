package com.ottapp.android.basemodule.services;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.apis.BannerResultObject;
import com.ottapp.android.basemodule.apis.RequestApi;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.dao.UserProfileDataDao;
import com.ottapp.android.basemodule.dao.task.user.GetAllUserActiveTask;
import com.ottapp.android.basemodule.dao.task.user.InsertUserTask;
import com.ottapp.android.basemodule.events.ActivateFCMEvent;
import com.ottapp.android.basemodule.models.AboutUsModel;
import com.ottapp.android.basemodule.models.ConfirmOtp;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.models.RequestOtp;
import com.ottapp.android.basemodule.models.UserDetailsModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.repository.responses.AboutUsResponseEvent;
import com.ottapp.android.basemodule.repository.responses.BannerModelResponse;
import com.ottapp.android.basemodule.repository.responses.GenerateConfirmOtpResponse;
import com.ottapp.android.basemodule.repository.responses.GenerateOtpResponse;
import com.ottapp.android.basemodule.repository.responses.GenreResponseEvent;
import com.ottapp.android.basemodule.repository.responses.LanguageResponseEvent;
import com.ottapp.android.basemodule.services.base.BaseService;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileService extends BaseService<UserProfileModel> {
    private UserProfileDataDao UserDao;
    private static UserProfileService userProfileService;
    public static UserProfileService getInstance(){
        if (userProfileService==null)
            userProfileService=new UserProfileService();
        return userProfileService;
    }
    private UserProfileService(){
        super();
    }

    @Override
    protected void setupDao(@NonNull AppDatabase appDatabase) {
        UserDao = appDatabase.profileModelDao();
    }

    @Override
    public void insert(UserProfileModel model) {
        if (UserDao != null && model != null) {
            new InsertUserTask(UserDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,model);
        }
    }

    @Override
    public void insertAll(List<UserProfileModel> models) {

    }

    @Override
    public List<UserProfileModel> getAll() {
        if (UserDao != null) {
            try {
                return new GetAllUserActiveTask(UserDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public LiveData<List<UserProfileModel>> getAllLive(int id) {
        return null;
    }

    @Override
    public UserProfileModel getById(int id) {
        return null;
    }

    @Override
    public void deleteAllInValid() {

    }

    @Override
    public long getLastUpdatedTimestamp() {
        return 0;
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public void destroy() {

    }

    public void generateOtpRequest(RequestOtp requestOtp, boolean needCallBack) {
        Call<ResultObject> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getOtp(requestOtp);
        qrDataCall.enqueue(new Callback<ResultObject>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<ResultObject> call, @NonNull Response<ResultObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("RESULT", response.toString());
                    ResultObject<UserProfileModel> resultObject = response.body();
                    if (resultObject != null) {
                        if (resultObject.isRequestStatus()) {
                            if (needCallBack) {
                                System.out.println("otp"+new Gson().toJson(resultObject.getData()));
                                EventBus.getDefault().post(new GenerateOtpResponse(null,true));
                            }
                        } else {
                            if (needCallBack) {
                                EventBus.getDefault().post(new GenerateOtpResponse(null,false));
                            }
                        }
                    } else {
                        if (needCallBack) {
                            EventBus.getDefault().post(new GenerateOtpResponse(null,false));
                        }
                    }

                }}

            @Override
            public void onFailure(@NonNull Call<ResultObject> call, @NonNull Throwable t) {
                if (needCallBack) {
                    EventBus.getDefault().post(new GenerateOtpResponse(null,false));
                }
            }
        });

    }

    public void generateOtpRequest(ConfirmOtp confirmOtp, boolean needCallBack) {
        Call<ResultObject<UserProfileModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).verifyOtp(confirmOtp);
        qrDataCall.enqueue(new Callback<ResultObject<UserProfileModel>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<ResultObject<UserProfileModel>> call, @NonNull Response<ResultObject<UserProfileModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("RESULT", response.toString());
                    ResultObject<UserProfileModel> resultObject = response.body();
                    if (resultObject != null) {
                        if (resultObject.isRequestStatus()) {
                            new EventBus().post(new ActivateFCMEvent(true));
                            if (needCallBack) {

                              //  insert(resultObject.getData());
                                PreferenceManager.getManager().setUserEmailId(response.body().getData().getEmailId());
                                PreferenceManager.getManager().setUserMobileNumber(response.body().getData().getMobileNumber());
                                PreferenceManager.getManager().setUserId(response.body().getData().getId());
                                EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,true));
                            }
                        } else {
                            if (needCallBack) {
                                EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,false));
                            }
                        }
                    } else {
                        if (needCallBack) {
                            EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,false));
                        }
                    }

                }}

            @Override
            public void onFailure(@NonNull Call<ResultObject<UserProfileModel>> call, @NonNull Throwable t) {
                if (needCallBack) {
                    EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,false));
                }
            }
        });

    }

    public void generateUserDetailsRequest(UserDetailsModel request, boolean needCallBack) {
        Call<ResultObject<UserProfileModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getUser(request);
        qrDataCall.enqueue(new Callback<ResultObject<UserProfileModel>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<ResultObject<UserProfileModel>> call, @NonNull Response<ResultObject<UserProfileModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("RESULT", response.toString());
                    ResultObject<UserProfileModel> resultObject = response.body();
                    System.out.println("userData1" + new Gson().toJson(response.body()));
                    if (resultObject != null) {
                        if (resultObject.isRequestStatus()) {
                            new EventBus().post(new ActivateFCMEvent(true));
                            if (needCallBack) {

                                System.out.println("userData" + new Gson().toJson(response.body().getData()));
                                insert(response.body().getData());
                                PreferenceManager.getManager().setUserEmailId(response.body().getData().getEmailId());
                                PreferenceManager.getManager().setUserMobileNumber(response.body().getData().getMobileNumber());
                                PreferenceManager.getManager().setUserId(response.body().getData().getId());
                                EventBus.getDefault().post(new GenerateConfirmOtpResponse(null, true));
                            }
                        } else {
                            if (needCallBack) {
                                EventBus.getDefault().post(new GenerateConfirmOtpResponse(null, true));
                            }
                        }
                    } else {
                        if (needCallBack) {
                            EventBus.getDefault().post(new GenerateConfirmOtpResponse(null, true));
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<UserProfileModel>> call, @NonNull Throwable t) {
                if (needCallBack) {
                    EventBus.getDefault().post(new GenerateConfirmOtpResponse(null, true));
                }
            }
        });
    }

        public void generateAboutDetailsRequest(boolean needCallBack) {
            Call<ResultObject<AboutUsModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getAboutUs();
            qrDataCall.enqueue(new Callback<ResultObject<AboutUsModel>>() {
                @SuppressWarnings("unchecked")
                @Override
                public void onResponse(@NonNull Call<ResultObject<AboutUsModel>> call, @NonNull Response<ResultObject<AboutUsModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("RESULT", response.toString());
                        ResultObject<AboutUsModel> resultObject = response.body();
                        if (resultObject != null) {
                            if (resultObject.isRequestStatus()) {
                                new EventBus().post(new ActivateFCMEvent(true));
                                if (needCallBack) {

                                    System.out.println("userData"+new Gson().toJson(response.body().getData()));

                                    EventBus.getDefault().post(new AboutUsResponseEvent(resultObject.getData(),true,false));
                                }
                            } else {
                                if (needCallBack) {
                                    EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,true));
                                }
                            }
                        } else {
                            if (needCallBack) {
                                EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,true));
                            }
                        }

                    }}

                @Override
                public void onFailure(@NonNull Call<ResultObject<AboutUsModel>> call, @NonNull Throwable t) {
                    if (needCallBack) {
                        EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,true));
                    }
                }
            });

    }
    public void languageDetailsRequest(boolean needCallBack) {
        Call<ResultObject<LanguageModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getLanguages();
        qrDataCall.enqueue(new Callback<ResultObject<LanguageModel>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<ResultObject<LanguageModel>> call, @NonNull Response<ResultObject<LanguageModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultObject<LanguageModel> resultObject = response.body();
                    if (resultObject != null) {
                        if (resultObject.isRequestStatus()) {
                            new EventBus().post(new ActivateFCMEvent(true));
                            if (needCallBack) {



                                EventBus.getDefault().post(new LanguageResponseEvent(resultObject.getList(),true));
                            }
                        } else {
                            if (needCallBack) {
                                EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,false));
                            }
                        }
                    } else {
                        if (needCallBack) {
                            EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,false));
                        }
                    }

                }}

            @Override
            public void onFailure(@NonNull Call<ResultObject<LanguageModel>> call, @NonNull Throwable t) {
                if (needCallBack) {
                    EventBus.getDefault().post(new GenerateConfirmOtpResponse(null,false));
                }
            }
        });

    }

    public void genreDetailsRequest(boolean needCallBack) {
        Call<ResultObject<GenreModel>> qrDataCall = RetrofitEngine.getRetrofitEngine().getApiRequests(RequestApi.class).getGenre();
        qrDataCall.enqueue(new Callback<ResultObject<GenreModel>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<ResultObject<GenreModel>> call, @NonNull Response<ResultObject<GenreModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("RESULT", response.toString());
                    ResultObject<GenreModel> resultObject = response.body();
                    if (resultObject != null) {
                        if (resultObject.isRequestStatus()) {
                            new EventBus().post(new ActivateFCMEvent(true));
                            if (needCallBack) {
                                EventBus.getDefault().post(new GenreResponseEvent(resultObject.getList(), true));
                            }
                        } else {
                            if (needCallBack) {
                                EventBus.getDefault().post(new GenreResponseEvent(null, false));
                            }
                        }
                    } else {
                        if (needCallBack) {
                            EventBus.getDefault().post(new GenreResponseEvent(null, false));
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultObject<GenreModel>> call, @NonNull Throwable t) {
                if (needCallBack) {
                    EventBus.getDefault().post(new GenreResponseEvent(null, false));
                }
            }
        });
    }


    public void getAllUpdatedBannersFromServer(boolean needReturnEvent) {
        long lastUpdatedDate = getLastUpdatedTimestamp();
        Call<BannerResultObject> cardCall = RetrofitEngine.getRetrofitEngine()
                .getApiRequests(RequestApi.class).getBanners(lastUpdatedDate);
        cardCall.enqueue(new Callback<BannerResultObject>() {
            @Override
            public void onResponse(@NonNull Call<BannerResultObject> call,
                                   @NonNull Response<BannerResultObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BannerResultObject data = response.body();
                    if (data != null && data.isRequestStatus()) {

                        if (needReturnEvent) {
                            EventBus.getDefault().post(new BannerModelResponse(data.getData(), data.isRequestStatus()));
                        }
                    } else {
                        if (needReturnEvent) {
                            EventBus.getDefault().post(new BannerModelResponse(null, false));
                        }
                    }
                } else {
                    if (needReturnEvent) {
                        EventBus.getDefault().post(new BannerModelResponse(null, false));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BannerResultObject> call, @NonNull Throwable t) {
                if (needReturnEvent) {
                    EventBus.getDefault().post(new BannerModelResponse(null, false));
                }
            }
        });
    }
}
