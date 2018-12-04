package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class AssetsMoreListResponse extends BaseResponse<AssetVideosDataModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError;
    public AssetsMoreListResponse(List<AssetVideosDataModel> datas, boolean success) {
        super(datas, success);
    }
    public AssetsMoreListResponse(List<AssetVideosDataModel> datas, boolean success,int maxLimit) {
        super(datas, success,maxLimit);
    }
    public AssetsMoreListResponse(List<AssetVideosDataModel> datas, boolean success, boolean isError) {
        super(datas, success);
    }
}
