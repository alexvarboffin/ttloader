package com.walhalla.extractors;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface TTExtractor {
    boolean isUrlValid(String url);

    String getClearUrl(String text);

}
