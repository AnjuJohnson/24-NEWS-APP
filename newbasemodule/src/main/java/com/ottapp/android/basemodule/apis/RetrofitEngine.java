package com.ottapp.android.basemodule.apis;

import com.ottapp.android.basemodule.R;
import com.ottapp.android.basemodule.app.BaseApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by George PJ on 23-04-2018.
 */

public class RetrofitEngine{
    private static RetrofitEngine retrofitEngine;
    private  Object apisObj;
    public static void init(String baseUrl){
        API_BASE_URL=baseUrl;
    }
   private Retrofit retrofit;
   private static String API_BASE_URL ;
    //http://b4f6f3ef.ngrok.io/eventManagement/androidWebServices/sADpT1524501921447
    @SuppressWarnings("unchecked")
    private RetrofitEngine() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("deviceTypeName", "Android")
                            .addQueryParameter("appName", BaseApplication.getApplication().getString(R.string.project_name))
                            .build();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create()
                );

        retrofit = builder.client(okHttpClient).build();
    }
    public static RetrofitEngine getRetrofitEngine() {
        if (API_BASE_URL==null){
             throw  new NullPointerException("Set base url before using apis");
        }
        if (retrofitEngine == null)
            retrofitEngine = new RetrofitEngine();
        return retrofitEngine;
    }


    public <T> T getApiRequests(final Class<T> service) {
        return retrofit.create(service);
    }
}
