package com.example.menu.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.menu.component.MenuCard
import com.example.menu.component.DeleteMenuDialog
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuEffect
import com.example.menu.viewmodel.MenuViewModel

/**
 * 카테고리 상세 화면
 * - 특정 카테고리의 모든 메뉴 리스트 표시
 * - 메뉴별 수정/삭제 기능
 * - 해당 카테고리에 메뉴 추가 기능
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryId: Long,
    categoryName: String,
    viewModel: MenuViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateToAddMenu: (Long) -> Unit = {},
    onNavigateToEditMenu: (Long) -> Unit = {}
) {
    // State 구독
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 해당 카테고리의 메뉴들
    val categoryMenus = state.getMenusForCategory(categoryId)

    // 메뉴 삭제 다이얼로그 상태
    var showDeleteMenuDialog by remember { mutableStateOf(false) }
    var menuToDelete by remember { mutableStateOf<com.example.domain.model.Menu?>(null) }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MenuEffect.MenuDeletedSuccessfully -> {
                    showDeleteMenuDialog = false
                    menuToDelete = null
                }
                // TODO: 다른 Effect들 처리
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$categoryName 메뉴") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onNavigateToAddMenu(categoryId) }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "메뉴 추가")
                    }
                }
            )
        }
    ) { paddingValues ->
        CategoryDetailContent(
            categoryName = categoryName,
            menus = categoryMenus,
            onDeleteMenu = { menu ->
                menuToDelete = menu
                showDeleteMenuDialog = true
            },
            onEditMenu = onNavigateToEditMenu,
            onAddMenu = { onNavigateToAddMenu(categoryId) },
            modifier = Modifier.padding(paddingValues)
        )

        // 메뉴 삭제 확인 다이얼로그
        DeleteMenuDialog(
            isVisible = showDeleteMenuDialog,
            menuName = menuToDelete?.name ?: "",
            onDismiss = {
                showDeleteMenuDialog = false
                menuToDelete = null
            },
            onConfirm = {
                menuToDelete?.let { menu ->
                    viewModel.handleIntent(MenuIntent.DeleteMenu(menu.id))
                }
            }
        )
    }
}

/**
 * 카테고리 상세 화면 내용
 */
@Composable
private fun CategoryDetailContent(
    categoryName: String,
    menus: List<com.example.domain.model.Menu>,
    onDeleteMenu: (com.example.domain.model.Menu) -> Unit,
    onEditMenu: (Long) -> Unit,
    onAddMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (menus.isNotEmpty()) {
            items(menus) { menu ->
                // 상세화면에서는 큰 카드로 표시
                MenuDetailCard(
                    menu = menu,
                    onEdit = { onEditMenu(menu.id) },
                    onDelete = { onDeleteMenu(menu) }
                )
            }
        } else {
            // 메뉴가 없을 때
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$categoryName 카테고리에\n등록된 메뉴가 없습니다",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = onAddMenu) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("첫 메뉴 추가하기")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 상세 화면용 큰 메뉴 카드
 */
@Composable
private fun MenuDetailCard(
    menu: com.example.domain.model.Menu,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 메뉴 이미지 (큰 사이즈)
            coil.compose.AsyncImage(
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(menu.imageUrl.ifEmpty { "https://via.placeholder.com/150" })
                    .crossfade(true)
                    .build(),
                contentDescription = menu.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 메뉴 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = menu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = java.text.NumberFormat.getNumberInstance(java.util.Locale.KOREA).format(menu.price) + "원",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                if (menu.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = menu.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 수정/삭제 버튼
            Row {
                TextButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "수정",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("수정")
                }

                TextButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("삭제")
                }
            }
        }
    }
}