package com.walhalla.ttvloader.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.Tracker;
import com.walhalla.compat.ComV19;
import com.walhalla.extractors.ExUtils;
import com.walhalla.extractors.TTExtractor;

import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.TTResponse;
import com.walhalla.extractors.presenters.RepositoryCallback;
import com.walhalla.extractors.presenters.VideoRepository;
import com.walhalla.ttvloader.ui.base.AutoPresenter;
import com.walhalla.ttvloader.utils.Utils;
import com.walhalla.ui.DLog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class AutoActivity extends AppCompatActivity {


    private ComV19 comv19;
    private AutoPresenter presenter;

    private final ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    o -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            //Android is 11 (R) or above
                            if (Environment.isExternalStorageManager()) {
                                //Manage External Storage Permissions Granted
                                DLog.d("onActivityResult: Manage External Storage Permissions Granted");
                            } else {
                                //Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Below android 11

                        }
                    });
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        String type = intent.getType();
        if (!validData1(action, type)) {
            return;
        }

        comv19 = new ComV19();
        presenter = new AutoPresenter(this, storageActivityResultLauncher);

        if (Intent.ACTION_SEND.equals(action) && !TextUtils.isEmpty(type)) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                handleSendText(sharedText); // Handle text being sent
            }
//            else if (type.startsWith("image/")) {
//                handleSendImage(resultIntent); // Handle single image being sent
//            }
        }
//        else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//            if (type.startsWith("image/")) {
//                handleSendMultipleImages(resultIntent); // Handle multiple images being sent
//            }
//        } else {
//            // Handle other intents, such as being started from the home screen
//        }


        Intent resultIntent = new Intent();
        resultIntent.putExtra("SOMETHING", "EXTRAS");
        this.setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean validData1(String action, String type) {
        return Intent.ACTION_SEND.equals(action) && !TextUtils.isEmpty(type);
    }


    private void handleSendText(String sharedText) {
        if (sharedText != null) {
            try {
                handleUrlFromIntent(sharedText);
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    }

    public void handleUrlFromIntent(String url) {
        if (url == null || url.trim().isEmpty()) {
            return;
        } else {
            List<TTExtractor> tmp = ExUtils.defExtractors();
            TTExtractor resolved = null;
            for (TTExtractor extractor : tmp) {
                if (extractor.isUrlValid(url)) {
                    resolved = extractor;
                    url = extractor.getClearUrl(url);
                    break;
                }
            }
            if (resolved == null) {
                String regex = "\\bhttps?://\\S+\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(url);
                while (matcher.find()) {
                    url = matcher.group();
                }
            }
        }
        DLog.d("[url] " + url + " [url]");
        url = url.trim();

        downloadVideoRequest(url, true);
    }

    private void downloadVideoRequest(String url, boolean removeWatermark) {

//@@@        if (mInterstitialAd.isLoaded()) {
//@@@            //mInterstitialAd.show()
//@@@        } else {
//@@@            DLog.d("The interstitial wasn't loaded yet.");
//@@@        }

        if (TextUtils.isEmpty(url) || !Utils.isValidUrl(url)) {
            //iUtils.ShowToast(getContext(), "Please Enter a valid URI");
            DLog.d("Please Enter a valid URI");
            makeToaster(R.string.err_enter_valid_url);
        } else {
            if (isNeedGrantPermission0()) {
                DLog.d("# wait permission");
            } else {
                Handler handler = new Handler();
                VideoRepository repository = new VideoRepository(this,
                        new RepositoryCallback() {
                            @Override
                            public void successResult(TTResponse result) {

                            }

                            @Override
                            public void errorResult(String error) {

                            }

                            @Override
                            public void showProgressDialog() {

                            }

                            @Override
                            public void hideProgressDialog() {

                            }

                            @Override
                            public void errorResult(int errWwwNotSupport) {

                            }
                        }, handler);
                repository.makeDownload(url, false, removeWatermark);
                Tracker.log(this, url);
            }
        }
    }

    private boolean isNeedGrantPermission0() {
        return presenter.isNeedGrantPermission();
    }


    private void makeToaster(int errEnterValidUrl) {
        Toasty.custom(this,
                errEnterValidUrl,
                comv19.getDrawable(this, R.drawable.ic_cancel),
                R.color.error,
                android.R.color.white, Toasty.LENGTH_SHORT, true, true).show();

    }
}
