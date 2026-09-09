package com.walhalla.extractors

import android.content.Context
import java.util.concurrent.Executor
import java.util.regex.Matcher
import java.util.regex.Pattern

interface TTExtractor {
    abstract fun isUrlValid(url: String): Boolean
    abstract fun getClearUrl(text: String): String
}
