package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.List;

public class CategoryModelResponse extends BaseResponse<CategoryListDataModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError;
    public CategoryModelResponse(List<CategoryListDataModel> data, boolean success) {
        super(data, success);
    }

    public CategoryModelResponse(List<CategoryListDataModel> data, boolean success,boolean isError) {
        super(data, success);
    }
}
