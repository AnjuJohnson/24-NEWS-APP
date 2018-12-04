package com.ottapp.android.basemodule.repository.responses;

import java.util.List;

public abstract class BaseResponse<T> {
    public BaseResponse(List<T> datas, boolean success) {
        this.datas = datas;
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    T data;
    public BaseResponse(T datas, boolean success,boolean isError) {
        this.data = datas;
        this.success = success;
    }
    public BaseResponse(List<T> datas, boolean success,int maxLimit) {
        this.datas = datas;
        this.success = success;
        this.maxLimit = maxLimit;
    }
    public BaseResponse(List<T> datas, boolean success,boolean isError) {
        this.datas = datas;
        this.success = success;
        this.isError = isError;
    }

    private List<T> datas;
    private boolean success;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isError;

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    private int maxLimit;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
