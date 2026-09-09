package com.walhalla.ttloader.compose

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.walhalla.extractors.presenters.RepositoryCallback
import com.walhalla.extractors.presenters.VideoRepository
import com.walhalla.ttloader.core.R as CoreR
import com.walhalla.ttvloader.TTResponse

class DownloadSession(
    context: Context,
    private val onState: (DownloadUiState) -> Unit
) : RepositoryCallback {
    private val appContext = context.applicationContext
    private val handler = Handler(Looper.getMainLooper())
    private val repository = VideoRepository(appContext, this, handler)

    fun start(url: String) {
        val trimmed = url.trim()
        if (trimmed.isEmpty()) {
            onState(DownloadUiState(error = "Clipboard or link is empty"))
            return
        }
        repository.makeDownload(trimmed, false, true)
    }

    override fun successResult(result: TTResponse) {
        handler.post { onState(DownloadUiState(loading = false, result = result)) }
    }

    override fun errorResult(error: String) {
        handler.post { onState(DownloadUiState(loading = false, error = error)) }
    }

    override fun errorResult(errWwwNotSupport: Int) {
        val message = try {
            appContext.getString(errWwwNotSupport)
        } catch (e: Exception) {
            appContext.getString(CoreR.string.abc_something_went_wrong)
        }
        handler.post { onState(DownloadUiState(loading = false, error = message)) }
    }

    override fun showProgressDialog() {
        handler.post { onState(DownloadUiState(loading = true)) }
    }

    override fun hideProgressDialog() {
        handler.post { onState(DownloadUiState(loading = false)) }
    }
}

data class DownloadUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val result: TTResponse? = null
)
