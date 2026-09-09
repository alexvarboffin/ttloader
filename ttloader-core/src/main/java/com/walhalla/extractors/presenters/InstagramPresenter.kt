package com.walhalla.extractors.presenters

import android.os.Handler
import android.text.TextUtils
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ttvloader.receiver.DownloadFile
import com.walhalla.ui.DLog
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

class InstagramPresenter : AbstractInfoExtractor {
    private var mError: Exception? = null
    var acceptLanguage: String = "en-US"
    constructor(callback: RepositoryCallback, repository: VideoRepository, handler: Handler) : super(callback, repository, handler) {
    }
    override fun execute(url: String) {
        mError = null
        executor.execute({
                var response: TTResponse = TTResponse()
                try {
                    var doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36").timeout(VideoRepository.TIMEOUT).`get`()
                    //                        .header("authority", "www.facebook.com")
                    //                        .header("Accept-Language", acceptLanguage).followRedirects(true)
                    try {
                        var data: String = doc.toString()
                        var v0: Element = doc.select("meta[property=\"og:video\"]").last()!!
                        var URL: String = ""
                        if (v0 != null) {
                            DLog.d("@" + doc.toString())
                            URL = v0.attr("content")
                        } else {
                            var start_hd: String = "browser_native_hd_url\":\""
                            if (data.contains(start_hd)) {
                                URL = FacebookPresenter.getHDLink(data) ?: ""
                            }
                            ////TxTUtil.onlyExtractFacebook(data, start_hd, "\",");
                            if (VideoRepository.isEmpty(URL)) {
                                URL = FacebookPresenter.getSDLink(data) ?: ""
                            }
                        }
                        if (TextUtils.isEmpty(URL)) {
                            response.cleanVideo = URL
                            response.contentURL = URL
                        } else {
                            // Поиск скрипта, содержащего JSON данные
                            var scriptTags: Elements = doc.getElementsByTag("script")
                            for (scriptTag in scriptTags) {
                                var scriptContent: String = scriptTag.html()
                                DLog.d("" + scriptContent)
                                if (scriptContent.contains("window._sharedData")) {
                                    var parts = scriptContent.split(" = ", limit = 2)
                                    var jsonData = if (parts.size > 1) parts[1].split(";</script>", limit = 2)[0] else ""
                                    // Парсинг JSON (вы можете использовать библиотеку, такую как org.json или Gson)
                                    // Пример с org.json
                                    try {
                                        var jsonObject: JSONObject = JSONObject(jsonData)
                                        var postData: JSONObject = jsonObject.getJSONObject("entry_data").getJSONArray("PostPage").getJSONObject(0).getJSONObject("graphql").getJSONObject("shortcode_media")
                                        URL = postData.getString("video_url")
                                        var description: String = postData.getJSONObject("edge_media_to_caption").getJSONArray("edges").getJSONObject(0).getJSONObject("node").getString("text")
                                        var username: String = postData.getJSONObject("owner").getString("username")
                                        var timestamp: Long = postData.getLong("taken_at_timestamp")
                                        response.username = username
                                        response.description = description
                                        response.timestamp = timestamp
                                    } catch (e: Exception) {
                                        throw RuntimeException(e)
                                    }
                                    break
                                }
                            }
                        }
                        DLog.d("@" + response)
                        if (TextUtils.isEmpty(URL)) {
                            response.cleanVideo = URL
                            response.contentURL = URL
                        }
                        response.title = doc.title()
                        response.ext = EXT_MP4
                    } catch (e: Exception) {
                        //iUtils.ShowToast(Mcontext, URL);
                        DLog.handleException(e)
                        mError = e
                    }
                } catch (e: Exception) {
                    DLog.handleException(e)
                    mError = e
                }
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
                var target: String = response.cleanVideo!!
                if (TextUtils.isEmpty(target)) {
                    if (callback != null) {
                        repository.downloadInstagramFile(target, response.title!!, response.ext!!)
                    }
                    if (callback != null) {
                        callback.successResult(response)
                    }
                }
                })
    }
}
