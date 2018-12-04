package com.ottapp.android.basemodule.apis;

import com.ottapp.android.basemodule.models.AboutUsModel;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.BrandKeyResponseModel;
import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.models.ConfirmOtp;
import com.ottapp.android.basemodule.models.FavouriteRequestModel;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.HeartBeatModel;
import com.ottapp.android.basemodule.models.HeartBeatStartServiceModel;
import com.ottapp.android.basemodule.models.HomeDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.models.RequestOtp;
import com.ottapp.android.basemodule.models.UserDetailsModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.models.VersionModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestApi {
    @POST("user/details")
    Call<ResultObject<UserProfileModel>> getUser(@Body UserDetailsModel request);

    @POST("user/otp")
    Call<ResultObject> getOtp(@Body RequestOtp requestOtp);

    @POST("user/otp")
    Call<ResultObject<UserProfileModel>> verifyOtp(@Body ConfirmOtp requestOtp);
    @GET("appVersion")
    Call<ResultObject<VersionModel>> getVersionUpdateInfo(@Query("version") int version);

    @POST("heartBeat/startDetails")
    Call<ResultObject<HeartBeatStartServiceModel>> getHeartBeatStart(@Body HeartBeatStartServiceModel request);

    @GET("user/{id}/favourite")
    Call<ResultObject<UserFavouritesModel>> fetchFavouriteVideo(@Path("id") long id, @Query("assettypeid") int assettypeid, @Query("limit") long limit);

    @POST("user/{id}/favourite")
    Call<ResultObject<FavouriteRequestModel>> setFavouriteVideo(@Path("id") long id, @Body FavouriteRequestModel request);

    @POST("heartBeat")
    Call<ResultObject<HeartBeatModel>> getHeartBeat(@Body HeartBeatModel request);


//    @GET("category/{id}/assets?")
//    Call<ResultObject<AssetVideosDataModel>> updateMenuVideos(@Path("id") long id, @Query("assetId") int assetId, @Query("modifiedDate") long updatedDate);

    @GET("category/{id}/subCategory/0/getAssets?")
    Call<ResultObject<AssetVideosDataModel>> updateMenuVideos(@Path("id") long id,@Query("updatedDate") long updatedDate, @Query("languageId") String languages);

    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getMenuVideos(@Query("maxLimit") int maxLimit, @Query("limit") int limit, @Query("languageId") String languages, @Query("genreId") String genres);

    @POST("branding/getByKeys?")
    Call<ResultObject<BrandKeyResponseModel>> getBrandByKey(@Body String[] requestOtp);


    @GET("appMenu?")
    Call<ResultObject<MenuDataModel>> getDynamicMenuList(@Query("updatedDate") long updatedDate);

    @GET("category/List?")
    Call<ResultObject<CategoryListDataModel>> getDynamicCategoryList(@Query("updatedDate") long updatedDate);
  //  Call<ResultObject<CategoryListDataModel>> getDynamicCategoryList();

    @GET("appMenu/categoryAssociation?")
    Call<ResultObject<CategoryAssosiationDataModel>> getDynamicCategoryAssosiation(@Query("updatedDate") long updatedDate);

    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getSpecificCategory(@Query("categoryId") int categoryId, @Query("limit") int limit, @Query("maxLimit") int maxLimit, @Query("languageId") String languages, @Query("genreId") String genres);

    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getSearchDataMore(@Query("categoryId") int categoryId, @Query("limit") int limit, @Query("maxLimit") int maxLimit, @Query("searchText") String searchText, @Query("languageId") String languages, @Query("genreId") String genres);

    @GET("appMenu/videos?")
    Call<ResultObject<HomeDataModel>> getSearchDataHome(@Query("limit") int limit, @Query("maxLimit") int maxLimit);

    @GET("asset/{assetId}/details?")
    Call<ResultObject<AssetVideosDataModel>> getPlayedDuration(@Path("assetId") int assetId, @Query("userid") int id);

    @GET("partner?")
    Call<ResultObject<AboutUsModel>> getAboutUs();

    @GET("language?")
    Call<ResultObject<LanguageModel>> getLanguages();

    @GET("genre?")
    Call<ResultObject<GenreModel>> getGenre();

    @GET("asset/filter/language")
    Call<ResultObject<AssetVideosDataModel>> getSelectedLanguages();

    @GET("banner")
    Call<BannerResultObject> getBanners(@Query("updatedDate") long lastUpdatedDate);


}
