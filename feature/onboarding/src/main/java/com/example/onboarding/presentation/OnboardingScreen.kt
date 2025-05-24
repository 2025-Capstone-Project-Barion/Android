// features/onboarding/src/main/java/com/example/onboarding/presentation/OnboardingScreen.kt

package com.example.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.onboarding.R
import com.example.ui.components.buttons.BarrionFullButton
import com.example.ui.components.buttons.BarrionNavigationButtons
import com.example.ui.components.buttons.BarrionPrimaryButton
import com.example.ui.theme.barrionColors
import kotlinx.coroutines.launch

/**
 * 온보딩 페이지 데이터 클래스
 * 각 온보딩 페이지의 제목, 설명, 이미지 리소스 ID를 포함합니다.
 */
data class OnboardingPage(
    val title: String,                // 페이지 제목
    val description: String,          // 페이지 설명
    val imageResId: Int               // 이미지 리소스 ID
)

/**
 * 온보딩 화면 컴포저블
 * 4개의 온보딩 페이지로 구성되며, 수평 페이저를 통해 사용자가 페이지를 넘길 수 있습니다.
 * 마지막 페이지에 도달하면 시작하기 버튼이 활성화됩니다.
 *
 * @param onNavigateToLogin 시작하기 버튼 클릭 시 호출될 콜백 함수 (로그인 화면으로 이동)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit  // 파라미터명 변경: onNavigateToHome → onNavigateToLogin
) {
    // 온보딩 페이지 정의 - 4개의 화면으로 구성
    val pages = listOf(
        OnboardingPage(
            title = "매출 관리",
            description = "오늘 우리 가게는 얼마나 벌었을까요?",
            imageResId = R.drawable.onboarding_sale   // 매출 관리 이미지
        ),
        OnboardingPage(
            title = "메뉴 관리",
            description = "메뉴 관리, 이제 터치 몇 번으로 끝",
            imageResId = R.drawable.onboarding_menu   // 메뉴 관리 이미지
        ),
        OnboardingPage(
            title = "주문 관리",
            description = "주문 현황을 한눈에 확인하세요.",
            imageResId = R.drawable.onboarding_order  // 주문 관리 이미지
        ),
        OnboardingPage(
            title = "직원 관리",
            description = "직원 정보, 쉽고 빠르게 관리해요.",
            imageResId = R.drawable.onboarding_staff  // 직원 관리 이미지
        )
    )

    // 페이저 상태 관리
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    // 현재 마지막 페이지인지 확인 (시작하기 버튼 활성화 여부에 사용)
    val isLastPage = pagerState.currentPage == pages.size - 1

    // 전체 화면 컨테이너
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 수평 페이저 - 온보딩 페이지들을 수평으로 스와이프하여 볼 수 있음
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                // 현재 페이지 인덱스에 해당하는 온보딩 페이지 컨텐츠를 표시
                OnboardingPageContent(pages[page])
            }

            // 하단 여백 추가
            Spacer(modifier = Modifier.height(16.dp))

            // 페이지 인디케이터 - 현재 몇 번째 페이지인지 표시하는 도트
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // 페이지 수만큼 도트 생성
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                // 현재 페이지는 파란색, 나머지는 회색으로 표시
                                if (isSelected) MaterialTheme.barrionColors.primaryBlue
                                else MaterialTheme.barrionColors.grayLight
                            )
                    )
                }
            }

            // 시작하기 버튼 - 마지막 페이지에서만 활성화됨
            BarrionPrimaryButton(
                text = "시작하기",
                onClick = {
                    if (isLastPage) {
                        // 마지막 페이지면 로그인 화면으로 이동
                        onNavigateToLogin()  // 함수명 변경: onNavigateToHome() → onNavigateToLogin()
                    } else {
                        // 아니면 다음 페이지로 이동
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp),
                enabled = isLastPage  // 마지막 페이지에서만 활성화
            )

            // 하단 추가 여백
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * 각 온보딩 페이지의 콘텐츠를 표시하는 컴포저블
 *
 * @param page 표시할 온보딩 페이지 데이터
 */
@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start  // 왼쪽 정렬
    ) {
        // 상단 여백 - 제목과 설명을 아래로 내려 배치
        Spacer(modifier = Modifier.height(36.dp))

        // 페이지 제목
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 페이지 설명
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.barrionColors.grayMediumDark,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 이미지 카드 - 해당 기능을 시각적으로, 보여주는 이미지
        if (page.imageResId != 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),  // 이미지 높이 설정
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)  // 그림자 효과
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = page.imageResId),
                        contentDescription = page.title,
                        contentScale = ContentScale.Crop,  // 이미지가 컨테이너를 꽉 채우도록 설정
                        alignment = Alignment.TopCenter,   // 이미지 상단이 보이도록 정렬
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else {
            // 이미지가 없는 경우 대체 UI 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.barrionColors.grayVeryLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${page.title} 이미지",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.barrionColors.grayMedium
                )
            }
        }
    }
}