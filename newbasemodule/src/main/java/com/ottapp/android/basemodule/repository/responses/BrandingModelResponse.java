package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.BrandKeyResponseModel;

import java.util.List;

public class BrandingModelResponse extends BaseResponse<BrandKeyResponseModel> {

    public BrandingModelResponse(List<BrandKeyResponseModel> data, boolean success) {
        super(data, success);
    }

}
