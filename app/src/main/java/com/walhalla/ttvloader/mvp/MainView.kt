package com.walhalla.ttvloader.mvp

import com.walhalla.ttvloader.activity.HandleIntentActivity
import com.walhalla.ttvloader.models.LocalVideo

interface MainView : HandleIntentActivity {
    abstract fun makeToaster(res: Int)
    abstract fun handleException(err: Exception)
    abstract fun makeToaster0(format: String)
    abstract fun action_share_video(localVideo: LocalVideo)
    abstract fun makeErrorToaster(accessError: Int)
    abstract fun watchVideo(item: LocalVideo)
    //void showNoStoragePermissionSnackbar();
    abstract fun openApplicationSettings()
}
