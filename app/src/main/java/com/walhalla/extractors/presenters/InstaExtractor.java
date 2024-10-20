package com.walhalla.extractors.presenters;

import com.walhalla.extractors.TTExtractor;
import com.walhalla.ttvloader.common.NetworkType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstaExtractor implements TTExtractor {

    @Override
    public boolean isUrlValid(String url) {
        return url.contains(NetworkType.INSTAGRAM.getValue());
    }

    @Override
    public String getClearUrl(String text) {
        String url = "";
        String regex = "\\bhttps?://\\S+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            url = matcher.group();
            return url;
        }
        return url;
    }
}
