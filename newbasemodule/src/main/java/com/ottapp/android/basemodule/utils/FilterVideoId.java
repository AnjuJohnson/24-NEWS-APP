package com.ottapp.android.basemodule.utils;

public  class FilterVideoId {
    public String getVideoId(String url){
        String[] separated = url.split("\\=");
        return separated[1];
    }

}
