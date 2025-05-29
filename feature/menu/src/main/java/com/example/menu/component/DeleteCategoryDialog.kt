package com.example.menu.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

/**
 * 카테고리 삭제 확인 다이얼로그
 * - 간단한 확인/취소 다이얼로그
 */
@Composable
fun DeleteCategoryDialog(
    isVisible: Boolean,
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "카테고리 삭제",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("'$categoryName' 카테고리를 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.")
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("취소")
                }
            }
        )
    }
}