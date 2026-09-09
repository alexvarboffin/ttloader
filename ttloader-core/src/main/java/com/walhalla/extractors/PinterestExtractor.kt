package com.walhalla.extractors

import java.util.regex.Matcher
import java.util.regex.Pattern

class PinterestExtractor : TTExtractor {
    override fun isUrlValid(url: String): Boolean {
        return url.contains("https://pin.it")
    }
    override fun getClearUrl(text: String): String {
        var url: String = ""
        var regex: String = "\\bhttps?://\\S+\\b"
        var pattern: Pattern = Pattern.compile(regex)
        var matcher: Matcher = pattern.matcher(text)
        while (matcher.find()) {
            url = matcher.group()
            return url
        }
        return url
    }
}
