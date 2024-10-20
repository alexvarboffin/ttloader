package com.walhalla.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinterestExtractor implements TTExtractor {


    @Override
    public boolean isUrlValid(String url) {
        return url.contains("https://pin.it");
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