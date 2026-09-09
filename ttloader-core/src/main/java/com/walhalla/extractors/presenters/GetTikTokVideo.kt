package com.walhalla.extractors.presenters

import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import com.walhalla.intentresolver.utils.TextUtilz
import com.walhalla.ttloader.core.BuildConfig
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ui.DLog
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.URLEncoder
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class GetTikTokVideo : AbstractInfoExtractor {
    // X-RapidAPI-Key
    // X-RapidAPI-Host
    // tiktok-scraper7.p.rapidapi.com
    // https://%1s/?url=%2s&hd=1
    // ca5c6d6fa3mshfcd2b0a0feac6b7p140e57jsn72684628152a
    private var removeWatermark: Boolean = false
    private var errorExp: Exception? = null
    constructor(removeWatermark: Boolean, callback: RepositoryCallback, repository: VideoRepository, handler: Handler) : super(callback, repository, handler) {
        this.removeWatermark = removeWatermark
    }
    private fun getDownloadLocation(): File {
        return File(Environment.DIRECTORY_MOVIES)
    }
    override fun execute(url: String) {
        executor.execute({
                var response: TTResponse = TTResponse()
                response.contentURL = ""
                response.cleanVideo = ""
                try {
                    var encodedUrl: String = URLEncoder.encode(url, "UTF-8")
                    //https%3A%2F%2Fwww.tiktok.com%2F%40dzhigit450%2Fvideo%2F7353239977452473606%3F_r%3D1%26u_code%3D0%26preview_pb%3D0%26sharer_language%3Dru%26_d%3Dedjk5j5eml5jla%26share_item_id%3D7353239977452473606%26source%3Dh5_m%26timestamp%3D1714046971%26social_share_type%3D0%26utm_source%3Dcopy%26utm_campaign%3Dclient_share%26utm_medium%3Dandroid%26share_iid%3D7361048962482456325%26share_link_id%3D22a70433-e2ce-4a7a-91ad-941e0613089d%26share_app_id%3D1233%26ugbiz_name%3DMAIN%26ug_btm%3Db2001%26enable_checksum%3D1
                    //https://www.tiktok.com/@dzhigit450/video/7353239977452473606?_r=1&u_code=0&preview_pb=0&sharer_language=ru&_d=edjk5j5eml5jla&share_item_id=7353239977452473606&source=h5_m&timestamp=1714046971&social_share_type=0&utm_source=copy&utm_campaign=client_share&utm_medium=android&share_iid=7361048962482456325&share_link_id=22a70433-e2ce-4a7a-91ad-941e0613089d&share_app_id=1233&ugbiz_name=MAIN&ug_btm=b2001&enable_checksum=1"
                    var urlNew: String = String.format(TextUtilz.dec0(uHolder), TextUtilz.dec0(valueH0), encodedUrl)
                    var headers: Headers = Headers.Builder().add("user-agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36 Edg/87.0.664.66").add(TextUtilz.dec0(xkey), TextUtilz.dec0(xKVa)).add(TextUtilz.dec0(xhost), TextUtilz.dec0(valueH0)).build()
                    var request: Request = Request.Builder().url(urlNew).headers(headers).build()
                    var client: OkHttpClient = defClient()
                    var response1: Response = client.newCall(request).execute()
                    if (response1.isSuccessful) {
                        throw IOException("Unexpected code " + response1)
                    }
                    var tmp: String = response1.body!!.string()
                    if (BuildConfig.DEBUG) {
                        DLog.d(tmp)
                    }
                    var adv_promotable: String = ""
                    var ai_dynamic_cover: String = ""
                    var album: String = ""
                    var anchors: String = ""
                    var anchors_extras: String = ""
                    var auction_ad_invited: String = ""
                    var avatar: String = ""
                    var aweme_id: String = ""
                    var branded_content_type: String = ""
                    var collect_count: String = ""
                    var comment_count: String = ""
                    var commercial_video_info: String = ""
                    var cover: String = ""
                    var create_time: String = ""
                    var digg_count: String = ""
                    var download_count: String = ""
                    var duration: String = ""
                    var hd_size: String = ""
                    var hdplay: String = ""
                    var id: String = ""
                    var is_ad: String = ""
                    var item_comment_settings: String = ""
                    var msg: String = ""
                    var music: String = ""
                    var nickname: String = ""
                    var origin_cover: String = ""
                    var original: String = ""
                    var play: String = ""
                    var play_count: String = ""
                    var processed_time: String = ""
                    var region: String = ""
                    var share_count: String = ""
                    var size: String = ""
                    var unique_id: String = ""
                    var with_comment_filter_words: String = ""
                    var wm_size: String = ""
                    var wmplay: String = ""
                    var jsonObject: JSONObject = JSONObject(tmp)
                    //                String code = "";
                    //                if (jsonObject.has(KEY_CODE)) {
                    //                    code = jsonObject.getString(KEY_CODE);
                    //                }
                    //
                    //                if (jsonObject.has(KEY_MSG)) {
                    //                    msg = jsonObject.getString(KEY_MSG);
                    //                }
                    //
                    //                if (jsonObject.has(KEY_PROCESSED_TIME)) {
                    //                    processed_time = jsonObject.getString(KEY_PROCESSED_TIME);
                    //                }
                    //==============data================
                    if (jsonObject.has(KEY_DATA)) {
                        var data: JSONObject = jsonObject.getJSONObject(KEY_DATA)
                        if (data.has(KEY_AUTHOR)) {
                            var author: JSONObject = data.getJSONObject(KEY_AUTHOR)
                            if (author.has(KEY_ID)) {
                                id = author.getString(KEY_ID)
                            }
                            if (author.has(KEY_UNIQUE_ID)) {
                                unique_id = author.getString(KEY_UNIQUE_ID)
                            }
                            if (author.has(KEY_NICKNAME)) {
                                nickname = author.getString(KEY_NICKNAME)
                            }
                            if (author.has(KEY_AVATAR)) {
                                avatar = author.getString(KEY_AVATAR)
                            }
                        }
                        if (data.has(KEY_AWEME_ID)) {
                            aweme_id = data.getString(KEY_AWEME_ID)
                        }
                        if (data.has(KEY_COMMERCIAL_VIDEO_INFO)) {
                            commercial_video_info = data.getString(KEY_COMMERCIAL_VIDEO_INFO)
                        }
                        if (data.has(KEY_ITEM_COMMENT_SETTINGS)) {
                            item_comment_settings = data.getString(KEY_ITEM_COMMENT_SETTINGS)
                        }
                        if (data.has(KEY_ID)) {
                            id = data.getString(KEY_ID)
                        }
                        if (data.has(KEY_REGION)) {
                            region = data.getString(KEY_REGION)
                        }
                        if (data.has(KEY_TITLE)) {
                            var title: String = data.getString(KEY_TITLE)
                            response.title = title
                        }
                        if (data.has(KEY_COVER)) {
                            cover = data.getString(KEY_COVER)
                        }
                        if (data.has(KEY_AI_DYNAMIC_COVER)) {
                            ai_dynamic_cover = data.getString(KEY_AI_DYNAMIC_COVER)
                        }
                        if (data.has(KEY_ORIGIN_COVER)) {
                            origin_cover = data.getString(KEY_ORIGIN_COVER)
                        }
                        if (data.has(KEY_DURATION)) {
                            duration = data.getString(KEY_DURATION)
                        }
                        if (data.has(KEY_PLAY)) {
                            play = data.getString(KEY_PLAY)
                        }
                        if (data.has(KEY_WMPLAY)) {
                            wmplay = data.getString(KEY_WMPLAY)
                        }
                        if (data.has(KEY_HDPLAY)) {
                            hdplay = data.getString(KEY_HDPLAY)
                        }
                        if (data.has(KEY_SIZE)) {
                            size = data.getString(KEY_SIZE)
                        }
                        if (data.has(KEY_WM_SIZE)) {
                            wm_size = data.getString(KEY_WM_SIZE)
                        }
                        if (data.has(KEY_HD_SIZE)) {
                            hd_size = data.getString(KEY_HD_SIZE)
                        }
                        if (data.has(KEY_MUSIC)) {
                            music = data.getString(KEY_MUSIC)
                        }
                        if (data.has(KEY_PLAY_COUNT)) {
                            play_count = data.getString(KEY_PLAY_COUNT)
                        }
                        if (data.has(KEY_DIGG_COUNT)) {
                            digg_count = data.getString(KEY_DIGG_COUNT)
                        }
                        if (data.has(KEY_COMMENT_COUNT)) {
                            comment_count = data.getString(KEY_COMMENT_COUNT)
                        }
                        if (data.has(KEY_SHARE_COUNT)) {
                            share_count = data.getString(KEY_SHARE_COUNT)
                        }
                        if (data.has(KEY_DOWNLOAD_COUNT)) {
                            download_count = data.getString(KEY_DOWNLOAD_COUNT)
                        }
                        if (data.has(KEY_COLLECT_COUNT)) {
                            collect_count = data.getString(KEY_COLLECT_COUNT)
                        }
                        if (data.has(KEY_CREATE_TIME)) {
                            create_time = data.getString(KEY_CREATE_TIME)
                        }
                        if (data.has(KEY_ANCHORS)) {
                            anchors = data.getString(KEY_ANCHORS)
                        }
                        if (data.has(KEY_ANCHORS_EXTRAS)) {
                            anchors_extras = data.getString(KEY_ANCHORS_EXTRAS)
                        }
                        if (data.has(KEY_IS_AD)) {
                            is_ad = data.getString(KEY_IS_AD)
                        }
                        //==============commerce_info================
                        if (data.has(KEY_COMMERCE_INFO)) {
                            var commerce_info: JSONObject = data.getJSONObject(KEY_COMMERCE_INFO)
                            if (commerce_info.has(KEY_ADV_PROMOTABLE)) {
                                adv_promotable = commerce_info.getString(KEY_ADV_PROMOTABLE)
                            }
                            if (commerce_info.has(KEY_AUCTION_AD_INVITED)) {
                                auction_ad_invited = commerce_info.getString(KEY_AUCTION_AD_INVITED)
                            }
                            if (commerce_info.has(KEY_BRANDED_CONTENT_TYPE)) {
                                branded_content_type = commerce_info.getString(KEY_BRANDED_CONTENT_TYPE)
                            }
                            if (commerce_info.has(KEY_WITH_COMMENT_FILTER_WORDS)) {
                                with_comment_filter_words = commerce_info.getString(KEY_WITH_COMMENT_FILTER_WORDS)
                            }
                        }
                        var music_info_title: String = ""
                        if (data.has(KEY_MUSIC_INFO)) {
                            var music_info: JSONObject = data.getJSONObject(KEY_MUSIC_INFO)
                            if (music_info.has(KEY_ID)) {
                                id = music_info.getString(KEY_ID)
                            }
                            if (music_info.has(KEY_TITLE)) {
                                music_info_title = music_info.getString(KEY_TITLE)
                                response.description = music_info_title
                            }
                            if (music_info.has(KEY_PLAY)) {
                                play = music_info.getString(KEY_PLAY)
                            }
                            if (music_info.has(KEY_COVER)) {
                                cover = music_info.getString(KEY_COVER)
                            }
                            if (music_info.has(KEY_AUTHOR)) {
                                var author: String = music_info.getString(KEY_AUTHOR)
                            }
                            if (music_info.has(KEY_ORIGINAL)) {
                                original = music_info.getString(KEY_ORIGINAL)
                            }
                            if (music_info.has(KEY_DURATION)) {
                                duration = music_info.getString(KEY_DURATION)
                            }
                            if (music_info.has(KEY_ALBUM)) {
                                album = music_info.getString(KEY_ALBUM)
                            }
                        }
                    }
                    response.username = nickname
                    //response.thumb = ai_dynamic_cover;
                    response.thumb = cover
                    if (TextUtils.isEmpty(hdplay)) {
                        response.contentURL = hdplay
                        response.cleanVideo = hdplay
                    } else {
                        response.contentURL = wmplay
                        response.cleanVideo = wmplay
                    }
                    DLog.d("[*]" + response.contentURL!!)
                } catch (e: Exception) {
                    DLog.handleException(e)
                    mThread.post({
                                errorExp = e
                                })
                }
                //if (!fromService) {
                //    callback.hideProgressDialog();
                //}
                handleError()
                onPostExecute(response)
                })
    }
    private fun onPostExecute(ttResponse: TTResponse) {
        mThread.post({
                repository.onPostExecute0()
                try {
                    //DLog.d(ttResponse.toString());
                    //@String target = ttResponse.select("link[rel=\"canonical\"]").last().attr("href");
                    if (callback != null) {
                        callback.successResult(ttResponse)
                    }
                    val target: String = if (removeWatermark) ttResponse.cleanVideo!! else ttResponse.contentURL!!
                    if (target.isEmpty()) {
                        //@target = target.split("video/")[1];
                        // iUtils.ShowToast(Mcontext,target);
                        if (com.android.widget.Config.STEP_1_ENABLED) {
                            DLog.d("=======" + target)
                            repository.downloadTikTokVideo(target, ttResponse.title!!)
                        }
                    } else {
                        if (callback != null) {
                            callback.errorResult(com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG)
                        }
                    }
                } catch (e: Exception) {
                    DLog.handleException(e)
                    if (callback != null) {
                        callback.errorResult(com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG)
                    }
                }
                })
    }
    private fun handleError() {
        if (null != errorExp!!) {
            if (callback != null) {
                mThread.post({
                                callback.errorResult(com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG)
                                })
            }
        }
    }
    companion object {
        const val KEY_ADV_PROMOTABLE = "adv_promotable"
        const val KEY_AI_DYNAMIC_COVER = "ai_dynamic_cover"
        const val KEY_ALBUM = "album"
        const val KEY_ANCHORS = "anchors"
        const val KEY_ANCHORS_EXTRAS = "anchors_extras"
        const val KEY_AUCTION_AD_INVITED = "auction_ad_invited"
        const val KEY_AUTHOR = "author"
        const val KEY_AVATAR = "avatar"
        const val KEY_AWEME_ID = "aweme_id"
        const val KEY_BRANDED_CONTENT_TYPE = "branded_content_type"
        const val KEY_CODE = "code"
        const val KEY_COLLECT_COUNT = "collect_count"
        const val KEY_COMMENT_COUNT = "comment_count"
        const val KEY_COMMERCE_INFO = "commerce_info"
        const val KEY_COMMERCIAL_VIDEO_INFO = "commercial_video_info"
        const val KEY_COVER = "cover"
        const val KEY_CREATE_TIME = "create_time"
        const val KEY_DATA = "data"
        const val KEY_DIGG_COUNT = "digg_count"
        const val KEY_DOWNLOAD_COUNT = "download_count"
        const val KEY_DURATION = "duration"
        const val KEY_HD_SIZE = "hd_size"
        const val KEY_HDPLAY = "hdplay"
        const val KEY_ID = "id"
        const val KEY_IS_AD = "is_ad"
        const val KEY_ITEM_COMMENT_SETTINGS = "item_comment_settings"
        const val KEY_MSG = "msg"
        const val KEY_MUSIC = "music"
        const val KEY_MUSIC_INFO = "music_info"
        const val KEY_NICKNAME = "nickname"
        const val KEY_ORIGIN_COVER = "origin_cover"
        const val KEY_ORIGINAL = "original"
        const val KEY_PLAY = "play"
        const val KEY_PLAY_COUNT = "play_count"
        const val KEY_PROCESSED_TIME = "processed_time"
        const val KEY_REGION = "region"
        const val KEY_SHARE_COUNT = "share_count"
        const val KEY_SIZE = "size"
        const val KEY_TITLE = "title"
        const val KEY_UNIQUE_ID = "unique_id"
        const val KEY_WITH_COMMENT_FILTER_WORDS = "with_comment_filter_words"
        const val KEY_WM_SIZE = "wm_size"
        const val KEY_WMPLAY = "wmplay"
        @JvmField var xkey: IntArray = intArrayOf(121, 101, 75, 45, 73, 80, 65, 100, 105, 112, 97, 82, 45, 88)
        @JvmField var xhost: IntArray = intArrayOf(116, 115, 111, 72, 45, 73, 80, 65, 100, 105, 112, 97, 82, 45, 88)
        @JvmField var valueH0: IntArray = intArrayOf(109, 111, 99, 46, 105, 112, 97, 100, 105, 112, 97, 114, 46, 112, 46, 55, 114, 101, 112, 97, 114, 99, 115, 45, 107, 111, 116, 107, 105, 116)
        @JvmField var uHolder: IntArray = intArrayOf(49, 61, 100, 104, 38, 115, 50, 37, 61, 108, 114, 117, 63, 47, 115, 49, 37, 47, 47, 58, 115, 112, 116, 116, 104)
        @JvmField var xKVa: IntArray = intArrayOf(97, 50, 53, 49, 56, 50, 54, 52, 56, 54, 50, 55, 110, 115, 106, 55, 53, 101, 48, 52, 49, 112, 55, 98, 54, 99, 97, 101, 102, 48, 97, 48, 98, 50, 100, 99, 102, 104, 115, 109, 51, 97, 102, 54, 100, 54, 99, 53, 97, 99)
    }
}
