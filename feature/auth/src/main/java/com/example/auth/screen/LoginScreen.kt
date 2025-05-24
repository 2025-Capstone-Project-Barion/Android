// feature/auth/screen/LoginScreen.kt (완전한 최종 버전)
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
 * 로그인 화면 (완전한 최종 버전)
 */
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
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
        ImprovedCodeBoxes(
            code = code,
            onCodeChange = { newCode ->
                code = newCode.take(4).filter { it.isDigit() }
                isError = false
            },
            isError = isError,
            onBoxClick = {
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

        // 로그인 버튼
        BarrionPrimaryButton(
            text = "로그인",
            onClick = {
                if (code == "1234") {
                    onNavigateToHome()
                } else {
                    isError = true
                }
            },
            enabled = code.length == 4,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * 개선된 4자리 코드 입력 박스들
 */
@Composable
fun ImprovedCodeBoxes(
    code: String,
    onCodeChange: (String) -> Unit,
    isError: Boolean = false,
    onBoxClick: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 컴포넌트가 처음 로드될 때 포커스 요청
    LaunchedEffect(Unit) {
        delay(100) // 약간의 지연 후 포커스
        focusRequester.requestFocus()
    }

    // 에러 해제될 때도 포커스 다시 요청
    LaunchedEffect(isError) {
        if (!isError && code.isEmpty()) {
            delay(50)
            focusRequester.requestFocus()
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 숨겨진 TextField - 키보드 입력을 받기 위함
        BasicTextField(
            value = code,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() }.take(4)
                onCodeChange(filtered)
            },
            modifier = Modifier
                .size(1.dp) // 최소 크기로 설정
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(color = Color.Transparent), // 텍스트도 투명
            cursorBrush = SolidColor(Color.Transparent), // 커서도 투명
            singleLine = true
        )

        // 4개 박스 표시
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.clickable {
                onBoxClick()
                // 박스 클릭 시 강제로 키보드 올리기
                focusRequester.requestFocus()
                keyboardController?.show()
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

// 가장 확실한 방법 - 항상 키보드가 나오는 버전
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