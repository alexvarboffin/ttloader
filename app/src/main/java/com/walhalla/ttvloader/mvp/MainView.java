package com.walhalla.ttvloader.mvp;

import com.walhalla.ttvloader.activity.HandleIntentActivity;
import com.walhalla.ttvloader.models.LocalVideo;

public interface MainView extends HandleIntentActivity {

    void makeToaster(int res);

    void handleException(Exception err);

    void makeToaster0(String format);



    void action_share_video(LocalVideo localVideo);

    void makeErrorToaster(int accessError);

    void watchVideo(LocalVideo item);

    //void showNoStoragePermissionSnackbar();

    void openApplicationSettings();
}
