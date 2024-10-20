package com.retrytech.medialoot.model;

public class VimeoModel {
    String videoUrl;
    String duration;
    String ext;
    String format;
    String id;
    String size;
    String thumnail;
    String title;
    String videourl;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getThumnail() {
        return this.thumnail;
    }

    public void setThumnail(String str) {
        this.thumnail = str;
    }

    public String getDownurl() {
        return this.videoUrl;
    }

    public void setDownurl(String str) {
        this.videoUrl = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String str) {
        this.ext = str;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String str) {
        this.format = str;
    }

    public String getVideourl() {
        return this.videourl;
    }

    public void setVideourl(String str) {
        this.videourl = str;
    }
}
