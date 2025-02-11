package com.walhalla.intentresolver

import android.content.Context
import java.io.File

class DefaultIntent : BaseIntent(null) {
    override fun shareMp4Selector(context: Context, file: File) {
        IntentUtils.resolveMp4ActivitiesForPackage(context, file, null)
    }


    override fun videoShare(context: Context, path: String) {
    }
}
