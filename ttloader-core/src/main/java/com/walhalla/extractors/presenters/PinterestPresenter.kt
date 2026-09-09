package com.walhalla.extractors.presenters

import android.os.Handler
import android.text.TextUtils
import com.walhalla.libcore.TxTUtil
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ui.DLog
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class PinterestPresenter : AbstractInfoExtractor {
    private var mError: Exception? = null
    constructor(callback: RepositoryCallback, repository: VideoRepository, handler: Handler) : super(callback, repository, handler) {
    }
    override fun execute(url: String) {
        mError = null
        executor.execute({
                var response: TTResponse = TTResponse()
                try {
                    var doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(VideoRepository.TIMEOUT).`get`()
                    try {
                        response.title = doc.title()
                        var resp: String = doc.toString()
                        //DLog.d("@" + doc.toString());username
                        var V0START: String = "\"contentUrl\":\""
                        var V0STARTIMG: String = "\"imageSpec_orig\":{\"url\":\""
                        var videoFileUrl: String = ""
                        var imgFileUrl: String = ""
                        var fileUrl: String = ""
                        var tmp = extractFullName(resp)
                        if (tmp != null && resp.contains(tmp)) {
                            response.username = tmp
                        }
                        if (resp.contains(V0START)) {
                            videoFileUrl = TxTUtil.eE(resp, V0START, "\"") ?: ""
                            response.ext = ".mp4"
                            fileUrl = videoFileUrl
                        }
                        if (resp.contains(V0STARTIMG)) {
                            imgFileUrl = TxTUtil.eE(resp, V0STARTIMG, "\"") ?: ""
                            response.thumb = imgFileUrl
                        }
                        if (TextUtils.isEmpty(videoFileUrl) && TextUtils.isEmpty(imgFileUrl)) {
                            response.ext = VideoRepository.EXT_JPG
                            fileUrl = imgFileUrl
                        }
                        response.cleanVideo = fileUrl
                        response.contentURL = fileUrl
                        DLog.d("@ " + fileUrl + " @ " + url)
                    } catch (e: Exception) {
                        //iUtils.ShowToast(Mcontext, fileUrl);
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
                        repository.downloadPinterestFile(target, response.title!!, response.ext!!)
                    }
                    if (callback != null) {
                        callback.successResult(response)
                    }
                }
                })
    }
    companion object {
        @JvmStatic fun extractFullName(text: String): String? {
            var regex: String = "\"fullName\":\\s*\"([^\"]+)\""
            var pattern: Pattern = Pattern.compile(regex)
            var matcher: Matcher = pattern.matcher(text)
            if (matcher.find()) {
                var username: String = matcher.group(1)
                if (TextUtils.isEmpty(username)) {
                    return username
                }
            }
            return null
        }
    }
}
