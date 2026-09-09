package com.walhalla.ttloader.compose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val shared = readShare(intent)
        setContent {
            TtTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val nav = rememberNavController()
                    TtNavHost(nav, startShare = shared)
                }
            }
        }
        ClipboardMonitorService.start(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        recreate()
    }
}

class AutoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val shared = readShare(intent)
        setContent {
            TtTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val nav = rememberNavController()
                    TtNavHost(nav, startShare = shared, autoMode = true)
                }
            }
        }
    }
}

data class SharedPayload(val text: String?, val image: Uri?)

fun readShare(intent: Intent?): SharedPayload {
    if (intent == null) return SharedPayload(null, null)
    val type = intent.type
    var text = intent.getStringExtra(Intent.EXTRA_TEXT)
    var image: Uri? = null
    if (Intent.ACTION_SEND == intent.action) {
        if (type != null && type.startsWith("image/")) {
            image = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        if (text.isNullOrBlank() && "text/plain" == type) {
            text = intent.getStringExtra(Intent.EXTRA_TEXT)
        }
    }
    return SharedPayload(text, image)
}
