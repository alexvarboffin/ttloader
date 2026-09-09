package com.walhalla.extractors.presenters

import android.os.Handler
import android.text.TextUtils
import com.walhalla.libcore.TxTUtil
import com.walhalla.ttloader.core.BuildConfig
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ui.DLog
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.util.HashMap
import java.util.Map
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

class GetLikeeVideo : AbstractInfoExtractor {
    private var r: Boolean = false
    private var mError: Exception? = null
    constructor(removeWatermark: Boolean, callback: RepositoryCallback, repository: VideoRepository, handler: Handler) : super(callback, repository, handler) {
        this.r = removeWatermark
    }
    override fun execute(url: String) {
        executor.execute({
                var response: TTResponse = TTResponse()
                var result: Document? = null
                try {
                    var headers: HashMap<String, String> = HashMap()
                    headers.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    headers.put("authority", "l.likee.video")
                    headers.put("upgrade-insecure-requests", "1")
                    //headers.put("user-agent", "Mozilla/5.0 (Linux; Android 8.0.0; Nexus 5X Build/OPR4.170623.006) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Mobile Safari/537.36");
                    headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    headers.put("sec-fetch-site", "none")
                    headers.put("sec-fetch-mode", "navigate")
                    headers.put("sec-fetch-dest", "document")
                    headers.put("dnt", "1")
                    headers.put("Content-type:", "application/json; charset=utf-8")
                    try {
                        var sc: SSLContext = SSLContext.getInstance("SSL")
                        sc.`init`(null, trustAllCerts, java.security.SecureRandom())
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
                    } catch (e: Exception) {
                        DLog.handleException(e)
                    }
                    var connection: Connection = Jsoup.connect(url).timeout(TIMEOUT).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").ignoreContentType(true).headers(headers).followRedirects(true)
                    result = connection.`get`()
                } catch (e: Exception) {
                    DLog.handleException(e)
                    mError = e
                }
                if (result != null) {
                    try {
                        var unescape: String = StringEscapeUtils.unescapeJava(result.title())
                        var regex: String = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]"
                        var aa: String = unescape.replace(regex, "")
                        response.title = aa
                    } catch (e: Exception) {
                        DLog.handleException(e)
                    }
                    if (BuildConfig.DEBUG) {
                        DLog.d(url)
                        DLog.d(result.toString())
                    }
                    var raw: String = ""
                    var aaa: Elements = result.select("script")
                    for (element in aaa) {
                        raw = element.toString()
                        if (raw.contains("video_url")) {
                            DLog.d("-->>" + raw)
                            break
                        }
                    }
                    try {
                        var rawUrl: String = raw.split("\"video_url\":\"")[1].split("\",")[0].replace("\\/", "/").trim()
                        // iUtils.ShowToast(Mcontext,rawUrl);
                        response.contentURL = rawUrl
                        response.thumb = TxTUtil.extract(raw, "image1")
                        response.username = TxTUtil.extract(raw, "nick_name")
                        //                response.videoKey = TUtil.getKey(response.contentURL!!);
                        if (TextUtils.isEmpty(rawUrl)) {
                            response.cleanVideo = rawUrl.replace("_4.mp4", ".mp4")
                        }
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
                        callback.errorResult(com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG)
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
