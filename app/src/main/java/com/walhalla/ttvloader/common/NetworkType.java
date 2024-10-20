package com.walhalla.ttvloader.common;

public enum NetworkType {

    INSTAGRAM("instagram.com"),
    FACEBOOK("facebook.com"),
    TIKTOK__("tiktok.com");
    //TIKTOK__("com");
    //XH("xhamster" + ".com");
    //PHUB("pornhub" + ".com");

    public String getValue() {
        return value;
    }

    private final String value;

    NetworkType(String s) {
        this.value = s;
    }
}
