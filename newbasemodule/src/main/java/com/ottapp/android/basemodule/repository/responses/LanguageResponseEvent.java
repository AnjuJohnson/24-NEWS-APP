package com.ottapp.android.basemodule.repository.responses;


import com.ottapp.android.basemodule.models.AboutUsModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class LanguageResponseEvent extends BaseResponse<LanguageModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isError;
    public LanguageResponseEvent(List<LanguageModel> datas, boolean success) {
        super(datas, success);
    }
    public LanguageResponseEvent(LanguageModel datas, boolean success, boolean isError) {

        super(datas, success,isError);
        this.isError = isError;
    }
}
