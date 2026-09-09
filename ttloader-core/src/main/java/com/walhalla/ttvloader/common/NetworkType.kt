package com.walhalla.ttvloader.common

enum class NetworkType(private val value: String) {
    INSTAGRAM("instagram.com"),
    FACEBOOK("facebook.com"),
    TIKTOK__("tiktok.com");
    //TIKTOK__("com");
    //XH("xhamster" + ".com");
    //PHUB("pornhub" + ".com");

    fun getValue(): String {
        return value
    }
}
