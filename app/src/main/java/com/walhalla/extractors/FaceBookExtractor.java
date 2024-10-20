package com.walhalla.extractors;

import com.walhalla.ttvloader.common.NetworkType;

public class FaceBookExtractor implements TTExtractor{

    //https://www.facebook.com/share/r/KPxYSmjqrziudsgq/
    //https://www.facebook.com/share/r/KPxYSmjqrziudsgq/ ok
    //https://www.facebook.com/share/v/Q2bLnDsU6uH2ouFF/
    @Override
    public boolean isUrlValid(String url) {
        return url.contains(NetworkType.FACEBOOK.getValue());
    }

    @Override
    public String getClearUrl(String url) {
        if (url.contains("https:\\\\")) {
            url = url.replace("\\", "/");
        }
        return url;
    }
}
