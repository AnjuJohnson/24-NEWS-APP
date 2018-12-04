package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.VersionModel;

import java.util.List;

public class VersionResponse extends BaseResponse<VersionModel> {
    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isError;

    public VersionResponse(List<VersionModel> datas, boolean success, boolean isError) {
        super(datas, success);
        this.isError = isError;
    }

    public VersionResponse(List<VersionModel> datas, boolean success) {
        super(datas, success);
    }
}
