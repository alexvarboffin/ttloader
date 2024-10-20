package com.retrytech.medialoot.Fragment;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.retrytech.medialoot.databinding.FragmentHomeBinding;
import com.retrytech.medialoot.Activity.Constant;
import com.retrytech.medialoot.Activity.SearchViewModel;
import com.retrytech.medialoot.R;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, MaterialSearchBar.OnSearchActionListener, Observer {


    private SearchViewModel searchViewModel;
    //private InterstitialAd interstitialAd;


    public HomeFragment() {
        // Required empty public constructor
    }

    NavDrawerFragment drawerFragment = new NavDrawerFragment();
    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);


        AdView adView = new AdView(binding.getRoot().getContext());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getContext().getString(R.string.admob_banner));
        AdRequest.Builder adRequest= new AdRequest.Builder();
        adView.loadAd(adRequest.build());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.adContainer.addView(adView, params);

//        interstitialAd=new InterstitialAd(getContext());
//        interstitialAd.setAdUnitId(getString(R.string.admob_intrestial));
//        AdRequest adRequest1=new AdRequest.Builder().build();
//        interstitialAd.loadAd(adRequest1);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setonClick();
        searchViewModel = new SearchViewModel(getActivity());
        setviewModel(searchViewModel);

    }


    private void setviewModel(Observable observable) {
        observable.addObserver(this);
    }

    private void setonClick() {
        binding.btninstagram.setOnClickListener(this);
        binding.btnFacebook.setOnClickListener(this);
        binding.btndailymotion.setOnClickListener(this);
        binding.btnSearch.setOnClickListener(this);
        binding.btnvimeo.setOnClickListener(this);
        binding.btnDownload.setOnClickListener(this);
        binding.etUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    binding.btnSearch.performClick();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btninstagram) {
            showAds();
            openFragment(Constant.INSTAGRAM, "https://www.instagram.com/");
        } else if (viewId == R.id.btnFacebook) {
            showAds();
            openFragment(Constant.FACEBOOK, "https://www.facebook.com/");
        } else if (viewId == R.id.btndailymotion) {
            showAds();
            openFragment(Constant.DAILYMOTION, "https://www.dailymotion.com");
        } else if (viewId == R.id.btn_search) {
            showAds();
            openFragment(Constant.BROWSER, binding.etUrl.getText().toString().trim());
        } else if (viewId == R.id.btnvimeo) {
            showAds();
            openFragment(Constant.BROWSER, "https://vimeo.com/watch");
        } else if (viewId == R.id.btn_download) {
            showAds();
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new SaveFileFragment(), "webview").addToBackStack("webview").commit();
        }
    }

    private void showAds() {
//        if (interstitialAd.isLoaded()){
//            interstitialAd.show();
//        }
    }

    private void openFragment(int flag, CharSequence text) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.FLAG, flag);
        bundle.putString(Constant.WORDS, text.toString());
        WebViewFragment webViewFragment = new WebViewFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, webViewFragment, "webview").addToBackStack("webview").commit();
        webViewFragment.setArguments(bundle);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
