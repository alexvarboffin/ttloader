package com.walhalla.ttloader.core

import android.content.ContentResolver
import android.provider.MediaStore
import com.walhalla.ttvloader.models.LocalVideo
import com.walhalla.ui.DLog
import java.util.ArrayList
import java.util.LinkedHashMap

data class GallerySnapshot(
    val videos: MutableList<LocalVideo>,
    val folderNames: MutableList<String>
)

object GalleryCatalog {
    const val KEY_ALL_FILES = "All"

    fun load(contentResolver: ContentResolver): GallerySnapshot {
        val videos = ArrayList<LocalVideo>()
        val folderNamesTmp = ArrayList<String>()
        val videosByFolder = LinkedHashMap<String, MutableList<LocalVideo>>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Thumbnails.DATA
        )
        val sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC"
        var cursor: android.database.Cursor? = null
        try {
            cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )
        } catch (e: Exception) {
            DLog.d("В поиск добавлено поле которого не существует")
        }
        try {
            if (cursor != null && cursor.count >= 1) {
                val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                val columnIndexFolder = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                val thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
                val duration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                var i = 0
                val sortedByDate = LinkedHashMap<String, LocalVideo>()
                while (cursor.moveToNext()) {
                    val absolutePath = cursor.getString(columnIndexData)
                    val folderName = cursor.getString(columnIndexFolder) ?: ""
                    val video = LocalVideo()
                    video.selected = false
                    video.path = absolutePath
                    video.thumb = cursor.getString(thum)
                    video.duration = cursor.getInt(duration)
                    video.id = i
                    sortedByDate.put(video.path ?: "", video)
                    if (!videosByFolder.containsKey(folderName)) {
                        videosByFolder.put(folderName, ArrayList())
                        folderNamesTmp.add(folderName)
                    }
                    videosByFolder.get(folderName)?.add(video)
                    i++
                }
                for (entry in sortedByDate.entries) {
                    videos.add(entry.value)
                }
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        } finally {
            cursor?.close()
        }
        val folders = ArrayList<String>()
        if (folderNamesTmp.isNotEmpty()) {
            folders.add(KEY_ALL_FILES)
            folders.addAll(folderNamesTmp)
        }
        return GallerySnapshot(videos, folders)
    }
}
