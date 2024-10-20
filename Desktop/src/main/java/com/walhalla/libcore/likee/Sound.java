package com.walhalla.libcore.likee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Sound {

    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("ownerName")
    @Expose
    private String ownerName;
    @SerializedName("soundId")
    @Expose
    private String soundId;
    @SerializedName("soundName")
    @Expose
    private String soundName;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSoundId() {
        return soundId;
    }

    public void setSoundId(String soundId) {
        this.soundId = soundId;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

}
