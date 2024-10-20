package com.walhalla.extractors.presenters;

import com.walhalla.ui.DLog;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class TUtil {

    public static String rrr = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:28.0) Gecko/20100101 Firefox/28.0";
    public static String rrr1 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36";

    public static final int TIMEOUT = 35 * 1000;

    public static String getTickTokKey(String contentURL) {
        String key = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Accept-Language", "en-US,en;q=0.9");
            headers.put("Range", "bytes=0-200000");
            headers.put("Cache-Control", "max-age=0");

            Document doc = Jsoup.connect(contentURL)
                    .timeout(TIMEOUT)
                    .userAgent(rrr1)
                    .ignoreContentType(true)
                    .headers(headers)
                    .followRedirects(true)
                    .get();
            String data = doc.toString();
            String[] tmp = data.split("vid:");

            if (tmp.length > 1) {
                key = tmp[1].split("%")[0].trim();
            }
            DLog.d("" + (tmp.length > 1) + "\t" + key + " ||| getKey: " + data);

            return key;
        } catch (HttpStatusException e) {
            DLog.handleException(e);
            DLog.d("@e1@" + contentURL);
        } catch (Exception e) {
            DLog.handleException(e);
            DLog.d("@e2@" + contentURL);
        }
        return key;
    }
}