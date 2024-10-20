package com.retrytech.medialoot.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.retrytech.medialoot.Adapter.ViewPaggerAdapter;
import com.retrytech.medialoot.Fragment.ContentFragment;
import com.retrytech.medialoot.Fragment.DownloadFragment;
import com.retrytech.medialoot.Fragment.NavDrawerFragment;
import com.retrytech.medialoot.Fragment.SaveFileFragment;
import com.retrytech.medialoot.R;
import com.retrytech.medialoot.databinding.ActivityHomeBinding;
import com.retrytech.medialoot.databinding.ActivityMainDashboardBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MainDashboardActivity extends AppCompatActivity implements View.OnClickListener, Observer, MaterialSearchBar.OnSearchActionListener, ViewPager.OnPageChangeListener {
    public ActivityMainDashboardBinding binding;
    ArrayList<String> liveData = new ArrayList<>();


    int PERMISSION_ALL = 1;
    boolean isCenterButtonClick = true;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ActionBarDrawerToggle drawerToggle;
    private int flag = -1;
    private Dialog main_dialogue;
    private UnifiedNativeAd nativeAd;

    private static boolean isBuy = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBind();
        if (!isBuy)
        {
            //showDailog();
        }

        //perMission();
        flag = getIntent().getIntExtra("flag", -1);
        setUpBottomNavigation(savedInstanceState);
        setonClick();
        String downloadPath = android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "Download" + File.separator;

        File dir = new File(downloadPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        SearchViewModel searchViewModel = new SearchViewModel(this);
        setviewModel(searchViewModel);

        SessionManager.storeFolderPath(MainDashboardActivity.this, downloadPath);
    }

    private void initBind() {
        setSupportActionBar(binding.toolbar);
        NavDrawerFragment navDrawerFragment = (NavDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        if (navDrawerFragment != null) {
            navDrawerFragment.setUp(R.id.fragment_navigation_drawer, binding.drawerLayout, binding.toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0f);
        }
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open, R.string.close);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        binding.drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (isCenterButtonClick && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            showalert();
        }
    }

//    private void showDailog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage("If you bought this application from codecanyon, Then contact us with your purchase code.\nSkype - Retry Tech ");
//        alertDialog.show();
//    }

    private void showalert() {


        LayoutInflater dialogue_layout = LayoutInflater.from(MainDashboardActivity.this);
        final View dialogueview = dialogue_layout.inflate(R.layout.alert_native_lout, null);
        main_dialogue = new Dialog(MainDashboardActivity.this);
        main_dialogue.setContentView(dialogueview);
        Button btn_yes, btn_no;
        btn_yes = dialogueview.findViewById(R.id.btn_yes);
        btn_no = dialogueview.findViewById(R.id.btn_no);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(main_dialogue.getWindow().getAttributes());
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        main_dialogue.getWindow().setAttributes(layoutParams);

        main_dialogue.setCancelable(false);
        btn_yes.setOnClickListener(v -> finish());
        btn_no.setOnClickListener(v -> main_dialogue.dismiss());

        nativeAds();



        main_dialogue.show();





/*


 View alertview=getLayoutInflater().inflate(R.layout.alert_native_lout,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertview);
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();*/

    }

    private void nativeAds() {
//        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.admob_nativ));
//        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//            // OnUnifiedNativeAdLoadedListener implementation.
//            @Override
//            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                // You must call destroy on old ads when you are done with them,
//                // otherwise you will have a memory leak.
//                if (nativeAd != null) {
//                    nativeAd.destroy();
//                }
//                nativeAd = unifiedNativeAd;
//                FrameLayout frameLayout = dialogueview.findViewById(R.id.fl_adplaceholder);
//                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
//                        .inflate(R.layout.ad_unified, null);
//                populateUnifiedNativeAdView(unifiedNativeAd, adView);
//                frameLayout.removeAllViews();
//                frameLayout.addView(adView);
//            }
//
//        });


//        VideoOptions videoOptions = new VideoOptions.Builder()
//                .build();
//
//        NativeAdOptions adOptions = new NativeAdOptions.Builder()
//                .setVideoOptions(videoOptions)
//                .build();
//
//        builder.withNativeAdOptions(adOptions);
//
//        AdLoader adLoader = builder.withAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(MainDashboardActivity.this, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
//            }
//        }).build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());
    }

//    @Override
//    protected void onDestroy() {
//        searchViewModel.compositeDisposable.clear();
//        super.onDestroy();
//    }

    private void setviewModel(Observable observable) {
        observable.addObserver(this);
    }

    private void perMission() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setonClick() {

        // getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment(), "home").commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //binding.space.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
       /* switch (view.getId()) {
            case R.id.btninstagram:
                startActivity(new Intent(MainDashboardActivity.this, InstagramActivity.class));
                break;
            case R.id.btnFacebook:
                startActivity(new Intent(MainDashboardActivity.this, FacebookVideoActivity.class));
                break;
            case R.id.btnweb:
                startActivity(new Intent(MainDashboardActivity.this, DailymotionActivity.class));
                break;
           *//* case R.id.btnweb2:
                startActivity(new Intent(MainDashboardActivity.this, WebBrowserActivity.class));
                break;*//*
            default:
                break;
        }*/
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("isdownload", false)) {
                binding.space.showBadgeAtIndex(0, 1, getResources().getColor(R.color.colorRed));
            } else {
                binding.space.showBadgeAtIndex(1, 1, getResources().getColor(R.color.colorGreen));
                Intent intent1 = new Intent("savefile");
                intent1.putExtra("isfinish", true);
                LocalBroadcastManager.getInstance(MainDashboardActivity.this).sendBroadcast(intent1);
            }
        }

    };

    private void setUpBottomNavigation(Bundle savedInstanceState) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("string"));
        binding.space.initWithSaveInstanceState(savedInstanceState);
        binding.space.addSpaceItem(new SpaceItem("Active", R.drawable.ic_file_download_black_24dp));
        binding.space.addSpaceItem(new SpaceItem("Completed ", R.drawable.ic_insert_drive_file_black_24dp));
        binding.space.setCentreButtonSelectable(true);
        binding.space.setCentreButtonIconColorFilterEnabled(true);
        binding.space.setActiveCentreButtonIconColor(getResources().getColor(R.color.black));
        binding.space.setCentreButtonSelected();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new DownloadFragment());
        fragments.add(new ContentFragment());
        fragments.add(new SaveFileFragment());

        ViewPaggerAdapter viewPaggerAdapter = new ViewPaggerAdapter(getSupportFragmentManager());
        viewPaggerAdapter.setFragment(fragments);
        binding.space.setCentreButtonSelected();
        binding.viewpager.setAdapter(viewPaggerAdapter);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.addOnPageChangeListener(this);
        binding.viewpager.setOffscreenPageLimit(3);

        binding.space.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                isCenterButtonClick = true;
                binding.toolbar.setVisibility(View.GONE);
                binding.viewpager.setCurrentItem(1);

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Log.i("itemName", "onItemClick: " + itemIndex);
                binding.space.hideBadgeAtIndex(itemIndex);
                isCenterButtonClick = false;
                switch (itemIndex) {

                    case 0:
                        binding.toolbar.setVisibility(View.VISIBLE);
                        binding.viewpager.setCurrentItem(0);
                        break;
                    case 1:

                        binding.toolbar.setVisibility(View.GONE);
                        binding.viewpager.setCurrentItem(2);
                        break;
                }
                // binding.space.setActiveSpaceItemColor(getResources().getColor(R.color.colorRed));
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //  binding.space.invalidate();
                isCenterButtonClick = false;
                switch (itemIndex) {
                    case 0:
                        binding.viewpager.setCurrentItem(0);
                        break;
                    case 1:
                        binding.viewpager.setCurrentItem(2);
                        break;

                }
                // binding.space.setActiveSpaceItemColor(getResources().getColor(R.color.colorRed));
                Log.i("itemName", "onItemReselected: " + itemIndex);
            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof SearchViewModel) {
            SearchViewModel searchViewModel = (SearchViewModel) observable;
            String searchResponce = searchViewModel.getmJobsDetails();
            if (searchResponce != null) {

                liveData.add(searchResponce);

//                binding.searchBar.updateLastSuggestions(liveData);
            }
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:

//                drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:

                break;
            case MaterialSearchBar.BUTTON_BACK:
//                binding.searchBar.disableSearch();
                break;
        }
    }

//    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView adView) {
//
//        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
//
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
//        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        //adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
//        if (nativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            adView.getIconView().setVisibility(View.GONE);
//        } else {
//            ((ImageView) adView.getIconView()).setImageDrawable(
//                    nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }
//
//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }
//
//        if (nativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad.
//        adView.setNativeAd(nativeAd);
//
//        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
//        // have a video asset.
//        VideoController vc = nativeAd.getVideoController();
//
//        // Updates the UI to say whether or not this ad has a video asset.
//
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.space.changeCurrentItem(0);
                break;
            case 1:
                binding.toolbar.setVisibility(View.GONE);
                binding.space.setCentreButtonSelected();
                binding.space.setActiveCentreButtonIconColor(getResources().getColor(R.color.black));
                break;
            case 2:
                binding.toolbar.setVisibility(View.GONE);
                binding.space.changeCurrentItem(1);
                break;
        }
    }

  /*  @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                binding.bottom.setSelectedItemId(R.id.download);
                break;
            case 1:
                binding.bottom.setSelectedItemId(R.id.home);
                break;
            case 2:
                binding.bottom.setSelectedItemId(R.id.savefile);
                break;
        }
    }*/

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
