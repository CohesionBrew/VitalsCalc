package com.measify.kappmaker.presentation.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.measify.kappmaker.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = AppTheme.colors.outline
) {
    HorizontalDivider(
        color = color,
        thickness = thickness,
        modifier = modifier
    )
}

@Preview
@Composable
fun DividerPreview() {
    PreviewHelper {
        Divider()
    }
}