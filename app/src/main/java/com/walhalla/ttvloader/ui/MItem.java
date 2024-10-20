package com.walhalla.ttvloader.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class MItem {

    public Drawable drawable;
    public final String name;

    public MItem(Drawable drawable, String name) {
        this.drawable = drawable;
        this.name = name;
    }

    public MItem(String name, Drawable drawable) {
        this.drawable = drawable;
        this.name = name;
    }
}
