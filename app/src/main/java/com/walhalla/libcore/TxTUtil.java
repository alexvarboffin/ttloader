package com.walhalla.libcore;

import com.walhalla.ui.DLog;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.regex.Pattern;

public class TxTUtil {

    public static String onlyExtractFacebook(String resp, String part0, String s1) {
        String sJava = "";
        try {
            String[] aa = resp.split(Pattern.quote(part0));
            if (aa.length > 0) {
                String buff = aa[1];
                //DLog.d("EXTRACTOR: " + resp.contains(part0) + " " + buff);
                sJava = buff.split(Pattern.quote(s1))[0];
                DLog.d("[EXTRACTOR] " + sJava);
            }
            return sJava;
        } catch (Exception e) {
            DLog.handleException(e);
            return sJava;
        }
    }

    public static String eE(String resp, String part0, String s1) {
        String sJava = "";
        try {
            String[] aa = resp.split(Pattern.quote(part0));
            if (aa.length > 0) {
                String buff = aa[1];
                //DLog.d("EXTRACTOR: " + resp.contains(part0) + " " + buff);
                sJava = buff.split(Pattern.quote(s1))[0];
                DLog.d("EXTRACTOR: " + sJava);
            }
            try {
                String raw = StringEscapeUtils.unescapeJava(sJava);//fail if convert tiktokurl
                String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
                return raw.replaceAll(regex, "");
            } catch (Exception e) {
                DLog.handleException(e);
                return sJava;
            }
        } catch (Exception e) {
            DLog.handleException(e);
            return "";
        }
    }


    //java decode unicode escape sequence
    public static String extract(String data, String o) {
        try {
            String sJava = data.split(Pattern.quote("\"" + o + "\":\""))[1].split(Pattern.quote("\""))[0];
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
        String[] aa = resp.split(Pattern.quote(s));
        String sJava = "";
        if (aa.length > 0) {
            String buff = aa[1];
            sJava = buff.split(Pattern.quote(s1))[0].trim();
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
