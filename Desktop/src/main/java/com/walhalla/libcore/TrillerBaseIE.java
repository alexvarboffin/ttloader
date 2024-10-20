package com.walhalla.libcore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrillerBaseIE {

    public static final String API_BASE_URL = "https://social.triller.co/v1.5";
    public static final String API_HEADERS = "https://triller.co";


    public static final Pattern VALID_URL = Pattern.compile(
            "https?://(?:www\\.)?triller\\.co/@(?<username>[\\w.]+)/video/(?<id>[\\da-f]{8}-(?:[\\da-f]{4}-){3}[\\da-f]{12})");
    protected static OkHttpClient defClient() {
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }


    private JsonParser jsonParser;
    public static final OkHttpClient httpClient = new OkHttpClient();

    public TrillerBaseIE() {
        jsonParser = new JsonParser();
    }

//    public void performLogin(String username, String password) throws IOException, ExtractorError {
//        if (API_HEADERS.contains("Authorization")) {
//            return;
//        }
//
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        headers.put("Origin", API_HEADERS);
//
//        JsonObject userCheckResponse = downloadJson(API_BASE_URL + "/api/user/is-valid-username", headers,
//                "{\"username\": \"" + username + "\"}");
//        boolean userExists = userCheckResponse.get("status").getAsBoolean();
//        if (userExists) {
//            throw new ExtractorError("Unable to login: Invalid username", true);
//        }
//
//        JsonObject loginResponse = downloadJson(API_BASE_URL + "/user/auth", headers,
//                "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}");
//        String authToken = loginResponse.get("auth_token").getAsString();
//        if (authToken == null || authToken.isEmpty()) {
//            int errorCode = loginResponse.get("error").getAsInt();
//            if (errorCode == 1008) {
//                throw new ExtractorError("Unable to login: Incorrect password", true);
//            } else {
//                throw new ExtractorError("Unable to login");
//            }
//        }
//
//        API_HEADERS = "Bearer " + authToken;
//    }


    private JsonObject downloadJson(String url, Map<String, String> headers, String requestBody) throws IOException {
        RequestBody body = null;
        if (requestBody != null) {
            body = FormBody.create(requestBody, okhttp3.MediaType.parse("application/json"));
        }
        Request request;
        if (body != null) {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(Headers.of(headers))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    //.post(body)
                    .headers(Headers.of(headers))
                    .build();
        }
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return jsonParser.parse(response.body().string()).getAsJsonObject();
        }
    }

    public JsonObject getComments(String videoId, int limit) throws IOException {
        String url = API_BASE_URL + "/api/videos/" + videoId + "/comments_v2?limit=" + limit;
        return downloadJson(url, headers0(), null);
    }

    private Map<String, String> headers0() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", API_HEADERS);
        headers.put("Content-Type", "application/json");
        headers.put("Origin", API_HEADERS);
        return headers;
    }


    public static void main(String[] args) {
        System.out.println("@@@");
        try {
            String url = "https://v.triller.co/AXdD6x";
            Matcher matcher = TrillerShortIE.VALID_URL_PATTERN.matcher(url);
            if (matcher.find()) {
                String videoId = matcher.group("id");
                System.out.println(videoId);

                try {

                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    OkHttpClient client = defClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);

                            String redirectUrl = response.request().url().toString();

                            Matcher matcher = TrillerBaseIE.VALID_URL.matcher(redirectUrl);
                            if (matcher.find()) {
                                String videoId = matcher.group("id");
                                System.out.println("Redirect URL: " + redirectUrl + " " + videoId);
                                JSONObject infoDict = getVideoInfo(videoId);
                                System.out.println(infoDict.toString(4));
                            } else {
                                System.out.println("Invalid URL");
                            }
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Invalid URL");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject getVideoInfo(String id) throws IOException {

        //JsonObject response = downloadJson(url, headers0(), null);

        String url = API_BASE_URL + "/api/videos/" + id;
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", API_HEADERS)
                .header("Content-Type", "application/json")
                .header("Origin", API_HEADERS)
                //.header("Referer", API_HEADERS)
                //.headers(Headers.of(headers0()))
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            String body = response.body().string();
            if (!response.isSuccessful()) {

                throw new IOException("Unexpected code " + response);
            }
            //System.out.println("@@@" + body);
            JSONObject data = new JSONObject(body).getJSONObject("data");
            JSONObject infoDict = new JSONObject();
            infoDict.put("id", data.getString("id"));
            infoDict.put("ext", "mp4");
            infoDict.put("title", "md5:2dfc89d154cd91a4a18cd9582ba03e16");
            infoDict.put("display_id", data.getString("videoId"));
            infoDict.put("thumbnail", data.getJSONArray("thumbnails").getJSONObject(0).getString("url"));
            infoDict.put("description", "md5:2dfc89d154cd91a4a18cd9582ba03e16");
            infoDict.put("uploader", data.getJSONObject("author").getString("username"));
            infoDict.put("uploader_id", data.getJSONObject("author").getString("id"));
            infoDict.put("creator", data.getJSONObject("author").getString("name"));
            infoDict.put("timestamp", data.getLong("createdAt"));
            infoDict.put("upload_date", "20210822");
            infoDict.put("duration", data.getInt("duration"));
            infoDict.put("view_count", JSONObject.NULL);
            infoDict.put("like_count", JSONObject.NULL);
            infoDict.put("artist", "Unknown");
            infoDict.put("track", "Unknown");
            infoDict.put("uploader_url", "https://triller.co/@" + data.getJSONObject("author").getString("username"));
            infoDict.put("comment_count", JSONObject.NULL);

            return infoDict;
        }
    }


    public Map<String, Object> parseVideoInfo(JsonObject videoInfo, String username, String userId, String displayId) {
        String videoId = videoInfo.get("id").getAsString();
        List<Map<String, Object>> formats = new ArrayList<>();
        JsonObject videoSet = videoInfo.getAsJsonObject("video_set");
        if (videoSet != null) {
            for (Map.Entry<String, JsonElement> entry : videoSet.entrySet()) {
                JsonObject video = (JsonObject) entry.getValue();
                String videoUrl = video.get("url").getAsString();
                String formatId = videoUrl.substring(videoUrl.lastIndexOf('/') + 1, videoUrl.lastIndexOf('.'));
                String ext = videoUrl.substring(videoUrl.lastIndexOf('.') + 1);
                Map<String, Object> format = new HashMap<>();
                format.put("url", videoUrl);
                format.put("ext", ext);
                format.put("format_id", formatId);
                // Parse resolution, vcodec, vbr, and other properties here
                formats.add(format);
            }
        }

        // Other format parsing logic...

        String videoUrl = videoInfo.get("video_url").getAsString();
        String ext = videoUrl.substring(videoUrl.lastIndexOf('.') + 1);
        String formatId = videoUrl.substring(videoUrl.lastIndexOf('/') + 1, videoUrl.lastIndexOf('.'));
        Map<String, Object> format = new HashMap<>();
        format.put("url", videoUrl);
        format.put("ext", ext);
        format.put("format_id", formatId);
        // Parse resolution, vcodec, vbr, and other properties here
        formats.add(format);

        // Other format parsing logic...

        Map<String, Object> parsedInfo = new HashMap<>();
        parsedInfo.put("id", videoId);
        parsedInfo.put("display_id", displayId);
        parsedInfo.put("uploader", username);
        parsedInfo.put("uploader_id", userId);
        parsedInfo.put("webpage_url", "https://triller.co/@" + username + "/video/" + displayId);
        parsedInfo.put("uploader_url", "https://triller.co/@" + username);
        parsedInfo.put("extractor_key", "TrillerIE");
        parsedInfo.put("extractor", "TrillerIE");
        parsedInfo.put("formats", formats);

        // Other fields parsing logic...

        return parsedInfo;
    }
}

