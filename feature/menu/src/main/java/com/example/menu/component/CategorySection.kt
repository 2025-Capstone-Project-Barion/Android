package com.example.menu.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.Category
import com.example.domain.model.Menu

/**
 * 카테고리별 메뉴 섹션 컴포넌트
 * - 카테고리 제목과 해당 카테고리의 메뉴들을 가로 스크롤로 표시
 * - 더보기 버튼과 추가 버튼 포함
 */
@Composable
fun CategorySection(
    category: Category,
    menus: List<Menu>,
    onSeeMore: () -> Unit,
    onAddMenu: () -> Unit,
    onDeleteMenu: (Menu) -> Unit,
    onEditMenu: (Menu) -> Unit,  // 새로운 파라미터 추가
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 카테고리 헤더 (제목 + 더보기 + 추가 버튼)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${category.name} (${menus.size}개 메뉴)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row {
                IconButton(onClick = onAddMenu) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "메뉴 추가"
                    )
                }

                TextButton(onClick = onSeeMore) {
                    Text("더보기")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 메뉴 목록 (가로 스크롤)
        if (menus.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                // 메뉴 목록 (가로 스크롤) 부분에서
                items(menus) { menu ->
                    MenuCard(
                        menu = menu,
                        onEdit = { onEditMenu(menu) },  // 수정: 메뉴 편집 콜백
                        onDelete = { onDeleteMenu(menu) }
                    )
                }
            }
        } else {
            // 메뉴가 없을 때 표시
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "등록된 메뉴가 없습니다",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}