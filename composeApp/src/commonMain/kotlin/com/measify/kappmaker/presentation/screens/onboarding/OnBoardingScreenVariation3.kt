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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_skip
import com.measify.kappmaker.presentation.components.AnimatedHorizontalPager
import com.measify.kappmaker.presentation.components.CircleButtonWithSteps
import com.measify.kappmaker.presentation.components.HorizontalPagerIndicator
import com.measify.kappmaker.presentation.components.HorizontalPagerIndicatorStyle
import com.measify.kappmaker.presentation.theme.SystemAppearance
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun OnBoardingScreenVariation3(
    modifier: Modifier = Modifier,
    uiState: OnBoardingUiState,
    onUiEvent: (OnBoardingUiEvent) -> Unit
) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
    }
    SystemAppearance(true)

    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.systemBars.getTop(this).toDp()
    }
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = -statusBarHeight)
                .background(MaterialTheme.colorScheme.primary)
                .height(50.dp)
        )

        Column(
            modifier = modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(scrollState)
                .padding(top = 20.dp, bottom = 30.dp),
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
                    .padding(horizontal = 20.dp)
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
                    .heightIn(min = 550.dp)
            ) { pageIndex ->
                val onBoardingScreenData = uiState.pages[pageIndex]
                OnBoardingPager(
                    item = onBoardingScreenData,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }



            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalPagerIndicator(
                    modifier = Modifier,
                    size = pagerState.pageCount,
                    selectedIndex = pagerState.currentPage,
                    style = HorizontalPagerIndicatorStyle.STYLE3,
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
                CircleButtonWithSteps(
                    size = 56.dp,
                    selectedStep = pagerState.currentPage,
                    nbSteps = uiState.pages.size,
                    buttonColor = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        if (isLastPage) {
                            onUiEvent(OnBoardingUiEvent.OnClickStart)
                        } else {
                            coroutineScope.launch {
                                val nextPage = kotlin.math.min(
                                    pagerState.currentPage + 1,
                                    uiState.pages.lastIndex
                                )
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        page = nextPage,
                                        animationSpec = tween()
                                    )
                                }
                            }
                        }
                    }
                )


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
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier
                .height(350.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(40.dp))
                .padding(horizontal = 8.dp)
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(24.dp)
        )


        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = stringResource(item.title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(item.description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

    }
}

@Composable
private fun SkipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = { onClick() }
    ) {
        Text(
            stringResource(Res.string.btn_skip),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

