package com.walhalla.extractors.presenters

import com.walhalla.extractors.TTExtractor
import com.walhalla.ttvloader.common.NetworkType
import java.util.regex.Matcher
import java.util.regex.Pattern

class InstaExtractor : TTExtractor {
    override fun isUrlValid(url: String): Boolean {
        return url.contains(NetworkType.INSTAGRAM.getValue())
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
