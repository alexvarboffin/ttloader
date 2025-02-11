package com.walhalla.intentresolver;

import android.content.Context;


import java.io.File;

public final class DefaultIntent extends BaseIntent {

    public DefaultIntent() {
        super(null);
    }

    @Override
    public void shareMp4Selector(Context context, File file) {
        IntentUtils.resolveMp4ActivitiesForPackage(context, file, null);
    }



    @Override
    public void videoShare(Context context, String path) {

    }
}
