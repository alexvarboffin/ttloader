package com.walhalla.libcore;


import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;


public class TxTUtil {


    public static void main(String[] args) {


        test0();
    }

    private static void test0() {//0
//        String str = "apple,,,orange,,,banana";
//        String delimiter = ",,,";
//        String[] parts = str.split(Pattern.quote(delimiter));
        try {
            String str = "aaaa url=..=url...url=....=url";
            //String delimiter = "url={2,3}";
            String delimiter = "url=";

            String[] parts = str.split(delimiter);

            System.out.println(Arrays.toString(parts));  // [apple, orange, banana,, grape]

            System.out.println(Pattern.quote(delimiter));
            System.out.println(Arrays.toString(parts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test1() {//0
//        String str = "apple,,,orange,,,banana";
//        String delimiter = ",,,";
//        String[] parts = str.split(Pattern.quote(delimiter));
        try {
            String str = "f,\"imageSpec_orig\":{\"__typename\":\"ImageDetails\",\"url\":\"https://i.pinimg.com/origcc.jpg\"},\"imageLargeUrl\":\"https://i.pinimg.com/1200x/43/96/a8/4396a874edc911a6e1b4543f716979cc.jpg\",\"descriptionHtml\":\" \",\"altText\":null,\"autoAltText\":\"lightning strikes in the night sky with bright blue lights\",\"isHidden\":false,\"shuffle\":null,\"imageSpec_60x60\":{\"url\":\"https://i.pinimg.com/60x60/43/96/a8/4396a874edc911a6e1b4543f716979cc.jpg\"},\"imageSpec_170x\":{\"url\":\"https://i.pinimg.com/236x/43/96/a8/4396a874edc911a6e1b4543f716979cc.jpg\"},\"imageSpec_474x\":{\"url\":\"https://i.pinimg.com/474x/43/96/a8/4396a874edc911a6e1b4543f716979cc.jpg\"},\"imageSpec_564x\":{\"url\":\"https://i.pinimg.com/564x/43/96/a8/4396a874edc911a6e1b4543f716979cc.jpg\"},\"imageSpec_736x\":{\"url\":\"https://i.pinimg.com/736x/43/96/a8/4396a874edc911a6e1b4543f716979cc.jpg\"},\"imageSpec_600x315\":{\"url\":\"https://i.pinimg.com/600x315/43/96/a8/4396a874edc911a6e1b4543f716979cc.jpg\"},\"shouldMute\":false,\"musicAttributions\":[],\"entityId\":\"1125968650525909\",\"mediaAttribution\":null,\"id\":\"UGluOjExMjU5Njg2NTA1MjU5MDk=\"}}}}}</script><div hidden id=\"S:5\"><div class=\"XiG zI7 iyn Hsu\" style=\"width:100%\"><div data-layout-shift-boundary-id=\"CloseupPageContainer\" class=\"wYR zI7 iyn Hsu \"><div data-layout-shift-boundary-id=\"CloseupPageContainerInner\" class=\"ZZS oy8 zI7 iyn Hsu\" style=\"transition:opacity 0.2s ease-out\"><div data-test-id=\"breadcrumbs\" class=\"zI7 iyn Hsu\"><style>.article-row {\n" +
                    "    -ms-overflow-style: none;\n" +
                    "9cc.jpg\"},\"imageSpec_orig\":{\"url\":\"https://i.pinimg.com/originals/43/96/a8/4396" +
                    "";
            String delimiter = "\"imageSpec_orig\":{\"url\":\"";
            String[] parts = str.split(delimiter);
//        String str = "apple.orange.banana";
//        String delimiter = ".";
//        String[] parts = str.split(Pattern.quote(delimiter));

            System.out.println(Pattern.quote(delimiter));
            System.out.println(Arrays.toString(parts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String eE(String resp, String part0, String s1) {
        String sJava = "";
        try {
            String[] aa = resp.split(Pattern.quote(part0));//PatternSyntaxException: Illegal repetition
            if (aa.length > 0) {
                String buff = aa[1];
                sJava = buff.split(Pattern.quote(s1))[0];
//                System.out.println("@0@" + sJava);
//                if (aa.length > 2) {
//                    System.out.println("@1@" + aa[2].split(Pattern.quote(s1))[0]);
//                }
            }
//            System.out.println(sJava);
            String raw = StringEscapeUtils.unescapeJava(sJava);//fail if convert tiktokurl and pinterest
//            System.out.println("@@@@" + raw);
            String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
            return raw.replaceAll(regex, "");
        } catch (Exception e) {

            e.printStackTrace();
            return sJava;
        }
    }


    //java decode unicode escape sequence
    public static String extract(String data, String o) {
        try {
            String sJava = data.split("\"" + o + "\":\"")[1].split("\"")[0];
            try {
                String raw = StringEscapeUtils.unescapeJava(sJava);//fail if convert tiktokurl
                String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
                return raw.replaceAll(regex, "");
            } catch (Exception e) {
                DLog.handleException(e);
                return sJava;
            }
        } catch (Exception ignore) {
            return "";
        }
    }

    public static String eURL(String resp, String s, String s1) {
        String[] aa = resp.split(s);
        String sJava = "";
        if (aa.length > 0) {
            String buff = aa[1];
            sJava = buff.split(s1)[0].trim();
        }
        String tmp = sJava;
        //String tmp = escape99(sJava);
        sJava = sJava
//
//                .replace("\\u003D", "%3D")
//                .replace("\\u0026", "%26")
//
////                .replace("\\u002F", "/")
////                .replace("\\u003F", "?")
////            //    .replace("\\u003D", "=")
////                .replace("\\u0026", "&")
////                .replace("\\u002523", "#")
////
////                .replace("%2F", "/")
////                .replace("%3F", "?")
////            //    .replace("%3D", "=")
////                .replace("%26", "&")
////                .replace("%2523", "#")
                .replace("\\u0026", "&")
                .replace("%3D%3D", "==");

        DLog.d("0 - EXTRACTOR-> : [" + sJava.length() + "] " + sJava);
        DLog.d("1 - EXTRACTOR-> : [" + tmp.length() + "] " + tmp);
        return sJava;
    }

    public static String escape99(String s) {
        if (s == null)
            return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }

    /**
     * @param s  - Must not be null.
     * @param sb
     */
    static void escape(String s, StringBuffer sb) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                    if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F') || (ch >= '\u2000' && ch <= '\u20FF')) {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }//for
    }
}
