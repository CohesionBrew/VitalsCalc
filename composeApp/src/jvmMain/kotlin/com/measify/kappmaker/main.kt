package com.measify.kappmaker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.measify.kappmaker.root.App
import com.measify.kappmaker.root.AppInitializer

fun main() = application {
    AppInitializer.initialize {}
    Window(
        onCloseRequest = ::exitApplication,
        title = "KAppMakerAllModules",
    ) {
        App()
    }
}