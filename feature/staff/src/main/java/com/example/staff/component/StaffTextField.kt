// :feature:staff/src/main/java/com/example/staff/component/StaffTextField.kt
package com.example.staff.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.ui.components.textfields.BarrionTextField
import com.example.ui.components.textfields.BarrionTextFieldState

/**
 * 직원 폼용 텍스트 필드
 * Core UI의 BarrionTextField를 활용한 Staff 전용 컴포넌트
 */
@Composable
fun StaffTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    // 에러 상태에 따른 필드 상태 결정
    val fieldState = when {
        isError -> BarrionTextFieldState.ERROR
        value.isNotEmpty() -> BarrionTextFieldState.FOCUSED
        !enabled -> BarrionTextFieldState.DISABLED
        else -> BarrionTextFieldState.NORMAL
    }

    BarrionTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = label,
        placeholder = placeholder.ifEmpty { "${label}을(를) 입력하세요" },
        fieldState = fieldState,
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )

    // 에러 메시지 표시는 BarrionTextField 내부에서 처리되므로
    // 별도로 추가하지 않음 (필요시 BarrionValidatedTextField 사용)
}