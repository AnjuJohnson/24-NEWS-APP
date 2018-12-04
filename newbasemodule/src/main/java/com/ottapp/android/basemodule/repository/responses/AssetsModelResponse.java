package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class AssetsModelResponse extends BaseResponse<AssetVideosDataModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError;
    public AssetsModelResponse(List<AssetVideosDataModel> datas, boolean success) {
        super(datas, success);
    }
    public AssetsModelResponse(List<AssetVideosDataModel> datas, boolean success,boolean isError) {
        super(datas, success);
    }
}
