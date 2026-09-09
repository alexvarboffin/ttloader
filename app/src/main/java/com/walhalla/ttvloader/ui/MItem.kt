package com.walhalla.ttvloader.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class MItem {
    @JvmField var drawable: Drawable? = null
    lateinit var name: String
    constructor(drawable: Drawable, name: String) {
        this.drawable = drawable
        this.name = name
    }
    constructor(name: String, drawable: Drawable) {
        this.drawable = drawable
        this.name = name
    }
}
