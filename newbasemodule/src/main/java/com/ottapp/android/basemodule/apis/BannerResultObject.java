package com.ottapp.android.basemodule.apis;

import com.google.gson.annotations.SerializedName;
import com.ottapp.android.basemodule.models.BannerModel;

import java.util.List;

public class BannerResultObject {
    @SerializedName("status")
    private boolean requestStatus;
    private String message;

    public boolean isRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BannerModel> getData() {
        return data;
    }

    public void setData(List<BannerModel> data) {
        this.data = data;
    }

    private List<BannerModel> data;
}
