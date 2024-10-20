package com.walhalla.libcore;

public class DLog {
    public static void d(String s) {
        System.out.println(s);
    }

    public static void handleException(Exception e) {
        System.out.println(e);
    }
}
