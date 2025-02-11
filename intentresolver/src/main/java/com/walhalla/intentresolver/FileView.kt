package com.walhalla.intentresolver

import android.content.Intent
import java.io.File

interface FileView {
    fun openFolderChooser(intent: Intent?)
    fun showSelectedFolder(folderPath: File?)
    fun showError(message: String?) //void shareMp4Selector(File file, String o);
}