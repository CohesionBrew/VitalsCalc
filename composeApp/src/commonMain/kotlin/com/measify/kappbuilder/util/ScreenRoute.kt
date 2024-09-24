package com.measify.kappbuilder.util

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import cafe.adriel.voyager.core.screen.Screen

interface ScreenRoute : Screen, JavaSerializable {

    @get:Composable
    val title: String get() = ""
}