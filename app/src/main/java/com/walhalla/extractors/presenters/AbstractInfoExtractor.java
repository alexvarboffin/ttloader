package com.walhalla.extractors.presenters;

import android.os.Handler;

import com.walhalla.ui.BuildConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public abstract class AbstractInfoExtractor {

    protected final Handler mThread;
    protected Executor executor = Executors.newFixedThreadPool(1);


    // Создание пустого ключевого хранилища
    protected TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };

    protected final int TIMEOUT = 35 * 1000;
    protected static final String EXT_MP4 = ".mp4";

    protected final RepositoryCallback callback;
    final VideoRepository repository;

    public AbstractInfoExtractor(RepositoryCallback callback, VideoRepository repository, Handler handler) {
        this.callback = callback;
        this.repository = repository;
        this.mThread = handler;
    }

    public abstract void execute(String url);

//    @Override
//    protected Document doInBackground(String... strings) {
//        return null;
//    }

    protected OkHttpClient defClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }
}
