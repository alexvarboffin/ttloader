package com.walhalla.ttvloader.activity.tools;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;
import static com.walhalla.ttvloader.Const.COMPONENT_REQUEST_CODE;
import static com.walhalla.ttvloader.activity.main.MainActivity.APPLICATION_DETAILS_SETTINGS;
import static com.walhalla.intentresolver.FilePresenter.REQUEST_CODE_CHOOSE_FOLDER;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.adapters.ViewpagerAdapter;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.databinding.ActivityMainBinding;
import com.walhalla.intentresolver.FilePresenter;
import com.walhalla.intentresolver.FileView;
import com.walhalla.ttvloader.databinding.FragmentFiletoolsBinding;
import com.walhalla.ui.DLog;

import java.io.File;

public class ToolsActivity extends AppCompatActivity implements FileView {


    private ActivityMainBinding binding;
    private FilePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        binding.tvVersion.setText(DLog.getAppVersion(this));

        mPresenter = new FilePresenter(this, this);

        binding.tvVersion.setOnClickListener(v -> mPresenter.chooseFolder(this));
        binding.tvVersion.setText("" + android.os.Build.VERSION.SDK_INT);
        binding.tvVersion.setBackgroundColor(Color.RED);

        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager(), BEHAVIOR_SET_USER_VISIBLE_HINT);

        adapter.addFragment(new Tools2Fragment(), getString(R.string.abc_tab_download));
//        adapter.addFragment(new ToolsFragment(), getString(R.string.abc_tab_download));
//        adapter.addFragment(new NumberInputFragment(), getString(R.string.abc_tab_gallery));

        binding.viewPager.setAdapter(adapter);

        binding.tabs.setupWithViewPager(binding.viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DLog.d("==> " + requestCode + "\t" + resultCode);
        //MainActivity.RESULT_CANCELED;
        if (requestCode == COMPONENT_REQUEST_CODE) {

        } else if (requestCode == APPLICATION_DETAILS_SETTINGS) {
            //setlayout();
        } else {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void openFolderChooser(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_FOLDER);
    }

    @Override
    public void showSelectedFolder(File file) {
        binding.toolbar.setSubtitle("[DIR]" + file.getAbsolutePath());
    }

    @Override
    public void showError(String message) {
        Toast.makeText(ToolsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void clickOk(int number, int selectedIntent) {
        mPresenter.start(number, selectedIntent);
    }



}
