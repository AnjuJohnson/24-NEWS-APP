package com.ottapp.android.basemodule.repository.responses;


import com.ottapp.android.basemodule.models.AboutUsModel;
import com.ottapp.android.basemodule.models.AssetDetaillsDataModel;

import java.util.List;

public class AboutUsResponseEvent extends BaseResponse<AboutUsModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isError;
    public AboutUsResponseEvent(List<AboutUsModel> datas, boolean success) {
        super(datas, success);
    }
    public AboutUsResponseEvent(AboutUsModel datas, boolean success, boolean isError) {

        super(datas, success,isError);
        this.isError = isError;
    }
}
