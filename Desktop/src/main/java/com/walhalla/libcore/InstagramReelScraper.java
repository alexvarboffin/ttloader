package com.walhalla.libcore;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class InstagramReelScraper {

    public static void main(String[] args) {
        String url = "https://www.instagram.com/reel/C6gL1u2gycA/?igsh=MTkwM2lxeGdxOGQ4bw";
        try {
            InstagramReelData reelData = scrapeInstagramReel(url);
            System.out.println(reelData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InstagramReelData scrapeInstagramReel(String url) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9150));
        Connection connection = Jsoup.connect(url).proxy(proxy);
        Document doc = Jsoup.connect(url).get();

        Element videoTag = doc.select("meta[property=og:video]").first();
        String videoUrl = (videoTag != null) ? videoTag.attr("content") : null;

        Element descriptionTag = doc.select("meta[property=og:description]").first();
        String description = (descriptionTag != null) ? descriptionTag.attr("content") : null;

        Element usernameTag = doc.select("meta[property=og:title]").first();
        String username = (usernameTag != null) ? usernameTag.attr("content").split(" • ")[0] : null;

        Element timestampTag = doc.select("meta[property=og:video:release_date]").first();
        String timestamp = (timestampTag != null) ? timestampTag.attr("content") : null;

        return new InstagramReelData(username, description, videoUrl, timestamp);
    }

    static class InstagramReelData {
        String username;
        String description;
        String videoUrl;
        String timestamp;

        public InstagramReelData(String username, String description, String videoUrl, String timestamp) {
            this.username = username;
            this.description = description;
            this.videoUrl = videoUrl;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "InstagramReelData{" +
                    "username='" + username + '\'' +
                    ", description='" + description + '\'' +
                    ", videoUrl='" + videoUrl + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}
