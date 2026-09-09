package com.walhalla.ttloader.compose

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.walhalla.ui.DLog

class ComposeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            MobileAds.initialize(this) { status ->
                DLog.d("COMPOSE_ADS " + status)
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        }
    }
}
