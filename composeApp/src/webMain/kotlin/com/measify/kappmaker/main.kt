package com.measify.kappmaker

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.measify.kappmaker.root.App
import com.measify.kappmaker.root.AppInitializer

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    AppInitializer.initialize {}
    ComposeViewport {
        App()
    }
}