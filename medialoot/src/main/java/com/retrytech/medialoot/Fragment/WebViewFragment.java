package com.retrytech.medialoot.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.retrytech.medialoot.Activity.Constant;
import com.retrytech.medialoot.Activity.FileSizes;
import com.retrytech.medialoot.Activity.SessionManager;
import com.retrytech.medialoot.Adapter.VimeoVideoAdapter;
import com.retrytech.medialoot.network.CallVimeoApi;
import com.retrytech.medialoot.network.ResponseStatus;
import com.retrytech.medialoot.model.DownloadFile;
import com.retrytech.medialoot.model.VimeoModel;
import com.retrytech.medialoot.model.VimeoModels;
import com.retrytech.medialoot.R;
import com.retrytech.medialoot.databinding.DownloadDialogBinding;
import com.retrytech.medialoot.databinding.FileDetailLayBinding;
import com.retrytech.medialoot.databinding.FragmentWebViewBinding;
import com.walhalla.ui.DLog;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class WebViewFragment extends Fragment implements View.OnClickListener {


    private String[] urlfilename;
    private int flag = 0;
    private String mFileSize = "";
    private ValueCallback<Uri[]> mFilePathCallback;


    private String now_download_url;
    private String search = "";
    private String downloadUrl = "";
    private Document doc;
    private Elements scriptElements;
    private JSONObject emp;
    ArrayList<VimeoModel> vimeoModelArrayList = new ArrayList<>();
    ArrayList<String> dataList;
    ArrayList<String> videoQuality;
    ArrayList<String> videoTitle;
    ArrayList<String> videoThumbline;
    private Dialog dateDialog;
    private AnimationSet animation;
    private Context context;

    public WebViewFragment() {
        // Required empty public constructor
    }

    private FragmentWebViewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false);

        AdView adView = new AdView(binding.getRoot().getContext());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getContext().getString(R.string.admob_banner));
        AdRequest.Builder adRequest= new AdRequest.Builder();
        adView.loadAd(adRequest.build());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.adContainer.addView(adView, params);

        Bundle bundle = getArguments();
        if (bundle != null) {
            flag = bundle.getInt("flag");
            search = getArguments().getString(Constant.WORDS);
        }
        context = getContext();

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(3000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(3000);
        fadeOut.setDuration(3000);

        animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
//        this.setAnimation(animation);

        loadURL();
        setOnClick();
        return binding.getRoot();
    }

    private void setOnClick() {
        binding.btnSearch.setOnClickListener(this);
        binding.fabCard.setOnClickListener(this);
        binding.etUrl.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                binding.btnSearch.performClick();
            }
            return false;
        });
    }

    private void loadURL() {
        binding.webview.setWebViewClient(new webClient());
        binding.webview.addJavascriptInterface(this, "browser");
        binding.webview.getSettings().setDomStorageEnabled(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
        binding.webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webview.getSettings().setUseWideViewPort(true);
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.getSettings().setSupportMultipleWindows(true);
        binding.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            binding.webview.getSettings().setMediaPlaybackRequiresUserGesture(true);
        }


        WebChromeClient webChromeClient = new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                binding.progress.setProgress(i);
                super.onProgressChanged(webView, i);
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, final FileChooserParams fileChooserParams) {
                mFilePathCallback = valueCallback;
                return true;
            }

        };
        binding.webview.setWebChromeClient(webChromeClient);
        if (!search.isEmpty()) {
            if (search.contains("http")) {
                binding.webview.loadUrl(search);
            } else {
                binding.webview.loadUrl("https://www.google.com/search?q=" + search);
            }
        }
        binding.webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //This is the filter
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (binding.webview.canGoBack()) {
                        binding.webview.goBack();
                        binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorGray));
                        binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                        downloadUrl = "";
                    } else {
                        Objects.requireNonNull(getActivity()).onBackPressed();
                    }
                    return true;
                }
                return false;
            }

        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.webview.canGoBack()) {
                    binding.webview.goBack();
                } else {
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
                }

            }
        });
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            }
        });

    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.fabCard) {
            if (binding.webview.getUrl().contains("youtube")) {
                Toast.makeText(context, "agianst of privacy policy", Toast.LENGTH_SHORT).show();
            } else {
                startDownLoad();
            }
        } else if (viewId == R.id.btn_search) {
            search = binding.etUrl.getText().toString();
            binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorGray));
            binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            if (!search.isEmpty()) {
                if (search.contains("http")) {
                    binding.webview.loadUrl(search);
                } else {
                    binding.webview.loadUrl("https://www.google.com/search?q=" + search);
                }
            }
        }

    }

    private void startDownLoad() {

        if (binding.webview.getUrl().contains("vimeo")) {

            dateDialog = new Dialog(getActivity());

            DownloadDialogBinding downloadDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.download_dialog, null, false);
            dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dateDialog.setContentView(downloadDialogBinding.getRoot());

            VimeoVideoAdapter adapter = new VimeoVideoAdapter(getActivity(), vimeoModelArrayList);
            downloadDialogBinding.downloadRecyler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            downloadDialogBinding.downloadRecyler.setAdapter(adapter);
            dateDialog.show();
        } else {
            FileDetailLayBinding fileDetailLayBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.file_detail_lay, null, false);
            fileDetailLayBinding.etFileName.setText(String.valueOf(System.currentTimeMillis()));
            fileDetailLayBinding.etVideoUrl.setText(downloadUrl);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //give YourVideoUrl below
            retriever.setDataSource(downloadUrl, new HashMap<String, String>());

            // this gets frame at 2nd second
            Bitmap image = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            fileDetailLayBinding.imgThumb.setImageBitmap(image);

            //  Glide.with(context).load(Uri.fromFile(new File(downloadUrl))).placeholder(R.drawable.placce_holder_video).into(fileDetailLayBinding.imgThumb);
            fileDetailLayBinding.tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", fileDetailLayBinding.etVideoUrl.getText().toString().trim());
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "copied text", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            final String[] filename = {fileDetailLayBinding.etFileName.getText().toString().trim() + ".mp4"};
            final String[] videoPath = {"file://" + SessionManager.getFolderPath(getActivity()) + filename[0]};
            final String[] savePath = {SessionManager.getFolderPath(getActivity()) + "/" + filename[0]};
            fileDetailLayBinding.savePath.setText(savePath[0]);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setView(fileDetailLayBinding.getRoot());
            alertDialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    filename[0] = fileDetailLayBinding.etFileName.getText().toString().trim() + ".mp4";
                    videoPath[0] = "file://" + SessionManager.getFolderPath(getActivity()) + filename[0];
                    savePath[0] = SessionManager.getFolderPath(getActivity()) + "/" + filename[0];
                    try {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                        request.setTitle("Video Download");
                        request.allowScanningByMediaScanner();
                        request.setDestinationUri(Uri.parse(videoPath[0]));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        final DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


                        long downloadId = 0;
                        if (manager != null) {
                            downloadId = manager.enqueue(request);
                        }
                        Toast.makeText(context, "Download Start", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("string");

                        intent.putExtra("isdownload", true);
                        // put your all data using put extra

                        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).sendBroadcast(intent);

                        long finalDownloadId = downloadId;
                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                boolean downloading = true;

                                while (downloading) {

                                    DownloadManager.Query q = new DownloadManager.Query();
                                    q.setFilterById(finalDownloadId);

                                    Cursor cursor = manager.query(q);
                                    cursor.moveToFirst();
                                    if (cursor.getCount() != 0) {
                                        int bytes_downloaded = cursor.getInt(cursor
                                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                            downloading = false;
                                        }

                                        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                                        long fileSizeInKB = bytes_total / 1024;
                                        float totalizeInMB = fileSizeInKB / 1024;
                                        float totalizeInGB = totalizeInMB / 1024;
                                        long SizeInKB = bytes_downloaded / 1024;
                                        long downloadSizeInMB = SizeInKB / 1024;

                                        String totalSize = "";
                                        String sizeDownload = "";
                                        if (downloadSizeInMB == 0) {
                                            sizeDownload = SizeInKB + "KB";
                                        } else {
                                            sizeDownload = downloadSizeInMB + " MB";
                                        }
                                        if (totalizeInGB > 1) {
                                            totalSize = String.format("%.2f", totalizeInGB).replace(".00", "") + " GB";
                                        } else if (totalizeInMB > 1) {
                                            totalSize = String.format("%.2f", totalizeInMB).replace(".00", "") + " MB";
                                        } else {
                                            totalSize = fileSizeInKB + " KB";
                                        }

                                        if (dl_progress >= 0 && dl_progress <= 100) {
                                            Intent intent = new Intent("filter_string");
                                            DownloadFile downloadFile = new DownloadFile();
                                            downloadFile.setId(finalDownloadId);
                                            downloadFile.setFilename(filename[0]);
                                            downloadFile.setTotalSize(totalSize);
                                            downloadFile.setCompleteSize(sizeDownload);
                                            downloadFile.setProgress((int) dl_progress);
                                            downloadFile.setPath(savePath[0]);
                                            intent.putExtra("progress", downloadFile);
                                            // put your all data using put extra

                                            LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).sendBroadcast(intent);
                                        }
                                    } else {
                                        Intent intent = new Intent("filter_string");
                                        DownloadFile downloadFile = new DownloadFile();
                                        downloadFile.setId((int) finalDownloadId);
                                        downloadFile.setFilename(filename[0]);
                                        downloadFile.setProgress(101);
                                        downloadFile.setPath(savePath[0]);
                                        intent.putExtra("progress", downloadFile);
                                        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).sendBroadcast(intent);
                                    }
                                    cursor.close();
                                }

                            }
                        }).start();
                    } catch (Exception e) {
                        Toast.makeText(context, "error :" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).setNegativeButton("Cancel", null);
            alertDialog.show();
        }
    }

    private class webClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.i("url", "shouldOverrideUrlLoading: " + view.getUrl());
            binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorGray));
            binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            binding.progress.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            sb.append("chkonPageStarted");
            sb.append(url);
            //  binding.etUrl.setText(view.getOriginalUrl());
            binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorGray));
            binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            String url1 = view.getOriginalUrl();
            String url2 = binding.etUrl.getText().toString();
            if (url1 != null && !url1.equals(url2)) {
                if (!binding.etUrl.hasFocus()) {
                    binding.etUrl.setText(view.getOriginalUrl());
                }
            }
            if (url.contains("facebook.com")) {
                try {
                    searchVideoFromFB(url);
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            } else if (url1 != null) {
                if (url1.contains("https://www.dailymotion.com/video")) {

                    new DailymotionDownloadAsync(url1).execute(new Object[]{AsyncTask.SERIAL_EXECUTOR});
                }
            }
            if (url.contains("player.vimeo.com/video/") && url.contains("config")) {
                String[] urlsSplit = url.split("/");
                Log.e("VimeoUrl", "onLoadResource: " + url);
                int count = 0;
                while (count < urlsSplit.length) {
                    if (urlsSplit[count].equals("video")) {
                        Log.e("VimeoUrl", "onLoadResource: " + urlsSplit[count + 1]);
                        getVimeoVideo(urlsSplit[count + 1], getActivity(), url);
                        count = urlsSplit.length;
                    }
                    count++;
                }


            } else if (url.contains(".mp4") || url.contains(".webm")) {
                Log.d("downloadUrl", "onLoadResource: " + url + "========\n" + url);
                if (url1 != null && !url1.contains("facebook.com")) {
                    downloadUrl = url;
                    binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorAccent));
                    binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            binding.progress.setVisibility(View.GONE);
        }
    }



    private void enableDownloadButton(final ArrayList<VimeoModel> arrayList, final Context context) {

        binding.fabCard.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        binding.downloadIcon.setAnimation(animation);
        binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.fabCard.setOnClickListener(view -> {
            dateDialog = new Dialog(context);

            DownloadDialogBinding downloadDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.download_dialog, null, false);
            dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dateDialog.setContentView(downloadDialogBinding.getRoot());

            VimeoVideoAdapter adapter = new VimeoVideoAdapter(context, arrayList);
            downloadDialogBinding.downloadRecyler.setLayoutManager(new GridLayoutManager(context, 1));
            downloadDialogBinding.downloadRecyler.setAdapter(adapter);
            dateDialog.show();

        });
    }

    public class DailymotionDownloadAsync extends AsyncTask<Object, Integer, Object> {
        String DailyMotionUrl;

        DailymotionDownloadAsync(String str) {
            DailyMotionUrl = str;
        }


        public Object doInBackground(Object... objArr) {
            try {
                String qulityUrl;
                String formate = ".mp4";
                videoThumbline = new ArrayList<>();
                if (DailyMotionUrl.contains("https://www.dailymotion.com/video/")) {
                    String[] split = DailyMotionUrl.split("/");
                    if (split[4] != null) {
                        videoQuality = new ArrayList<>();
                        dataList = new ArrayList<>();
                        vimeoModelArrayList = new ArrayList<>();
                        String sb = ("https://www.dailymotion.com/embed/video/" + split[4] + "?autoplay=1");

                        doc = Jsoup.connect(sb).get();
                        scriptElements = doc.getElementsByTag("script");
                        try {
                            String[] split2 = String.valueOf(scriptElements).split("var config =\\s");
                            emp = new JSONObject(split2[1]).getJSONObject("metadata");

                            Log.d("JsonOfDaily", "doInBackground: " + emp);

                            if (emp.getJSONObject("qualities").optJSONArray("144") != null) {
                                JSONObject quality144 = emp.getJSONObject("qualities").optJSONArray("144").getJSONObject(1);
                                qulityUrl = quality144.optString("url");
                                dataList.add(qulityUrl);
                                videoQuality.add("144p");
                            }
                            if (emp.getJSONObject("qualities").optJSONArray("240") != null) {
                                JSONObject quality240p = emp.getJSONObject("qualities").optJSONArray("240").getJSONObject(1);
                                qulityUrl = quality240p.optString("url");
                                dataList.add(qulityUrl);
                                videoQuality.add("240p");
                            }
                            if (emp.getJSONObject("qualities").optJSONArray("380") != null) {
                                JSONObject quality380p = emp.getJSONObject("qualities").optJSONArray("380").getJSONObject(1);
                                qulityUrl = quality380p.optString("url");
                                dataList.add(qulityUrl);
                                videoQuality.add("380p");
                            }
                            if (emp.getJSONObject("qualities").optJSONArray("480") != null) {
                                JSONObject quality480p = emp.getJSONObject("qualities").optJSONArray("480").getJSONObject(1);
                                qulityUrl = quality480p.optString("url");
                                dataList.add(qulityUrl);
                                videoQuality.add("480p");
                            }
                            if (emp.getJSONObject("qualities").optJSONArray("720") != null) {
                                JSONObject quality720p = emp.getJSONObject("qualities").optJSONArray("720").getJSONObject(1);
                                qulityUrl = quality720p.optString("url");
                                dataList.add(qulityUrl);
                                videoQuality.add("720p");
                            }
                            if (emp.getJSONObject("qualities").optJSONArray("1080") != null) {
                                JSONObject quality1080p = emp.getJSONObject("qualities").optJSONArray("1080").getJSONObject(1);
                                qulityUrl = quality1080p.optString("url");
                                dataList.add(qulityUrl);
                                videoQuality.add("1080p");
                            }
                            if (dataList.size() != 0) {
                                int i7 = 0;
                                while (i7 < videoQuality.size()) {
                                    try {
                                        VimeoModel modelBase = new VimeoModel();
                                        modelBase.setExt("mp4");
                                        modelBase.setFormat((String) videoQuality.get(i7));
                                        modelBase.setDuration(emp.getString("duration"));
                                        modelBase.setTitle(emp.getString("title"));
                                        modelBase.setDownurl((String) dataList.get(i7));
                                        modelBase.setThumnail(emp.getString("duration"));
                                        modelBase.setVideourl(emp.getString("duration"));
                                        modelBase.setId(formate);
                                        modelBase.setSize(emp.getString("duration"));
                                        vimeoModelArrayList.add(modelBase);
                                        i7++;
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }


                            Log.e("now_download_url", "doInBackground: " + vimeoModelArrayList.size());
                            enableDownloadButton(vimeoModelArrayList, context);
//                            dec.setDownload_url(now_download_url);
                        } catch (Exception e) {
                            Log.v("DailyMotionParsinError", e.getMessage());
                        }
                    }
                }
            } catch (Exception unused) {
                Log.e("Error: ", "doInBackground: " + unused);
            }
            return null;
        }


        public void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            if (downloadUrl != null) {
                Log.e("SelectDownload", "doInBackground: " + downloadUrl);
//                now_download_url=downloadUrl;
                binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private void enableButton() {
        Log.e("SelectDownload", "doInBackground: " + downloadUrl);
        binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorAccent));
        binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

    }

    private void searchVideoFromFB(String url) {
        if (url.contains("facebook.com") || url.contains("video")) {
            //Facebook
            String FB_JAVA_SCRIPT = "javascript:function clickOnVideo(link, classValueName){browser.getVideoData(link);}function getVideoLink(){try{var items = document.getElementsByTagName(\"div\");for(i = 0; i < items.length; i++){if(items[i].getAttribute(\"data-sigil\") == \"inlineVideo\"){var classValueName = items[i].getAttribute(\"class\");var jsonString = items[i].getAttribute(\"data-store\");var obj = JSON && JSON.parse(jsonString) || $.parseJSON(jsonString);var videoLink = obj.src;var videoName = obj.videoID;items[i].setAttribute('onclick', \"clickOnVideo('\"+videoLink+\"','\"+classValueName+\"')\");}}var links = document.getElementsByTagName(\"a\");for(i = 0; i < links.length; i++){if(links[ i ].hasAttribute(\"data-store\")){var jsonString = links[i].getAttribute(\"data-store\");var obj = JSON && JSON.parse(jsonString) || $.parseJSON(jsonString);var videoName = obj.videoID;var videoLink = links[i].getAttribute(\"href\");var res = videoLink.split(\"src=\");var myLink = res[1];links[i].parentNode.setAttribute('onclick', \"browser.getVideoData('\"+myLink+\"')\");}}}catch(e){}}getVideoLink();";
            binding.webview.loadUrl(FB_JAVA_SCRIPT);
        }
    }

    @JavascriptInterface
    public void getVideoData(final String url) {
        downloadUrl = url;
        getActivity().runOnUiThread(() -> {
            if (url != null) {
                try {
                    if (!url.contains("undefined")) {
                        downloadUrl = url;
                        binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorAccent));
                        binding.downloadIcon.setAnimation(animation);
                        binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                        calculateVideoSize(url);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Download Failed: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void calculateVideoSize(final String url) {
        new AsyncTask<Void, Void, Long>() {
            public Long doInBackground(Void... voidArr) {
                try {
                    return (long) new URL(url).openConnection().getContentLength();
                } catch (IOException e) {
                    DLog.handleException(e);
                    return 0L;
                }
            }

            public void onPostExecute(Long longs) {
                super.onPostExecute(longs);
                binding.fabCard.setCardBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorAccent));
                binding.downloadIcon.setAnimation(animation);
                binding.downloadIcon.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

                mFileSize = FileSizes.getHumanReadableSize(longs, getContext());
//                animateDownloadIcon();
            }
        }.execute();
    }


    private void getVimeoVideo(final String str, final Context context, final String url) {
        dataList = new ArrayList<>();
        videoQuality = new ArrayList<>();
        videoTitle = new ArrayList<>();
        videoThumbline = new ArrayList<>();
        CallVimeoApi.getInstance().getVimeoVideoResponce(context, str, new ResponseStatus() {
            public void onSuccess(Object obj) {
                String duration = "null";
                vimeoModelArrayList.clear();
                VimeoModels vimeoVideo = (VimeoModels) obj;
                VimeoModels.Video video = vimeoVideo.getVideo();
                VimeoModels.Thumbs thumbs = video.getThumbs();
                List responceData = vimeoVideo.getRequest().getFiles().getProgressive();
                Log.i("size", "prog" + responceData.size());
                dataList.clear();
                videoQuality.clear();
                videoTitle.clear();
                videoThumbline.clear();
                String title = video.getTitle();
                String thumbe = thumbs.getBase();
                videoTitle.add(video.getTitle());
                videoThumbline.add(thumbs.getBase());

                for (int i = 0; i < responceData.size(); i++) {
                    if (((VimeoModels.Progressive) responceData.get(i)).getUrl().contains(".mp4")) {
                        videoQuality.add(((VimeoModels.Progressive) responceData.get(i)).getQuality());
                        dataList.add(((VimeoModels.Progressive) responceData.get(i)).getUrl());
                        Log.d("DataAdd", "onSuccess: " + ((VimeoModels.Progressive) responceData.get(i)).getUrl());
                    }
                }
                int j = 0;
                if (dataList.size() != 0) {
                    while (j < videoQuality.size()) {
                        try {
                            VimeoModel vimeoModel = new VimeoModel();
                            vimeoModel.setId(duration);
                            vimeoModel.setTitle(title);
                            vimeoModel.setVideourl(url);
                            vimeoModel.setDownurl(dataList.get(j).toString());
                            vimeoModel.setSize(duration);
                            vimeoModel.setDuration(duration);
                            vimeoModel.setFormat(videoQuality.get(j).toString());
                            vimeoModel.setThumnail(thumbe);
                            vimeoModel.setExt("mp4");
                            vimeoModelArrayList.add(vimeoModel);
                            j++;
                        } catch (Exception e) {
                            DLog.handleException(e);
                        }
                    }
                    enableButton();
                }
                enableDownloadButton(vimeoModelArrayList, context);
            }

            public void onFail(Object obj) {
                Toast.makeText(getActivity(), "Something is Wrong..Please Check you Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
