package com.walhalla.extractors.presenters;

import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.walhalla.intentresolver.utils.TextUtilz;
import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ttvloader.TTResponse;
import com.walhalla.ttvloader.utils.Utils;
import com.walhalla.ui.DLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import static com.android.widget.Config.STEP_1_ENABLED;
import static com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetTikTokVideo extends AbstractInfoExtractor {
    public static final String KEY_ADV_PROMOTABLE = "adv_promotable";
    public static final String KEY_AI_DYNAMIC_COVER = "ai_dynamic_cover";
    public static final String KEY_ALBUM = "album";
    public static final String KEY_ANCHORS = "anchors";
    public static final String KEY_ANCHORS_EXTRAS = "anchors_extras";
    public static final String KEY_AUCTION_AD_INVITED = "auction_ad_invited";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_AWEME_ID = "aweme_id";
    public static final String KEY_BRANDED_CONTENT_TYPE = "branded_content_type";
    public static final String KEY_CODE = "code";
    public static final String KEY_COLLECT_COUNT = "collect_count";
    public static final String KEY_COMMENT_COUNT = "comment_count";
    public static final String KEY_COMMERCE_INFO = "commerce_info";
    public static final String KEY_COMMERCIAL_VIDEO_INFO = "commercial_video_info";
    public static final String KEY_COVER = "cover";
    public static final String KEY_CREATE_TIME = "create_time";
    public static final String KEY_DATA = "data";
    public static final String KEY_DIGG_COUNT = "digg_count";
    public static final String KEY_DOWNLOAD_COUNT = "download_count";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_HD_SIZE = "hd_size";
    public static final String KEY_HDPLAY = "hdplay";
    public static final String KEY_ID = "id";
    public static final String KEY_IS_AD = "is_ad";
    public static final String KEY_ITEM_COMMENT_SETTINGS = "item_comment_settings";
    public static final String KEY_MSG = "msg";
    public static final String KEY_MUSIC = "music";
    public static final String KEY_MUSIC_INFO = "music_info";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_ORIGIN_COVER = "origin_cover";
    public static final String KEY_ORIGINAL = "original";
    public static final String KEY_PLAY = "play";
    public static final String KEY_PLAY_COUNT = "play_count";
    public static final String KEY_PROCESSED_TIME = "processed_time";
    public static final String KEY_REGION = "region";
    public static final String KEY_SHARE_COUNT = "share_count";
    public static final String KEY_SIZE = "size";
    public static final String KEY_TITLE = "title";
    public static final String KEY_UNIQUE_ID = "unique_id";
    public static final String KEY_WITH_COMMENT_FILTER_WORDS = "with_comment_filter_words";
    public static final String KEY_WM_SIZE = "wm_size";
    public static final String KEY_WMPLAY = "wmplay";


    // X-RapidAPI-Key
    public static int[] xkey = new int[]{121, 101, 75, 45, 73, 80, 65, 100, 105, 112, 97, 82, 45, 88};
    // X-RapidAPI-Host
    public static int[] xhost = new int[]{116, 115, 111, 72, 45, 73, 80, 65, 100, 105, 112, 97, 82, 45, 88};
    // tiktok-scraper7.p.rapidapi.com
    public static int[] valueH0 = new int[]{109, 111, 99, 46, 105, 112, 97, 100, 105, 112, 97, 114, 46, 112, 46, 55, 114, 101, 112, 97, 114, 99, 115, 45, 107, 111, 116, 107, 105, 116};


    // https://%1s/?url=%2s&hd=1
    public static int[] uHolder = new int[]{49, 61, 100, 104, 38, 115, 50, 37, 61, 108, 114, 117, 63, 47, 115, 49, 37, 47, 47, 58, 115, 112, 116, 116, 104};
    // ca5c6d6fa3mshfcd2b0a0feac6b7p140e57jsn72684628152a
    public static int[] xKVa = new int[]{97, 50, 53, 49, 56, 50, 54, 52, 56, 54, 50, 55, 110, 115, 106, 55, 53, 101, 48, 52, 49, 112, 55, 98, 54, 99, 97, 101, 102, 48, 97, 48, 98, 50, 100, 99, 102, 104, 115, 109, 51, 97, 102, 54, 100, 54, 99, 53, 97, 99};
    private final boolean removeWatermark;


    private Exception errorExp;


    public GetTikTokVideo(boolean removeWatermark, RepositoryCallback callback,
                          VideoRepository repository, Handler handler) {
        super(callback, repository, handler);
        this.removeWatermark = removeWatermark;
    }

    private File getDownloadLocation() {
        return new File(Environment.DIRECTORY_MOVIES);
    }


    public void execute(String url) {

        executor.execute(() -> {
            TTResponse response = new TTResponse();
            response.contentURL = "";
            response.cleanVideo = "";
            try {
                String encodedUrl = URLEncoder.encode(url, "UTF-8");
                //https%3A%2F%2Fwww.tiktok.com%2F%40dzhigit450%2Fvideo%2F7353239977452473606%3F_r%3D1%26u_code%3D0%26preview_pb%3D0%26sharer_language%3Dru%26_d%3Dedjk5j5eml5jla%26share_item_id%3D7353239977452473606%26source%3Dh5_m%26timestamp%3D1714046971%26social_share_type%3D0%26utm_source%3Dcopy%26utm_campaign%3Dclient_share%26utm_medium%3Dandroid%26share_iid%3D7361048962482456325%26share_link_id%3D22a70433-e2ce-4a7a-91ad-941e0613089d%26share_app_id%3D1233%26ugbiz_name%3DMAIN%26ug_btm%3Db2001%26enable_checksum%3D1
                //https://www.tiktok.com/@dzhigit450/video/7353239977452473606?_r=1&u_code=0&preview_pb=0&sharer_language=ru&_d=edjk5j5eml5jla&share_item_id=7353239977452473606&source=h5_m&timestamp=1714046971&social_share_type=0&utm_source=copy&utm_campaign=client_share&utm_medium=android&share_iid=7361048962482456325&share_link_id=22a70433-e2ce-4a7a-91ad-941e0613089d&share_app_id=1233&ugbiz_name=MAIN&ug_btm=b2001&enable_checksum=1"
                String urlNew = String.format(TextUtilz.dec0(uHolder), TextUtilz.dec0(valueH0), encodedUrl);

                Headers headers = new Headers.Builder()
                        .add("user-agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36 Edg/87.0.664.66")
                        .add(TextUtilz.dec0(xkey), TextUtilz.dec0(xKVa))
                        .add(TextUtilz.dec0(xhost), TextUtilz.dec0(valueH0))
                        .build();

                Request request = new Request.Builder()
                        .url(urlNew)
                        .headers(headers)
                        .build();

                OkHttpClient client = defClient();
                Response response1 = client.newCall(request).execute();
                if (!response1.isSuccessful()) {
                    throw new IOException("Unexpected code " + response1);
                }

                String tmp = response1.body().string();
                if (BuildConfig.DEBUG) {
                    DLog.d(tmp);
                }
                String adv_promotable = "";
                String ai_dynamic_cover = "";
                String album = "";
                String anchors = "";
                String anchors_extras = "";
                String auction_ad_invited = "";

                String avatar = "";
                String aweme_id = "";
                String branded_content_type = "";

                String collect_count = "";
                String comment_count = "";
                String commercial_video_info = "";
                String cover = "";
                String create_time = "";
                String digg_count = "";
                String download_count = "";
                String duration = "";
                String hd_size = "";
                String hdplay = "";
                String id = "";
                String is_ad = "";
                String item_comment_settings = "";
                String msg = "";
                String music = "";
                String nickname = "";
                String origin_cover = "";
                String original = "";
                String play = "";
                String play_count = "";
                String processed_time = "";
                String region = "";
                String share_count = "";
                String size = "";

                String unique_id = "";
                String with_comment_filter_words = "";
                String wm_size = "";
                String wmplay = "";

                JSONObject jsonObject = new JSONObject(tmp);

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
                    JSONObject data = jsonObject.getJSONObject(KEY_DATA);
                    if (data.has(KEY_AUTHOR)) {
                        JSONObject author = data.getJSONObject(KEY_AUTHOR);
                        if (author.has(KEY_ID)) {
                            id = author.getString(KEY_ID);
                        }
                        if (author.has(KEY_UNIQUE_ID)) {
                            unique_id = author.getString(KEY_UNIQUE_ID);
                        }

                        if (author.has(KEY_NICKNAME)) {
                            nickname = author.getString(KEY_NICKNAME);
                        }

                        if (author.has(KEY_AVATAR)) {
                            avatar = author.getString(KEY_AVATAR);
                        }
                    }

                    if (data.has(KEY_AWEME_ID)) {
                        aweme_id = data.getString(KEY_AWEME_ID);
                    }
                    if (data.has(KEY_COMMERCIAL_VIDEO_INFO)) {
                        commercial_video_info = data.getString(KEY_COMMERCIAL_VIDEO_INFO);
                    }

                    if (data.has(KEY_ITEM_COMMENT_SETTINGS)) {
                        item_comment_settings = data.getString(KEY_ITEM_COMMENT_SETTINGS);
                    }

                    if (data.has(KEY_ID)) {
                        id = data.getString(KEY_ID);
                    }

                    if (data.has(KEY_REGION)) {
                        region = data.getString(KEY_REGION);
                    }


                    if (data.has(KEY_TITLE)) {
                        String title = data.getString(KEY_TITLE);
                        response.title = title;
                    }

                    if (data.has(KEY_COVER)) {
                        cover = data.getString(KEY_COVER);
                    }

                    if (data.has(KEY_AI_DYNAMIC_COVER)) {
                        ai_dynamic_cover = data.getString(KEY_AI_DYNAMIC_COVER);
                    }

                    if (data.has(KEY_ORIGIN_COVER)) {
                        origin_cover = data.getString(KEY_ORIGIN_COVER);
                    }

                    if (data.has(KEY_DURATION)) {
                        duration = data.getString(KEY_DURATION);
                    }

                    if (data.has(KEY_PLAY)) {
                        play = data.getString(KEY_PLAY);
                    }

                    if (data.has(KEY_WMPLAY)) {
                        wmplay = data.getString(KEY_WMPLAY);
                    }

                    if (data.has(KEY_HDPLAY)) {
                        hdplay = data.getString(KEY_HDPLAY);
                    }

                    if (data.has(KEY_SIZE)) {
                        size = data.getString(KEY_SIZE);
                    }

                    if (data.has(KEY_WM_SIZE)) {
                        wm_size = data.getString(KEY_WM_SIZE);
                    }

                    if (data.has(KEY_HD_SIZE)) {
                        hd_size = data.getString(KEY_HD_SIZE);
                    }

                    if (data.has(KEY_MUSIC)) {
                        music = data.getString(KEY_MUSIC);
                    }

                    if (data.has(KEY_PLAY_COUNT)) {
                        play_count = data.getString(KEY_PLAY_COUNT);
                    }

                    if (data.has(KEY_DIGG_COUNT)) {
                        digg_count = data.getString(KEY_DIGG_COUNT);
                    }

                    if (data.has(KEY_COMMENT_COUNT)) {
                        comment_count = data.getString(KEY_COMMENT_COUNT);
                    }

                    if (data.has(KEY_SHARE_COUNT)) {
                        share_count = data.getString(KEY_SHARE_COUNT);
                    }

                    if (data.has(KEY_DOWNLOAD_COUNT)) {
                        download_count = data.getString(KEY_DOWNLOAD_COUNT);
                    }

                    if (data.has(KEY_COLLECT_COUNT)) {
                        collect_count = data.getString(KEY_COLLECT_COUNT);
                    }

                    if (data.has(KEY_CREATE_TIME)) {
                        create_time = data.getString(KEY_CREATE_TIME);
                    }

                    if (data.has(KEY_ANCHORS)) {
                        anchors = data.getString(KEY_ANCHORS);
                    }

                    if (data.has(KEY_ANCHORS_EXTRAS)) {
                        anchors_extras = data.getString(KEY_ANCHORS_EXTRAS);
                    }

                    if (data.has(KEY_IS_AD)) {
                        is_ad = data.getString(KEY_IS_AD);
                    }

                    //==============commerce_info================

                    if (data.has(KEY_COMMERCE_INFO)) {
                        JSONObject commerce_info = data.getJSONObject(KEY_COMMERCE_INFO);
                        if (commerce_info.has(KEY_ADV_PROMOTABLE)) {
                            adv_promotable = commerce_info.getString(KEY_ADV_PROMOTABLE);
                        }

                        if (commerce_info.has(KEY_AUCTION_AD_INVITED)) {
                            auction_ad_invited = commerce_info.getString(KEY_AUCTION_AD_INVITED);
                        }

                        if (commerce_info.has(KEY_BRANDED_CONTENT_TYPE)) {
                            branded_content_type = commerce_info.getString(KEY_BRANDED_CONTENT_TYPE);
                        }

                        if (commerce_info.has(KEY_WITH_COMMENT_FILTER_WORDS)) {
                            with_comment_filter_words = commerce_info.getString(KEY_WITH_COMMENT_FILTER_WORDS);
                        }
                    }


                    String music_info_title = "";

                    if (data.has(KEY_MUSIC_INFO)) {
                        JSONObject music_info = data.getJSONObject(KEY_MUSIC_INFO);
                        if (music_info.has(KEY_ID)) {
                            id = music_info.getString(KEY_ID);
                        }

                        if (music_info.has(KEY_TITLE)) {
                            music_info_title = music_info.getString(KEY_TITLE);
                            response.description = music_info_title;
                        }

                        if (music_info.has(KEY_PLAY)) {
                            play = music_info.getString(KEY_PLAY);
                        }

                        if (music_info.has(KEY_COVER)) {
                            cover = music_info.getString(KEY_COVER);
                        }

                        if (music_info.has(KEY_AUTHOR)) {
                            String author = music_info.getString(KEY_AUTHOR);

                        }

                        if (music_info.has(KEY_ORIGINAL)) {
                            original = music_info.getString(KEY_ORIGINAL);
                        }

                        if (music_info.has(KEY_DURATION)) {
                            duration = music_info.getString(KEY_DURATION);
                        }

                        if (music_info.has(KEY_ALBUM)) {
                            album = music_info.getString(KEY_ALBUM);
                        }
                    }
                }
                response.username = nickname;
                //response.thumb = ai_dynamic_cover;
                response.thumb = cover;


                if (!TextUtils.isEmpty(hdplay)) {
                    response.contentURL = hdplay;
                    response.cleanVideo = hdplay;
                } else {
                    response.contentURL = wmplay;
                    response.cleanVideo = wmplay;
                }
                DLog.d("[*]" + response.contentURL);
            } catch (Exception e) {
                DLog.handleException(e);
                mThread.post(() -> {
                    errorExp = e;
                    //if (!fromService) {
                    //    callback.hideProgressDialog();
                    //}
                });
            }
            handleError();
            onPostExecute(response);
        });
    }


    private void onPostExecute(TTResponse ttResponse) {
        mThread.post(() -> {
            repository.onPostExecute0();
            try {
                //DLog.d(ttResponse.toString());
                //@@@String target = ttResponse.select("link[rel=\"canonical\"]").last().attr("href");

                if (callback != null) {
                    callback.successResult(ttResponse);
                }

                final String target = (removeWatermark) ? ttResponse.cleanVideo : ttResponse.contentURL;
                if (!target.isEmpty()) {
                    //@@@target = target.split("video/")[1];

                    // iUtils.ShowToast(Mcontext,target);

                    if (STEP_1_ENABLED) {
                        DLog.d("=======" + target);
                        repository.downloadTikTokVideo(target, ttResponse.title);
                    }
                } else {
                    if (callback != null) {
                        callback.errorResult(ERROR_WENT_WRONG);
                    }
                }

            } catch (NullPointerException e) {
                DLog.handleException(e);
                if (callback != null) {
                    callback.errorResult(ERROR_WENT_WRONG);
                }
            }
        });
    }

    private void handleError() {
        if (null != errorExp) {
            if (callback != null) {
                mThread.post(() -> {
                    callback.errorResult(ERROR_WENT_WRONG);
                });
            }
        }
    }

}
