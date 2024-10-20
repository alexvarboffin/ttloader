package com.walhalla.ttvloader.activity;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Player;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;

import com.walhalla.abcsharedlib.Share;
import com.walhalla.ttvloader.databinding.FragmentPlayerBinding;
import com.walhalla.ui.DLog;

import java.io.File;

@SuppressLint("UnsafeOptInUsageError")
public class PlayerActivity extends AppCompatActivity {
    private static final String TAG = "@@@";


    private ExoPlayer player;
    private boolean playWhenReady = true;
    private int currentItem = 0;
    private long playbackPosition = 0L;

    private FragmentPlayerBinding binding;
    private Uri videoUrl;
    private final Player.Listener playbackStateListener = new Player.Listener() {


        @Override
        public void onPlaybackStateChanged(int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -" + videoUrl;
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -" + videoUrl;
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -" + videoUrl;
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -" + videoUrl;
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -" + videoUrl;
            }
            Log.d(TAG, "changed state to " + stateString);
        }
    };

//    public static Intent newIntent(Context context, String path) {
//        Intent intent = new Intent(context, PlayerActivity.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        final File videoFile = new File(path);
//        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + Share.KEY_FILE_PROVIDER, videoFile);
//        boolean exists = DocumentFile.fromSingleUri(context, fileUri).exists();
//        intent.setData(fileUri);
//        return intent;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            if (uri != null) {
                // Здесь вы можете получить данные из URI
                videoUrl = uri;
                // Далее вы можете использовать URL видео
                // Например, передать его в ваш метод initializePlayer()
                // initializePlayer(videoUrl);
                DLog.d("@@@@" + videoUrl + " " + videoUrl.getEncodedAuthority());
                //getSupportActionBar().setSubtitle(String.valueOf(videoUrl));
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (videoUrl == null) {
            return;
        }

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());
        player = new ExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();
        binding.videoView.setPlayer(player);

        //DLog.d("Get-> " + (player != null) + "@@" + videoUrl);


        //If Online = application/dash+xml
        //MimeTypes.APPLICATION_MPD

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoUrl)
                .setMimeType(MimeTypes.BASE_TYPE_VIDEO)
                .build();
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentItem, playbackPosition);
        player.addListener(playbackStateListener);
        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentItem = player.getCurrentMediaItemIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(playbackStateListener);
            player.release();
            player = null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), binding.videoView);
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
    }
}