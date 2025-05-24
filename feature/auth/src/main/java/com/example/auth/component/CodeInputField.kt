// feature/auth/component/CodeInputField.kt (최종 버전)
package com.example.auth.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.barrionColors
import kotlinx.coroutines.delay

/**
 * 4자리 코드 입력을 위한 커스텀 컴포넌트 (원래 디자인)
 */
@Composable
fun CodeInputField(
    code: String,
    onCodeChange: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 4자리 완성 시 키보드 숨김
    LaunchedEffect(code) {
        if (code.length >= 4) {
            try {
                keyboardController?.hide()
            } catch (e: Exception) {
                // 키보드 숨김 실패 시 무시
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 숨겨진 텍스트 필드 (실제 입력을 받는 부분)
        BasicTextField(
            value = code,
            onValueChange = { newCode ->
                try {
                    // 안전한 필터링
                    val filteredCode = newCode.filter { it.isDigit() }.take(4)
                    onCodeChange(filteredCode)
                } catch (e: Exception) {
                    // 입력 처리 실패 시 무시
                }
            },
            modifier = Modifier
                .size(0.dp) // 완전히 숨김
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            cursorBrush = SolidColor(Color.Transparent),
            textStyle = TextStyle(color = Color.Transparent),
            singleLine = true
        )

        // 코드 입력 박스들 (4개)
        Row(
            modifier = Modifier.clickable {
                try {
                    focusRequester.requestFocus()
                } catch (e: Exception) {
                    // 포커스 요청 실패 시 무시
                }
            },
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(4) { index ->
                CodeDigitBox(
                    digit = code.getOrNull(index)?.toString() ?: "",
                    isActive = code.length == index,
                    isError = isError,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }

    // 안전한 자동 포커스
    LaunchedEffect(Unit) {
        try {
            delay(300) // 충분한 지연
            focusRequester.requestFocus()
        } catch (e: Exception) {
            // 포커스 요청 실패 시 무시
        }
    }
}

/**
 * 개별 코드 입력 박스 (원래 디자인)
 */
@Composable
private fun CodeDigitBox(
    digit: String,
    isActive: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isError -> MaterialTheme.barrionColors.error
        isActive -> MaterialTheme.barrionColors.primaryBlue
        digit.isNotEmpty() -> MaterialTheme.barrionColors.primaryBlue
        else -> MaterialTheme.barrionColors.grayMediumLight
    }

    val borderWidth = if (isActive || digit.isNotEmpty()) 2.dp else 1.dp

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ),
            color = if (isError) {
                MaterialTheme.barrionColors.error
            } else {
                MaterialTheme.barrionColors.grayBlack
            }
        )
    }
}