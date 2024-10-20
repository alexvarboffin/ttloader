package com.walhalla.libcore.likee;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("otherValue")
    @Expose
    private OtherValue otherValue;
    @SerializedName("videoList")
    @Expose
    private List<Video> videoList;
    @SerializedName("privateAccount")
    @Expose
    private Boolean privateAccount;

    public OtherValue getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(OtherValue otherValue) {
        this.otherValue = otherValue;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public Boolean getPrivateAccount() {
        return privateAccount;
    }

    public void setPrivateAccount(Boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

}
