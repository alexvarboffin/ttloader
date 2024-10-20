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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookPresenter extends AbstractInfoExtractor {
    private static final String FACEBOOK_USERAGENT =
            //"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0";
            //+++"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.47 Safari/537.36";
            //"Mozilla/5.0 (Linux; Android 7.1.2; cepheus Build/N2G48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.70 Mobile Safari/537.36";
            //"Mozilla/5.0 (Linux; Android 7.1.2; cepheus Build/N2G48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.70 Mobile Safari/537.36";
            "facebookexternalhit/1.1";

    private Exception mError;
    private TTResponse response = new TTResponse();

    public FacebookPresenter(RepositoryCallback callback, VideoRepository repository, Handler handler) {
        super(callback, repository, handler);
    }

    @Override
    public void execute(final String url) {
        mError = null;
        response = null;

        String acceptLanguage = "en-US";

        executor.execute(() -> {
            try {
                //String url = "http://httpbin.org/get";
                DLog.d("[FacebookVideo]" + url);
                //doc = Jsoup.connect(FacebookApi).data("v",urls[0]).get();
//                try {
//                    SSLContext sc = SSLContext.getInstance("SSL");
//                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
//                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//                } catch (Exception e) {
//                    DLog.handleException(e);
//                }

                Document doc = Jsoup.connect(url)
                        .timeout(TIMEOUT)
                        //.userAgent()
                        //.userAgent(rrr)
                        //.userAgent(FACEBOOK_USERAGENT)
                        .ignoreContentType(true)
                        .header("user-agent", FACEBOOK_USERAGENT)
                        .header("authority", "www.facebook.com")
                        .header("Accept-Language", acceptLanguage)
                        .header("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2")
                        //.header("Accept-Encoding", "gzip")
                        .header("accept-encoding", "gzip")

                        //.headers(headers)
                        //.headers("Content-Type: application/json")
                        .followRedirects(true)
                        .get();
                //v1
                String data = doc.toString();

//
//                Request request = new Request.Builder()
//                        .url(url)
////                        .addHeader("user-agent", FACEBOOK_USERAGENT)
//                        .addHeader("User-Agent", FACEBOOK_USERAGENT)
//                        .addHeader("Cookie", "facebook.com; locale=en_US")
////                        .addHeader("Accept-Language", acceptLanguage)
////                        //.addHeader("authority", "www.facebook.com")
////                        .addHeader("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2")
////                        .addHeader("Accept-Encoding", "gzip")
//                        .build();

                try {
//                    StringBuilder sb = new StringBuilder();
//                    Response response = client.newCall(request).execute();
//                    if (!response.isSuccessful()) {
//                        throw new IOException("Unexpected code " + response);
//                    }
//                    ResponseBody responseBody = response.body();
//                    if (responseBody != null) {
//                        // Проверяем, был ли ответ сжат с помощью gzip
//                        if ("gzip".equalsIgnoreCase(response.header("Content-Encoding"))) {
//                            // Если да, распаковываем его
//                            try (GZIPInputStream gzipInputStream = new GZIPInputStream(responseBody.byteStream())) {
//                                // Считываем содержимое ответа
//                                BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream));
//                                String line;
//                                System.out.println("=================");
//                                while ((line = reader.readLine()) != null) {
//                                    sb.append(line);
//                                    System.out.println(line);
//                                }
//                                System.out.println("=================");
//                            }
//                        } else {
//                            // Если ответ не был сжат, считываем его как обычно
//                            sb.append(responseBody.string());
//                            System.out.println("=================");
//                            System.out.println(sb.toString());
//                            System.out.println("=================");
//                        }
//                    }
//                    String data = sb.toString();

//                    String URL = TxTUtil.eE(data, "playable_url_quality_hd", "\"");
//                    if (TextUtils.isEmpty(URL)) {
//                        //v2
//                        DLog.d("======>>" + URL);
//                        URL = doc.select("meta[property=\"og:video\"]").last().attr("content");
//                    }

                    String URL = "";
                    String start_hd = "browser_native_hd_url\":\"";
                    if (data.contains(start_hd)) {
                        URL = getHDLink(data);////TxTUtil.onlyExtractFacebook(data, start_hd, "\",");
                    }
                    if (VideoRepository.isEmpty(URL)) {
                        URL = getSDLink(data);
                    }
                    response = new TTResponse();
                    response.title = doc.title();

                    DLog.d("...| " + URL + " |..." + start_hd);

                    // iUtils.ShowToast(Mcontext,URL);

                    String finalURL = URL;
                    response.cleanVideo = finalURL;

                } catch (NullPointerException e) {
                    DLog.handleException(e);
                    mError = e;
                }
                // new DownloadTikTokVideo().execute(URL);

            } catch (IOException e) {
                DLog.handleException(e);
                mError = e;
            }
            onPostExecute(response);
        });
    }

    private void onPostExecute(TTResponse response) {
        mThread.post(() -> {
            repository.onPostExecute0();
            if (mError != null) {
                if (callback != null) {
                    callback.errorResult(ERROR_WENT_WRONG);
                }
            }
            if (callback != null) {
                callback.successResult(response);
            }
            String target = response.cleanVideo;
            if (!TextUtils.isEmpty(target)) {
                if (callback != null) {
                    repository.downloadFacebookVideo(target, response.title);
                }
                if (callback != null) {
                    callback.successResult(response);
                }
            }
        });
    }


    private static String cleanStr(String str) {
        try {
            JSONObject json = new JSONObject("{\"text\": \"" + str + "\"}");
            return json.getString("text");
        } catch (JSONException e) {
            DLog.handleException(e);
        }
        return null;
    }

    public static String getHDLink(String curlContent) {
        String regexRateLimit = "browser_native_hd_url\":\"([^\"]+)\"";
        Matcher matcher = Pattern.compile(regexRateLimit).matcher(curlContent);
        if (matcher.find()) {
            return cleanStr(matcher.group(1));
        } else {
            return null;
        }
    }

    public static String getSDLink(String curlContent) {
        String regexRateLimit = "browser_native_sd_url\":\"([^\"]+)\"";
        Matcher matcher = Pattern.compile(regexRateLimit).matcher(curlContent);
        if (matcher.find()) {
            return cleanStr(matcher.group(1));
        } else {
            return null;
        }
    }
}
