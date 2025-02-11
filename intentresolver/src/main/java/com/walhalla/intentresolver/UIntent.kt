package com.walhalla.intentresolver

import android.content.Context
import java.io.File

interface UIntent {
    fun shareMp4Selector(context: Context, file: File)

    fun isClientPackage(packageName: String?): Boolean

    fun videoShare(context: Context, path: String)
}
