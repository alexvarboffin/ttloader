package com.walhalla.extractors

import com.walhalla.ui.DLog
import java.util.regex.Matcher

class TrillerExtractor : TTExtractor {
    override fun isUrlValid(url: String): Boolean {
        var matcher: Matcher = TrillerShortIE.VALID_URL_PATTERN.matcher(url)
        if (matcher.find()) {
            return true
        }
        return false
    }
    override fun getClearUrl(text: String): String {
        return text
    }
}
