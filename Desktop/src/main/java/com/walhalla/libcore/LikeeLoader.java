package com.walhalla.libcore;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.walhalla.libcore.likee.LikeeResponse;
import com.walhalla.libcore.likee.UserInfo;
import com.walhalla.libcore.likee.Video;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LikeeLoader {


    private static final int MAX_COUNT_ = 100;


    OkHttpClient client;

    public static void main(String[] args) {
        new LikeeLoader().start();
    }

    private void start() {
        try {
            client = new okhttp3.OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();

            UserInfo a = new UserInfo("maybelike", 412718202);
            //           UserInfo a = new UserInfo("yapidortransvesi", 1436856973);
            //UserInfo a = new UserInfo("mother.nyafek");
            //UserInfo a = new UserInfo("Mayby_baby_1143");

            //UserInfo a = new UserInfo("zemielfox666", 1189743070);
            //UserInfo a = new UserInfo("965573924", 696935810);
            //UserInfo a = new UserInfo("SEXSUALNI11",460354764);
            //UserInfo a = new UserInfo("Erida",1381938637);

            //@alina_efimenko94

            //getUid(a.userName);
            Set<String> result = new HashSet<>();
            File parent = new File("C:\\storage\\likee\\" + a.userName);

            if (!parent.exists()) {
                boolean aa = parent.mkdirs();
            }
            Set<String> allVideo = geAllVideo(a);
            for (String videoId : allVideo) {

                String _u = "https://likee.video/@" + a.userName + "/video/" + videoId;
                String newUrl = getVideo(_u);
                String[] mm = newUrl.split(".mp4");
                if (mm.length > 1) {
                    newUrl = newUrl.replace(mm[1], "");
                    newUrl = newUrl.replace("_4.mp4", ".mp4");
                }

                System.out.println(_u + "\t" + newUrl);

                if (newUrl.isEmpty()) {
                    System.out.println("@@@@@@@@@@@@@@ EMPTY @@@@ " + _u);
                } else {
                    result.add("wget -nc " + newUrl);
                }
            }

//        for (String videoId : uniqList) {
//            System.out.println("yt-dlp -vU https://likee.video/@" + a.userName + "/video/" + videoId);
//            //System.out.println(video.getMsgText());
//        }

            //saveSetToFile(uniqList, "C:\\storage\\likee\\"+a.userName + ".txt");

            File loader = new File(parent, a.userName + "_" + a.userId + "_" + result.size() + "lines" + ".cmd");
            if (!loader.exists()) {
                boolean aa = loader.createNewFile();
            }
            saveSetToFile(result, loader.getAbsolutePath());
            System.out.println("00000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> readSetFromFile(String fileName) {
        Set<String> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                set.add(line);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading from the file: " + e.getMessage());
        }
        return set;
    }

    private String getVideo(String url) throws IOException {
        String resp = defaultRequest(url, null);
        String eE = TxTUtil.eE(resp, "videoUrl\":\"", "\",");
        if (isEmpty(eE)) {
            eE = TxTUtil.eE(resp, "videoUrl\":\"", "\",");
        }
        return eE;
    }

    private Set<String> geAllVideo(UserInfo a) throws IOException {
        Set<String> uniqList = new HashSet<>();
        if (a.userId <= 0) {
            String uid = getUid(a.userName);
            System.out.println("[]" + uid);
            a.userId = Long.parseLong(uid);
            System.out.println("@@@");
            //return uniqList;
        }

        String lastIndex = null;

        int prev = uniqList.size();
        int newSize = uniqList.size();
        do {
            prev = uniqList.size();
            List<String> list0 = likee(a.userName, a.userId, lastIndex);//uid
            int totalsize = list0.size();
            for (int i = 0; i < totalsize; i++) {
                String item = list0.get(i);
                uniqList.add(item);
                if (i == totalsize - 1) {
                    lastIndex = item;
                }
            }
            newSize = uniqList.size();

            System.out.println("-----------------" + lastIndex);

        } while (prev < newSize);
        return uniqList;
    }

    public static void saveSetToFile(Set<String> set, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String item : set) {
                writer.write(item);
                writer.newLine();
            }
            System.out.println("Data has been written to " + fileName);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static final String URL = "https://api.like-video.com/likee-activity-flow-micro/official_website/WebView/getProfileDetail";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0";
    private static final String REQUEST_ID = "9b1d3678-64f6-46e9-b1cb-304eb3f11674";


    private String getUid(String userName) throws IOException {
        JSONObject json = new JSONObject();
        json.put("likeeId", userName);
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        String mm = defaultRequest(URL, body);
        String eE = TxTUtil.eE(mm, "\"uid\":\"", "\"");
        return eE;
    }

    private String defaultRequest(String url, RequestBody body) throws IOException {

        Request.Builder builder = new Request.Builder()
                .url(url);
        if (body != null) {
            builder.post(body);
        }
        Request request = builder.addHeader("User-Agent", USER_AGENT)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                //.addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Content-Type", "application/json")
                .addHeader("requestid", REQUEST_ID)
                .addHeader("Origin", "https://likee.video")
                .addHeader("DNT", "1")
                .addHeader("Connection", "keep-alive")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Site", "cross-site")
                .addHeader("TE", "trailers")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }


    private List<String> likee(String userName, long userId, String lastPostId) {

        List<String> videoids = new ArrayList<>();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        JSONObject json = new JSONObject();
        json.put("uid", userId);
        json.put("count", MAX_COUNT_);
        if (lastPostId != null) {
            json.put("lastPostId", lastPostId);
        }
        json.put("tabType", 0);


        //System.out.println(json.toString());

        RequestBody body = RequestBody.create(
                json.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://api.like-video.com/likee-activity-flow-micro/videoApi/getUserVideo")
                .post(body)
//                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
//                .addHeader("Accept", "application/json, text/plain, */*")
//                .addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
//                //.addHeader("Accept-Encoding", "gzip, deflate, br")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("X-Auth-Token", "o3EsTBKO07ONw2ZL4fkTyl4t51695IvRTf8GnJAqIP8woERbeOiKQ6u1bJXsFaPMXfraYmQGcSjTk1KGrkBtSY0mV4WZ8fQUB0yJFKXs8N0XvoNcoZw498tQPmak16Z9wW1JZCpnOOrlInWn2it77VC2hvlI2PNq0RjivFp22852WoQyZSib8lMxu6yx1OF4xun7G2C4cRUFSmNLDXMkp29fC9FTQiJkUVlbuic6XOlmpIBA8eB8Koz3FdxYVVFKxPQVyqwfaHiC9CZqzxjDDwDK")
//                .addHeader("X-Uid", "2643569890")
//                .addHeader("X-Client-DeviceId", "9c7e0568861749c51a52c2720ed124ac")
//                .addHeader("X-Channel", "normal")
//                .addHeader("requestid", "de088683-2053-4c0d-af97-96bc3b911283")
//                .addHeader("Origin", "https://likee.video")
//                .addHeader("DNT", "1")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Sec-Fetch-Dest", "empty")
//                .addHeader("Sec-Fetch-Mode", "cors")
//                .addHeader("Sec-Fetch-Site", "cross-site")
//                .addHeader("TE", "trailers")


//                .addHeader("User-Agent",
//                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21"
//
//                )
//                .addHeader("authority", "l.likee.video")
//                .addHeader("upgrade-insecure-requests", "1")
//                //.addHeader("user-agent", "Mozilla/5.0 (Linux; Android 8.0.0; Nexus 5X Build/OPR4.170623.006) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Mobile Safari/537.36");
//                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .addHeader("sec-fetch-site", "none")
//                .addHeader("sec-fetch-mode", "navigate")
//                .addHeader("sec-fetch-dest", "document")
//                .addHeader("dnt", "1")
//                .addHeader("Content-type:", "application/json; charset=utf-8")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String m = response.body().string();
            Type listType = new TypeToken<LikeeResponse>() {
            }.getType();


            //System.out.println(m);

            LikeeResponse posts = new Gson().fromJson(m, listType);
            List<Video> list = posts.getData().getVideoList();

            //System.out.println("===============================");

            for (Video video : list) {
                String videoId = video.getPostId();
                videoids.add(videoId);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return videoids;
    }
}
