package com.walhalla.extractors.presenters;

import static com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG;

import android.os.Handler;
import android.text.TextUtils;

import com.walhalla.ttvloader.TTResponse;
import com.walhalla.ttvloader.receiver.DownloadFile;
import com.walhalla.ui.DLog;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class InstagramPresenter extends AbstractInfoExtractor {
    private Exception mError;
    String acceptLanguage = "en-US";

    public InstagramPresenter(RepositoryCallback callback, VideoRepository repository, Handler handler) {
        super(callback, repository, handler);
    }

    @Override
    public void execute(String url) {
        mError = null;
        executor.execute(() -> {
            TTResponse response = new TTResponse();
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(VideoRepository.TIMEOUT)
//                        .header("authority", "www.facebook.com")
//                        .header("Accept-Language", acceptLanguage).followRedirects(true)
                        .get();
                try {
                    String data = doc.toString();
                    Element v0 = doc.select("meta[property=\"og:video\"]").last();
                    String URL = "";
                    if (v0 != null) {
                        DLog.d("@" + doc.toString());
                        URL = v0.attr("content");
                    } else {
                        String start_hd = "browser_native_hd_url\":\"";
                        if (data.contains(start_hd)) {
                            URL = FacebookPresenter.getHDLink(data);////TxTUtil.onlyExtractFacebook(data, start_hd, "\",");
                        }
                        if (VideoRepository.isEmpty(URL)) {
                            URL = FacebookPresenter.getSDLink(data);
                        }
                    }
                    if (!TextUtils.isEmpty(URL)) {
                        response.cleanVideo = URL;
                        response.contentURL = URL;
                    } else {
                        // Поиск скрипта, содержащего JSON данные
                        Elements scriptTags = doc.getElementsByTag("script");
                        for (Element scriptTag : scriptTags) {
                            String scriptContent = scriptTag.html();
                            DLog.d("@@@@"+scriptContent);

                            if (scriptContent.contains("window._sharedData")) {

                                String jsonData = scriptContent.split(" = ", 2)[1].split(";</script>", 2)[0];

                                // Парсинг JSON (вы можете использовать библиотеку, такую как org.json или Gson)
                                // Пример с org.json
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonData);
                                    JSONObject postData = jsonObject
                                            .getJSONObject("entry_data")
                                            .getJSONArray("PostPage")
                                            .getJSONObject(0)
                                            .getJSONObject("graphql")
                                            .getJSONObject("shortcode_media");

                                    URL = postData.getString("video_url");
                                    String description = postData.getJSONObject("edge_media_to_caption")
                                            .getJSONArray("edges")
                                            .getJSONObject(0)
                                            .getJSONObject("node")
                                            .getString("text");
                                    String username = postData.getJSONObject("owner").getString("username");
                                    long timestamp = postData.getLong("taken_at_timestamp");

                                    response.username = username;
                                    response.description = description;
                                    response.timestamp = timestamp;
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                break;
                            }
                        }
                    }

                    DLog.d("@@@" + response);

                    if (!TextUtils.isEmpty(URL)) {
                        response.cleanVideo = URL;
                        response.contentURL = URL;
                    }
                    response.title = doc.title();
                    response.ext = EXT_MP4;
                    //iUtils.ShowToast(Mcontext, URL);

                } catch (NullPointerException e) {
                    DLog.handleException(e);
                    mError = e;
                }
            } catch (IOException e) {
                DLog.handleException(e);
                mError = e;
            }
            onPostExecute(response);
        });
    }

    protected void onPostExecute(TTResponse response) {
        mThread.post(() -> {
            repository.onPostExecute0();
            if (mError != null) {
                if (callback != null) {
                    callback.errorResult(ERROR_WENT_WRONG);
                }
            }
            String target = response.cleanVideo;
            if (!TextUtils.isEmpty(target)) {
                if (callback != null) {
                    repository.downloadInstagramFile(target, response.title, response.ext);
                }
                if (callback != null) {
                    callback.successResult(response);
                }
            }
        });
    }
}
