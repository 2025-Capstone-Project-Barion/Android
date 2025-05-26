package com.example.menu.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * 카테고리 추가 다이얼로그
 * - 카테고리 이름 입력
 * - 입력 검증
 * - 추가/취소 버튼
 */
@Composable
fun AddCategoryDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var categoryName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // 다이얼로그가 열릴 때마다 상태 초기화
    LaunchedEffect(isVisible) {
        if (isVisible) {
            categoryName = ""
            isError = false
            errorMessage = ""
        }
    }

    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 다이얼로그 제목
                    Text(
                        text = "카테고리 추가",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // 입력 필드
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { newValue ->
                            categoryName = newValue
                            // 입력할 때마다 에러 상태 초기화
                            if (isError) {
                                isError = false
                                errorMessage = ""
                            }
                        },
                        label = { Text("카테고리 이름") },
                        placeholder = { Text("예: 디저트, 음료, 사이드 등") },
                        isError = isError,
                        supportingText = if (isError) {
                            { Text(errorMessage, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 버튼 영역
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onDismiss
                        ) {
                            Text("취소")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                // 입력 검증
                                when {
                                    categoryName.isBlank() -> {
                                        isError = true
                                        errorMessage = "카테고리 이름을 입력해주세요"
                                    }
                                    categoryName.length > 20 -> {
                                        isError = true
                                        errorMessage = "카테고리 이름은 20자 이하로 입력해주세요"
                                    }
                                    else -> {
                                        onConfirm(categoryName.trim())
                                    }
                                }
                            }
                        ) {
                            Text("추가")
                        }
                    }
                }
            }
        }
    }
}