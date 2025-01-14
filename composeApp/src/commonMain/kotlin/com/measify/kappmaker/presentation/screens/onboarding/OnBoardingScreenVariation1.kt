package com.measify.kappmaker.presentation.screens.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_get_started
import com.measify.kappmaker.generated.resources.btn_next
import com.measify.kappmaker.generated.resources.btn_skip
import com.measify.kappmaker.presentation.components.AnimatedHorizontalPager
import com.measify.kappmaker.presentation.components.HorizontalPagerIndicator
import com.measify.kappmaker.presentation.components.PrimaryButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun OnBoardingScreenVariation1(
    modifier: Modifier = Modifier,
    uiState: OnBoardingUiState,
    onUiEvent: (OnBoardingUiEvent) -> Unit
) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
    }

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { uiState.pages.size }
        )
        val isLastPage = pagerState.currentPage == (pagerState.pageCount - 1)
        Row(
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            androidx.compose.animation.AnimatedVisibility(
                visible = isLastPage.not(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SkipButton(
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(uiState.pages.lastIndex) }
                    })
            }

        }

        AnimatedHorizontalPager(
            pagerState = pagerState,
            modifier = Modifier
                .padding(top = 16.dp)
                .heightIn(min = 450.dp)
        ) { pageIndex ->
            val onBoardingScreenData = uiState.pages[pageIndex]
            OnBoardingPager(
                item = onBoardingScreenData,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        }
        HorizontalPagerIndicator(
            modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
            size = pagerState.pageCount,
            selectedIndex = pagerState.currentPage,
            style = com.measify.kappmaker.presentation.components.HorizontalPagerIndicatorStyle.STYLE1,
            onClickIndicator = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        page = index,
                        animationSpec = tween()
                    )
                }
            }

        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                if (isLastPage.not()) {
                    PrimaryButton(
                        text = stringResource(Res.string.btn_next),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            coroutineScope.launch {
                                val nextPage = kotlin.math.min(
                                    pagerState.currentPage + 1,
                                    uiState.pages.lastIndex
                                )
                                pagerState.animateScrollToPage(
                                    page = nextPage,
                                    animationSpec = tween()
                                )

                            }
                        })
                }

                if (isLastPage) {
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.btn_get_started),
                        onClick = { onUiEvent(OnBoardingUiEvent.OnClickStart) }
                    )
                }

            }

        }
    }

}

@Composable
private fun OnBoardingPager(
    modifier: Modifier = Modifier,
    item: OnBoardingScreenData,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {

        Image(
            painter = painterResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier.height(250.dp)
        )


        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(item.title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier,
            text = stringResource(item.description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

    }
}

@Composable
private fun SkipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Text(
            stringResource(Res.string.btn_skip),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

