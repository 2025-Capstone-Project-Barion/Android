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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.menu.component.CategoryManagementCard
import com.example.menu.component.CategorySection
import com.example.menu.component.DeleteMenuDialog  // 추가
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuState
import com.example.menu.type.MenuEffect
import com.example.menu.viewmodel.MenuViewModel

/**
 * 메뉴 메인 화면
 * - MVI 패턴으로 구현된 Compose 화면
 * - 카테고리별 메뉴 미리보기 표시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuMviScreen(
    viewModel: MenuViewModel,
    onNavigateToCategoryManagement: () -> Unit = {},
    onNavigateToAddMenu: () -> Unit = {},
    onNavigateToCategoryDetail: (Long, String) -> Unit = { _, _ -> },
    onNavigateToEditMenu: (Long) -> Unit = {}  // 새로운 파라미터 추가
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
                is MenuEffect.MenuDeletedSuccessfully -> {  // 추가
                    showDeleteMenuDialog = false
                    menuToDelete = null
                }
                // TODO: 다른 Effect들 처리
                else -> {}
            }
        }
    }

    // UI 구성
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("메뉴 관리") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.handleIntent(MenuIntent.NavigateToCategoryManagement) }
                    ) {
                        Icon(Icons.Default.GridView, contentDescription = "카테고리 관리")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleIntent(MenuIntent.NavigateToAddMenu) }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "메뉴 추가")
                    }
                }
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
                onNavigateToEditMenu(menu.id)  // 네비게이션 실행
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
 * 메뉴 화면 내용 컴포넌트
 */
@Composable
private fun MenuContent(
    state: MenuState,
    onIntent: (MenuIntent) -> Unit,
    onDeleteMenu: (com.example.domain.model.Menu) -> Unit,  // 새로운 파라미터 추가
    onEditMenu: (com.example.domain.model.Menu) -> Unit,  // 새로운 파라미터 추가
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
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
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onIntent(MenuIntent.RefreshData) }
                    ) {
                        Text("다시 시도")
                    }
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 카테고리 관리 카드
                item {
                    CategoryManagementCard(
                        onClick = { onIntent(MenuIntent.NavigateToCategoryManagement) }
                    )
                }

                // 카테고리별 메뉴 섹션들
                // CategorySection 호출 부분
                items(state.categories) { category ->
                    CategorySection(
                        category = category,
                        menus = state.getMenusForCategory(category.id),
                        onSeeMore = { onIntent(MenuIntent.NavigateToCategoryDetail(category.id)) },
                        onAddMenu = { onIntent(MenuIntent.NavigateToAddMenu) },
                        onDeleteMenu = onDeleteMenu,
                        onEditMenu = onEditMenu  // 새로운 콜백 전달
                    )
                }
            }
        }
    }
}