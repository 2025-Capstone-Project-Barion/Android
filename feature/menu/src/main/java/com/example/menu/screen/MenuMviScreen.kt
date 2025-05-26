package com.example.menu.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.menu.component.CategoryManagementCard
import com.example.menu.component.CategorySection
import com.example.menu.component.DeleteMenuDialog
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuState
import com.example.menu.type.MenuEffect
import com.example.menu.viewmodel.MenuViewModel
import com.example.ui.theme.Spacing
import com.example.ui.theme.barrionColors

/**
 * 메뉴 메인 화면 - 커스텀 디자인 시스템 적용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuMviScreen(
    viewModel: MenuViewModel,
    onNavigateToCategoryManagement: () -> Unit = {},
    onNavigateToAddMenu: () -> Unit = {},
    onNavigateToCategoryDetail: (Long, String) -> Unit = { _, _ -> },
    onNavigateToEditMenu: (Long) -> Unit = {}
) {
    // State 구독
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 메뉴 삭제 다이얼로그 상태
    var showDeleteMenuDialog by remember { mutableStateOf(false) }
    var menuToDelete by remember { mutableStateOf<com.example.domain.model.Menu?>(null) }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MenuEffect.NavigateToCategoryManagement -> onNavigateToCategoryManagement()
                is MenuEffect.NavigateToAddMenu -> onNavigateToAddMenu()
                is MenuEffect.NavigateToCategoryDetail -> {
                    onNavigateToCategoryDetail(effect.categoryId, effect.categoryName)
                }
                is MenuEffect.MenuDeletedSuccessfully -> {
                    showDeleteMenuDialog = false
                    menuToDelete = null
                }
                else -> {}
            }
        }
    }

    // UI 구성 - 헤더 위치 조정
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.barrionColors.white,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "메뉴 관리",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.barrionColors.grayBlack
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.handleIntent(MenuIntent.NavigateToCategoryManagement) }
                    ) {
                        Icon(
                            Icons.Default.GridView,
                            contentDescription = "카테고리 관리",
                            tint = MaterialTheme.barrionColors.primaryBlue
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleIntent(MenuIntent.NavigateToAddMenu) }
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
                    navigationIconContentColor = MaterialTheme.barrionColors.primaryBlue,
                    actionIconContentColor = MaterialTheme.barrionColors.primaryBlue
                ),
                windowInsets = WindowInsets.statusBars  // 상태바 inset 사용
            )
        }
    ) { paddingValues ->
        MenuContent(
            state = state,
            onIntent = viewModel::handleIntent,
            onDeleteMenu = { menu ->
                menuToDelete = menu
                showDeleteMenuDialog = true
            },
            onEditMenu = { menu ->
                onNavigateToEditMenu(menu.id)
            },
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
 * 메뉴 화면 내용 컴포넌트 - 간격 및 색상 적용
 */
@Composable
private fun MenuContent(
    state: MenuState,
    onIntent: (MenuIntent) -> Unit,
    onDeleteMenu: (com.example.domain.model.Menu) -> Unit,
    onEditMenu: (com.example.domain.model.Menu) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.barrionColors.primaryBlue
                )
            }
        }

        state.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.barrionColors.error
                    )
                    Spacer(modifier = Modifier.height(Spacing.Medium))
                    Button(
                        onClick = { onIntent(MenuIntent.RefreshData) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.barrionColors.primaryBlue
                        )
                    ) {
                        Text(
                            text = "다시 시도",
                            color = MaterialTheme.barrionColors.white
                        )
                    }
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(Spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.Large)
            ) {
                // 카테고리 관리 카드 - 중앙 정렬
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryManagementCard(
                            onClick = { onIntent(MenuIntent.NavigateToCategoryManagement) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // 카테고리별 메뉴 섹션들
                items(state.categories) { category ->
                    CategorySection(
                        category = category,
                        menus = state.getMenusForCategory(category.id),
                        onSeeMore = { onIntent(MenuIntent.NavigateToCategoryDetail(category.id)) },
                        onAddMenu = { onIntent(MenuIntent.NavigateToAddMenu) },
                        onDeleteMenu = onDeleteMenu,
                        onEditMenu = onEditMenu
                    )
                }
            }
        }
    }
}