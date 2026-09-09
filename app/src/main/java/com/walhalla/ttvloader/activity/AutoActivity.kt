package com.walhalla.ttvloader.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.walhalla.Tracker
import com.walhalla.compat.ComV19
import com.walhalla.extractors.ExUtils
import com.walhalla.extractors.TTExtractor
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.TTResponse
import com.walhalla.extractors.presenters.RepositoryCallback
import com.walhalla.extractors.presenters.VideoRepository
import com.walhalla.ttvloader.ui.base.AutoPresenter
import com.walhalla.ttvloader.utils.Utils
import com.walhalla.ui.DLog
import java.util.List
import java.util.regex.Matcher
import java.util.regex.Pattern
import es.dmoral.toasty.Toasty

class AutoActivity : AppCompatActivity() {
    private var comv19: ComV19? = null
    private var presenter: AutoPresenter? = null
    private val storageActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), { o -> 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            if (Environment.isExternalStorageManager()) {
                //Manage External Storage Permissions Granted
                DLog.d("onActivityResult: Manage External Storage Permissions Granted")
            } else {
            }
        } else {
        }
        })
    //Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
    //Below android 11
    override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState!!)
        var intent: Intent = getIntent()
        if (intent == null) {
            return
        }
        var action: String = intent.getAction()!!
        var type: String = intent.getType()!!
        if (validData1(action, type)) {
            return
        }
        comv19 = ComV19()
        presenter = AutoPresenter(this, storageActivityResultLauncher)
        if (Intent.ACTION_SEND.equals(action) && TextUtils.isEmpty(type)) {
            if ("text/plain".equals(type)) {
                var sharedText: String = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                handleSendText(sharedText)
            }
        }
        // Handle text being sent
        //            else if (type.startsWith("image/")) {
        //                handleSendImage(resultIntent); // Handle single image being sent
        //            }
        //        else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
        //            if (type.startsWith("image/")) {
        //                handleSendMultipleImages(resultIntent); // Handle multiple images being sent
        //            }
        //        } else {
        //            // Handle other intents, such as being started from the home screen
        //        }
        var resultIntent: Intent = Intent()
        resultIntent.putExtra("SOMETHING", "EXTRAS")
        this.setResult(RESULT_OK, resultIntent)
        finish()
    }
    private fun validData1(action: String, type: String): Boolean {
        return Intent.ACTION_SEND.equals(action) && TextUtils.isEmpty(type)
    }
    private fun handleSendText(sharedText: String) {
        if (sharedText != null) {
            try {
                handleUrlFromIntent(sharedText)
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
    }
    fun handleUrlFromIntent(urlIn: String) {
        var url = urlIn
        if (url == null || url.trim().isEmpty()) {
            return
        } else {
            var tmp: MutableList<TTExtractor> = ExUtils.defExtractors()
            var resolved: TTExtractor? = null
            for (extractor in tmp) {
                if (extractor.isUrlValid(url)) {
                    resolved = extractor
                    url = extractor.getClearUrl(url)
                    break
                }
            }
            if (resolved == null) {
                var regex: String = "\\bhttps?://\\S+\\b"
                var pattern: Pattern = Pattern.compile(regex)
                var matcher: Matcher = pattern.matcher(url)
                while (matcher.find()) {
                    url = matcher.group()
                }
            }
        }
        DLog.d("[url] " + url + " [url]")
        url = url.trim()
        downloadVideoRequest(url, true)
    }
    private fun downloadVideoRequest(url: String, removeWatermark: Boolean) {
        //@        if (mInterstitialAd.isLoaded()) {
        //@            //mInterstitialAd.show()
        //@        } else {
        //@            DLog.d("The interstitial wasn't loaded yet.");
        //@        }
        if (TextUtils.isEmpty(url) || Utils.isValidUrl(url)) {
            //iUtils.ShowToast(getContext()!!, "Please Enter a valid URI");
            DLog.d("Please Enter a valid URI")
            makeToaster(R.string.err_enter_valid_url)
        } else {
            if (isNeedGrantPermission0()) {
                DLog.d("# wait permission")
            } else {
                var handler: Handler = Handler()
                var repository: VideoRepository = VideoRepository(this, object : RepositoryCallback {
                                    override fun successResult(result: TTResponse) {
                                    }
                                    override fun errorResult(error: String) {
                                    }
                                    override fun showProgressDialog() {
                                    }
                                    override fun hideProgressDialog() {
                                    }
                                    override fun errorResult(errWwwNotSupport: Int) {
                                    }
                                }, handler)
                repository.makeDownload(url, false, removeWatermark)
                Tracker.log(this, url)
            }
        }
    }
    private fun isNeedGrantPermission0(): Boolean {
        return presenter!!.isNeedGrantPermission()
    }
    private fun makeToaster(errEnterValidUrl: Int) {
        Toasty.custom(this, errEnterValidUrl, comv19!!.getDrawable(this, R.drawable.ic_cancel)!!, R.color.error, android.R.color.white, Toasty.LENGTH_SHORT, true, true).show()
    }
}
