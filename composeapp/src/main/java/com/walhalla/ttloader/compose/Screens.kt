package com.walhalla.ttloader.compose

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.widget.Config
import com.walhalla.abcsharedlib.SharedNetwork
import com.walhalla.extractors.ExUtils
import com.walhalla.ttloader.core.GalleryCatalog
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ttvloader.models.LocalVideo
import com.walhalla.ui.utils.PackageUtils
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadScreen(shared: SharedPayload, autoStart: Boolean, onOpenTools: () -> Unit, onOpenMime: () -> Unit) {
    val context = LocalContext.current
    var url by remember { mutableStateOf(shared.text.orEmpty()) }
    var state by remember { mutableStateOf(DownloadUiState()) }
    val session = remember { DownloadSession(context) { state = it } }
    LaunchedEffect(shared.text, autoStart) {
        if (!shared.text.isNullOrBlank() && autoStart) {
            url = shared.text
            session.start(shared.text)
        } else if (!shared.text.isNullOrBlank()) {
            url = shared.text
        }
    }
    Scaffold(topBar = { TopAppBar(title = { Text("Download") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Paste a TikTok, Instagram, Facebook, Likee, Pinterest or Triller link.", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Video link") },
                minLines = 2
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(onClick = {
                    val clip = (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip
                    val text = clip?.getItemAt(0)?.text?.toString().orEmpty()
                    if (text.isBlank()) {
                        Toast.makeText(context, "Clipboard is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        url = text
                    }
                }) {
                    Icon(Icons.Default.ContentPaste, contentDescription = null)
                    Text(" Paste")
                }
                Button(onClick = { session.start(url) }) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Text(" Download")
                }
            }
            if (shared.image != null) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                    Text("Shared image received: ${shared.image}", modifier = Modifier.padding(12.dp))
                }
            }
            if (state.loading) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator()
                    Text("Resolving download link…")
                }
            }
            state.error?.let { err ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(err, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
            state.result?.let { ResultCard(it) }
            Text("Open source apps", style = MaterialTheme.typography.titleMedium)
            SourceApps()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = onOpenTools, label = { Text("Tools") })
                AssistChip(onClick = onOpenMime, label = { Text("Mime handlers") })
            }
        }
    }
}

@Composable
private fun ResultCard(result: TTResponse) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(result.title ?: "Video", style = MaterialTheme.typography.titleMedium)
            Text(result.username ?: "Unknown author", style = MaterialTheme.typography.labelLarge)
            if (!result.description.isNullOrBlank()) {
                Text(result.description!!, maxLines = 4, overflow = TextOverflow.Ellipsis)
            }
            Text("Link: ${result.cleanVideo ?: result.contentURL ?: "—"}", maxLines = 3, overflow = TextOverflow.Ellipsis)
            Text("Ext: ${result.ext ?: ".mp4"}")
        }
    }
}

@Composable
private fun SourceApps() {
    val context = LocalContext.current
    val apps = listOf(
        "TikTok" to SharedNetwork.Package.TIKTOK_T_PACKAGE,
        "TikTok Lite" to SharedNetwork.Package.TIKTOK_LITE,
        "Facebook" to SharedNetwork.Package.FACEBOOK,
        "Instagram" to SharedNetwork.Package.INSTAGRAM,
        "Likee" to SharedNetwork.Package.LIKEE,
        "Pinterest" to SharedNetwork.Package.PINTEREST,
        "Triller" to SharedNetwork.Package.TRILLER
    )
    LazyColumn(modifier = Modifier.height(180.dp)) {
        items(apps) { (label, pkg) ->
            val installed = PackageUtils.isPackageInstalledForLaunch(context, pkg)
            FilledTonalButton(
                onClick = { launchPackage(context, pkg) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(if (installed) label else "$label (not installed)")
            }
        }
    }
    Text("Extractors: " + ExUtils.defExtractors().joinToString { it.javaClass.simpleName }, style = MaterialTheme.typography.bodySmall)
}

private fun launchPackage(context: Context, pkg: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(pkg)
    if (intent != null) {
        context.startActivity(intent)
    } else {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")))
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg")))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen() {
    val context = LocalContext.current
    var granted by remember { mutableStateOf(hasVideoPermission(context)) }
    var videos by remember { mutableStateOf<List<LocalVideo>>(emptyList()) }
    var folders by remember { mutableStateOf<List<String>>(emptyList()) }
    var folder by remember { mutableStateOf(GalleryCatalog.KEY_ALL_FILES) }
    var menuFor by remember { mutableStateOf<LocalVideo?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        granted = hasVideoPermission(context)
    }
    fun reload() {
        val snap = GalleryCatalog.load(context.contentResolver)
        videos = snap.videos
        folders = snap.folderNames
        if (folder !in folders) folder = GalleryCatalog.KEY_ALL_FILES
    }
    LaunchedEffect(granted) { if (granted) reload() }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Gallery") }, actions = {
            IconButton(onClick = { if (granted) reload() }) { Icon(Icons.Default.FolderOpen, contentDescription = "Refresh") }
        })
    }) { padding ->
        Column(Modifier.padding(padding).padding(12.dp).fillMaxSize()) {
            if (!granted) {
                EmptyBlock("Storage permission is required to show saved videos.") {
                    launcher.launch(videoPermissions())
                }
            } else if (videos.isEmpty()) {
                EmptyBlock("No downloaded videos yet.") { reload() }
            } else {
                var open by remember { mutableStateOf(false) }
                Box {
                    FilledTonalButton(onClick = { open = true }) { Text(if (folder.isBlank()) "All files" else folder) }
                    DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                        (folders.ifEmpty { listOf(GalleryCatalog.KEY_ALL_FILES) }).forEach { name ->
                            DropdownMenuItem(text = { Text(name.ifBlank { "(no folder)" }) }, onClick = {
                                folder = name
                                open = false
                            })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                val shown = if (folder == GalleryCatalog.KEY_ALL_FILES || folder.isBlank()) videos else videos
                LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), contentPadding = PaddingValues(4.dp)) {
                    items(shown, key = { it.id.toString() + (it.path ?: "") }) { video ->
                        Card(onClick = { menuFor = video }, modifier = Modifier.padding(6.dp)) {
                            Column(Modifier.padding(12.dp)) {
                                Text(File(video.path ?: "").name, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleSmall)
                                Text("${video.duration} ms", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
    menuFor?.let { video ->
        VideoActionsDialog(video, onDismiss = { menuFor = null })
    }
}

@Composable
private fun VideoActionsDialog(video: LocalVideo, onDismiss: () -> Unit) {
    val context = LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Video") },
        text = { Text(video.path ?: "") },
        confirmButton = {
            Row {
                IconButton(onClick = { watchVideo(context, video); onDismiss() }) { Icon(Icons.Default.PlayArrow, contentDescription = "Watch") }
                IconButton(onClick = { shareVideo(context, video); onDismiss() }) { Icon(Icons.Default.Share, contentDescription = "Share") }
                IconButton(onClick = { openFolder(context, video); onDismiss() }) { Icon(Icons.Default.FolderOpen, contentDescription = "Folder") }
            }
        },
        dismissButton = { FilledTonalButton(onClick = onDismiss) { Text("Close") } }
    )
}

private fun watchVideo(context: Context, video: LocalVideo) {
    val path = video.path ?: return
    val file = File(path)
    val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
    val intent = Intent(context, PlayerActivity::class.java).setDataAndType(uri, "video/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}

private fun shareVideo(context: Context, video: LocalVideo) {
    val path = video.path ?: return
    val file = File(path)
    val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
    val send = Intent(Intent.ACTION_SEND).setType("video/*").putExtra(Intent.EXTRA_STREAM, uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(send, "Share video"))
}

private fun openFolder(context: Context, video: LocalVideo) {
    val path = video.path ?: return
    val folder = File(path).parentFile ?: return
    val intent = Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(folder), "resource/folder")
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, folder.absolutePath, Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun EmptyBlock(message: String, action: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        Button(onClick = action) { Text("Continue") }
    }
}

private fun videoPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}

private fun hasVideoPermission(context: Context): Boolean {
    return videoPermissions().all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val version = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) {
        "?"
    }
    Scaffold(topBar = { TopAppBar(title = { Text("About") }) }) { padding ->
        Column(Modifier.padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(context.getString(R.string.app_name), style = MaterialTheme.typography.headlineMedium)
            Text("Compose edition · $version")
            Text("Downloads social video links, keeps a local gallery, plays files, and watches the clipboard. Same extractors as the original app.")
            Button(onClick = { openUrl(context, context.getString(R.string.url_privacy_policy)) }) { Text("Privacy policy") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onOpenTools: () -> Unit, onOpenMime: () -> Unit) {
    val context = LocalContext.current
    var clipOn by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text("More") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Clipboard monitor", modifier = Modifier.weight(1f))
                Switch(checked = clipOn, onCheckedChange = {
                    clipOn = it
                    if (it) ClipboardMonitorService.start(context) else ClipboardMonitorService.stop(context)
                })
            }
            Button(onClick = onOpenTools, modifier = Modifier.fillMaxWidth()) { Text("Tools") }
            Button(onClick = onOpenMime, modifier = Modifier.fillMaxWidth()) { Text("Mime handlers") }
            Button(onClick = { openAppSettings(context) }, modifier = Modifier.fillMaxWidth()) { Text("App settings") }
            Button(onClick = { shareThisApp(context) }, modifier = Modifier.fillMaxWidth()) { Text("Share this app") }
            Button(onClick = { rateApp(context) }, modifier = Modifier.fillMaxWidth()) { Text("Rate app") }
            Button(onClick = { openUrl(context, context.getString(R.string.url_privacy_policy)) }, modifier = Modifier.fillMaxWidth()) { Text("Privacy policy") }
            Button(onClick = {
                val pkg = context.getString(R.string.p_more_app_02)
                launchPackage(context, pkg)
            }, modifier = Modifier.fillMaxWidth()) { Text("More apps") }
            Button(onClick = {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                    .putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                context.startActivity(Intent.createChooser(intent, "Feedback"))
            }, modifier = Modifier.fillMaxWidth()) { Text("Feedback") }
            Text("Downloads go to " + Config.videoFolder(context).absolutePath, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("file_tools", Context.MODE_PRIVATE) }
    var number by remember { mutableStateOf(prefs.getInt("number", 0).toString()) }
    var selected by remember { mutableStateOf(prefs.getInt("intent", 0)) }
    val labels = listOf("YouTube", "Instagram", "OK.ru", "TikTok", "Likee")
    Scaffold(topBar = {
        TopAppBar(title = { Text("Tools") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })
    }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Number input used by the original file tools.", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(value = number, onValueChange = { number = it.filter { ch -> ch.isDigit() } }, label = { Text("Number") })
            labels.forEachIndexed { index, label ->
                FilledTonalButton(onClick = { selected = index }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (selected == index) "● $label" else label)
                }
            }
            Button(onClick = {
                val n = number.toIntOrNull() ?: 0
                prefs.edit().putInt("number", n).putInt("intent", selected).apply()
                Toast.makeText(context, "Saved $n / ${labels[selected]}", Toast.LENGTH_SHORT).show()
            }) { Text("Save") }
            Button(onClick = { ApkInstallHelper.download(context, "https://f-droid.org/F-Droid.apk") }) { Text("Download F-Droid APK") }
            Text("Folder: " + prefs.getString("key_folder", Config.videoFolder(context).absolutePath), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MimeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val types = listOf("video/mp4", "video/*", "audio/*", "image/*", "*/*", "application/vnd.android.package-archive")
    var selected by remember { mutableStateOf(types.first()) }
    val handlers = remember(selected) { queryHandlers(context, selected) }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Mime handlers") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        })
    }) { padding ->
        Column(Modifier.padding(padding).padding(12.dp)) {
            Text("Activities that can open $selected", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.padding(vertical = 8.dp)) {
                types.forEach { type ->
                    AssistChip(onClick = { selected = type }, label = { Text(type) }, modifier = Modifier.padding(end = 6.dp))
                }
            }
            if (handlers.isEmpty()) {
                Text("No handlers found for this type.")
            } else {
                LazyColumn {
                    items(handlers) { label ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text(label, Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun queryHandlers(context: Context, mime: String): List<String> {
    val intent = Intent(Intent.ACTION_VIEW).setType(mime)
    return context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        .map { info ->
            val pkg = info.activityInfo?.packageName ?: info.serviceInfo?.packageName ?: "?"
            val name = info.activityInfo?.name ?: info.serviceInfo?.name ?: "?"
            "$pkg\n$name"
        }
}

object ApkInstallHelper {
    fun download(context: Context, url: String) {
        com.walhalla.ttvloader.receiver.DownloadFile.newInstance().makeLoad77(context, url, "F-Droid", ".apk")
    }
}

private fun openUrl(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
    context.startActivity(intent)
}

private fun shareThisApp(context: Context) {
    val text = "https://play.google.com/store/apps/details?id=" + context.packageName
    val send = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text)
    context.startActivity(Intent.createChooser(send, "Share"))
}

private fun rateApp(context: Context) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.packageName)))
    } catch (e: Exception) {
        openUrl(context, "https://play.google.com/store/apps/details?id=" + context.packageName)
    }
}
