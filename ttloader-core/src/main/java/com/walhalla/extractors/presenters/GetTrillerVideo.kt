package com.walhalla.extractors.presenters

import android.os.Handler
import android.text.TextUtils
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ui.DLog
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.Iterator
import java.util.regex.Matcher
import java.util.regex.Pattern
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

class GetTrillerVideo : AbstractInfoExtractor {
    private var r: Boolean = false
    private var mError: Exception? = null
    constructor(removeWatermark: Boolean, callback: RepositoryCallback, repository: VideoRepository, handler: Handler) : super(callback, repository, handler) {
        this.r = removeWatermark
    }
    override fun execute(url: String) {
        executor.execute({
                var tmp: String? = null
                try {
                    var r0: Request = Request.Builder().url(url).build()
                    var client: OkHttpClient = defClient()
                    var response0: Response = client.newCall(r0).execute()
                    var redirectUrl: String = response0.request.url.toString()
                    var matcher: Matcher = VALID_URL.matcher(redirectUrl)
                    if (matcher.find()) {
                        var parts: Array<String> = redirectUrl.split("/video/").toTypedArray()
                        var videoId: String = redirectUrl
                        if (parts.size > 1) {
                            videoId = parts[1]
                        }
                        var url9: String = API_BASE_URL + "/api/videos/" + videoId
                        var request: Request = Request.Builder().url(url9).header("Authorization", API_HEADERS).header("Content-Type", "application/json").header("Origin", API_HEADERS).build()
                        //.header("Referer", API_HEADERS)
                        //.headers(Headers.of(headers0()))
                        var response1: Response = client.newCall(request).execute()
                        var body: ResponseBody = response1.body!!
                        if (body != null) {
                            tmp = response1.body!!.string()
                        }
                    } else {
                        DLog.d("Invalid URL")
                    }
                } catch (e: Exception) {
                    DLog.handleException(e)
                    mError = e
                }
                var response: TTResponse = TTResponse()
                if (tmp != null) {
                    try {
                        var root: JSONObject = JSONObject(tmp)
                        if (root.has(KEY_USERS)) {
                            var jsonObject: JSONObject = root.getJSONObject(KEY_USERS)
                            var keys = jsonObject.keys()
                            while (keys.hasNext()) {
                                var key: String = keys.next()
                                if (jsonObject.`get`(key) is JSONObject) {
                                    var userObject: JSONObject = jsonObject.getJSONObject(key)
                                    // Извлекаем нужные данные
                                    var userId: Int = userObject.getInt("user_id")
                                    var profileType: String = userObject.getString("profile_type")
                                    var username: String = userObject.getString("username")
                                    var isPrivate: Boolean = userObject.getBoolean("private")
                                    var isVerifiedUser: Boolean = userObject.getBoolean("verified_user")
                                    if (userObject.has(KEY_USER_NAME)) {
                                        response.title = userObject.getString(KEY_USER_NAME)
                                        response.username = userObject.getString(KEY_USER_NAME)
                                    }
                                    //                        if (user.has(KEY_USER_AVATAR_URL)) {
                                    //                            response.thumb = user.getString(KEY_USER_AVATAR_URL);
                                    //                        }
                                    // Выводим данные
                                    DLog.d("User ID: " + userId)
                                    DLog.d("Profile Type: " + profileType)
                                    DLog.d("Username: " + username)
                                    DLog.d("Private: " + isPrivate)
                                    DLog.d("Verified User: " + isVerifiedUser)
                                }
                            }
                        }
                        if (root.has(KEY_VIDEOS)) {
                            var data: JSONArray = root.getJSONArray(KEY_VIDEOS)
                            var video: JSONObject = data.getJSONObject(0)
                            if (video.has(KEY_VIDEOS_PREVIEW_URL)) {
                                response.thumb = video.getString(KEY_VIDEOS_PREVIEW_URL)
                            }
                            if (video.has(KEY_VIDEO_URL)) {
                                response.contentURL = video.getString(KEY_VIDEO_URL)
                                response.cleanVideo = video.getString(KEY_VIDEO_URL)
                            }
                        }
                    } catch (e: Exception) {
                        //                response.videoKey = TUtil.getKey(response.contentURL!!);
                        DLog.handleException(e)
                        mError = e
                    } catch (e: Exception) {
                        DLog.handleException(e)
                        mError = e
                    }
                }
                // new DownloadTikTokVideo().execute(URL);
                onPostExecute(response)
                })
    }
    protected fun onPostExecute(response: TTResponse) {
        mThread.post({
                repository.onPostExecute0()
                if (mError!! != null) {
                    if (callback != null) {
                        callback.errorResult(VideoRepository.ERROR_WENT_WRONG)
                    }
                }
                var target: String = if (r) response.cleanVideo!! else response.contentURL!!
                if (TextUtils.isEmpty(target)) {
                    if (callback != null) {
                        repository.downloadLikeeVideo(target, response.title!!)
                    }
                    if (callback != null) {
                        callback.successResult(response)
                    }
                }
                })
    }
    companion object {
        @JvmField val VALID_URL: Pattern = Pattern.compile("https?://(?:www\\.)?triller\\.co/@(?<username>[\\w.]+)/video/(?<id>[\\da-f]{8}-(?:[\\da-f]{4}-){3}[\\da-f]{12})")
        const val API_BASE_URL = "https://social.triller.co/v1.5"
        const val API_HEADERS = "https://triller.co"
        private const val KEY_USERS = "users"
        private const val KEY_USER_NAME = "name"
        private const val KEY_USER_AVATAR_URL = "avatar_url"
        private const val KEY_VIDEOS_PREVIEW_URL = "preview_url"
        private const val KEY_VIDEOS = "videos"
        private const val KEY_VIDEO_URL = "video_url"
    }
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
