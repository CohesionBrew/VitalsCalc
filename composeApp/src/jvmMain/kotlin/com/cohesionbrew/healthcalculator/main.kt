package com.cohesionbrew.healthcalculator

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cohesionbrew.healthcalculator.root.App
import com.cohesionbrew.healthcalculator.root.AppInitializer

fun main() = application {
    AppInitializer.initialize {}
    Window(
        onCloseRequest = ::exitApplication,
        title = "VitalsCalc",
    ) {
        App()
    }
}