// feature/auth/screen/LoginScreen.kt (업데이트된 버전)
package com.example.auth.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.buttons.BarrionPrimaryButton
import com.example.ui.theme.barrionColors
import kotlinx.coroutines.delay

/**
 * 로그인 화면 - 코드별 분기 처리
 */
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,        // 기능 시연용 (바로 홈)
    onNavigateToSetup: () -> Unit,       // 최초 사용자 Setup 플로우
    modifier: Modifier = Modifier
) {
    var code by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(1f))

        // 제목 텍스트
        Text(
            text = "제공받은 코드를 입력해주세요 !",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.barrionColors.grayBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 설명 텍스트
        Text(
            text = "코드를 받지 못하셨으면\nBarion에 문의해주시기 바랍니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.barrionColors.grayMedium,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 코드 입력 박스들
        MostReliableCodeBoxes(
            code = code,
            onCodeChange = { newCode ->
                code = newCode.take(4).filter { it.isDigit() }
                isError = false // 새 입력 시 에러 해제
            },
            isError = isError,
            onBoxClick = {
                // 에러 상태일 때 박스 클릭하면 코드 초기화
                if (isError) {
                    code = ""
                    isError = false
                }
            }
        )

        // 에러 메시지
        if (isError) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Code Error",
                color = MaterialTheme.barrionColors.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 로그인 버튼 - 코드별 분기 처리
        BarrionPrimaryButton(
            text = "로그인",
            onClick = {
                when (code) {
                    "1234" -> {
                        // 첫 번째 시연: 최초 사용자 Setup 플로우
                        onNavigateToSetup()
                    }
                    "9999" -> {
                        // 두 번째 시연: 기존 사용자 바로 홈
                        onNavigateToHome()
                    }
                    else -> {
                        // 잘못된 코드
                        isError = true
                    }
                }
            },
            enabled = code.length == 4,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * 가장 확실한 방법 - 항상 키보드가 나오는 버전
 */
@Composable
fun MostReliableCodeBoxes(
    code: String,
    onCodeChange: (String) -> Unit,
    isError: Boolean = false,
    onBoxClick: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    var requestFocus by remember { mutableStateOf(true) }

    // 포커스 강제 요청
    LaunchedEffect(requestFocus) {
        if (requestFocus) {
            delay(100)
            focusRequester.requestFocus()
            requestFocus = false
        }
    }

    Box {
        // 투명한 TextField - 항상 활성화
        OutlinedTextField(
            value = code,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() }.take(4)
                onCodeChange(filtered)
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .alpha(0.01f), // 거의 투명
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true
        )

        // 박스들을 위에 오버레이
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.clickable {
                    onBoxClick()
                    requestFocus = true // 다시 포커스 요청
                }
            ) {
                repeat(4) { index ->
                    val isActive = code.length == index
                    val hasValue = index < code.length

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .border(
                                width = if (isActive || hasValue || isError) 2.dp else 1.dp,
                                color = when {
                                    isError -> MaterialTheme.barrionColors.error
                                    hasValue -> MaterialTheme.barrionColors.primaryBlue
                                    isActive -> MaterialTheme.barrionColors.primaryBlue
                                    else -> MaterialTheme.barrionColors.grayMediumLight
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = code.getOrNull(index)?.toString() ?: "",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isError) {
                                MaterialTheme.barrionColors.error
                            } else {
                                MaterialTheme.barrionColors.grayBlack
                            }
                        )
                    }
                }
            }
        }
    }
}