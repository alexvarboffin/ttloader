package com.walhalla.intentresolver

import android.content.Context
import com.walhalla.abcsharedlib.SharedNetwork
import java.io.File

class TiktokIntent : BaseIntent(SharedNetwork.Package.TIKTOK_M_PACKAGE) {

    override fun shareMp4Selector(context: Context, file: File) {
        IntentUtils.resolveMp4ActivitiesForPackage(context, file, packageName)
    }


    override fun videoShare(context: Context, path: String) {
    }
}
