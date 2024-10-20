package com.walhalla.libcore;

public class FacebookUtils {

    public static void main(String[] args) {
        String aa = getFaceBookUrl("https:\\\\scontent-cdg4-3.xx.fbcdn.net\\v\\t50.33967-16\\435966760_959006542384716_6863818273081530899_n.mp4?_nc_cat104&ccb1-");
        System.out.println(aa);

    }

    public static void parse(String resp) {
        System.out.println(resp);
    }

    public static String getFaceBookUrl(String url) {
        if (url.contains("https:\\\\")) {
            url = url.replace("\\", "/");
        }
        return url;
    }
}
