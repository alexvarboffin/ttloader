package com.walhalla.extractors

import com.walhalla.ttvloader.common.NetworkType

class FaceBookExtractor : TTExtractor {
    //https://www.facebook.com/share/r/KPxYSmjqrziudsgq/
    //https://www.facebook.com/share/r/KPxYSmjqrziudsgq/ ok
    //https://www.facebook.com/share/v/Q2bLnDsU6uH2ouFF/
    override fun isUrlValid(url: String): Boolean {
        return url.contains(NetworkType.FACEBOOK.getValue())
    }
    override fun getClearUrl(url: String): String {
        var clear = url
        if (clear.contains("https:\\\\")) {
            clear = clear.replace("\\", "/")
        }
        return clear
    }
}
