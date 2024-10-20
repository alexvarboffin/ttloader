package com.walhalla.compat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import com.walhalla.ttvloader.R;

public class ComV19 {

    public Drawable getDrawable(Context context, int resId) {
        Drawable draw;
        try {
            if (Build.VERSION.SDK_INT > 19) {
                //draw = ContextCompat.getDrawable(context, 999);
                Resources res = context.getResources();
                draw = ResourcesCompat.getDrawable(res, resId, null);
            } else {
                //Вектор падает на 4.4 sdk 19
                draw = AppCompatResources.getDrawable(context, resId);
            }
        } catch (Resources.NotFoundException e) {
            Resources res = context.getResources();
            draw = ResourcesCompat.getDrawable(res, R.drawable.ic_corner5, null);
//            interactor.screen(context.getClass().getSimpleName() + DIVIDER
//                            + context.getPackageName()
//                            + DIVIDER
//                            + Build.FINGERPRINT
//                            + DIVIDER
//                            + Locale.getDefault()
//                            + DIVIDER
//                            + e.getLocalizedMessage(),
//                    new TelegramInteractorImpl.QCallback<>() {
//                        @Override
//                        public void onMessageRetrieved(String message) {
//                            //DLog.d(message);
//                        }
//
//                        @Override
//                        public void onRetrievalFailed(String error) {
//                            //DLog.d(error);
//                        }
//                    });
        }
        return draw;
    }
}
