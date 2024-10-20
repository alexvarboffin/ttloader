package com.walhalla.libcore.likee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OtherValue {

    @SerializedName("likeListMode")
    @Expose
    private String likeListMode;

    public String getLikeListMode() {
        return likeListMode;
    }

    public void setLikeListMode(String likeListMode) {
        this.likeListMode = likeListMode;
    }

}