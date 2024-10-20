package com.walhalla.ttvloader.ui.downloader;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.Tracker;
import com.walhalla.extractors.ExUtils;
import com.walhalla.extractors.TTExtractor;
import com.walhalla.ttvloader.BuildConfig;
import com.walhalla.ttvloader.TTResponse;
import com.walhalla.ttvloader.activity.main.MainActivity;
import com.walhalla.ttvloader.clipboard.ClipboardMonitorService;
import com.walhalla.ttvloader.clipboard.MyPref;
import com.walhalla.ttvloader.databinding.FragmentMainBinding;
import com.walhalla.ttvloader.mvp.MainView;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.AppAdapter;
import com.walhalla.extractors.presenters.RepositoryCallback;
import com.walhalla.extractors.presenters.VideoRepository;
import com.walhalla.ttvloader.ui.gallery.DownloadManagerPresenter;
import com.walhalla.ttvloader.ui.gallery.GalleryPresenter;
import com.walhalla.ttvloader.utils.Utils;
import com.walhalla.ui.DLog;

import static com.android.widget.Config.DOWNLOADING_MSG;
import static com.android.widget.Config.STOP_FOREGROUND_ACTION;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadManager extends Fragment implements RepositoryCallback, DownloadManagerPresenter.View {


    private static final String KEY_URL_ = "keyurl";
    ProgressDialog pd;


    private static final String KEY_WITHOUT_WATER_MARK = "key_kwwm_110";

    //private InterstitialAd mInterstitialAd;


    private boolean csRunning_ = false;


    private EditText editText;
    private MainView mainView;

    private SharedPreferences preferences;
    private MyPref pref;
    private Handler handler;
    private FragmentMainBinding binding;
    private final boolean defValue = true;
    private RecyclerView recyclerView;

    private DownloadManagerPresenter presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        handler = new Handler();


        ActivityResultLauncher<String[]> launcher29 = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), map -> {


            StringBuilder sb = new StringBuilder();

            boolean isGranted = false;

            for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                boolean tmp = entry.getValue();
                if (tmp) {
                    isGranted = true;
                }
                sb.append("").append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
            }

            if (isGranted) {// Permission is granted. Continue with updating the UI
                updateGUI();
                //Toast.makeText(getContext(), "GRANTED", Toast.LENGTH_LONG).show();
            } else { // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.

                //mainView.showNoStoragePermissionSnackbar();
                showNoStoragePermissionSnackbar();
                Toast.makeText(getContext(), "NOT GRANTED\n"
                        + sb.toString(), Toast.LENGTH_LONG).show();
            }
        });


        ActivityResultLauncher<Intent> storageActivityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        o -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                //Android is 11 (R) or above
                                if (Environment.isExternalStorageManager()) {
                                    //Manage External Storage Permissions Granted
                                    DLog.d("onActivityResult: Manage External Storage Permissions Granted");
                                } else {
                                    Toast.makeText(getActivity(), "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Below android 11

                            }
                        });
        presenter = new DownloadManagerPresenter(this, handler, (AppCompatActivity) getActivity(),
                launcher29,
                storageActivityResultLauncher
        );
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void startClipboardMonitorRequest() {
        pref.setKeyBoardMonitor(true);
        ClipboardMonitorService.startClipboardMonitor(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.authorInfoView.setCloseAction(v -> binding.authorInfoView.setVisibility(View.GONE));

//@@@        mInterstitialAd = new InterstitialAd(getActivity());
//@@@        mInterstitialAd.setAdUnitId(getString(R.string.AdmobInterstitial));
//@@@        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        pref = MyPref.getInstance(getContext());
        csRunning_ = pref.getKeyBoardMonitor();

        editText = view.findViewById(R.id.url);
        //editText.setText("https://www.tiktok.com/@nadyadorofeeva/video/6803993696493686022");
        //editText.setText("https://rt.pornhub.com/view_video.php?viewkey=ph5b7b503295c60");
        //editText.setText("https://xhamster.com/videos/russian-slave-1-10075621");
        //editText.setText("https://chaturbate.com/senzuallips/");
        //editText.setText("https://rt.pornhub.com/view_video.php?viewkey=ph5e4b45478dcc9");
        //editText.setText("https://l.likee.video/v/TupnZv");


        String url = preferences.getString(KEY_URL_,
                BuildConfig.DEBUG ? "https://vm.tiktok.com/ZSJNWvXY7/" : ""
        );
        if (!TextUtils.isEmpty(url)) {
            editText.setText(url);
        }

        createNotificationChannel(getActivity(), NotificationManagerCompat.IMPORTANCE_LOW,
                true,
                getString(R.string.app_name),
                getString(R.string.notification_description)
        );

        extendedOptions();

        ImageButton clear_text = view.findViewById(R.id.clear_text);
        clear_text.setOnClickListener(v -> {
            editText.setText("");
//                mainView.makeToaster();
        });
        //view.findViewById(R.id.action_download);
        View action_download_video = view.findViewById(R.id.action_download_video);
        //View action_download_watermark_free = view.findViewById(R.id.action_download_watermark_free);

        action_download_video.setOnClickListener(v -> {
            // view.btnDownload.visibility=View.GONE
            //  pbFetchingVideo.visibility=View.VISIBLE
            String tmp = editText.getText().toString().trim();
            if (BuildConfig.DEBUG) {
                preferences.edit().putString(KEY_URL_, tmp).apply();
            }
            downloadVideoRequest(tmp, isRemoveWatermark());
        });

//        action_download_watermark_free.setOnClickListener(v -> {
//            // view.btnDownload.visibility=View.GONE
//            //  pbFetchingVideo.visibility=View.VISIBLE
//            String url = vv.getText().toString().trim();
//            downloadVideoRequest(url, true);
//        });

        AppAdapter adapter = new AppAdapter(getActivity(), mainView);
        recyclerView = view.findViewById(R.id.recycler_view_app);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.recycler_count)
        ));
        recyclerView.setAdapter(adapter);

        // Добавьте слушатель, чтобы дождаться добавления первого элемента
//        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
//            @Override
//            public void onChildViewAttachedToWindow(@NonNull View view) {
//                if (recyclerView.getChildAdapterPosition(view) == 0) {
//                    //showHint(view, getActivity());
//                    // Удалите слушатель после добавления первого элемента
//                    recyclerView.removeOnChildAttachStateChangeListener(this);
//                }
//            }
//
//            @Override
//            public void onChildViewDetachedFromWindow(@NonNull View view) {
//                // Do nothing
//            }
//        });
        /**
         * ClipboardManager
         */
        View copy_from_buffer = view.findViewById(R.id.link);
        copy_from_buffer.setOnClickListener(v -> {
            ClipboardManager clipBoardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData primaryClipData = clipBoardManager.getPrimaryClip();
            String clip = "";
            if (primaryClipData != null) {
                clip = primaryClipData.getItemAt(0).getText().toString();
            }

            Editable text = Editable.Factory.getInstance().newEditable(clip);
            if (text.toString().isEmpty()) {
                mainView.makeToaster(R.string.err_empty_buffer);
                return;
            }
            ((EditText) view.findViewById(R.id.url)).setText(text);
            downloadVideoRequest(clip, true);
        });
    }

    private boolean isRemoveWatermark() {
        return true;//checkBox.isChecked()
    }

    private void extendedOptions() {
//        chkAutoDownload = view.findViewById(R.id.chkAutoDownload);
//        checkBox = view.findViewById(R.id.cb_remove_water_mark);
//        //Android Q == Android 10 sdk29
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            View rvAutoDownload = view.findViewById(R.id.rvAutoDownload);
//            rvAutoDownload.setVisibility(View.GONE);
//        }
//
//        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            preferences.edit().putString(KEY_WITHOUT_WATER_MARK, String.valueOf(isChecked)).apply();
//        });

        if (csRunning_) {
//            chkAutoDownload.setChecked(true);
            startClipboardMonitorRequest();
        } else {
//            chkAutoDownload.setChecked(false);
            stopClipboardMonitor();
        }
//        chkAutoDownload.setOnClickListener(v -> {
//            boolean checked = chkAutoDownload.isChecked();
//            if (checked) {
//                DLog.d("testing checked!");
//                startClipboardMonitorRequest();
//            } else {
//                DLog.d("testing unchecked!");
//                stopClipboardMonitor();
//                // setNofication(false);
//            }
//        });
    }

    private void createNotificationChannel(Context context, Integer importance, Boolean showBadge,
                                           String name,
                                           String description
    ) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2
            String channelId = context.getPackageName() + "-" + name;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(showBadge);
            channel.setSound(null, null);
            // 3
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            //DLog.d("Notificaion Channel Created!");
        }
    }


    private void stopClipboardMonitor() {
        pref.setKeyBoardMonitor(false);
        Intent intent = new Intent(requireContext(), ClipboardMonitorService.class);
        intent.setAction(STOP_FOREGROUND_ACTION);
        boolean service = getActivity().stopService(intent);
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
            if (mainView != null) {
                mainView.makeToaster(R.string.err_enter_valid_url);
            }
        } else {
            if (presenter != null) {
                boolean m = presenter.isNeedGrantPermission();
                if (m) {
                } else {
                    VideoRepository mm = new VideoRepository(getContext(), DownloadManager.this, handler);
                    mm.makeDownload(url, false, removeWatermark);
                    Tracker.log(getContext(), url);
                }
                DLog.d("# isNeedGrantPermission: " + m);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainView) {
            mainView = (MainView) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainView != null) {
            mainView.handleIntent();
        }

        //Update gui
        String raw = preferences.getString(KEY_WITHOUT_WATER_MARK, null);
        //String raw = preferences.getString(KEY_WITHOUT_WATER_MARK, "true");

        boolean no_water;
        if (raw == null) {
            no_water = defValue;
        } else {
            try {
                no_water = Boolean.parseBoolean(raw);
            } catch (Exception e) {
                no_water = false;
            }
        }
        if (no_water) {
            //@@@ checkBox.setChecked(true);
        }

        //showHint(recyclerView.getChildAt(0), getActivity());
    }

//    private void showHint(View firstItemView, Activity activity) {
//        if (firstItemView != null) {
////            new MaterialTapTargetPrompt.Builder(this)
////                    .setTarget(firstItemView)
////                    .setPrimaryText("This is the target view")
////                    .setSecondaryText("Here is a description of what this view does")
////                    .show();
////
////            Toast.makeText(getContext(), "@@@@@", Toast.LENGTH_SHORT).show();
//            Rect rickTarget = new Rect(0, 0, 0, 0);
//            Drawable rick = ContextCompat.getDrawable(getContext(), R.drawable.favorite);
//            TapTargetSequence.Listener listener = new TapTargetSequence.Listener() {
//                // This listener will tell us when interesting(tm) events happen in regards
//                // to the sequence
//                @Override
//                public void onSequenceFinish() {
//                    // Yay
//                }
//
//                @Override
//                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                    // Perform action for the current target
//                }
//
//                @Override
//                public void onSequenceCanceled(TapTarget lastTarget) {
//                    // Boo
//                }
//            };
//            TapTargetSequence sequence = new TapTargetSequence(activity)
//                    .targets(
//                            TapTarget.forView(firstItemView, "Gonna", "aaaaa")
//                                    .dimColor(R.color.never)
//                                    .outerCircleColor(R.color.gonna)
//                                    .targetCircleColor(R.color.colorPrimary)
//                                    .textColor(R.color.you)
//                                    .descriptionTextColor(R.color.you),
//                            TapTarget.forView(@@, "You", "Up")
//                                    .dimColor(R.color.never)
//                                    .outerCircleColor(R.color.gonna)
//                                    .targetCircleColor(R.color.let)
//                                    .textColor(R.color.you),
//                            TapTarget.forBounds(rickTarget, "Down", ":^)")
//                                    //.cancelable(false)
//                                    .icon(rick)
//                    )
//                    .listener(listener);
//            sequence.start();
//        }
//    }

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
        editText.setText(url.trim());
        editText.setFocusable(true);
        editText.findFocus();
    }

    @Override
    public void successResult(TTResponse result) {
        binding.authorInfoView.bind(result);
    }

    @Override
    public void showProgressDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(DOWNLOADING_MSG);
        pd.setCancelable((BuildConfig.DEBUG));
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public void errorResult(int err) {
        Utils.ShowErrorToast0(getActivity(), err);
    }

    @Override
    public void errorResult(String error) {
        Utils.ShowToast0(getActivity(), error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public void updateGUI() {

    }

    private void showNoStoragePermissionSnackbar() {
    }

    @Override
    public void showPermission33SnackBar() {

    }
}
