package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.MenuDataModel;

import java.util.List;

public class MenuModelResponse extends BaseResponse<MenuDataModel> {
    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError;
    public MenuModelResponse(List<MenuDataModel> data, boolean success) {
        super(data, success);
    }

    public MenuModelResponse(List<MenuDataModel> data, boolean success, boolean isError) {
        super(data, success);
    }
}
