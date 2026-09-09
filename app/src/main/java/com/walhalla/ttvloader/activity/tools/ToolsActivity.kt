package com.walhalla.ttvloader.activity.tools

import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.walhalla.adapters.ViewpagerAdapter
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.databinding.ActivityMainBinding
import com.walhalla.intentresolver.FilePresenter
import com.walhalla.ttvloader.activity.main.MainActivity
import com.walhalla.intentresolver.FileView
import com.walhalla.ttvloader.databinding.FragmentFiletoolsBinding
import com.walhalla.ui.DLog
import java.io.File

class ToolsActivity : AppCompatActivity(), FileView {
    private var binding: ActivityMainBinding? = null
    private var mPresenter: FilePresenter? = null
    override protected fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState!!)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())
        setSupportActionBar(binding!!.toolbar)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.setTitle(null)
        }
        binding!!.tvVersion.setText(DLog.getAppVersion(this))
        mPresenter = FilePresenter(this, this)
        binding!!.tvVersion.setOnClickListener({
                v -> mPresenter!!.chooseFolder(this)
                })
        binding!!.tvVersion.setText("" + android.os.Build.VERSION.SDK_INT)
        binding!!.tvVersion.setBackgroundColor(Color.RED)
        var adapter: ViewpagerAdapter = ViewpagerAdapter(getSupportFragmentManager(), BEHAVIOR_SET_USER_VISIBLE_HINT)
        adapter.addFragment(Tools2Fragment(), getString(R.string.abc_tab_download))
        //        adapter.addFragment(new ToolsFragment(), getString(R.string.abc_tab_download));
        //        adapter.addFragment(new NumberInputFragment(), getString(R.string.abc_tab_gallery));
        binding!!.viewPager.setAdapter(adapter)
        binding!!.tabs.setupWithViewPager(binding!!.viewPager)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DLog.d("==> " + requestCode + "\t" + resultCode)
        //MainActivity.RESULT_CANCELED;
        if (requestCode == com.walhalla.ttvloader.Const.COMPONENT_REQUEST_CODE) {
        } else if (requestCode == MainActivity.APPLICATION_DETAILS_SETTINGS) {
        } else {
            //setlayout();
            if (data != null) mPresenter!!.onActivityResult(requestCode, resultCode, data)
        }
    }
    override protected fun onResume() {
        super.onResume()
        mPresenter!!.resume()
    }
    override fun openFolderChooser(intent: Intent?) {
        if (intent != null) startActivityForResult(intent, FilePresenter.REQUEST_CODE_CHOOSE_FOLDER)
    }
    override fun showSelectedFolder(file: File?) {
        binding!!.toolbar.setSubtitle("[DIR]" + file?.getAbsolutePath())
    }
    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun clickOk(number: Int, selectedIntent: Int) {
        mPresenter!!.start(number, selectedIntent)
    }
}
