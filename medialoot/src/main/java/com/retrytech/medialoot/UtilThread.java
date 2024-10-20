package com.retrytech.medialoot;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class UtilThread {
    private static final Handler sHandler = new Handler(Looper.getMainLooper());
    private static Handler workThreadHandler;

    public static Handler getMainThreadHandler() {
        return sHandler;
    }

    public synchronized static Handler getWorkThreadHandler() {
        if (workThreadHandler == null) {
            HandlerThread ht = new HandlerThread("worker.thread.handler");
            ht.start();
            workThreadHandler = new Handler(ht.getLooper());
        }
        return workThreadHandler;
    }

    public static Thread newThread(Runnable runnable) {
        Thread t = new Thread(runnable, "screencast.thread");
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
