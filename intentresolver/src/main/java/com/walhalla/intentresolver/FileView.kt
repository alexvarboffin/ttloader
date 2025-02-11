package com.walhalla.intentresolver;

import android.content.Intent;

import java.io.File;

public interface FileView {
    void openFolderChooser(Intent intent);
    void showSelectedFolder(File folderPath);
    void showError(String message);

    //void shareMp4Selector(File file, String o);
}