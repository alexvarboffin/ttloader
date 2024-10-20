package com.walhalla.libcore.likee;

import com.walhalla.libcore.TxTUtil;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PinterestExtractor {
    private static final OkHttpClient client;

    static {
        // Set up a CookieManager with a custom CookiePolicy
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy((uri, cookie) -> {
            // Add custom logic to accept/reject cookies
            return true; // Accept all cookies
        });
        java.net.CookieHandler.setDefault(cookieManager);

        // Set up OkHttpClient
        client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                //.cookieJar(new JavaNetCookieJar(cookieManager))
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    public static List<String> loadUrlsList(File baseFile) {
        try {
            List<String> urls = Files.readAllLines(baseFile.toPath());
            return urls;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        List<String> m = loadUrlsList(new File("C:\\dictionaries\\pin_url.txt"));
        new PinterestExtractor().test(m);
        //new PinterestExtractor().test("https://www.pinterest.com/pin/107312403612599381");

    }

    private void test(List<String> m) {
        for (String s : m) {
            test(s);
        }
    }


    private void test(String url) {
        try {
            // Set up request headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            //headers.put("Accept-Encoding", "deflate, br");
            headers.put("Accept-Language", "en-US,en;q=0.9");
            headers.put("Range", "bytes=0-200000");
            headers.put("Cache-Control", "max-age=0");

            // Build request
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21");

            // Add headers to the request
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }

            Request request = requestBuilder.build();

            // Execute request
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                // Get response body as a string
                String responseBody = response.body().string();

                // Parse response with Jsoup
                //Document doc = Jsoup.parse(responseBody);

                // Extract desired content
                String V0START = "\"contentUrl\":\"";
                String thumb_if_video_or_img_if_img = "\"imageSpec_orig\":{\"url\":\"";
                String END = "\"";
                String defaultQuality = "";
                if (responseBody.contains(V0START)) {
                    int strIndex = responseBody.indexOf(V0START) + V0START.length();
                    defaultQuality = responseBody.substring(strIndex, responseBody.indexOf("\"", responseBody.indexOf(V0START) + V0START.length()));
                }
                if (StringUtils.isEmpty(defaultQuality) && responseBody.contains(thumb_if_video_or_img_if_img)) {
                    defaultQuality = TxTUtil.eE(responseBody, thumb_if_video_or_img_if_img, "\"");
                    //System.out.println("111");
                }
                //System.out.println("[] " + defaultQuality);
//                https://v1.pinimg.com/videos/mc/720p/e1/e5/f6/e1e5f65ab0eb267ff38155cf3990d336.mp4
//                https://v1.pinimg.com/videos/mc/expMp4/e1/e5/f6/e1e5f65ab0eb267ff38155cf3990d336_t1.mp4

                String goodQuality = defaultQuality;
                if (defaultQuality.contains("/expMp4/") && defaultQuality.endsWith("_t1.mp4")) {
                    goodQuality = defaultQuality
                            .replace("/expMp4/", "/720p/")
                            .replace("_t1.mp4", ".mp4");
                }
                if (goodQuality.endsWith(".jpg")) {
                    System.out.println("" + goodQuality + " --> " + url);
                } else {
                    System.out.println("" + goodQuality);
                }

                //download(goodQuality);
            }
        } catch (Exception e) {
            System.out.println("----> " + url);
            e.printStackTrace();

        }
    }

    private void download(String goodQuality) {
        String[] tmp = goodQuality.split("/");
        String fileName = tmp[tmp.length - 1];
        File savePath = new File(new File("C:\\Users\\combo\\Downloads"), fileName);
        try (BufferedInputStream in = new BufferedInputStream(new URL(goodQuality).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

        } catch (IOException e) {
            // Обработка исключений
            e.printStackTrace();
        }
    }
}
