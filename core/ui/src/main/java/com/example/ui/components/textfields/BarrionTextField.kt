// core/ui/src/main/java/com/example/ui/components/textfields/BarrionTextField.kt
package com.example.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ui.theme.barrionColors

/**
 * 텍스트 필드 상태를 정의하는 Enum
 */
enum class BarrionTextFieldState {
    NORMAL,     // 기본 상태 (회색 테두리)
    FOCUSED,    // 포커스된 상태 (파란색 테두리)
    ERROR,      // 오류 상태 (빨간색 테두리)
    DISABLED    // 비활성화 상태 (회색 배경)
}

/**
 * 커스텀 텍스트 필드 컴포넌트
 *
 * @param value 현재 입력된 텍스트 값
 * @param onValueChange 텍스트 값 변경 시 호출되는 콜백
 * @param modifier Modifier
 * @param title 텍스트 필드 위에 표시되는 제목 (null일 경우 표시하지 않음)
 * @param placeholder 입력 전 표시되는 플레이스홀더 텍스트
 * @param fieldState 텍스트 필드의 현재 상태
 * @param enabled 텍스트 필드 활성화 여부
 * @param readOnly 읽기 전용 모드 여부
 * @param keyboardOptions 키보드 옵션
 * @param keyboardActions 키보드 액션
 * @param singleLine 한 줄 입력 모드 여부
 * @param maxLines 최대 줄 수
 * @param visualTransformation 입력 값의 시각적 변환 (비밀번호 마스킹 등)
 */
@Composable
fun BarrionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String = "",
    fieldState: BarrionTextFieldState = BarrionTextFieldState.NORMAL,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val interactionSource = remember { MutableInteractionSource() }

    // 상태에 따른 색상 및 스타일 설정
    val borderColor = when (fieldState) {
        BarrionTextFieldState.FOCUSED -> MaterialTheme.barrionColors.primaryBlue
        BarrionTextFieldState.ERROR -> MaterialTheme.barrionColors.error
        else -> MaterialTheme.barrionColors.grayMediumLight
    }

    val backgroundColor = when (fieldState) {
        BarrionTextFieldState.DISABLED -> MaterialTheme.barrionColors.grayVeryLight
        else -> Color.White
    }

    val textColor = when {
        !enabled -> MaterialTheme.barrionColors.grayMedium
        fieldState == BarrionTextFieldState.ERROR -> MaterialTheme.barrionColors.error
        else -> MaterialTheme.barrionColors.grayBlack
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // 제목이 있는 경우 표시
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.barrionColors.grayDark,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // 텍스트 필드
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(backgroundColor),
            enabled = enabled && !readOnly,
            readOnly = readOnly,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.barrionColors.primaryBlue),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // 플레이스홀더 텍스트 (입력값이 비어있을 때만 표시)
                    if (value.isEmpty() && !readOnly) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.barrionColors.grayMedium
                        )
                    }

                    // 실제 입력 필드
                    innerTextField()
                }
            }
        )
    }
}

/**
 * 유효성 검사 기능이 포함된 텍스트 필드
 *
 * @param value 현재 입력된 텍스트 값
 * @param onValueChange 텍스트 값 변경 시 호출되는 콜백
 * @param validator 입력 값의 유효성을 검사하는 함수, true를 반환하면 유효한 값
 * @param title 텍스트 필드 위에 표시되는 제목
 * @param placeholder 입력 전 표시되는 플레이스홀더 텍스트
 * @param errorMessage 유효성 검사 실패 시 표시되는 오류 메시지
 * @param modifier Modifier
 * @param enabled 텍스트 필드 활성화 여부
 * @param keyboardOptions 키보드 옵션
 * @param keyboardActions 키보드 액션
 * @param singleLine 한 줄 입력 모드 여부
 * @param visualTransformation 입력 값의 시각적 변환 (비밀번호 마스킹 등)
 */
@Composable
fun BarrionValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    validator: (String) -> Boolean,
    title: String,
    placeholder: String,
    errorMessage: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val isValid = if (value.isEmpty()) true else validator(value)
    val fieldState = when {
        value.isEmpty() -> BarrionTextFieldState.NORMAL
        isValid -> BarrionTextFieldState.FOCUSED
        else -> BarrionTextFieldState.ERROR
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BarrionTextField(
            value = value,
            onValueChange = onValueChange,
            title = title,
            placeholder = placeholder,
            fieldState = fieldState,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            visualTransformation = visualTransformation
        )

        // 유효성 검사 실패 시 오류 메시지 표시
        if (fieldState == BarrionTextFieldState.ERROR) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.barrionColors.error,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

/**
 * 읽기 전용 텍스트 필드 (수정 불가능한 정보 표시용)
 *
 * @param value 표시할 텍스트 값
 * @param title 텍스트 필드 위에 표시되는 제목
 * @param modifier Modifier
 */
@Composable
fun BarrionReadOnlyTextField(
    value: String,
    title: String,
    modifier: Modifier = Modifier
) {
    BarrionTextField(
        value = value,
        onValueChange = { },  // 읽기 전용이므로 변경 불가
        title = title,
        fieldState = BarrionTextFieldState.DISABLED,
        readOnly = true,
        modifier = modifier
    )
}