package com.walhalla.extractors;

import com.walhalla.ui.DLog;

import java.util.regex.Matcher;

public class TrillerExtractor implements TTExtractor {

    @Override
    public boolean isUrlValid(String url) {
        Matcher matcher = TrillerShortIE.VALID_URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    @Override
    public String getClearUrl(String text) {
        return text;
    }
}
