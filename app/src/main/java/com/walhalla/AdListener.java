package com.walhalla;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;


public class AdListener extends com.google.android.gms.ads.AdListener {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private final Object mObject;

    //Constructor #1
    public AdListener(Object o) {
        super();
        this.mObject = o;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();

        if (DEBUG) {
            if (mObject instanceof AdView) {
                DLog.d("onAdClosed: " + ((AdView) mObject).getAdUnitId());
            } else if (mObject instanceof InterstitialAd) {
                InterstitialAd interstitialAd = (InterstitialAd) mObject;
                DLog.d("onAdClosed: " + interstitialAd.getAdUnitId());

                // Load the next interstitial.
                //@@interstitialAd.loadAd(AdMobCase.buildAdRequest());
            }
        }
    }

    @Override
    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        super.onAdFailedToLoad(loadAdError);
        if (DEBUG) {
            String errorReason = "";
            switch (loadAdError.getCode()) {
                case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                    errorReason = "Internal error";
                    break;
                case AdRequest.ERROR_CODE_INVALID_REQUEST:
                    errorReason = "Invalid request";
                    break;
                case AdRequest.ERROR_CODE_NETWORK_ERROR:
                    errorReason = "Network Error";
                    break;
                /*
                 * The ad request was successful, but no ad was returned due to lack of ad inventory.
                 * */
                case AdRequest.ERROR_CODE_NO_FILL:
                    errorReason = "No fill";

//                PACKAGE_NAME_KEY_LEGACY_VISIBLE}, //#{@link #PACKAGE_NAME_KEY_LEGACY_NOT_VISIBLE
//                case VISIBILITY_UNDEFINED:
//                    errorReason = "onAdFailedToLoad: VISIBILITY_UNDEFINED";
                    break;
            }

            if (mObject instanceof AdView) {
                DLog.d(String.format("Ad %s failed to load with error %s.", ((AdView) mObject).getAdUnitId(), errorReason));

                if (loadAdError.getCode() == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                    if (((AdView) mObject).getVisibility() == View.VISIBLE) {
                        ((AdView) mObject).setVisibility(View.GONE);
                    }
                }
            } else if (mObject instanceof InterstitialAd) {
                DLog.d(String.format("Ad %s failed to load with error %s.", ((InterstitialAd) mObject).getAdUnitId(), errorReason));
            }
        }
    }


    @Override
    public void onAdOpened() {
        super.onAdOpened();
        if (mObject instanceof AdView) {
            DLog.d("onAdOpened: " + ((AdView) mObject).getAdUnitId());
        } else if (mObject instanceof InterstitialAd) {
            DLog.d("onAdOpened: " + ((InterstitialAd) mObject).getAdUnitId());
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

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();

        if (mObject instanceof AdView) {
            DLog.d("onAdLoaded: " + ((AdView) mObject).getAdUnitId());
            if (((AdView) mObject).getVisibility() == View.GONE) {
                ((AdView) mObject).setVisibility(View.VISIBLE);
            }
        } else if (mObject instanceof InterstitialAd) {
            DLog.d("onAdLoaded: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();

        if (mObject instanceof AdView) {
            DLog.d("onAdClicked: " + ((AdView) mObject).getAdUnitId());
        } else if (mObject instanceof InterstitialAd) {
            DLog.d("onAdClicked: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }

    @Override
    public void onAdImpression() {
        super.onAdImpression();

        if (mObject instanceof AdView) {
            DLog.d("onAdImpression: " + ((AdView) mObject).getAdUnitId());
        } else if (mObject instanceof InterstitialAd) {
            DLog.d("onAdImpression: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }
}
