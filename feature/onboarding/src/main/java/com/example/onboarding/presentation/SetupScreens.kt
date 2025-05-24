// features/onboarding/src/main/java/com/example/onboarding/presentation/SetupScreens.kt
package com.example.onboarding.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.buttons.BarrionNavigationButtons
import com.example.ui.theme.barrionColors

/**
 * Setup 프로그레스 바 컴포넌트
 */
@Composable
fun SetupProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = if (index < currentStep) {
                            MaterialTheme.barrionColors.primaryBlue
                        } else {
                            MaterialTheme.barrionColors.grayLight
                        },
                        shape = RoundedCornerShape(2.dp)
                    )
            )
            if (index < totalSteps - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

/**
 * 1단계: 상호명 입력 화면
 */
@Composable
fun SetupStoreInfoScreen(
    onNavigateNext: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var storeName by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 프로그레스 바
        SetupProgressBar(currentStep = 1, totalSteps = 3)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // 제목
            Text(
                text = "운영하시는 가게 이름이\n뭔가요 ?",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 36.sp
                ),
                color = MaterialTheme.barrionColors.grayBlack,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(64.dp))

            // 라벨
            Text(
                text = "상호 명",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.barrionColors.grayBlack,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 입력 필드
            OutlinedTextField(
                value = storeName,
                onValueChange = { storeName = it },
                placeholder = {
                    Text(
                        "상호명을 입력해주세요",
                        color = MaterialTheme.barrionColors.grayMedium,
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.barrionColors.primaryBlue,
                    unfocusedBorderColor = MaterialTheme.barrionColors.grayMediumLight,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // 네비게이션 버튼
            BarrionNavigationButtons(
                leftText = "이전",
                rightText = "다음",
                onLeftClick = onNavigateBack,
                onRightClick = {
                    if (storeName.isNotBlank()) {
                        onNavigateNext(storeName)
                    }
                },
                rightEnabled = storeName.isNotBlank()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 2단계: 업종 선택 화면
 */
@Composable
fun SetupBusinessTypeScreen(
    onNavigateNext: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf("") }

    val businessTypes = listOf(
        "일반 음식점" to "식사위주로 많이 팔아요 !",
        "휴게 음식점" to "주로 카페가 많아요 !",
        "주점업" to "술이 메인인 곳입니다 !"
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 프로그레스 바
        SetupProgressBar(currentStep = 2, totalSteps = 3)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // 제목
            Text(
                text = "어떤 업종으로 등록하셨나요?",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 36.sp
                ),
                color = MaterialTheme.barrionColors.grayBlack,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(64.dp))

            // 업종 선택 리스트 - 카드 스타일로 변경
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                businessTypes.forEach { (type, description) ->
                    val isSelected = selectedType == type

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable { selectedType = type },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.barrionColors.primaryBlue.copy(alpha = 0.1f)
                            } else {
                                MaterialTheme.barrionColors.white
                            }
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) {
                                MaterialTheme.barrionColors.primaryBlue
                            } else {
                                MaterialTheme.barrionColors.grayMediumLight
                            }
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = type,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.barrionColors.grayBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp
                                    ),
                                    color = MaterialTheme.barrionColors.grayMedium
                                )
                            }

                            if (isSelected) {
                                Text(
                                    text = "✓",
                                    color = MaterialTheme.barrionColors.primaryBlue,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 네비게이션 버튼
            BarrionNavigationButtons(
                leftText = "이전",
                rightText = "다음",
                onLeftClick = onNavigateBack,
                onRightClick = {
                    if (selectedType.isNotBlank()) {
                        onNavigateNext(selectedType)
                    }
                },
                rightEnabled = selectedType.isNotBlank()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 3단계: 키오스크 카테고리 설정 화면
 */
@Composable
fun SetupKioskCategoryScreen(
    onNavigateNext: (List<String>) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 카테고리 리스트 상태 관리 (최대 4개)
    var categories by remember { mutableStateOf(listOf("", "")) }
    val maxCategories = 4

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 프로그레스 바
        SetupProgressBar(currentStep = 3, totalSteps = 3)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // 제목
            Text(
                text = "키오스크에 등록할 카테고리\n를 정해주세요 !",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 36.sp
                ),
                color = MaterialTheme.barrionColors.grayBlack,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(64.dp))

            // 스크롤 가능한 영역
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // 스크롤 가능한 카테고리 리스트
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories.size) { index ->
                        OutlinedTextField(
                            value = categories[index],
                            onValueChange = { newValue ->
                                categories = categories.toMutableList().apply {
                                    this[index] = newValue
                                }
                            },
                            placeholder = {
                                Text(
                                    "카테고리 ${index + 1}",
                                    color = MaterialTheme.barrionColors.grayMedium,
                                    fontSize = 16.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.barrionColors.grayMediumLight,
                                unfocusedBorderColor = MaterialTheme.barrionColors.grayMediumLight,
                                focusedContainerColor = MaterialTheme.barrionColors.grayVeryLight,
                                unfocusedContainerColor = MaterialTheme.barrionColors.grayVeryLight
                            ),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 추가하기 버튼
                Button(
                    onClick = {
                        if (categories.size < maxCategories) {
                            categories = categories + ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = categories.size < maxCategories,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.barrionColors.grayMedium,
                        contentColor = MaterialTheme.barrionColors.white,
                        disabledContainerColor = MaterialTheme.barrionColors.grayLight,
                        disabledContentColor = MaterialTheme.barrionColors.white
                    )
                ) {
                    Text(
                        text = if (categories.size < maxCategories) {
                            "추가하기"
                        } else {
                            "최대 4개까지 가능합니다"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 네비게이션 버튼 - 하단 고정
            BarrionNavigationButtons(
                leftText = "이전",
                rightText = "다음",
                onLeftClick = onNavigateBack,
                onRightClick = {
                    val nonEmptyCategories = categories.filter { it.isNotBlank() }
                    onNavigateNext(nonEmptyCategories)
                },
                rightEnabled = categories.any { it.isNotBlank() }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}