package com.measify.kappmaker.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

@Composable
fun AnimatedHorizontalPager(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { pageIndex ->
        val pageOffset =
            ((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction).absoluteValue

        Box(
            Modifier
                .graphicsLayer {
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    val scale = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            pageContent(pageIndex)
        }
    }

}