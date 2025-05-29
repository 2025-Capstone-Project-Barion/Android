package com.example.menu.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.menu.component.DeleteMenuDialog
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuEffect
import com.example.menu.viewmodel.MenuViewModel
import com.example.ui.theme.Spacing
import com.example.ui.theme.CornerRadius
import com.example.ui.theme.barrionColors
import java.text.NumberFormat
import java.util.Locale

/**
 * 카테고리 상세 화면 - 디자인 개선
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
                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.barrionColors.white,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "$categoryName 메뉴",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.barrionColors.grayBlack
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.barrionColors.grayBlack
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onNavigateToAddMenu(categoryId) }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "메뉴 추가",
                            tint = MaterialTheme.barrionColors.primaryBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.barrionColors.white,
                    titleContentColor = MaterialTheme.barrionColors.grayBlack,
                    navigationIconContentColor = MaterialTheme.barrionColors.grayBlack,
                    actionIconContentColor = MaterialTheme.barrionColors.primaryBlue
                ),
                windowInsets = WindowInsets.statusBars  // 헤더 위로 올리기
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
 * 카테고리 상세 화면 내용 - 세로 카드 디자인
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
            .padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        if (menus.isNotEmpty()) {
            items(menus) { menu ->
                // 세로 카드 디자인 (디자인 이미지 스타일)
                MenuVerticalCard(
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
                        .padding(vertical = Spacing.XXLarge),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.barrionColors.grayVeryLight
                    ),
                    shape = RoundedCornerShape(CornerRadius.Medium)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.XXLarge),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$categoryName 카테고리에\n등록된 메뉴가 없습니다",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.barrionColors.grayMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(Spacing.Medium))

                        Button(
                            onClick = onAddMenu,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.barrionColors.primaryBlue
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(Spacing.Small))
                            Text(
                                text = "첫 메뉴 추가하기",
                                color = MaterialTheme.barrionColors.white
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 세로형 메뉴 카드 (디자인 이미지 스타일)
 */
@Composable
private fun MenuVerticalCard(
    menu: com.example.domain.model.Menu,
    onEdit: () -> Unit,
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
        Column(
            modifier = Modifier.padding(Spacing.Medium)
        ) {
            // 메뉴 이미지
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(menu.imageUrl.ifEmpty { "https://via.placeholder.com/300x200" })
                    .crossfade(true)
                    .build(),
                contentDescription = menu.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(CornerRadius.Small)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(Spacing.Small))

            // 메뉴 정보
            Text(
                text = menu.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.barrionColors.grayBlack
            )

            Text(
                text = NumberFormat.getNumberInstance(Locale.KOREA).format(menu.price) + "원",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.barrionColors.primaryBlue,
                fontWeight = FontWeight.SemiBold
            )

            if (menu.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Spacing.XSmall))
                Text(
                    text = menu.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.barrionColors.grayMedium
                )
            }

            Spacer(modifier = Modifier.height(Spacing.Small))

            // 수정/삭제 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                // 수정 버튼
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.barrionColors.primaryBlue
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.barrionColors.primaryBlue
                        ).brush
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "수정",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(Spacing.XSmall))
                    Text("수정")
                }

                // 삭제 버튼
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.barrionColors.error
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.barrionColors.error
                        ).brush
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "삭제",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(Spacing.XSmall))
                    Text("삭제")
                }
            }
        }
    }
}