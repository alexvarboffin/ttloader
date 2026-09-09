package com.walhalla.ttvloader.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.walhalla.Lesson
import com.walhalla.Tracker
import com.walhalla.abcsharedlib.Share
import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor
import com.walhalla.boilerplate.threading.MainThreadImpl
import com.walhalla.common.KonfettiPresenter
import com.walhalla.compat.ComV19
import com.walhalla.domain.interactors.AdvertInteractor
import com.walhalla.domain.interactors.impl.AdvertInteractorImpl
import com.walhalla.ttvloader.Application
import com.walhalla.ttvloader.BuildConfig
import com.walhalla.ttvloader.activity.tools.ToolsActivity
import com.walhalla.ttvloader.databinding.ActivityMainBinding
import com.walhalla.intentresolver.DefaultIntent
import com.walhalla.ttvloader.mvp.MainView
import com.walhalla.ttvloader.R
import com.walhalla.adapters.ViewpagerAdapter
import com.walhalla.ttvloader.ui.AboutFragment
import com.walhalla.ttvloader.ui.base.MainActivityPresenter
import com.walhalla.ttvloader.ui.downloader.DownloadManager
import com.walhalla.ttvloader.ui.gallery.GalleryFragment
import com.walhalla.ttvloader.models.LocalVideo
import com.walhalla.ui.DLog
import com.walhalla.ui.UConst
import com.walhalla.ui.observer.RateAppModule
import com.walhalla.ui.plugins.Launcher
import com.walhalla.ui.plugins.DialogAbout
import com.walhalla.ui.plugins.Module_U
import java.io.File
import java.util.List
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity(), MainView {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mRateAppModule: RateAppModule? = null
    private var comv19: ComV19? = null
    private var binding: ActivityMainBinding? = null
    private var permissionResolver: MainActivityPresenter? = null
    private var k: KonfettiPresenter? = null
    private var snackbar: Snackbar? = null
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
    private var content: FrameLayout? = null
    private val callback = object : AdvertInteractor.Callback<android.view.View> {
            override fun onMessageRetrieved(id: Int, message: View) {
                DLog.d(message.javaClass.getName() + " --> " + message.hashCode())
                if (content!! != null) {
                    DLog.d("" + content!!.javaClass.getName())
                    try {
                        //content.removeView(message);
                        if (message.getParent() != null) {
                            (message.getParent() as ViewGroup)
                        }
                        var params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                        params.gravity = Gravity.BOTTOM or Gravity.CENTER
                        message.setLayoutParams(params)
                        var vto: ViewTreeObserver = message.getViewTreeObserver()
                        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                                                override fun onGlobalLayout() {
                                                    if (Build.VERSION.SDK_INT < 16) {
                                                        message.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                                                    } else {
                                                        message.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                                                    }
                                                }
                                            })
                        //int width = message.getMeasuredWidth();
                        //int height = message.getMeasuredHeight();
                        //DLog.i("" + height + "x" + width);
                        //setSpaceForAd(height);
                        content!!.addView(message)
                    } catch (e: Exception) {
                        DLog.handleException(e)
                    }
                }
            }
            override fun onRetrievalFailed(error: String) {
                DLog.d("---->" + error)
            }
        }
    private var interactor: AdvertInteractorImpl? = null
    override protected fun onCreate(savedInstanceState: Bundle?) {
        //        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
        //            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //        }
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState!!)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())
        k = KonfettiPresenter(binding!!.konfettiView)
        mRateAppModule = RateAppModule(this)
        lifecycle.addObserver(mRateAppModule!!)
        comv19 = ComV19()
        permissionResolver = MainActivityPresenter(this, storageActivityResultLauncher)
        setSupportActionBar(binding!!.toolbar)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.setTitle(null)
        }
        binding!!.tvVersion.setText(DLog.getAppVersion(this))
        var adapter: ViewpagerAdapter = ViewpagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        //BEHAVIOR_SET_USER_VISIBLE_HINT
        adapter.addFragment(DownloadManager(), getString(R.string.abc_tab_download))
        adapter.addFragment(GalleryFragment(), getString(R.string.abc_tab_gallery))
        adapter.addFragment(AboutFragment(), getString(R.string.abc_tab_about))
        binding!!.viewPager.setAdapter(adapter)
        binding!!.tabs.setupWithViewPager(binding!!.viewPager)
        //        tabs.getTabAt(0).setIcon(R.drawable.ic_download_color_24dp);
        //        tabs.getTabAt(1).setIcon(R.drawable.ic_gallery_color_24dp);
        //        tabs.getTabAt(2).setIcon(R.drawable.ic_info_outline_black_24dp);
        //none        isNeedGrantPermission();
        Lesson(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        interactor = AdvertInteractorImpl(mAdvertRepository = Application.repository!!)
        //        try {
        //            YoutubeDL.getInstance().init(getApplication(), this);
        //        } catch (YoutubeDLException e) {
        //            DLog.e("failed to initialize youtubedl-android" + e);
        //        }
        adsBanner()
    }
    //        binding!!.fab0.setOnClickListener(v -> {
    //            showNoStoragePermissionSnackbar();
    //        });
    private fun adsBanner() {
        //        binding!!.adView.setAdListener(new AdListener(binding!!.adView));
        //        // Create an ad request. Check your logcat output for the hashed device ID to
        //        // get test ads on a physical device. e.g.
        //        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        //
        //        // Start loading the ad in the background.
        //        binding!!.adView.loadAd(new AdRequest.Builder()
        //                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //                //.addTestDevice("28964E2506C9A8C6400A9E8FF42D3486")
        //                .build());
        setupAdAtBottom(binding!!.bottomContainer)
    }
    protected fun setupAdAtBottom(content: FrameLayout) {
        //FrameLayout content = findViewById(android.R.id.content);
        this.content = content
        //        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
        //                FrameLayout.LayoutParams.MATCH_PARENT,
        //                FrameLayout.LayoutParams.WRAP_CONTENT);
        //        params.gravity = Gravity.BOTTOM;
        //        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater()
        //                .inflate(R.layout.ad_layout, null);
        //        linearLayout.setLayoutParams(params);
        //
        //        // adding viewtreeobserver to get height of linearLayout layout , so that
        //        // android.R.id.content will set margin of that height
        //        ViewTreeObserver vto = linearLayout.getViewTreeObserver();
        //        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //            @SuppressLint("ObsoleteSdkInt")
        //            @Override
        //            public void onGlobalLayout() {
        //                if (Build.VERSION.SDK_INT < 16) {
        //                    linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        //                } else {
        //                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        //                }
        //                int width = linearLayout.getMeasuredWidth();
        //                int height = linearLayout.getMeasuredHeight();
        //                //DLog.i("" + height + "x" + width);
        //                setSpaceForAd(height);
        //            }
        //        });
        //        addLayoutToContent(linearLayout);
        //aa.attach(this);
        //DLog.d("---->" + aa.hashCode());
        interactor!!.selectView(content, callback)
    }
    fun handleSendText(sharedText: String) {
        if (sharedText != null) {
            var outputFragment: Fragment? = null
            try {
                outputFragment = getSupportFragmentManager().getFragments().`get`(0)
                if (outputFragment!! != null) {
                    if (outputFragment!! is DownloadManager) {
                        (outputFragment!! as DownloadManager)
                    }
                }
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
    }
    //    @Override
    //    public void showNoStoragePermissionSnackbar() {
    //        //makeToaster(R.string.info_permission_denied);
    //        if (binding!!.coordinator1 != null) {
    //
    //
    //            if (snackbar == null) {
    //                snackbar = Snackbar.make(binding!!.layoutSnackbarContainer
    //                                , R.string.label_no_storage_permission, Snackbar.LENGTH_LONG)
    //                        //.setAction("Нет", null)
    //                        .setBackgroundTint(getResources().getColor(android.R.color.black))
    //                        .setTextColor(getResources().getColor(android.R.color.white))
    //                        .setActionTextColor(getResources().getColor(android.R.color.white))
    //                        .setAction(getString(R.string.action_settings), v -> {
    //                            openApplicationSettings();
    //                            Toast t = Toast.makeText(this, getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG);
    //                            t.show();
    //                        });
    //            }
    //            else {
    //                if (snackbar.isShown()) {
    //                    snackbar.dismiss();
    //                }
    //            }
    //            snackbar.show();
    //
    ////            Snackbar.make(binding!!.layoutSnackbarContainer
    ////                            , R.string.label_no_storage_permission, Snackbar.LENGTH_LONG)
    ////                    .setActionTextColor(getResources().getColor(android.R.color.white))
    ////                    .setAction(getString(R.string.action_settings), v -> {
    ////                        openApplicationSettings();
    ////                        Toast t = Toast.makeText(this, getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG);
    ////                        t.show();
    ////                    }).show();
    //
    ////            Snackbar.make(binding!!.coordinator2, getString(R.string.label_no_storage_permission), Snackbar.LENGTH_LONG)
    ////                    .setActionTextColor(getResources().getColor(android.R.color.white))
    ////                    .setAnchorView(binding!!.bottomContainer)
    ////                    .setAction(getString(R.string.action_settings), v -> {
    ////                        openApplicationSettings();
    ////                        Toast t = Toast.makeText(this, getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG);
    ////                        t.show();
    ////                    }).show();
    //
    ////            Snackbar.make(binding!!.coordinator1, getString(R.string.label_no_storage_permission), Snackbar.LENGTH_LONG)
    ////                    .setActionTextColor(getResources().getColor(android.R.color.white))
    ////                    //.setAnchorView(binding!!.toolbar)
    ////                    .setAction(getString(R.string.action_settings), v -> {
    ////                        openApplicationSettings();
    ////                        Toast t = Toast.makeText(this, getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG);
    ////                        t.show();
    ////                    }).show();
    //        }
    //
    //        // Проверяем наличие контейнера
    ////        if (binding!!.bottomContainer != null) {
    ////            Snackbar.make(binding!!.bottomContainer, getString(R.string.label_no_storage_permission), Snackbar.LENGTH_LONG)
    ////                    .setActionTextColor(getResources().getColor(android.R.color.white))
    ////                    .setAction(getString(R.string.action_settings), v -> {
    ////                        openApplicationSettings();
    ////                        Toast t = Toast.makeText(this, getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG);
    ////                        t.show();
    ////                    }).show();
    ////        }
    //    }
    override fun openApplicationSettings() {
        var appSettingsIntent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.getPackageName()))
        startActivityForResult(appSettingsIntent, APPLICATION_DETAILS_SETTINGS)
    }
    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        try {
    //            if (requestCode == PermissionResolver.REQUEST_PERMISSION_CODE) {
    //                if (grantResults != null && grantResults.length > 0
    //                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //                    setlayout();
    //                } else {
    //                    //iUtils.ShowToast(this, getString(R.string.info_permission_denied));
    //                    showNoStoragePermissionSnackbar();
    //                }
    //            }
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //            //iUtils.ShowToast(this, getString(R.string.info_permission_denied));
    //            showNoStoragePermissionSnackbar();
    //        }
    //    }
    override fun makeToaster(res: Int) {
        if (binding!!.coordinator1 != null) {
            Snackbar.make(binding!!.coordinator1, res, Snackbar.LENGTH_LONG).show()
        }
    }
    override fun handleException(err: Exception) {
        var accessError: String = "Error occurred - " + err.localizedMessage
        Toasty.custom(this, accessError, comv19!!.getDrawable(this, R.drawable.ic_cancel)!!, ContextCompat.getColor(this, R.color.error), ContextCompat.getColor(this, android.R.color.white), Toasty.LENGTH_LONG, true, true).show()
    }
    override fun makeToaster0(format: String) {
        Toasty.custom(this, format, comv19!!.getDrawable(this, R.drawable.ic_info)!!, ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, android.R.color.white), Toasty.LENGTH_SHORT, true, true).show()
    }
    override fun makeErrorToaster(accessError: Int) {
        Toasty.custom(this, accessError, comv19!!.getDrawable(this, R.drawable.ic_cancel)!!, R.color.error, android.R.color.white, Toasty.LENGTH_SHORT, true, true).show()
    }
    override fun watchVideo(item: LocalVideo) {
        if (item.path != null) {
            val path: String = item.path!!
            val videoFile: File = File(path)
            //            Intent intent = new Intent(Intent.ACTION_VIEW);
            //            intent.setDataAndType(fileUri, "video/*");
            //            intent.putExtra(SharedNetwork.comPinterestEXTRA_DESCRIPTION, "123");
            //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//DO NOT FORGET THIS EVER
            //            context.startActivity(intent);
            //startActivityForResult(PlayerActivity.newIntent(this, path), 1);
            //we share file
            //we send path
            //we send url
            viewVideoFile(videoFile)
        }
    }
    //FileUriExposedException @ file:///storage/emulated/0/DCIM/Likee%20Video/Like_7367804818799714443.mp4 exposed beyond app through Intent.getData()
    private fun viewVideoFile(videoFile: File) {
        var fileUri: Uri = FileProvider.getUriForFile(this, getPackageName() + Share.KEY_FILE_PROVIDER, videoFile)
        var defaultVideoIntent: Intent = Intent(Intent.ACTION_VIEW)
        defaultVideoIntent.setDataAndType(fileUri, "video/*")
        // temp permission for receiving app to read this file
        defaultVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        var resInfoList = getPackageManager().queryIntentActivities(defaultVideoIntent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resInfoList.isEmpty()) {
            for (resolveInfo in resInfoList) {
                var packageName: String = resolveInfo.activityInfo.packageName
                var activityName: String = resolveInfo.activityInfo.name
                DLog.d("[" + packageName + "]" + activityName)
                grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } else {
            DLog.d("Not found activity...")
        }
        try {
            var chooser: Intent = Intent.createChooser(defaultVideoIntent, "")
            startActivity(chooser)
        } catch (e: Exception) {
            DLog.handleException(e)
        }
    }
    //, Uri.parse(videoFile)
    private var doubleBackToExitPressedOnce: Boolean = true
    private var toast: Toast? = null
    override fun onBackPressed() {
        var count: Int = getSupportFragmentManager().getBackStackEntryCount()
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.setHomeButtonEnabled(count > 0)
        }
        if (count > 0) {
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            //count == 0
            //                Dialog
            //                new AlertDialog.Builder(this)
            //                        .setIcon(android.R.drawable.ic_dialog_alert)
            //                        .setTitle("Leaving this App?")
            //                        .setMessage("Are you sure you want to close this application?")
            //                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            //                            @Override
            //                            public void onClick(DialogInterface dialog, int which) {
            //                                finish();
            //                            }
            //
            //                        })
            //                        .setNegativeButton("No", null)
            //                        .show();
            //super.onBackPressed();
            if (isFirstPage()) {
                if (doubleBackToExitPressedOnce) {
                    //                super.onBackPressed();
                    //                return;
                    // Move the task containing the MActivity to the back of the activity stack, instead of
                    // destroying it. Therefore, MActivity will be shown when the user switches back to the app.
                    moveTaskToBack(true)
                    return
                }
                this.doubleBackToExitPressedOnce = true
                //Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
                toast = Toasty.custom(this, R.string.press_again_to_exit, comv19!!.getDrawable(this, R.drawable.ic_info)!!, R.color.colorPrimaryDark, android.R.color.white, Toasty.LENGTH_SHORT, true, true)
                toast!!.show()
                Handler().postDelayed({
                                doubleBackToExitPressedOnce = false
                                }, 1400)
            } else {
                binding!!.viewPager.setCurrentItem(0, true)
            }
        }
    }
    private fun isFirstPage(): Boolean {
        var currentPosition: Int = binding!!.viewPager.getCurrentItem()
        return currentPosition == 0
    }
    override fun handleIntent() {
        var intent: Intent = getIntent()
        var action: String = intent.getAction()!!
        var type: String = intent.getType()!!
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                var sharedText: String = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                handleSendText(sharedText)
            }
        }
    }
    // Handle text being sent
    //            else if (type.startsWith("image/")) {
    //                handleSendImage(intent); // Handle single image being sent
    //            }
    //        else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
    //            if (type.startsWith("image/")) {
    //                handleSendMultipleImages(intent); // Handle multiple images being sent
    //            }
    //        } else {
    //            // Handle other intents, such as being started from the home screen
    //        }
    private fun setlayout() {
    }
    //update ui
    //From recycler
    override fun action_share_video(localVideo: LocalVideo) {
        //Intent intent = new Intent(Intent.ACTION_SEND);
        var file: File = File(localVideo.path)
        DefaultIntent().shareMp4Selector(this, file)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DLog.d("==> " + requestCode + "\t" + resultCode)
        //MainActivity.RESULT_CANCELED;
        if (requestCode == com.walhalla.ttvloader.Const.COMPONENT_REQUEST_CODE) {
        } else if (requestCode == APPLICATION_DETAILS_SETTINGS) {
            setlayout()
        } else {
        }
    }
    //mPresenter.onActivityResult(requestCode, resultCode, data);
    //=======================================================================
    override fun onPause() {
        //        if (binding!!.adView != null) {
        //            binding!!.adView.pause();
        //        }
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        if (binding!!.konfettiView.isActive()) {
            binding!!.konfettiView.reset()
        }
        //        if (binding!!.adView != null) {
        //            binding!!.adView.resume();
        //        }
        Tracker.resume(this)
    }
    override fun onDestroy() {
        //        if (binding!!.adView != null) {
        //            binding!!.adView.destroy();
        //        }
        super.onDestroy()
    }
    override protected fun onSaveInstanceState(outState: Bundle) {
        if (mRateAppModule!! != null) {
            mRateAppModule!!.appReloadedHandler()
        }
        super.onSaveInstanceState(outState)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (BuildConfig.DEBUG) {
            menu.add(0, 0, 0, "Tools").setOnMenuItemClickListener(lmb1@{ v -> 
                        startActivity(Intent(this, ToolsActivity::class.java))
                        return@lmb1 true
                        })
            menu.add(0, 0, 0, "Perm").setOnMenuItemClickListener(lmb2@{ v -> 
                        var uri0: Uri = Uri.parse(String.format("package:%s", getApplicationContext().getPackageName()))
                        var uri1: Uri = Uri.fromParts("package", getPackageName(), null)
                        //DLog.d("@" + uri0 + "|" + uri1 + "|");
                        //                try {
                        //                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        //                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                        //                    intent.setData(uri0);
                        //                    storageActivityResultLauncher.launch(intent);
                        //                    Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
                        //                } catch (Exception e) {
                        //                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        //                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                        //                    storageActivityResultLauncher.launch(intent);
                        //                }
                        try {
                            var intent: Intent = Intent()
                            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                            intent.setData(uri1)
                            storageActivityResultLauncher.launch(intent)
                        } catch (e: Exception) {
                            //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
                            var intent: Intent = Intent()
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                            storageActivityResultLauncher.launch(intent)
                        }
                        return@lmb2 true
                        })
        }
        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }
    @SuppressLint("NonConstantResourceId") override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //            case R.id.action_rate:
        //
        //                new AlertDialog.Builder(this)
        //                        .setTitle(getString(R.string.RateAppTitle))
        //                        .setMessage(getString(R.string.RateApp))
        //                        .setCancelable(false)
        //                        .setPositiveButton("RATE", (dialog, which) -> {
        //                            try {
        //                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
        //                                        + getPackageName())));
        //                            } catch (android.content.ActivityNotFoundException anfe) {
        //                                startActivity(
        //                                        new Intent(
        //                                                Intent.ACTION_VIEW,
        //                                                Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())
        //                                        )
        //                                );
        //                            }
        //                        })
        //                        .setNegativeButton("LATER", null).show();
        //                return true;
        //            case R.id.action_refresh:
        //                return false;
        //            case android.R.id.home:
        //                Intent intent = m1.newIntent(this);
        //                startActivity(intent);
        //                return true;
        //            case R.string.start_test_again:
        //                return false;
        //            case R.id.action_exit:
        //                this.finish();
        //                return true;
        var itemId: Int = item.getItemId()
        if (itemId == R.id.action_about) {
            DialogAbout.aboutDialog(this)
            return true
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, getString(R.string.url_privacy_policy))
            return true
        } else if (itemId == R.id.action_rate_app) {
            k!!.explode()
            Launcher.rateUs(this)
            return true
        } else if (itemId == R.id.action_share_app) {
            k!!.rain()
            Module_U.shareThisApp(this)
            return true
        } else if (itemId == R.id.action_discover_more_app) {
            Module_U.moreApp(this)
            return true
        } else if (itemId == R.id.action_feedback) {
            Module_U.feedback(this)
            return true
        } else if (itemId == R.id.action_more_app_02) {
            var packageName: String = getString(R.string.p_more_app_02)
            try {
                var uri: Uri = Uri.parse(String.format("market://details?id=%1\$s", packageName))
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } catch (anfe: Exception) {
                Launcher.openBrowser(this, UConst.GOOGLE_PLAY_CONSTANT + packageName)
            }
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
    //action_how_to_use_app
    //action_support_developer
    //return super.onOptionsItemSelected(item);
    fun isNeedGrantPermission0(): Boolean {
        return permissionResolver!!.isNeedGrantPermission()
    }
    companion object {
        const val APPLICATION_DETAILS_SETTINGS = 10001
        private const val REQUEST_SHARE = 1882
    }
}
