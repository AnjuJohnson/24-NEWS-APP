package com.ottapp.android.basemodule.apis;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by George PJ on 23-04-2018.
 */

public final class ResultObject<T> {
    @SerializedName("status")
    private boolean requestStatus;
    private String message;
    private List<T> list;
    public boolean isRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
