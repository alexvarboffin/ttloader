package com.walhalla.extractors.presenters

import android.os.Handler
import com.walhalla.ui.BuildConfig
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

abstract class AbstractInfoExtractor {
    protected lateinit var mThread: Handler
    protected var executor: Executor = Executors.newFixedThreadPool(1)
    // Создание пустого ключевого хранилища
    protected var trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return emptyArray()
            }
            override fun checkClientTrusted(certs: Array<java.security.cert.X509Certificate>, authType: String) {
            }
            override fun checkServerTrusted(certs: Array<java.security.cert.X509Certificate>, authType: String) {
            }
        })
    protected val TIMEOUT: Int = 35 * 1000
    protected lateinit var callback: RepositoryCallback
    lateinit var repository: VideoRepository
    constructor(callback: RepositoryCallback, repository: VideoRepository, handler: Handler) {
        this.callback = callback
        this.repository = repository
        this.mThread = handler
    }
    abstract fun execute(url: String)
    //    @Override
    //    protected Document doInBackground(String... strings) {
    //        return null;
    //    }
    protected fun defClient(): OkHttpClient {
        var builder: OkHttpClient.Builder = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            var loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }
    companion object {
        protected const val EXT_MP4 = ".mp4"
    }
}
