package com.walhalla.ttvloader.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import android.os.Environment
import android.text.TextUtils
import android.util.Patterns
import android.view.MenuItem
import android.webkit.URLUtil
import android.widget.Toast
import com.walhalla.compat.ComV19
import com.walhalla.ttvloader.R
import com.walhalla.ui.DLog
import java.io.File
import java.net.URL
import java.util.Locale
import java.util.regex.Pattern
import com.android.widget.Config.*
import es.dmoral.toasty.Toasty

class Utils {
    companion object {
        //private InterstitialAd interstitialAd;
        @JvmStatic fun isSameDomain(url: String, url1: String): Boolean {
            return getRootDomainUrl(url.lowercase()).equals(getRootDomainUrl(url1.lowercase()))
        }
        @JvmStatic private fun getRootDomainUrl(url: String): String {
            var domainKeys = url.split("/").toTypedArray()[2].split("\\.")
            var length: Int = domainKeys.size
            var dummy: Int = if (domainKeys[0].equals("www")) 1 else 0
            if (length - dummy == 2) {
                return domainKeys[length - 2] + "." + domainKeys[length - 1]
            } else {
                if (domainKeys[length - 1].length == 2) {
                    return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1]
                } else {
                    return domainKeys[length - 2] + "." + domainKeys[length - 1]
                }
            }
        }
        @JvmStatic fun tintMenuIcon(context: Context, item: MenuItem, color: Int) {
            var drawable = item.getIcon()
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawable's with this id will have a color
                // filter applied to it.
                drawable.mutate()
                drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP)
            }
        }
        @JvmStatic fun bookmarkUrl(context: Context, url: String) {
            var pref: SharedPreferences = context.getSharedPreferences(com.android.widget.Config.PREF_APPNAME, 0)
            // 0 - for private mode
            var editor: SharedPreferences.Editor = pref.edit()
            // if url is already bookmarked, unbookmark it
            if (pref.getBoolean(url, false)) {
                editor.remove(url).apply()
            } else {
                editor.putBoolean(url, true)
            }
            editor.commit()
        }
        @JvmStatic fun isBookmarked(context: Context, url: String): Boolean {
            var pref: SharedPreferences = context.getSharedPreferences(com.android.widget.Config.PREF_APPNAME, 0)
            return pref.getBoolean(url, false)
        }
        @JvmStatic fun ShowErrorToast0(context: Context, err: Int) {
            var comv19: ComV19 = ComV19()
            Toasty.custom(context, err, comv19.getDrawable(context, R.drawable.ic_cancel)!!, R.color.error, android.R.color.white, Toasty.LENGTH_SHORT, true, true).show()
        }
        @JvmStatic fun ShowToast0(context: Context, str: String) {
            var comv19: ComV19 = ComV19()
            Toasty.custom(context, str, comv19.getDrawable(context, R.drawable.ic_info)!!, ContextCompat.getColor(context, R.color.colorPrimaryDark), ContextCompat.getColor(context, android.R.color.white), Toasty.LENGTH_SHORT, true, true).show()
        }
        @JvmStatic fun ShowToast0(context: Context, res: Int) {
            var comv19: ComV19 = ComV19()
            Toasty.custom(context, res, comv19.getDrawable(context, R.drawable.ic_info)!!, R.color.colorPrimaryDark, android.R.color.white, Toasty.LENGTH_SHORT, true, true).show()
        }
        @JvmStatic fun isValidUrl(input: CharSequence): Boolean {
            if (TextUtils.isEmpty(input)) {
                return false
            }
            var URL_PATTERN: Pattern = Patterns.WEB_URL
            var matches: Boolean = URL_PATTERN.matcher(input).matches()
            if (!matches) {
                var urlString: String = input.toString() + ""
                if (URLUtil.isNetworkUrl(urlString)) {
                    try {
                        URL(urlString)
                        matches = true
                    } catch (e: Exception) {
                        DLog.handleException(e)
                    }
                }
            }
            return matches
        }
        //   public static void GetSessionID(final Context cntx){
        //       final String[] ID = new String[1];
        //
        //       AsyncTask.execute(new Runnable() {
        //           @Override
        //           public void run() {
        //
        //               try {
        //                   Document doc = Jsoup.connect(API_URL2).post();
        //
        //                   Elements scriptElements = doc.getElementsByTag("script");
        //                   for (Element element : scriptElements) {
        //                       if (element.data().contains("sid")) {
        //                            // find the line which contains 'infosite.token = <...>;'
        //                           Pattern pattern = Pattern.compile("(?is)sid=\'(.+?)\'");
        //                           Matcher matcher = pattern.matcher(element.data());
        //                           // we only expect a single match here so there's no need to loop through the matcher's groups
        //                           if (matcher.find()) {
        //                               //System.out.println(matcher.group());
        //                               //System.out.println(matcher.group(1));
        //                               ID[0] = matcher.group(1).toString();
        //                           } else {
        //                               System.err.println("No match found!");
        //                           }
        //                           break;
        //                       }
        //                   }
        //               } catch (IOException e) {
        //                   DLog.handleException(e);
        //               }
        //
        //
        //               Session session;
        //               session = new Session(cntx);
        //               session.setSid(ID[0]);
        //                   }
        //
        //
        //       });
        //
        //
        //
        //
        //    //    return ID[0];
        //   }
        @JvmStatic fun getRootDirPath(context: Context): String {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                var file: File = ContextCompat.getExternalFilesDirs(context.getApplicationContext(), null)[0]
                return file.getAbsolutePath()
            } else {
                return context.getApplicationContext().getFilesDir().getAbsolutePath()
            }
        }
        @JvmStatic fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String {
            return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
        }
        @JvmStatic private fun getBytesToMBString(bytes: Long): String {
            return String.format(Locale.ENGLISH, "%.2fMb", bytes / 1024.00 * 1024.00)
        }
    }
}
