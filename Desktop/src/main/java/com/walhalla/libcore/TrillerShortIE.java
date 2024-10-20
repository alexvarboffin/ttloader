package com.walhalla.libcore;

import static com.walhalla.libcore.TrillerBaseIE.API_BASE_URL;
import static com.walhalla.libcore.TrillerBaseIE.API_HEADERS;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrillerShortIE {

    /**
     * https://v.triller.co/WWZNWk --> https://triller.co/@statefairent/video/f4480e1f-fb4e-45b9-a44c-9e6c679ce7eb
     */
    private static final String VALID_URL_REGEX = "https?://v\\.triller\\.co/(?<id>\\w+)";
    public static final Pattern VALID_URL_PATTERN = Pattern.compile(VALID_URL_REGEX);


    public void extract(String url) throws IOException {

//        Matcher matcher = VALID_URL_PATTERN.matcher(url);
//        if (matcher.find()) {
//            String videoId = matcher.group("id");
//            JsonObject videoInfo = getVideoInfo(videoId);
//            // Дальнейшая обработка videoInfo
//            System.out.println(videoInfo.toString());
//        } else {
//            System.out.println("Invalid URL");
//        }


//        Matcher matcher = VALID_URL.matcher(url);
//        if (!matcher.matches()) {
//            throw new IllegalArgumentException("Invalid URL");
//        }
//        String username = matcher.group("username");
//        String displayId = matcher.group("id");

//        String videoUrl = API_BASE_URL + "/api/videos/" + displayId;
//        JSONObject videoInfo = downloadJson(videoUrl, displayId, API_HEADERS);
//        JSONArray videos = videoInfo.optJSONArray("videos");
//        if (videos == null || videos.length() == 0) {
//            throw new IllegalArgumentException("No video information found");
//        }
//        JSONObject video = videos.getJSONObject(0);
//
//        JSONObject result = parseVideoInfo(video, username, null, displayId);
//        System.out.println(result.toString());
    }

    private Map<String, String> headers0() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", API_HEADERS);
        headers.put("Content-Type", "application/json");
        headers.put("Origin", API_HEADERS);
        return headers;
    }
}