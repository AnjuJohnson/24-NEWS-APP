package com.ottapp.android.basemodule.repository.responses;

import com.ottapp.android.basemodule.models.UserProfileModel;

import java.util.List;

public class UserProfileResponse extends BaseResponse<UserProfileModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError;
    public UserProfileResponse(List<UserProfileModel> datas, boolean success) {
        super(datas, success);
    }
    public UserProfileResponse(List<UserProfileModel> datas, boolean success, boolean isError) {
        super(datas, success);
    }
}
