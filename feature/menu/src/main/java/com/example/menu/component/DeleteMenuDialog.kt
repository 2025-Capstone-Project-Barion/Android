package com.example.menu.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

/**
 * 메뉴 삭제 확인 다이얼로그
 * - 메뉴 삭제 시 확인 다이얼로그
 */
@Composable
fun DeleteMenuDialog(
    isVisible: Boolean,
    menuName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "메뉴 삭제",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("'$menuName' 메뉴를 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.")
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