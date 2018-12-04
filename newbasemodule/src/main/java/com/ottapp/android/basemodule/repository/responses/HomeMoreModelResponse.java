package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class HomeMoreModelResponse extends BaseResponse<AssetVideosDataModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError;
    public HomeMoreModelResponse(List<AssetVideosDataModel> data, boolean success) {
        super(data, success);
    }
    public HomeMoreModelResponse(List<AssetVideosDataModel> data, boolean success, int maxLimit) {
        super(data, success,maxLimit);
    }
    public HomeMoreModelResponse(List<AssetVideosDataModel> data, boolean success, boolean isError) {
        super(data, success);
    }
}
