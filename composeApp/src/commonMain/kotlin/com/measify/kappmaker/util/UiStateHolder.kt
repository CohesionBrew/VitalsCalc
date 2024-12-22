package com.measify.kappmaker.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

interface UiStateHolder : ScreenModel

val UiStateHolder.uiStateHolderScope: CoroutineScope get() = screenModelScope


@Composable
inline fun <reified T : ScreenModel> Screen.uiStateHolder(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    val currentParameters by rememberUpdatedState(parameters)
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberScreenModel(tag = tag) {
        scope.get(qualifier) {
            currentParameters?.invoke() ?: emptyParametersHolder()
        }
    }
}

@Composable
inline fun <reified T : ScreenModel> Navigator.navigatorUiStateHolder(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    val currentParameters by rememberUpdatedState(parameters)
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberNavigatorScreenModel(tag = tag) {
        scope.get(qualifier) {
            currentParameters?.invoke() ?: emptyParametersHolder()
        }
    }
}

