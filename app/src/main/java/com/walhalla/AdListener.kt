package com.walhalla

import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.walhalla.ui.BuildConfig
import com.walhalla.ui.DLog

class AdListener : com.google.android.gms.ads.AdListener {
    private val mObject: Any
    //Constructor #1
    constructor(o: Any) : super() {
        this.mObject = o
    }

    override fun onAdClosed() {
        super.onAdClosed()
        if (DEBUG) {
            if (mObject is AdView) {
                DLog.d("onAdClosed: " + (mObject as AdView).adUnitId)
            } else if (mObject is InterstitialAd) {
                val interstitialAd = mObject as InterstitialAd
                DLog.d("onAdClosed: " + interstitialAd.adUnitId)
                // Load the next interstitial.
                //interstitialAd.loadAd(AdMobCase.buildAdRequest());
            }
        }
    }

    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
        super.onAdFailedToLoad(loadAdError)
        if (DEBUG) {
            var errorReason = ""
            when (loadAdError.code) {
                AdRequest.ERROR_CODE_INTERNAL_ERROR -> errorReason = "Internal error"
                AdRequest.ERROR_CODE_INVALID_REQUEST -> errorReason = "Invalid request"
                AdRequest.ERROR_CODE_NETWORK_ERROR -> errorReason = "Network Error"
                /*
                 * The ad request was successful, but no ad was returned due to lack of ad inventory.
                 * */
                AdRequest.ERROR_CODE_NO_FILL -> {
                    errorReason = "No fill"
                    //                PACKAGE_NAME_KEY_LEGACY_VISIBLE}, //#{@link #PACKAGE_NAME_KEY_LEGACY_NOT_VISIBLE
                    //                case VISIBILITY_UNDEFINED:
                    //                    errorReason = "onAdFailedToLoad: VISIBILITY_UNDEFINED";
                }
            }
            if (mObject is AdView) {
                DLog.d(String.format("Ad %s failed to load with error %s.", (mObject as AdView).adUnitId, errorReason))
                if (loadAdError.code == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                    if ((mObject as AdView).visibility == View.VISIBLE) {
                        (mObject as AdView).visibility = View.GONE
                    }
                }
            } else if (mObject is InterstitialAd) {
                DLog.d(String.format("Ad %s failed to load with error %s.", (mObject as InterstitialAd).adUnitId, errorReason))
            }
        }
    }

    override fun onAdOpened() {
        super.onAdOpened()
        if (mObject is AdView) {
            DLog.d("onAdOpened: " + (mObject as AdView).adUnitId)
        } else if (mObject is InterstitialAd) {
            DLog.d("onAdOpened: " + (mObject as InterstitialAd).adUnitId)
        }
    }

    //    @Override
    //    public void onAdLeftApplication() {
    //        super.onAdLeftApplication();
    //        if (mObject instanceof AdView) {
    //            DLog.d("onAdLeftApplication: " + ((AdView) mObject).getAdUnitId());
    //        } else if (mObject instanceof InterstitialAd) {
    //            DLog.d("onAdLeftApplication: " + ((InterstitialAd) mObject).getAdUnitId());
    //        }
    //    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        if (mObject is AdView) {
            DLog.d("onAdLoaded: " + (mObject as AdView).adUnitId)
            if ((mObject as AdView).visibility == View.GONE) {
                (mObject as AdView).visibility = View.VISIBLE
            }
        } else if (mObject is InterstitialAd) {
            DLog.d("onAdLoaded: " + (mObject as InterstitialAd).adUnitId)
        }
    }

    override fun onAdClicked() {
        super.onAdClicked()
        if (mObject is AdView) {
            DLog.d("onAdClicked: " + (mObject as AdView).adUnitId)
        } else if (mObject is InterstitialAd) {
            DLog.d("onAdClicked: " + (mObject as InterstitialAd).adUnitId)
        }
    }

    override fun onAdImpression() {
        super.onAdImpression()
        if (mObject is AdView) {
            DLog.d("onAdImpression: " + (mObject as AdView).adUnitId)
        } else if (mObject is InterstitialAd) {
            DLog.d("onAdImpression: " + (mObject as InterstitialAd).adUnitId)
        }
    }

    companion object {
        private val DEBUG: Boolean = BuildConfig.DEBUG
    }
}
