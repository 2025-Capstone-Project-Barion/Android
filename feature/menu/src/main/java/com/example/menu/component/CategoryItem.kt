package com.example.menu.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.Category
import com.example.ui.theme.Spacing
import com.example.ui.theme.CornerRadius
import com.example.ui.theme.barrionColors

/**
 * 카테고리 아이템 - 색상 및 레이아웃 개선
 */
@Composable
fun CategoryItem(
    category: Category,
    menuCount: Int,
    order: Int,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.XSmall),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.barrionColors.grayVeryLight
        ),
        shape = RoundedCornerShape(CornerRadius.Medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 순서 번호
            Surface(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(CornerRadius.Small),
                color = MaterialTheme.barrionColors.primaryBlue
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = order.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.barrionColors.white
                    )
                }
            }

            Spacer(modifier = Modifier.width(Spacing.Medium))

            // 카테고리 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.barrionColors.grayBlack
                )

                Text(
                    text = "(${menuCount}개 메뉴)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.barrionColors.grayMedium
                )
            }

            // 기본 카테고리 표시 또는 삭제 버튼
            if (category.isDefault) {
                Surface(
                    shape = RoundedCornerShape(CornerRadius.Small),
                    color = MaterialTheme.barrionColors.blueVeryPale
                ) {
                    Text(
                        text = "기본 카테고리",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.barrionColors.primaryBlue,
                        modifier = Modifier.padding(
                            horizontal = Spacing.Small,
                            vertical = Spacing.XSmall
                        )
                    )
                }
            } else {
                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.barrionColors.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "카테고리 삭제"
                    )
                }
            }
        }
    }
}