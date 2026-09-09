package com.walhalla.extractors

import com.walhalla.extractors.presenters.InstaExtractor
import java.util.ArrayList
import java.util.List

class ExUtils {
    companion object {
        @JvmStatic fun defExtractors(): MutableList<TTExtractor> {
            var extractors: MutableList<TTExtractor> = ArrayList()
            extractors.add(PinterestExtractor())
            extractors.add(LikeExtractor())
            extractors.add(FaceBookExtractor())
            extractors.add(TrillerExtractor())
            extractors.add(InstaExtractor())
            return extractors
        }
    }
}
