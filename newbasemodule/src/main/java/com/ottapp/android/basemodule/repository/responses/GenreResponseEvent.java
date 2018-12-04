package com.ottapp.android.basemodule.repository.responses;


import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class GenreResponseEvent extends BaseResponse<GenreModel> {

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isError;
    public GenreResponseEvent(List<GenreModel> datas, boolean success) {
        super(datas, success);
    }
    public GenreResponseEvent(GenreModel datas, boolean success, boolean isError) {

        super(datas, success,isError);
        this.isError = isError;
    }
}
