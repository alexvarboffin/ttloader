package com.walhalla.ttvloader.ui.gallery

import android.content.pm.ResolveInfo

class QWrap(
    @JvmField var mime: String,
    @JvmField var map: Map<String, MutableList<ResolveInfo>>
)
