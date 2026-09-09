package com.walhalla.ttvloader.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.documentfile.provider.DocumentFile
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.walhalla.abcsharedlib.Share
import com.walhalla.ttvloader.databinding.FragmentPlayerBinding
import com.walhalla.ui.DLog
import java.io.File

@SuppressLint("UnsafeOptInUsageError") class PlayerActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private var playWhenReady: Boolean = true
    private var currentItem: Int = 0
    private var playbackPosition: Long = 0L
    private var binding: FragmentPlayerBinding? = null
    private var videoUrl: Uri? = null
    private val playbackStateListener: Player.Listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                var stateString: String? = null
                when (playbackState) {
                    ExoPlayer.STATE_IDLE -> {
                        stateString = "ExoPlayer.STATE_IDLE      -" + videoUrl!!
                    }
                    ExoPlayer.STATE_BUFFERING -> {
                        stateString = "ExoPlayer.STATE_BUFFERING -" + videoUrl!!
                    }
                    ExoPlayer.STATE_READY -> {
                        stateString = "ExoPlayer.STATE_READY     -" + videoUrl!!
                    }
                    ExoPlayer.STATE_ENDED -> {
                        stateString = "ExoPlayer.STATE_ENDED     -" + videoUrl!!
                    }
                    else -> {
                        stateString = "UNKNOWN_STATE             -" + videoUrl!!
                    }
                }
                Log.d(TAG, "changed state to " + stateString!!)
            }
        }
    //    public static Intent newIntent(Context context, String path) {
    //        Intent intent = new Intent(context, PlayerActivity.class);
    //        intent.setAction(Intent.ACTION_VIEW);
    //        final File videoFile = new File(path);
    //        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + Share.KEY_FILE_PROVIDER, videoFile);
    //        boolean exists = DocumentFile.fromSingleUri(context, fileUri).exists();
    //        intent.setData(fileUri);
    //        return intent;
    //    }
    override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState!!)
        binding = FragmentPlayerBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        }
        var intent: Intent = getIntent()
        if (intent != null && intent.getAction()!! != null && intent.getAction()!!.equals(Intent.ACTION_VIEW)) {
            var uri = intent.getData()
            if (uri != null) {
                // Здесь вы можете получить данные из URI
                videoUrl = uri
                // Далее вы можете использовать URL видео
                // Например, передать его в ваш метод initializePlayer()
                // initializePlayer(videoUrl);
                DLog.d("" + videoUrl!! + " " + videoUrl!!.getEncodedAuthority())
            }
        }
    }
    //getSupportActionBar()?.setSubtitle(java.lang.String.valueOf(videoUrl));
    override protected fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }
    override protected fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }
    override protected fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }
    override protected fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }
    private fun initializePlayer() {
        if (videoUrl == null) {
            return
        }
        var trackSelector: DefaultTrackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())
        player = ExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        binding!!.videoView.setPlayer(player!!)
        //DLog.d("Get-> " + (player != null) + "" + videoUrl);
        //If Online = application/dash+xml
        //MimeTypes.APPLICATION_MPD
        var mediaItem: MediaItem = MediaItem.Builder().setUri(videoUrl!!).setMimeType(MimeTypes.BASE_TYPE_VIDEO).build()
        player!!.setMediaItem(mediaItem)
        player!!.setPlayWhenReady(playWhenReady)
        player!!.seekTo(currentItem, playbackPosition)
        player!!.addListener(playbackStateListener)
        player!!.prepare()
    }
    private fun releasePlayer() {
        if (player!! != null) {
            playbackPosition = player!!.getCurrentPosition()
            currentItem = player!!.getCurrentMediaItemIndex()
            playWhenReady = player!!.getPlayWhenReady()
            player!!.removeListener(playbackStateListener)
            player!!.release()
            player = null
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("InlinedApi") private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false)
        var controller: WindowInsetsControllerCompat = WindowInsetsControllerCompat(getWindow(), binding!!.videoView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE)
    }
    companion object {
        private const val TAG = "@"
    }
}
