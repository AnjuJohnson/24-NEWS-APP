package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.CategoryAssosiationDataModel;

import java.util.List;

public class CategoryMenuAssociationModelResponse extends BaseResponse<CategoryAssosiationDataModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError;
    public CategoryMenuAssociationModelResponse(List<CategoryAssosiationDataModel> data, boolean success) {
        super(data, success);
    }
    public CategoryMenuAssociationModelResponse(List<CategoryAssosiationDataModel> data, boolean success, boolean isError) {
        super(data, success);
    }
}
