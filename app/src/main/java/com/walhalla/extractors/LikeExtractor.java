package com.walhalla.extractors;

public class LikeExtractor implements TTExtractor {

    @Override
    public boolean isUrlValid(String url) {
        return (((url.contains("likee") && url.contains("video")) || url.contains("likeevideo"))
                || url.contains("l.likee.video")) || url.contains("mobile.like-video");
    }

    @Override
    public String getClearUrl(String url) {
        if (url.contains("l.likee.video/v/")) {
            int indexOf = url.indexOf("l.likee.video/v/");
            url = ("https://" + url.substring(indexOf)).trim();
            url = url.split("\n")[0];
            url = url.split(" ")[0];
        }
        return url;
    }
}
