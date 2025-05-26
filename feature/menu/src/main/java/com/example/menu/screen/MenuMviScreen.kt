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
    onNavigateToCategoryDetail: (Long, String) -> Unit = { _, _ -> }
) {
    // State 구독
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MenuEffect.NavigateToCategoryManagement -> onNavigateToCategoryManagement()
                is MenuEffect.NavigateToAddMenu -> onNavigateToAddMenu()
                is MenuEffect.NavigateToCategoryDetail -> {
                    onNavigateToCategoryDetail(effect.categoryId, effect.categoryName)
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
            modifier = Modifier.padding(paddingValues)
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
                items(state.categories) { category ->
                    CategorySection(
                        category = category,
                        menus = state.getMenusForCategory(category.id),
                        onSeeMore = { onIntent(MenuIntent.NavigateToCategoryDetail(category.id)) },
                        onAddMenu = { onIntent(MenuIntent.NavigateToAddMenu) }
                    )
                }
            }
        }
    }
}