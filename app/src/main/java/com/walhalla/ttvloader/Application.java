package com.walhalla.ttvloader;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository;
import com.walhalla.domain.repository.from_internet.AdvertConfig;
import com.walhalla.ui.DLog;

import java.util.ArrayList;


public class Application extends MultiDexApplication {

    public static AdvertAdmobRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        java.util.List<String> list = new ArrayList<>();
        list.add(AdRequest.DEVICE_ID_EMULATOR);
        list.add("A4FCA41661DBE5F0457211245BBF9B72");
        list.add("E21E11488E91F8F5F8C10EE86909083B");
        list.add("DC1F7C8F25CB6790BFA7ED6262D01489");

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(list)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.

        MobileAds.initialize(this, initializationStatus -> {
            DLog.d("INIT_STATUS" + initializationStatus);
        });

        AdvertConfig config = AdvertConfig.newBuilder()
                .setAppId(getString(R.string.app_id))
                .setBannerId(getString(R.string.b1))
                .build();
        repository = AdvertAdmobRepository.getInstance(config);

        //PRDownloader.initialize(getApplicationContext());


        // Setting timeout globally for the download network requests:
//        com.downloader.PRDownloaderConfig config = com.downloader.PRDownloaderConfig.newBuilder()
//                //.setReadTimeout(30_000)
//                .setDatabaseEnabled(true)
//                //.setConnectTimeout(30_000)
//                .build();
//        com.downloader.PRDownloader.initialize(getApplicationContext(), config);

//        new Thread(() ->
//                //TUtil.getKey("https://fms.1pondo.tv/hls/sample/1pondo.tv/072820_001/720p.mp4")
//                TUtil.getKey("https://fms.1pondo.tv/hls/sample/1pondo.tv/072820_001/720p.mp4Frag3Num3.ts")
//
//        ).start();

        //new PinterestIntent().videoShare(this, null);
    }
}
