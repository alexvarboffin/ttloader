package com.walhalla.extractors.presenters

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import com.walhalla.extractors.ExUtils
import com.walhalla.extractors.FaceBookExtractor
import com.walhalla.extractors.LikeExtractor
import com.walhalla.extractors.PinterestExtractor
import com.walhalla.extractors.TTExtractor
import com.walhalla.extractors.TrillerExtractor
import com.walhalla.ttloader.core.R
import com.walhalla.ttvloader.common.NetworkType
import com.walhalla.ttvloader.receiver.DownloadFile
import com.walhalla.ui.DLog
import org.jsoup.nodes.Node
import java.util.List
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import android.content.Context.MODE_PRIVATE
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class VideoRepository {
    private lateinit var context: Context
    private var errorExp: Exception? = null
    var trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return emptyArray()
            }
            override fun checkClientTrusted(certs: Array<java.security.cert.X509Certificate>, authType: String) {
            }
            override fun checkServerTrusted(certs: Array<java.security.cert.X509Certificate>, authType: String) {
            }
        })
    var executor: Executor = Executors.newFixedThreadPool(1)
    private lateinit var callback: RepositoryCallback
    private lateinit var mThread: Handler
    fun onPostExecute0() {
        if (!fromService) {
            callback.hideProgressDialog()
        }
    }
    constructor(context: Context, callback: RepositoryCallback, handler: Handler) {
        this.callback = callback
        this.mThread = handler
        this.context = context
    }
    //public Context context;
    @JvmField var prefs: SharedPreferences? = null
    fun makeDownload(urlIn: String, service: Boolean, removeWatermark: Boolean) {
        var url = urlIn
        //this.context = context;
        fromService = service
        //SessionID=title;
        if (url.startsWith("http://") && url.startsWith("https://")) {
            url = "http://" + url
        }
        if (!fromService) {
            callback.showProgressDialog()
        }
        if (url.contains(NetworkType.TIKTOK__.getValue())) {
            var executor0: GetTikTokVideo = GetTikTokVideo(removeWatermark, callback, this, mThread)
            executor0.execute(url)
        } else {
            //        else if (url.contains("twitter.com")) {
            //            DLog.d("@\t\t" + url);
            //        }
            var tmp: MutableList<TTExtractor> = ExUtils.defExtractors()
            var resolved: TTExtractor? = null
            for (extractor in tmp) {
                if (extractor.isUrlValid(url)) {
                    resolved = extractor
                    break
                }
            }
            if (resolved == null) {
                //DLog.d("----------------");
                if (!fromService) {
                    if (callback != null) {
                        callback.hideProgressDialog()
                        callback.errorResult(R.string.err_www_not_support)
                    }
                }
            } else {
                if (resolved is PinterestExtractor) {
                    var mm: PinterestPresenter = PinterestPresenter(callback, this, mThread)
                    mm.execute(url)
                } else if (resolved is FaceBookExtractor) {
                    val newValue: String = resolved.getClearUrl(url)
                    //String[] Furl = url.split("/").toTypedArray();
                    // url = Furl[Furl.length-1];
                    //iUtils.ShowToast(Mcontext,Furl[Furl.length-1]);
                    var mm: FacebookPresenter = FacebookPresenter(callback, this, mThread)
                    mm.execute(newValue)
                } else if (resolved is InstaExtractor) {
                    var mm: InstagramPresenter = InstagramPresenter(callback, this, mThread)
                    mm.execute(url)
                } else if (resolved is LikeExtractor) {
                    //int aa = url.indexOf("l.likee.video/v/");
                    var mm: GetLikeeVideo = GetLikeeVideo(removeWatermark, callback, this, mThread)
                    mm.execute(url)
                } else if (resolved is TrillerExtractor) {
                    var mm: AbstractInfoExtractor = GetTrillerVideo(removeWatermark, callback, this, mThread)
                    mm.execute(url)
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
        //                    String tm0 = parsed.getPath().split("/").toTypedArray()[1];
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
        prefs = context.getSharedPreferences("AppConfig", MODE_PRIVATE)
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
    ////                    DLog.d("" + index);//tt.substring(index)
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
    //                for (Map.Entry<String, String> entry : map.entries) {
    //                    DLog.d("\nkey: " + entry.key + "\t\t" + entry.value);
    //                }
    //
    //                DLog.d("@ " + map.get("quality_1080p"));
    //                DLog.d("@ " + map.get("quality_720p"));
    //                DLog.d("@ " + map.get("quality_480p"));
    //                DLog.d("@ " + map.get("quality_240p"));
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
    //            String tmp = result.body.toString();
    //            Log.d(TAG, "\$\$\$\$\$\$\$\$\$\$\$\$\$: " + tmp);
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
    fun downloadTikTokVideo(url: String, title: String) {
        errorExp = null
        executor.execute({
                //in UI thread!
                mThread.post({
                        if (!fromService) {
                            callback.hideProgressDialog()
                        }
                        try {
                            makeLoadRequest(context, url, title, EXT_MP4)
                        } catch (err: Exception) {
                            DLog.handleException(err)
                            errorExp = err
                        }
                        })
                })
    }
    fun downloadLikeeVideo(target: String, title: String) {
        makeLoadRequest(context, target, title, EXT_MP4)
    }
    fun downloadFacebookVideo(finalURL: String, title: String) {
        makeLoadRequest(context, finalURL, title, EXT_MP4)
    }
    fun downloadPinterestFile(target: String, title: String, ext: String) {
        makeLoadRequest(context, target, title, ext)
    }
    fun downloadInstagramFile(target: String, title: String, ext: String) {
        makeLoadRequest(context, target, title, ext)
    }
    private fun makeLoadRequest(context: Context, target: String, title: String, ext: String) {
        DownloadFile.newInstance().makeLoad77(context, target, title, ext)
    }
    companion object {
        @JvmField val ERROR_WENT_WRONG: Int = R.string.abc_something_went_wrong
        @JvmField val TIMEOUT: Int = 35 * 1000
        private const val EXT_MP4 = ".mp4"
        const val EXT_JPG = ".jpg"
        @JvmField var dialog: Dialog? = null
        @JvmField var SessionID: String? = null
        @JvmField var error: Int = 1
        @JvmField var fromService: Boolean = false
        @JvmStatic fun isEmpty(cs: CharSequence): Boolean {
            return cs == null || cs.length == 0
        }
        @JvmStatic private fun removeComments(node: Node) {
            var i: Int = 0
            while (i < node.childNodes().size) {
                var child: Node = node.childNode(i)
                if (child.nodeName().equals("#comment")) {
                    child.remove()
                } else {
                    removeComments(child)
                    i++
                }
            }
        }
    }
}
