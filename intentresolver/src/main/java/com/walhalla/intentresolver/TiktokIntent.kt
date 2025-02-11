package com.walhalla.intentresolver;

import static com.walhalla.intentresolver.IntentUtils.resolveMp4ActivitiesForPackage;

import android.content.Context;

import com.walhalla.abcsharedlib.SharedNetwork;

import java.io.File;

public class TiktokIntent extends BaseIntent{

    public TiktokIntent() {
        super(SharedNetwork.Package.TIKTOK_M_PACKAGE);
    }

    @Override
    public void shareMp4Selector(Context context, File file) {
        resolveMp4ActivitiesForPackage(context, file, packageName);
    }

    @Override
    public void videoShare(Context context, String path) {

    }
}
