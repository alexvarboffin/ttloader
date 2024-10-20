package com.walhalla.libcore;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BaseDownloaderRepository {

    private static final String FACEBOOK_USERAGENT =
            //"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0";
            //+++"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.47 Safari/537.36";
            //"Mozilla/5.0 (Linux; Android 7.1.2; cepheus Build/N2G48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.70 Mobile Safari/537.36";
            "Mozilla/5.0 (Linux; Android 7.1.2; cepheus Build/N2G48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.70 Mobile Safari/537.36";


    // https://video-ams2-1.xx.fbcdn.net/o1/v/t2/f1/m69/GKbmZxkuL89WjtIDAEwr9WBW2sd1bmdjAAAF.mp4?efg=eyJ2ZW5jb2RlX3RhZyI6Im9lcF9oZCJ9&_nc_ht=video-ams2-1.xx.fbcdn.net&_nc_cat=109&strext=1&vs=a94469f793a74aaf&_nc_vs=HBksFQIYOnBhc3N0aHJvdWdoX2V2ZXJzdG9yZS9HS2JtWnhrdUw4OVdqdElEQUV3cjlXQlcyc2QxYm1kakFBQUYVAALIAQAVAhg6cGFzc3Rocm91Z2hfZXZlcnN0b3JlL0dQc2diZ3VvX2NsZ2lJUUFBRlp0NVFZWmFPRTdidjRHQUFBRhUCAsgBAEsHiBJwcm9ncmVzc2l2ZV9yZWNpcGUBMQ1zdWJzYW1wbGVfZnBzABB2bWFmX2VuYWJsZV9uc3ViACBtZWFzdXJlX29yaWdpbmFsX3Jlc29sdXRpb25fc3NpbQAoY29tcHV0ZV9zc2ltX29ubHlfYXRfb3JpZ2luYWxfcmVzb2x1dGlvbgAddXNlX2xhbmN6b3NfZm9yX3ZxbV91cHNjYWxpbmcAEWRpc2FibGVfcG9zdF9wdnFzABUAJQAcjBdAAAAAAAAAABERAAAAJrCXuaadqpAHFQIoAkMzGAt2dHNfcHJldmlldxwXQEiZmZmZmZoYIWRhc2hfZ2VuMmh3YmFzaWNfaHEyX2ZyYWdfMl92aWRlbxIAGBh2aWRlb3MudnRzLmNhbGxiYWNrLnByb2Q4ElZJREVPX1ZJRVdfUkVRVUVTVBsKiBVvZW1fdGFyZ2V0X2VuY29kZV90YWcGb2VwX2hkE29lbV9yZXF1ZXN0X3RpbWVfbXMBMAxvZW1fY2ZnX3J1bGUHdW5tdXRlZBNvZW1fcm9pX3JlYWNoX2NvdW50BzE1NjgwNzARb2VtX2lzX2V4cGVyaW1lbnQADG9lbV92aWRlb19pZBAyMDA2MjM0NzEyODY0MjU3Em9lbV92aWRlb19hc3NldF9pZBAyMDA2MjM0NzA2MTk3NTkxFW9lbV92aWRlb19yZXNvdXJjZV9pZBAyMDA2MjM0Njk2MTk3NTkyHG9lbV9zb3VyY2VfdmlkZW9fZW5jb2RpbmdfaWQQMTExMDQwNzU3MzQ5NjE1NA52dHNfcmVxdWVzdF9pZAAlAhwAJb4BGweIAXMEOTg4NgJjZAoyMDIxLTA1LTI5A3JjYgcxNTY4MDAwA2FwcAVWaWRlbwJjdBlDT05UQUlORURfUE9TVF9BVFRBQ0hNRU5UE29yaWdpbmFsX2R1cmF0aW9uX3MGNDkuMjk1AnRzFXByb2dyZXNzaXZlX2VuY29kaW5ncwA%3D&ccb=9-4&oh=00_AfDqYUFxg2c1pmkjmFnE8hnBpim-ARng1uWMGfuyG4ndmQ&oe=66272C84&_nc_sid=1d576d&_nc_rid=554143279723899&_nc_store_type=1&dl=1
    //static String url = "https://www.facebook.com/omar.khayyam.citati/videos/2006234712864257/";
    //static String url = "https://www.facebook.com/share/v/ng3mMYKqerNM1toV/";
    static String url = "https://www.facebook.com/share/p/yQTjiNJNMmBHAf9d/";

    //https://fb.watch/rCeA6aCNH_/
    //https://fb.watch/rCffz628lB/

    //static String url = "http://httpbin.org/get";

    public static void main(String[] args) {
        System.out.println("===================");

        new BaseDownloaderRepository().getFacebookVideo(url);
        System.out.println("===================");
    }

    Executor executor = Executors.newFixedThreadPool(1);


    public static final int TIMEOUT = 35 * 1000;
    private static final String EXT_MP4 = ".mp4";


    private void getFacebookVideo(String url) {

        executor.execute(() -> {
            try {
                //doc = Jsoup.connect(FacebookApi).data("v",urls[0]).get();
//                try {
//                    SSLContext sc = SSLContext.getInstance("SSL");
//                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
//                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//                } catch (Exception e) {
//                    DLog.handleException(e);
//                }

                Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9150));

                Document doc = Jsoup.connect(url)
                        .timeout(TIMEOUT)
                        .proxy(proxy)
                        //.userAgent()
                        //.userAgent(rrr)
                        .userAgent(FACEBOOK_USERAGENT)
                        .ignoreContentType(true)
                        //.headers(headers)
                        //.headers("Content-Type: application/json")
                        .followRedirects(true)
                        .get();

                try {
                    //v1
                    String resp = doc.toString();
                    FacebookUtils.parse(resp);

                    //String URL = TxTUtil.eE(resp, "playable_url_quality_hd", "\"");
                    String URL = TxTUtil.eE(resp, "browser_native_hd_url\":\"", "\",");
                    if (isEmpty(URL)) {
                        URL = TxTUtil.eE(resp, "browser_native_sd_url\":\"", "\",");
                    }
//"browser_native_sd_url"
                    //
                    if (isEmpty(URL)) {
                        //v2
                        DLog.d("======>>0" + URL);
                        Elements mm = doc.select("meta[property=\"og:video\"]");
                        if (mm != null) {
                            System.out.println("@@"+mm.toString());

                            Element nn = mm.last();
                            if (nn != null) {
                                URL = nn.attr("content");
                            }
                        }
                    }else {

                    }

                    DLog.d("--------------------------------");
                    DLog.d("" + URL);
                    DLog.d("--------------------------------");

                    // iUtils.ShowToast(Mcontext,URL);
                    System.out.println(URL);
                    //DownloadFile.newInstance().makeLoad(context, URL, doc.title(), EXT_MP4);

                } catch (NullPointerException e) {
                    DLog.handleException(e);
//                    mThread.post(() -> {
//                        ERR-TOAST
//                    });
                }
                // new DownloadTikTokVideo().execute(URL);
//
//                mThread.post(() -> {
//                    if (!fromService) {
//                        callback.hideProgressDialog();
//                    }
//                });

            } catch (IOException e) {
                DLog.handleException(e);
//                mThread.post(() -> {
//                    if (!fromService) {
//                        callback.hideProgressDialog();
//                    }
//                    ERR-TOAST
//                });
            }
        });
    }
}