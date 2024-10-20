package com.walhalla.extractors;

import com.walhalla.extractors.presenters.InstaExtractor;

import java.util.ArrayList;
import java.util.List;

public class ExUtils {


    public static List<TTExtractor> defExtractors() {
        List<TTExtractor> extractors = new ArrayList<>();
        extractors.add(new PinterestExtractor());
        extractors.add(new LikeExtractor());
        extractors.add(new FaceBookExtractor());
        extractors.add(new TrillerExtractor());
        extractors.add(new InstaExtractor());
        return extractors;
    }
}
