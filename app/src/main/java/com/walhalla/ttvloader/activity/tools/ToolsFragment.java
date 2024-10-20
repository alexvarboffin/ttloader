package com.walhalla.ttvloader.activity.tools;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.walhalla.ttvloader.databinding.FragmentFiletoolsBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ToolsFragment extends Fragment {

    private FragmentFiletoolsBinding binding;
    private static final int REQUEST_CODE_PERMISSION = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFiletoolsBinding.inflate(inflater, container, false);
        binding.readTagsFromFile.setOnClickListener((v) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    readTagsFromFile(getContext());
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            } else {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                } else {
                    readTagsFromFile(getContext());
                }
            }
        });
        binding.clear.setOnClickListener(v->{
            binding.text.setText(null);
        });
        return binding.getRoot();
    }

    private void readTagsFromFile(Context context) {
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard, "tags.txt"); // Путь к файлу

        List<String> tags = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                tags.add(line.trim()); // Добавление тега в список
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Ошибка чтения файла", Toast.LENGTH_SHORT).show();
            return;
        }

        // Рандомизация списка тегов
        Collections.shuffle(tags);

//        // Пример вывода рандомизированных тегов
//        for (String tag : tags) {
//            System.out.println(tag); // Или используйте любой другой способ вывода
//        }
        StringBuilder tagsToCopy = new StringBuilder();
        tagsToCopy.append("\uD83D\uDCFA Ultimate.TV: IPTV Player\uD83D\uDD0D\n" +
                "\uD83D\uDD17Google Play: https://bit.ly/3SUvBo7\n\n");
        for (String tag : tags) {
            //tagsToCopy.append(tag).append("\n");
            tagsToCopy.append(tag).append(" ");

        }


        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("tags", tagsToCopy.toString());

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        //Toast.makeText(context, "Success!!!", Toast.LENGTH_SHORT).show();
        binding.text.setText(tagsToCopy.toString());
    }
}
