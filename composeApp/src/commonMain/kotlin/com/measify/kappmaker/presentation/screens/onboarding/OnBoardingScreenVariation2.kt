package com.measify.kappmaker.presentation.screens.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_get_started
import com.measify.kappmaker.generated.resources.btn_next
import com.measify.kappmaker.generated.resources.btn_skip
import com.measify.kappmaker.presentation.components.AnimatedHorizontalPager
import com.measify.kappmaker.presentation.components.HorizontalPagerIndicator
import com.measify.kappmaker.presentation.components.HorizontalPagerIndicatorStyle
import com.measify.kappmaker.presentation.components.PrimaryButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue
import kotlin.math.min


@Composable
fun OnBoardingScreenVariation2(
    modifier: Modifier = Modifier,
    uiState: OnBoardingUiState,
    onUiEvent: (OnBoardingUiEvent) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { uiState.pages.size }
    )
    val pagerOffset = pagerState.currentPageOffsetFraction.absoluteValue

    Box(modifier = modifier.fillMaxSize()) {
        val statusBarHeight = with(LocalDensity.current) {
            WindowInsets.systemBars.getTop(this).toDp()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = -statusBarHeight)
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp)
        ) {

            val imageAnimationOffset = lerp(0.dp, 400.dp, pagerOffset)
            val imageAnimationAlpha = lerp(start = 0.05f, stop = 1f, fraction = 1f - pagerOffset)
            Image(
                painter = painterResource(uiState.pages[pagerState.currentPage].imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(imageAnimationAlpha)
                    .offset(y = imageAnimationOffset)
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val isLastPage = pagerState.currentPage == (pagerState.pageCount - 1)
            AnimatedHorizontalPager(
                pagerState = pagerState,
            ) { pageIndex ->
                val onBoardingScreenData = uiState.pages[pageIndex]
                OnBoardingPager(
                    item = onBoardingScreenData,
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            top = 32.dp,
                            start = 24.dp,
                            end = 24.dp
                        )
                )
            }
            HorizontalPagerIndicator(
                modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
                size = pagerState.pageCount,
                selectedIndex = pagerState.currentPage,
                style = HorizontalPagerIndicatorStyle.STYLE2,
                onClickIndicator = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = index,
                            animationSpec = tween()
                        )
                    }
                }

            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                modifier = Modifier.padding(top = 32.dp)
            )
            Box(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isLastPage.not(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SkipAndContinueButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClickContinue = {
                            coroutineScope.launch {
                                val nextPage = min(
                                    pagerState.currentPage + 1,
                                    uiState.pages.lastIndex
                                )
                                pagerState.animateScrollToPage(
                                    page = nextPage,
                                    animationSpec = tween()
                                )

                            }
                        },
                        onClickSkip = {
                            coroutineScope.launch { pagerState.animateScrollToPage(uiState.pages.lastIndex) }
                        }
                    )
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = isLastPage,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier,
            text = stringResource(item.description),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun SkipAndContinueButton(
    modifier: Modifier = Modifier,
    onClickContinue: () -> Unit,
    onClickSkip: () -> Unit
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        SkipButton(
            modifier = Modifier.weight(1f),
            onClick = { onClickSkip() }
        )

        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.btn_next),
            onClick = { onClickContinue() }
        )
    }

}

@Composable
private fun SkipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = { onClick() },
    ) {
        Text(
            text = stringResource(Res.string.btn_skip),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


