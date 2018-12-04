package com.ottapp.android.basemodule.repository;

import java.util.List;

public class RepoRequestEvent<T> {
    private int requestType;
    private List<T> datas;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private T data;

    public RepoRequestEvent(int requestType, List<T> datas) {
        this.requestType = requestType;
        this.datas = datas;
    }
    public RepoRequestEvent(int requestType, T data) {
        this.requestType = requestType;
        this.data = data;
    }
    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

}
