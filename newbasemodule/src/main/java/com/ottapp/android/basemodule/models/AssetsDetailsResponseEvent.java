package com.ottapp.android.basemodule.models;


import com.ottapp.android.basemodule.repository.responses.BaseResponse;

import java.util.List;

public class AssetsDetailsResponseEvent extends BaseResponse<AssetDetaillsDataModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isError;
    public AssetsDetailsResponseEvent(List<AssetDetaillsDataModel> datas, boolean success) {
        super(datas, success);
    }
    public AssetsDetailsResponseEvent(AssetDetaillsDataModel datas, boolean success, boolean isError) {

        super(datas, success,isError);
        this.isError = isError;
    }
}
