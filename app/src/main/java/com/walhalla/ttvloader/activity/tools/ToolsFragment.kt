package com.walhalla.ttvloader.activity.tools

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.walhalla.ttvloader.databinding.FragmentFiletoolsBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList
import java.util.Collections
import java.util.List

class ToolsFragment : Fragment() {
    private var binding: FragmentFiletoolsBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFiletoolsBinding.inflate(inflater, container!!, false)
        binding!!.readTagsFromFile.setOnClickListener({ v -> 
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        readTagsFromFile(getContext()!!)
                    } else {
                        var intent: Intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        var uri: Uri = Uri.fromParts("package", getContext()!!.getPackageName(), null)
                        intent.setData(uri)
                        startActivity(intent)
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(getContext()!!, Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity()!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
                    } else {
                        readTagsFromFile(getContext()!!)
                    }
                }
                })
        binding!!.clear.setOnClickListener({ v -> 
                binding!!.text.setText(null)
                })
        return binding!!.getRoot()
    }
    private fun readTagsFromFile(context: Context) {
        var sdCard: File = Environment.getExternalStorageDirectory()
        var file: File = File(sdCard, "tags.txt")
        // Путь к файлу
        var tags: MutableList<String> = ArrayList()
        BufferedReader(FileReader(file)).use { br ->
            var line = br.readLine()
            while (line != null) {
                tags.add(line.trim())
                line = br.readLine()
            }
        }
        /* catches after use */
        // Добавление тега в список
        // Рандомизация списка тегов
        Collections.shuffle(tags)
        //        // Пример вывода рандомизированных тегов
        //        for (String tag : tags) {
        //            System.out.println(tag); // Или используйте любой другой способ вывода
        //        }
        var tagsToCopy: StringBuilder = StringBuilder()
        tagsToCopy.append("?? Ultimate.TV: IPTV Player??\n" + "??Google Play: https://bit.ly/3SUvBo7\n\n")
        for (tag in tags) {
            //tagsToCopy.append(tag).append("\n");
            tagsToCopy.append(tag).append(" ")
        }
        var clipboard: ClipboardManager = (getActivity()!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        var clip: ClipData = ClipData.newPlainText("tags", tagsToCopy.toString())
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip)
        }
        //Toast.makeText(context, "Success!!!", Toast.LENGTH_SHORT).show();
        binding!!.text.setText(tagsToCopy.toString())
    }
    companion object {
        private const val REQUEST_CODE_PERMISSION = 1
    }
}
