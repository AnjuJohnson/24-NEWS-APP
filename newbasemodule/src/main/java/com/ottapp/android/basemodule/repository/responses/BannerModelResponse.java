package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.BannerModel;

import java.util.List;

public class BannerModelResponse extends BaseResponse<BannerModel> {

    public BannerModelResponse(List<BannerModel> data, boolean success) {
        super(data, success);
    }

}
