package com.walhalla.extractors;

import java.util.regex.Pattern;

public class TrillerShortIE {

    /**
     * https://v.triller.co/WWZNWk --> https://triller.co/@statefairent/video/f4480e1f-fb4e-45b9-a44c-9e6c679ce7eb
     */
    private static final String VALID_URL_REGEX = "https?://v\\.triller\\.co/(?<id>\\w+)";
    public static final Pattern VALID_URL_PATTERN = Pattern.compile(VALID_URL_REGEX);
}
