//package com.walhalla.extractors.presenters;
//
//import static com.android.widget.Config.STEP_1_ENABLED;
//import static com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG;
//
//import android.os.Environment;
//import android.os.Handler;
//
//import com.walhalla.ttvloader.TTResponse;
//import com.walhalla.ui.DLog;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URLEncoder;
//
//import okhttp3.Headers;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class GetTikTokVideoOld extends AbstractInfoExtractor {
//
//    private final boolean removeWatermark;
//
//
//    private Exception errorExp;
//
//
//    public GetTikTokVideoOld(boolean removeWatermark, RepositoryCallback callback,
//                             VideoRepository repository, Handler handler) {
//        super(callback, repository, handler);
//        this.removeWatermark = removeWatermark;
//    }
//
//    private File getDownloadLocation() {
//        return new File(Environment.DIRECTORY_MOVIES);
//    }
//
//
//    public void execute(String url) {
//
//        executor.execute(() -> {
//            TTResponse response = new TTResponse();
//            response.contentURL = "";
//            response.cleanVideo = "";
//            try {
//                String encodedUrl = URLEncoder.encode(url, "UTF-8");
//                System.out.println("Encoded URL: " + encodedUrl);
//
//                //https%3A%2F%2Fwww.tiktok.com%2F%40dzhigit450%2Fvideo%2F7353239977452473606%3F_r%3D1%26u_code%3D0%26preview_pb%3D0%26sharer_language%3Dru%26_d%3Dedjk5j5eml5jla%26share_item_id%3D7353239977452473606%26source%3Dh5_m%26timestamp%3D1714046971%26social_share_type%3D0%26utm_source%3Dcopy%26utm_campaign%3Dclient_share%26utm_medium%3Dandroid%26share_iid%3D7361048962482456325%26share_link_id%3D22a70433-e2ce-4a7a-91ad-941e0613089d%26share_app_id%3D1233%26ugbiz_name%3DMAIN%26ug_btm%3Db2001%26enable_checksum%3D1
//                //https://www.tiktok.com/@dzhigit450/video/7353239977452473606?_r=1&u_code=0&preview_pb=0&sharer_language=ru&_d=edjk5j5eml5jla&share_item_id=7353239977452473606&source=h5_m&timestamp=1714046971&social_share_type=0&utm_source=copy&utm_campaign=client_share&utm_medium=android&share_iid=7361048962482456325&share_link_id=22a70433-e2ce-4a7a-91ad-941e0613089d&share_app_id=1233&ugbiz_name=MAIN&ug_btm=b2001&enable_checksum=1"
//                String urlNew = "http://137.220.62.39:51027/getlinktt?url=" + encodedUrl;
//
//                Headers headers = new Headers.Builder()
//                        .add("user-agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36 Edg/87.0.664.66")
//                        //.add("Host", "137.220.62.39:51027")
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url(urlNew)
//                        .headers(headers)
//                        .build();
//
//                Response response1 = client.newCall(request).execute();
//                if (!response1.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response1);
//                }
//                String tmp = response1.body().string();
//                String videoUrl = parseUrl(tmp);
//                response.contentURL = videoUrl;
//                response.cleanVideo = videoUrl;
//                DLog.d("[*]" + videoUrl);
//
//            } catch (Exception e) {
//                DLog.handleException(e);
//                mThread.post(() -> {
//                    errorExp = e;
//                    //if (!fromService) {
//                    //    callback.hideProgressDialog();
//                    //}
//                });
//            }
//            handleError();
//            onPostExecute(response);
//        });
//    }
//
//
//    private String parseUrl(String jsonResponse) throws JSONException {
//        JSONObject jsonObject = new JSONObject(jsonResponse);
//        return jsonObject.getString("nwm_video_url");
//    }
//
//    private void onPostExecute(TTResponse ttResponse) {
//        mThread.post(() -> {
//            repository.onPostExecute0();
//            try {
//                //DLog.d(ttResponse.toString());
//                //@@@String target = ttResponse.select("link[rel=\"canonical\"]").last().attr("href");
//
//                if (callback != null) {
//                    callback.successResult(ttResponse);
//                }
//
//                final String target = (removeWatermark) ? ttResponse.cleanVideo : ttResponse.contentURL;
//                if (!target.isEmpty()) {
//                    //@@@target = target.split("video/")[1];
//
//                    // iUtils.ShowToast(Mcontext,target);
//
//                    if (STEP_1_ENABLED) {
//                        DLog.d("=======" + target);
//                        repository.downloadTikTokVideo(target, ttResponse.title);
//                    }
//                } else {
//                    if (callback != null) {
//                        callback.errorResult(ERROR_WENT_WRONG);
//                    }
//                }
//
//            } catch (NullPointerException e) {
//                DLog.handleException(e);
//                if (callback != null) {
//                    callback.errorResult(ERROR_WENT_WRONG);
//                }
//            }
//        });
//    }
//
//    private void handleError() {
//        if (null != errorExp) {
//            if (callback != null) {
//                mThread.post(() -> {
//                    callback.errorResult(ERROR_WENT_WRONG);
//                });
//            }
//        }
//    }
//
//}
