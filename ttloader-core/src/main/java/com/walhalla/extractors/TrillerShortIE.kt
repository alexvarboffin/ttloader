package com.walhalla.extractors

import java.util.regex.Pattern

class TrillerShortIE {
    companion object {
        /**
             * https://v.triller.co/WWZNWk --> https://triller.co/@statefairent/video/f4480e1f-fb4e-45b9-a44c-9e6c679ce7eb
             */
        private const val VALID_URL_REGEX = "https?://v\\.triller\\.co/(?<id>\\w+)"
        @JvmField val VALID_URL_PATTERN: Pattern = Pattern.compile(VALID_URL_REGEX)
    }
}
