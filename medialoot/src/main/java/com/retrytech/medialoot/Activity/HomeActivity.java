package com.retrytech.medialoot.Activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.retrytech.medialoot.databinding.ActivityHomeBinding;
import com.retrytech.medialoot.BuildConfig;
import com.retrytech.medialoot.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setonClick();
    }

    private void setonClick() {
        binding.laySearch.setOnClickListener(this);
        binding.layDownload.setOnClickListener(this);
        binding.layShare.setOnClickListener(this);
        binding.layRateus.setOnClickListener(this);
        binding.layMoreApps.setOnClickListener(this);
        binding.layPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lay_search) {
            startActivity(new Intent(this, MainDashboardActivity.class).putExtra("flag", 0));
        } else if (id == R.id.lay_download) {
            startActivity(new Intent(this, MainDashboardActivity.class).putExtra("flag", 1));
        } else if (id == R.id.lay_share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        } else if (id == R.id.lay_rateus) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } else if (id == R.id.lay_more_apps) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + getPackageName())));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + getPackageName())));
            }
        } else if (id == R.id.lay_privacy_policy) {
            /* Create the Intent */
            try{
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "abcd@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                startActivity(intent);
            }catch(ActivityNotFoundException e){
                //TODO smth
            }
        }

    }

}
