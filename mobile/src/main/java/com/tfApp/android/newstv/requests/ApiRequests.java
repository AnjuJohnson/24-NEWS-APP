package com.tfApp.android.newstv.requests;


import com.tfApp.android.newstv.models.BrandKeyResponseModel;
import com.tfApp.android.newstv.models.VersionModel;
import com.ottapp.android.basemodule.apis.ResultObject;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.ConfirmOtp;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.HomeDataModel;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.models.RequestOtp;
import com.ottapp.android.basemodule.models.UserProfileModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by George PJ on 23-04-2018.
 */

public interface ApiRequests  {

    @GET("appVersion?")
    Call<ResultObject<VersionModel>> getVersionUpdateInfo(@Query("version") int version);

    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getMenuVideos(@Query("maxLimit") int maxLimit, @Query("limit") int limit);

    @POST("branding/getByKeys?")
    Call<ResultObject<BrandKeyResponseModel>> getBrandByKey(@Body String[] requestOtp);

    @POST("user/otp")
    Call<ResultObject> getOtp(@Body RequestOtp requestOtp);

    @POST("user/otp")
    Call<ResultObject<UserProfileModel>> verifyOtp(@Body ConfirmOtp requestOtp);

    @GET("appMenu?")
    Call<ResultObject<MenuDataModel>> getDynamicMenuList();

    @GET("category/List?")
//    Call<ResultObject<CategoryListDataModel>> getDynamicCategoryList(@Query("updatedDate") long updatedDate);
    Call<ResultObject<CategoryListDataModel>> getDynamicCategoryList();

    @GET("appMenu/categoryAssociation?")
    Call<ResultObject<CategoryAssosiationDataModel>> getDynamicCategoryAssosiation();

    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getSpecificCategory(@Query("categoryId") int categoryId, @Query("limit") int limit, @Query("maxLimit") int maxLimit);

    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getSearchDataMore(@Query("categoryId") int categoryId, @Query("limit") int limit, @Query("maxLimit") int maxLimit, @Query("searchText") String searchText);
    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getSearchDataHome(@Query("limit") int limit, @Query("maxLimit") int maxLimit);

    @GET("asset/{assetId}/details?")
    Call<ResultObject<AssetDetaillsDataModel>> getPlayedDuration(@Path("assetId") int assetId, @Query("userid") int id);

    @POST("user/{id}/favourite")
    Call<ResultObject> setFavouriteVideo(@Path("id") long id, @Body FavouriteRequestModel request);



}
