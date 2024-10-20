package com.walhalla;

import static com.walhalla.abcsharedlib.SharedNetwork.Package;
import static com.walhalla.abcsharedlib.SharedNetwork.Package.tube;
import static com.walhalla.intentresolver.utils.TextUtilz.dec0;

import android.content.Context;

import com.walhalla.adapters.AppModel;

import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.utils.Utils;
import com.walhalla.ui.utils.PackageUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {

    private static final String BuildConfigFLAVOR = com.walhalla.ttvloader.BuildConfig.FLAVOR;
    public static final boolean BuildConfigDEBUG0 = BuildConfig.DEBUG;


    public static List<AppModel> makeList(Context context) {
        List<AppModel> data = new ArrayList<>();
//        R.drawable.cerclebackgroundpurple;
//        R.drawable.cerclebackgroundpink;
//        R.drawable.cerclebackgroundgreen;
//        R.drawable.cerclebackgroundyello;
//        R.drawable.cerclebackgroundpurple;
 
        if (!"fbvideosaver".equals(BuildConfigFLAVOR)) {
            data.add(new AppModel(R.string.app_tiktok, Package.TIKTOK_T_PACKAGE, R.drawable.ic_raw_logo, R.drawable.cerclebackgrountt));
            data.add(new AppModel(R.string.app_tiktok_musical, Package.TIKTOK_M_PACKAGE, R.drawable.ic_raw_logo, R.drawable.cerclebackgrountt));
            data.add(new AppModel(R.string.app_tiktok_musical_lite, Package.TIKTOK_LITE, R.drawable.ic_raw_logo, R.drawable.cerclebackgrountt));

        }

        data.add(new AppModel(R.string.app_facebook, Package.FACEBOOK, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpurple));
        data.add(new AppModel(R.string.app_facebookLite, Package.FACEBOOKLITE, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpurple));


        if (!"fbvideosaver".equals(BuildConfigFLAVOR)) {
            data.add(new AppModel(R.string.app_instagram, Package.INSTAGRAM, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundyello));


            //appViewModels.add(new AppModel(R.string.action_open_browser, null, R.drawable.browser));


            //appViewModels.add(new AppModel(R.string.app_tumblr, "com.tumblr", R.drawable.browser));
            //appViewModels.add(new AppModel(R.string.app_reddit, "com.reddit.frontpage", R.drawable.browser));
            data.add(new AppModel(
                    R.string.app_likee, Package.LIKEE, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpink
            ));

            data.add(new AppModel(
                    R.string.app_pinterest, Package.PINTEREST,
                    R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpinterest
            ));
            data.add(new AppModel(
                    R.string.app_pinterestLite, Package.PINTERESTLITE,
                    R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpinterestlite
            ));

            //=====================================================
            data.add(new AppModel(R.string.app_triller, Package.TRILLER, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpinterestlite));
            //twitter    data.add(new AppModel(R.string.app_x, Package.TWITTER, R.drawable.ic_raw_logo, R.drawable.cerclebackgrountt));

            //@@@ appViewModels.add(new AppModel(R.string.app_twitter_lite, Package.TWITTER_LITE, R.drawable.browser));

            //     data.add(new AppModel(R.string.app_kwai, Package.KWAI, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpinterestlite));

            //    data.add(new AppModel(R.string.app_okrulive, Package.RUOKLIVE, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpinterestlite));

            if (com.walhalla.ui.utils.PackageUtils.isPackageInstalledForLaunch(context, Package.RUOKANDROID)) {
                data.add(new AppModel(R.string.app_okru, Package.RUOKANDROID, R.drawable.ic_raw_logo, R.drawable.cerclebackgroundpinterestlite));

            }
            //=====================================================

            if (BuildConfigDEBUG0) {
                data.add(new AppModel(R.string.app_name, "com.alibaba.aliexpresshd", R.drawable.browser, R.drawable.cerclebackgroundyello));
                data.add(new AppModel(R.string.app_name, "@@@@", R.drawable.browser, R.drawable.cerclebackgroundyello));
                data.add(new AppModel(R.string.app_youtube, dec0(tube), R.mipmap.ic_youtube, R.drawable.cerclebackgroundpurple));
                data.add(new AppModel(R.string.app_youtube, "", R.mipmap.ic_youtube, R.drawable.cerclebackgroundpurple));
            }
        }
        return data;
    }

    public static List<AppModel> makeShareList(Context context) {
        List<AppModel> data = makeList(context);
        data.add(new AppModel(
                R.string.app_okru,
                Package.RUOKANDROID,
                R.drawable.ic_raw_logo,
                R.drawable.cerclebackgroundpink
        ));

        return data;
    }
}
