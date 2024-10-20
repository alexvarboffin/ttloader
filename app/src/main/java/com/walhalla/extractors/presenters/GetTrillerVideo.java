package com.walhalla.extractors.presenters;

import android.os.Handler;
import android.text.TextUtils;

import com.walhalla.ttvloader.TTResponse;
import com.walhalla.ui.DLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetTrillerVideo extends AbstractInfoExtractor {
    public static final Pattern VALID_URL = Pattern.compile(
            "https?://(?:www\\.)?triller\\.co/@(?<username>[\\w.]+)/video/(?<id>[\\da-f]{8}-(?:[\\da-f]{4}-){3}[\\da-f]{12})");

    public static final String API_BASE_URL = "https://social.triller.co/v1.5";
    public static final String API_HEADERS = "https://triller.co";
    private static final String KEY_USERS = "users";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_AVATAR_URL = "avatar_url";
    private static final String KEY_VIDEOS_PREVIEW_URL = "preview_url";
    private static final String KEY_VIDEOS = "videos";
    private static final String KEY_VIDEO_URL = "video_url";

    private final boolean r;


    private Exception mError;


    public GetTrillerVideo(boolean removeWatermark, RepositoryCallback callback,
                           VideoRepository repository, Handler handler) {
        super(callback, repository, handler);
        this.r = removeWatermark;
    }


    public void execute(final String url) {

        executor.execute(() -> {
            String tmp = null;
            try {
                Request r0 = new Request.Builder()
                        .url(url)
                        .build();
                OkHttpClient client = defClient();
                Response response0 = client.newCall(r0).execute();
                String redirectUrl = response0.request().url().toString();

                Matcher matcher = VALID_URL.matcher(redirectUrl);
                if (matcher.find()) {
                    String[] parts = redirectUrl.split("/video/");
                    String videoId = redirectUrl;
                    if (parts.length > 1) {
                        videoId = parts[1];
                    }

                    String url9 = API_BASE_URL + "/api/videos/" + videoId;
                    Request request = new Request.Builder()
                            .url(url9)
                            .header("Authorization", API_HEADERS)
                            .header("Content-Type", "application/json")
                            .header("Origin", API_HEADERS)
                            //.header("Referer", API_HEADERS)
                            //.headers(Headers.of(headers0()))
                            .build();
                    Response response1 = client.newCall(request).execute();
                    ResponseBody body = response1.body();
                    if (body != null) {
                        tmp = response1.body().string();
                    }
                } else {
                    DLog.d("Invalid URL");
                }
            } catch (IOException e) {
                DLog.handleException(e);
                mError = e;
            }
            TTResponse response = new TTResponse();
            if (tmp != null) {
                try {
                    JSONObject root = new JSONObject(tmp);
                    if (root.has(KEY_USERS)) {
                        JSONObject jsonObject = root.getJSONObject(KEY_USERS);
                        Iterator<String> keys = jsonObject.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            if (jsonObject.get(key) instanceof JSONObject) {
                                JSONObject userObject = jsonObject.getJSONObject(key);

                                // Извлекаем нужные данные
                                int userId = userObject.getInt("user_id");
                                String profileType = userObject.getString("profile_type");
                                String username = userObject.getString("username");

                                boolean isPrivate = userObject.getBoolean("private");
                                boolean isVerifiedUser = userObject.getBoolean("verified_user");

                                if (userObject.has(KEY_USER_NAME)) {
                                    response.title = userObject.getString(KEY_USER_NAME);
                                    response.username = userObject.getString(KEY_USER_NAME);
                                }
//                        if (user.has(KEY_USER_AVATAR_URL)) {
//                            response.thumb = user.getString(KEY_USER_AVATAR_URL);
//                        }
                                // Выводим данные
                                DLog.d("User ID: " + userId);
                                DLog.d("Profile Type: " + profileType);
                                DLog.d("Username: " + username);

                                DLog.d("Private: " + isPrivate);
                                DLog.d("Verified User: " + isVerifiedUser);
                            }

                        }
                    }
                    if (root.has(KEY_VIDEOS)) {
                        JSONArray data = root.getJSONArray(KEY_VIDEOS);
                        JSONObject video = data.getJSONObject(0);
                        if (video.has(KEY_VIDEOS_PREVIEW_URL)) {
                            response.thumb = video.getString(KEY_VIDEOS_PREVIEW_URL);
                        }
                        if (video.has(KEY_VIDEO_URL)) {
                            response.contentURL = video.getString(KEY_VIDEO_URL);
                            response.cleanVideo = video.getString(KEY_VIDEO_URL);
                        }
                    }
//                response.videoKey = TUtil.getKey(response.contentURL);

                } catch (NullPointerException e) {
                    DLog.handleException(e);
                    mError = e;
                } catch (JSONException e) {
                    DLog.handleException(e);
                    mError = e;
                }
                // new DownloadTikTokVideo().execute(URL);

            }
            onPostExecute(response);
        });
    }

    protected void onPostExecute(TTResponse response) {
        mThread.post(() -> {
            repository.onPostExecute0();
            if (mError != null) {
                if (callback != null) {
                    callback.errorResult(VideoRepository.ERROR_WENT_WRONG);
                }
            }
            String target = (r) ? response.cleanVideo : response.contentURL;
            if (!TextUtils.isEmpty(target)) {
                if (callback != null) {
                    repository.downloadLikeeVideo(target, response.title);
                }
                if (callback != null) {
                    callback.successResult(response);
                }
            }
        });
    }

//    public static int m13654b(String str) {
//        try {
//            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
//            StringBuilder sb = new StringBuilder(digest.length * 2);
//            for (byte b : digest) {
//                int b2 = b & 255;
//                if (b2 < 16) {
//                    sb.append("0");
//                }
//                sb.append(Integer.toHexString(b2));
//            }
//            return sb.toString().hashCode();
//        } catch (UnsupportedEncodingException | NoSuchAlgorithmException unused) {
//            return new Random(12223).nextInt();
//        }
//    }

}
