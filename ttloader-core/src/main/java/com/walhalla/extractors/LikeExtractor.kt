package com.walhalla.extractors

class LikeExtractor : TTExtractor {
    override fun isUrlValid(url: String): Boolean {
        return url.contains("likee") && url.contains("video") || url.contains("likeevideo") || url.contains("l.likee.video") || url.contains("mobile.like-video")
    }
    override fun getClearUrl(url: String): String {
        var clear = url
        if (clear.contains("l.likee.video/v/")) {
            var indexOf: Int = clear.indexOf("l.likee.video/v/")
            clear = "https://" + clear.substring(indexOf)
            clear = clear.split("\n")[0]
            clear = clear.split(" ")[0]
        }
        return clear
    }
}
