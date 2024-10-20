package com.walhalla.extractors.presenters;

import static com.walhalla.extractors.presenters.VideoRepository.ERROR_WENT_WRONG;
import static com.walhalla.extractors.presenters.VideoRepository.EXT_JPG;

import android.os.Handler;
import android.text.TextUtils;

import com.walhalla.libcore.TxTUtil;
import com.walhalla.ttvloader.TTResponse;
import com.walhalla.ui.DLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinterestPresenter extends AbstractInfoExtractor {

    private Exception mError;

    public PinterestPresenter(RepositoryCallback callback, VideoRepository repository, Handler handler) {
        super(callback, repository, handler);
    }

    @Override
    public void execute(String url) {
        mError = null;
        executor.execute(() -> {
            TTResponse response = new TTResponse();
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(VideoRepository.TIMEOUT)
                        .get();
                try {
                    response.title = doc.title();
                    String resp = doc.toString();
                    //DLog.d("@" + doc.toString());username

                    String V0START = "\"contentUrl\":\"";
                    String V0STARTIMG = "\"imageSpec_orig\":{\"url\":\"";


                    String videoFileUrl = "";
                    String imgFileUrl = "";
                    String fileUrl = "";

                    String tmp = extractFullName(resp);
                    if (resp.contains(tmp)) {
                        response.username = tmp;
                    }

                    if (resp.contains(V0START)) {
                        videoFileUrl = TxTUtil.eE(resp, V0START, "\"");
                        response.ext = EXT_MP4;
                        fileUrl = videoFileUrl;
                    }


                    if (resp.contains(V0STARTIMG)) {
                        imgFileUrl = TxTUtil.eE(resp, V0STARTIMG, "\"");
                        response.thumb = imgFileUrl;
                    }

                    if (TextUtils.isEmpty(videoFileUrl) && !TextUtils.isEmpty(imgFileUrl)) {
                        response.ext = EXT_JPG;
                        fileUrl = imgFileUrl;
                    }
                    response.cleanVideo = fileUrl;
                    response.contentURL = fileUrl;
                    DLog.d("@ " + fileUrl + " @ " + url);
                    //iUtils.ShowToast(Mcontext, fileUrl);
                } catch (NullPointerException e) {
                    DLog.handleException(e);
                    mError = e;
                }
            } catch (IOException e) {
                DLog.handleException(e);
                mError = e;
            }
            onPostExecute(response);
        });
    }

    public static String extractFullName(String text) {
        String regex = "\"fullName\":\\s*\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String username = matcher.group(1);
            if (!TextUtils.isEmpty(username)) {
                return username;
            }
        }
        return null;
    }

    protected void onPostExecute(TTResponse response) {
        mThread.post(() -> {
            repository.onPostExecute0();
            if (mError != null) {
                if (callback != null) {
                    callback.errorResult(ERROR_WENT_WRONG);
                }
            }
            String target = response.cleanVideo;
            if (!TextUtils.isEmpty(target)) {
                if (callback != null) {
                    repository.downloadPinterestFile(target, response.title, response.ext);
                }
                if (callback != null) {
                    callback.successResult(response);
                }
            }
        });
    }
}
