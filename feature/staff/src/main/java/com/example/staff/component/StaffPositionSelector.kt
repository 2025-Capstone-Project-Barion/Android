// :feature:staff/src/main/java/com/example/staff/component/StaffPositionSelector.kt
package com.example.staff.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.Position
import com.example.ui.components.textfields.BarrionTextField
import com.example.ui.components.textfields.BarrionTextFieldState
import com.example.ui.theme.Spacing
import com.example.ui.theme.barrionColors

/**
 * 직원 직무 선택 드롭다운
 * Core UI의 BarrionTextField를 활용한 드롭다운 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffPositionSelector(
    selectedPosition: Position,
    onPositionSelected: (Position) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "직무",
    isError: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    // 에러 상태에 따른 필드 상태 결정
    val fieldState = when {
        isError -> BarrionTextFieldState.ERROR
        !enabled -> BarrionTextFieldState.DISABLED
        else -> BarrionTextFieldState.FOCUSED
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded }
        ) {
            BarrionTextField(
                value = selectedPosition.displayName,
                onValueChange = {}, // 읽기 전용
                title = label,
                placeholder = "직무를 선택하세요",
                fieldState = fieldState,
                enabled = enabled,
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .clickable(enabled = enabled) {
                        if (enabled) expanded = true
                    }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Position.entries.forEach { position ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = position.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (position == selectedPosition) {
                                    MaterialTheme.barrionColors.primaryBlue
                                } else {
                                    MaterialTheme.barrionColors.grayBlack
                                }
                            )
                        },
                        onClick = {
                            onPositionSelected(position)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = if (position == selectedPosition) {
                                MaterialTheme.barrionColors.primaryBlue
                            } else {
                                MaterialTheme.barrionColors.grayBlack
                            }
                        )
                    )
                }
            }
        }

        // 에러 메시지 표시
        if (isError && errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.barrionColors.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

/**
 * 선택 가능한 직무 목록을 미리 정의된 순서대로 반환
 */
private val Position.sortOrder: Int
    get() = when (this) {
        Position.MANAGER -> 0
        Position.BARISTA -> 1
        Position.CASHIER -> 2
        Position.KITCHEN -> 3
        Position.KIOSK_MANAGER -> 4
    }