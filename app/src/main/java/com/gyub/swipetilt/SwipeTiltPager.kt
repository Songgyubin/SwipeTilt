package com.gyub.swipetilt

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.gyub.swipetilt.MainActivity.Companion.sampleImageResourceIds
import kotlin.math.absoluteValue

/**
 * SwipeTiltHorizontalPager
 *
 * @author   Gyub
 * @created  2024/04/12
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeTiltHorizontalPager(
    modifier: Modifier = Modifier,
    images: List<Int>
) {
    val pagerState = rememberPagerState(
        initialPage = images.size / 2,
        pageCount = { images.size }
    )
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        pageSpacing = 33.dp,
        contentPadding = PaddingValues(horizontal = 50.dp)
    ) { page ->
        Card(
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(458.dp)
                .graphicsLayer {
                    // Calculate the absolute distance from the current page to this page
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue // 현재 페이지와 다른 페이지 간의 상대적 거리 계산

                    // Apply rotation effect based on page position relative to the current page
                    rotationZ = when {
                        page < pagerState.currentPage -> PREVIOUS_PAGE_ROTATION * pageOffset // 현재 페이지보다 이전 페이지면, 정해진 회전값(PREVIOUS_PAGE_ROTATION)을 왼쪽으로 적용
                        page > pagerState.currentPage -> NEXT_PAGE_ROTATION * pageOffset // 현재 페이지보다 다음 페이지면, 정해진 회전값(NEXT_PAGE_ROTATION)을 오른쪽으로 적용
                        else -> 0f // 현재 페이지일 때는 회전 없음
                    }

                    // Apply an alpha effect to fade pages based on their distance from the current page
                    alpha = lerp(
                        start = 0.5f, // 시작 투명도는 50%
                        stop = 1f,    // 끝 투명도는 100%
                        fraction = 1f - pageOffset.coerceIn(0f, 1f) // 페이지 거리에 따라 투명도 계산, 0에서 1 사이의 값을 강제적용
                    )
                }
        ) {
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Page Image $page",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SwipeTiltHorizontalPagerPreview() {
    SwipeTiltHorizontalPager(images = sampleImageResourceIds)
}

const val PREVIOUS_PAGE_ROTATION = -7f
const val NEXT_PAGE_ROTATION = 7f