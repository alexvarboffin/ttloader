package com.walhalla.extractors.presenters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.walhalla.extractors.ExUtils;
import com.walhalla.extractors.FaceBookExtractor;
import com.walhalla.extractors.LikeExtractor;
import com.walhalla.extractors.PinterestExtractor;
import com.walhalla.extractors.TTExtractor;
import com.walhalla.extractors.TrillerExtractor;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.common.NetworkType;
import com.walhalla.ttvloader.receiver.DownloadFile;
import com.walhalla.ui.DLog;

import org.jsoup.nodes.Node;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;


import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class VideoRepository {

    private final Context context;

    private Exception errorExp = null;


    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };

    Executor executor = Executors.newFixedThreadPool(1);


    private final RepositoryCallback callback;


    public static final int ERROR_WENT_WRONG = R.string.abc_something_went_wrong;
    private final Handler mThread;


    public void onPostExecute0() {
        if (!fromService) {
            callback.hideProgressDialog();
        }
    }


    public VideoRepository(Context context, RepositoryCallback callback, Handler handler) {
        this.callback = callback;
        this.mThread = handler;
        this.context = context;
    }

    public static final int TIMEOUT = 35 * 1000;
    private static final String EXT_MP4 = ".mp4";
    public static final String EXT_JPG = ".jpg";


    //public Context context;

    public static Dialog dialog;
    static String SessionID;
    static int error = 1;

    public SharedPreferences prefs;
    public static boolean fromService;


    public void makeDownload(String url, Boolean service, boolean removeWatermark) {

        //this.context = context;
        fromService = service;

        //SessionID=title;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        if (!fromService) {
            callback.showProgressDialog();
        }
        if (url.contains(NetworkType.TIKTOK__.getValue())) {
            GetTikTokVideo executor0 = new GetTikTokVideo(removeWatermark, callback, this, mThread);
            executor0.execute(url);
        }
//        else if (url.contains("twitter.com")) {
//            DLog.d("@\t\t" + url);
//        }
        else {
            List<TTExtractor> tmp = ExUtils.defExtractors();
            TTExtractor resolved = null;
            for (TTExtractor extractor : tmp) {
                if (extractor.isUrlValid(url)) {
                    resolved = extractor;
                    break;
                }
            }

            if (resolved == null) {
                //DLog.d("----------------");
                if (!fromService) {
                    if (callback != null) {
                        callback.hideProgressDialog();
                        callback.errorResult(R.string.err_www_not_support);
                    }
                }
            } else {
                if (resolved instanceof PinterestExtractor) {
                    PinterestPresenter mm = new PinterestPresenter(callback, this, mThread);
                    mm.execute(url);
                } else if (resolved instanceof FaceBookExtractor) {
                    final String newValue = resolved.getClearUrl(url);
                    //String[] Furl = url.split("/");
                    // url = Furl[Furl.length-1];
                    //iUtils.ShowToast(Mcontext,Furl[Furl.length-1]);
                    FacebookPresenter mm = new FacebookPresenter(callback, this, mThread);
                    mm.execute(newValue);
                } else if (resolved instanceof InstaExtractor) {
                    InstagramPresenter mm = new InstagramPresenter(callback, this, mThread);
                    mm.execute(url);
                } else if (resolved instanceof LikeExtractor) {
                    //int aa = url.indexOf("l.likee.video/v/");
                    GetLikeeVideo mm = new GetLikeeVideo(removeWatermark, callback, this, mThread);
                    mm.execute(url);
                } else if (resolved instanceof TrillerExtractor) {
                    AbstractInfoExtractor mm = new GetTrillerVideo(removeWatermark, callback, this, mThread);
                    mm.execute(url);
                }
            }
        }
//        else if (url.contains(NetworkType.XH.getValue())) {
//            new GetVideoXH().execute(url);
//        }
//        else if (url.contains(NetworkType.CB.getValue())) {
//            new GetVideoCB().execute(url);
//        }
//        else if (url.contains(NetworkType.PHUB.getValue())) {
//            URL parsed = null;
//            try {
//                parsed = new URL(url);
//            } catch (MalformedURLException e) {
//                DLog.handleException(e);
//            }
//            String regions[] = new String[]{
//                    "www", "cn", "cz", "de", "es", "fr", "it", "nl", "jp", "pt", "pl", "rt"
//            };
//            if (parsed == null) {
//
//                return;
//            }
//            for (String region : regions) {
//                String rr = null;
//                if ((region + ".pornhub.com").equals(parsed.getHost())) {
//                    String tm0 = parsed.getPath().split("/")[1];
//                    rr = ("PornHub url validated.");
//                    if ("model".equals(tm0)) {
//                        rr = ("This is a MODEL url,");
//                    } else if (tm0.equals("pornstar")) {
//                        rr = ("This is a PORNSTAR url,");
//                    } else if (tm0.equals("channels")) {
//                        rr = ("This is a CHANNEL url,");
//                    } else if (tm0.equals("users")) {
//                        rr = ("This is a USER url,");
//                    } else if (tm0.equals("playlist")) {
//                        rr = ("This is a PLAYLIST url,");
//                    } else if (tm0.equals("view_video.php")) {
//                        rr = ("This is a VIDEO url. Please paste a model/pornstar/user/channel/playlist url.");
//                    } else {
//                        DLog.d("Somethings wrong with the url. Please check it out.");
//                    }
//                    if (rr != null) {
//                        new GetVideoPHUB().execute(url);
//                    }
//                    return;
//                }
//
//            }
//            DLog.d("This is not a PornHub url.");
//
//        }

        //################################################

        //iUtils.ShowToast(Mcontext,url);
        //iUtils.ShowToast(Mcontext,SessionID);

        prefs = context.getSharedPreferences("AppConfig", MODE_PRIVATE);
    }


    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

//    public static class GetVideoXH extends AsyncTask<String, Void, Document> {
//
//        private final String error;
//        Document doc;
//
//        public GetVideoXH(String ERROR_WENT_WRONG) {
//            this.error = ERROR_WENT_WRONG;
//        }
//
//        @Override
//        protected Document doInBackground(String... urls) {
//            try {
//                doc = Jsoup.connect(urls[0])
//                        .timeout(VideoRepository.TIMEOUT)
//                        .get();
//            } catch (IOException e) {
//                DLog.handleException(e);
//            }
//            return doc;
//        }
//
//        //post_execute
//        @Override
//        protected void onPostExecute(Document result) {
//            if (!fromService) {
//                callback.hideProgressDialog();
//            }
//
//            try {
//                String url = null;
//                Pattern pattern = Pattern.compile("\"downloadFile\":\"(.*?)\"");
//                Matcher mm = pattern.matcher(doc.toString());
//                if (mm.find()) {
//                    url = mm.group(1);
//                }
//                if (url != null) {
//                    url = url.replace("\\", "").trim();
//                    if (url.contains(".mp4")) {
//                        Utils.ShowToast0(context, url);
//                        DLog.d("||" + url);
//                        makeLoadRequest(context, url, result.title(), EXT_MP4);
//                    }
//                } else {
//                    Utils.ShowToast0(context, error);
//                }
//
//            } catch (NullPointerException e) {
//                DLog.handleException(e);
//                Utils.ShowToast0(context, error);
//            }
//        }
//    }

//    public static class GetVideoCB extends AsyncTask<String, Void, Document> {
//        private final String error;
//        Document doc;
//
//        public GetVideoCB(String ERROR_WENT_WRONG) {
//            this.error = ERROR_WENT_WRONG;
//        }
//
//        @Override
//        protected Document doInBackground(String... urls) {
//            try {
//                doc = Jsoup.connect(urls[0])
//                        .userAgent(System.getProperty("http.agent"))
//                        .timeout(VideoRepository.TIMEOUT)
//                        .followRedirects(true)
//                        .get();
//            } catch (IOException e) {
//                DLog.handleException(e);
//            }
//            return doc;
//        }
//
//        //post_execute
//        @Override
//        protected void onPostExecute(Document result) {
//            if (!fromService) {
//                callback.hideProgressDialog();
//            }
//            DLog.d(result.toString());
//            try {
//                String url = null;
//                Pattern pattern = Pattern.compile("\\u0022hls_source\\u0022: \\u0022(.*?)\\u0022");
//                Matcher mm = pattern.matcher(doc.toString());
//                if (mm.find()) {
//                    url = mm.group(1);
//                }
//                if (url != null) {
//                    url = url.replace("\\", "").trim();
//                    if (url.contains(".mp3u8")) {
//                        Utils.ShowToast0(context, url);
//                        DLog.d("||" + url);
//                        //DownloadFile.newInstance().make(context, url, result.title(), EXT_MP4);
//                    }
//                } else {
//                    Utils.ShowToast0(context, error);
//                }
//
//            } catch (NullPointerException e) {
//                DLog.handleException(e);
//                Utils.ShowToast0(context, error);
//            }
//        }
//    }

    //    public static class GetVideoPHUB extends AsyncTask<String, Void, Document> {
//        Document doc;
//
//        @Override
//        protected Document doInBackground(String... urls) {
//            try {
//                doc = Jsoup.connect(urls[0])
//                        //.userAgent(System.getProperty("http.agent"))
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0")
//                        .timeout(VideoRepository.TIMEOUT)
//                        .followRedirects(true)
//                        .get();
//            } catch (SocketTimeoutException e) {
//                DLog.handleException(e);
//                //Utils.ShowToast0(context, "Server Timeout Exception");
//            } catch (IOException e) {
//                DLog.handleException(e);
//            }
//            return doc;
//        }
//
//        //post_execute
//        @Override
//        protected void onPostExecute(final Document result) {
//            if (!fromService) {
//                pd.dismiss();
//            }
//
//            try {
//                String tt = result.toString();
////                if (tt.contains("downloadBtn")) {
////                    int index = tt.indexOf("downloadBtn");
////                    DLog.d("@@@@" + index);//tt.substring(index)
////                }
//                //Tracker.writeToFile(tt, context);
//
////                Elements select0 = result.select("body");//div#player > script
////                if (!select0.isEmpty()) {
////                    String kk = select0.get(0).toString();
////                }
//                //removeComments(result);
//
//                String kk = result.toString();
//                    Pattern p0 = Pattern.compile("/*\\s(.*?)\\s*/");
//                    Matcher m = p0.matcher(kk);
//                    while (m.find()) {
//                        String ll = m.group();
//                        kk = kk.replace(ll, "");
//                    }
//
//                Map<String, String> map = new HashMap<>();
//                Pattern pattern = Pattern.compile("(;|\\s)var (.*?)=\"(.*?)\";");
//                Matcher matcher = pattern.matcher(kk);
//                while (matcher.find()) {
//                    map.put(matcher.group(2), matcher.group(3).replace("\" + \"", ""));
//                }
//
//                for (Map.Entry<String, String> entry : map.entrySet()) {
//                    DLog.d("\nkey: " + entry.getKey() + "\t\t" + entry.getValue());
//                }
//
//                DLog.d("@@@ " + map.get("quality_1080p"));
//                DLog.d("@@@ " + map.get("quality_720p"));
//                DLog.d("@@@ " + map.get("quality_480p"));
//                DLog.d("@@@ " + map.get("quality_240p"));
//
//
////                String url = null;
//
////                //Utils.ShowToast0(context, WENT_WRONG);
//
//            } catch (NullPointerException e) {
//                DLog.handleException(e);
//                Utils.ShowToast0(context, WENT_WRONG);
//            }
//        }
//    }
    private static void removeComments(Node node) {
        for (int i = 0; i < node.childNodes().size(); ) {
            Node child = node.childNode(i);
            if (child.nodeName().equals("#comment")) child.remove();
            else {
                removeComments(child);
                i++;
            }
        }
    }


//    private static class DownloadTikTokVideo extends AsyncTask<String, Void, Document> {
//        private Document doc;
//
//        @Override
//        protected Document doInBackground(String... urls) {
//            try {
//                Map<String, String> Headers = new HashMap<String, String>();
//                Headers.put("Cookie", "1");
//                Headers.put("User-Agent", "1");
//                Headers.put("Accept", "application/json");
//
//
//                Headers.put("Host", QBASE);
//                Headers.put("Connection", "keep-alive");
//                Connection tmp = Jsoup.connect(TIKTOKAPI).data("aweme_id", urls[0]).ignoreContentType(true).headers(Headers);
//                doc = tmp.get();
//
//            } catch (IOException e) {
//                DLog.handleException(e);
//                Log.d(TAG, "doInBackground: Error");
//                iUtils.ShowToast(context, WENT_WRONG);
//            }
//
//            return doc;
//        }
//
//        protected void onPostExecute(Document result) {
//            if (!fromService) {
//                pd.dismiss();
//            }
//            String tmp = result.body().toString();
//            Log.d(TAG, "$$$$$$$$$$$$$: " + tmp);
//            String URL = tmp.replace("<body>", "").replace("</body>", "");
//            JSONObject jsonObject;
//            try {
//                jsonObject = new JSONObject(URL);
//                String URLs = jsonObject.getJSONObject("aweme_detail").getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").getString(0);
//
//                new DownloadFile().Downloading(context, URLs, title, EXT_MP4);
//// iUtils.ShowToast(Mcontext,URLs);
//
//            } catch (JSONException err) {
//                Log.d("Error", err.toString());
//                iUtils.ShowToast(context, WENT_WRONG);
//            }
//        }
//    }


    public void downloadTikTokVideo(String url, String title) {
        errorExp = null;
        executor.execute(() -> {

            //in UI thread!
            mThread.post(() -> {
                if (!fromService) {
                    callback.hideProgressDialog();
                }
                try {
                    makeLoadRequest(context, url, title, EXT_MP4);
                } catch (Exception err) {
                    DLog.handleException(err);
                    errorExp = err;
                }
            });
        });
    }

    public void downloadLikeeVideo(String target, String title) {
        makeLoadRequest(context, target, title, EXT_MP4);
    }

    public void downloadFacebookVideo(String finalURL, String title) {
        makeLoadRequest(context, finalURL, title, EXT_MP4);
    }

    public void downloadPinterestFile(String target, String title, String ext) {
        makeLoadRequest(context, target, title, ext);
    }

    public void downloadInstagramFile(String target, String title, String ext) {
        makeLoadRequest(context, target, title, ext);
    }

    private void makeLoadRequest(Context context, String target, String title, String ext) {
        DownloadFile.newInstance().makeLoad77(context, target, title, ext);
    }
}
