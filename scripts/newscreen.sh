#!/bin/bash

# Usage: ./newscreen.sh [-s ScreenSuffix] [-u UiStateSuffix] [-v UiStateHolderSuffix] ScreenName

# Default values
PACKAGE_NAME="com.measify.kappbuilder"
SCREENS_PACKAGE_NAME="${PACKAGE_NAME}.presentation.screens"
UISTATE_SUFFIX="UiState"
UIEVENT_SUFFIX="UiEvent"
VIEWMODEL_SUFFIX="UiStateHolder"
SCREEN_SUFFIX="Screen"


# Parse options
while getopts "u:s:v:" opt; do
  case $opt in
    u) UISTATE_SUFFIX=$OPTARG ;;  # UiState suffix
    s) SCREEN_SUFFIX=$OPTARG ;;   # Screen suffix
    v) VIEWMODEL_SUFFIX=$OPTARG ;;   # UiStateHolder suffix
    \?) echo "Invalid option -$OPTARG" >&2; exit 1 ;;
  esac
done

# Shift positional parameters to handle remaining arguments
shift $((OPTIND - 1))

# Get the screen name, and add screen suffix e
SCREEN_BASE_PREFIX=${1}
SCREEN_CLASS_NAME="${SCREEN_BASE_PREFIX}${SCREEN_SUFFIX}"
UISTATE_CLASS_NAME="${SCREEN_BASE_PREFIX}${UISTATE_SUFFIX}"
UIEVENT_CLASS_NAME="${SCREEN_BASE_PREFIX}${UIEVENT_SUFFIX}"
UISTATEHOLDER_CLASS_NAME="${SCREEN_BASE_PREFIX}${VIEWMODEL_SUFFIX}"
SCREEN_ROUTE_CLASS_NAME="${SCREEN_BASE_PREFIX}${SCREEN_SUFFIX}Route"



# Convert the screen name to lowercase
LOWER_SCREEN_NAME=$(echo "$SCREEN_BASE_PREFIX" | tr '[:upper:]' '[:lower:]')

# Define the path to the screen folder
SCREEN_DIR="composeApp/src/commonMain/kotlin/com/measify/kappbuilder/presentation/screens/$LOWER_SCREEN_NAME"

# Create the folder structure if it doesn't exist
mkdir -p $SCREEN_DIR

# Generate the UiState.kt file content
cat <<EOL > "$SCREEN_DIR/${UISTATE_CLASS_NAME}.kt"
package $SCREENS_PACKAGE_NAME.$LOWER_SCREEN_NAME

class ${UISTATE_CLASS_NAME}()

sealed class ${UIEVENT_CLASS_NAME} {
    data object OnClick : ${UIEVENT_CLASS_NAME}()
}

EOL


# Generate the UiStateHolder.kt (a.k.a ViewModel) file content
cat <<EOL > "$SCREEN_DIR/${UISTATEHOLDER_CLASS_NAME}.kt"
package $SCREENS_PACKAGE_NAME.$LOWER_SCREEN_NAME

import $PACKAGE_NAME.util.UiStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ${UISTATEHOLDER_CLASS_NAME}() : UiStateHolder {
    private val _uiState = MutableStateFlow(${UISTATE_CLASS_NAME}())
    val uiState: StateFlow<${UISTATE_CLASS_NAME}> = _uiState.asStateFlow()

}

EOL


# Generate the Screen.kt file content
cat <<EOL > "$SCREEN_DIR/${SCREEN_CLASS_NAME}.kt"
package $SCREENS_PACKAGE_NAME.$LOWER_SCREEN_NAME

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ${SCREEN_CLASS_NAME}(
    modifier: Modifier = Modifier,
    uiStateHolder: ${UISTATEHOLDER_CLASS_NAME},
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

    ${SCREEN_CLASS_NAME}(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = {}
    )
}

@Composable
fun ${SCREEN_CLASS_NAME}(
    modifier: Modifier = Modifier,
    uiState: ${UISTATE_CLASS_NAME},
    onUiEvent: (${UIEVENT_CLASS_NAME}) -> Unit
) {

    Box(modifier = modifier){

    }

}

EOL

# Generate the ScreenRoute.kt file content
cat <<EOL > "$SCREEN_DIR/${SCREEN_ROUTE_CLASS_NAME}.kt"
package $SCREENS_PACKAGE_NAME.$LOWER_SCREEN_NAME

import androidx.compose.runtime.Composable
import ${PACKAGE_NAME}.util.ScreenRoute
import ${PACKAGE_NAME}.util.uiStateHolder

class ${SCREEN_ROUTE_CLASS_NAME} : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<${UISTATEHOLDER_CLASS_NAME}>()
        ${SCREEN_CLASS_NAME}(uiStateHolder = uiStateHolder)
    }

}


EOL



echo "Screen files for ${SCREEN_CLASS_NAME} have been created in $SCREEN_DIR"
