package com.walhalla.ttloader.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

object Routes {
    const val DOWNLOAD = "download"
    const val GALLERY = "gallery"
    const val ABOUT = "about"
    const val TOOLS = "tools"
    const val MIME = "mime"
    const val SETTINGS = "settings"
}

@Composable
fun TtNavHost(
    nav: NavHostController,
    startShare: SharedPayload,
    autoMode: Boolean = false
) {
    val tabs = listOf(
        Routes.DOWNLOAD to ("Download" to Icons.Default.Download),
        Routes.GALLERY to ("Gallery" to Icons.Default.PhotoLibrary),
        Routes.ABOUT to ("About" to Icons.Default.Info),
        Routes.SETTINGS to ("More" to Icons.Default.Settings)
    )
    val entry by nav.currentBackStackEntryAsState()
    val current = entry?.destination?.route ?: Routes.DOWNLOAD
    Scaffold(
        bottomBar = {
            if (current in tabs.map { it.first }) {
                NavigationBar {
                    tabs.forEach { (route, meta) ->
                        NavigationBarItem(
                            selected = current == route,
                            onClick = {
                                nav.navigate(route) {
                                    popUpTo(Routes.DOWNLOAD) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(meta.second, contentDescription = meta.first) },
                            label = { Text(meta.first) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Routes.DOWNLOAD,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.DOWNLOAD) {
                DownloadScreen(
                    shared = startShare,
                    autoStart = autoMode,
                    onOpenTools = { nav.navigate(Routes.TOOLS) },
                    onOpenMime = { nav.navigate(Routes.MIME) }
                )
            }
            composable(Routes.GALLERY) { GalleryScreen() }
            composable(Routes.ABOUT) { AboutScreen() }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onOpenTools = { nav.navigate(Routes.TOOLS) },
                    onOpenMime = { nav.navigate(Routes.MIME) }
                )
            }
            composable(Routes.TOOLS) { ToolsScreen(onBack = { nav.popBackStack() }) }
            composable(Routes.MIME) { MimeScreen(onBack = { nav.popBackStack() }) }
        }
    }
}
