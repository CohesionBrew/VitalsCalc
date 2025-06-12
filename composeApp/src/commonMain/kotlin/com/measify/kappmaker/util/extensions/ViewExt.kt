package com.measify.kappmaker.util.extensions

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.util.fastRoundToInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// This is to force layout to go beyond the borders of its parent
fun Modifier.fillWidthOfParent(totalParentHorizontalPaddingInPx: Float) =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth + totalParentHorizontalPaddingInPx.fastRoundToInt()
            )
        )
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }

/**
 * val uiMessageChannel = Channel<UiMessage>()
 * val uiMessageFlow = uiMessageChannel.receiveAsFlow()
 */
@Composable
fun <T> ObserveFlowAsEvent(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current
    val updatedOnEvent by rememberUpdatedState(onEvent)
    LaunchedEffect(flow, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(updatedOnEvent)
            }
        }
    }
}

@Composable
fun isKeyboardOpen(): Boolean {
    val bottomInset = WindowInsets.ime.getBottom(LocalDensity.current)
    return rememberUpdatedState(bottomInset > 300).value
}