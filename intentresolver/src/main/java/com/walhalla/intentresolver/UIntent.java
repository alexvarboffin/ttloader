package com.walhalla.intentresolver;

import android.content.Context;

import java.io.File;

public interface UIntent {
   void shareMp4Selector(Context context, File file);

    boolean isClientPackage(String packageName);

    void videoShare(Context context, String path);
}
