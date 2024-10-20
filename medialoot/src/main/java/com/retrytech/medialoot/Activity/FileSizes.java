package com.retrytech.medialoot.Activity;

import android.content.Context;

import com.retrytech.medialoot.R;

public class FileSizes {
    public static String getHumanReadableSize(long j, Context context) {
        if (context == null) {
            return "";
        }
        if (j < 1024) {
            return String.format(context.getString(R.string.BYTE_Size), new Object[]{Double.valueOf((double) j)});
        }
        double d = (double) j;
        if (d < Math.pow(1024.0d, 2.0d)) {
            return String.format(context.getString(R.string.KB_Size), new Object[]{Double.valueOf((double) (j / 1024))});
        } else if (d < Math.pow(1024.0d, 3.0d)) {
            return String.format(context.getString(R.string.MB_Size), new Object[]{Double.valueOf(d / Math.pow(1024.0d, 2.0d))});
        } else {
            return String.format(context.getString(R.string.GB_Size), new Object[]{Double.valueOf(d / Math.pow(1024.0d, 3.0d))});
        }
    }
}
