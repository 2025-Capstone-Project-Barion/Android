package com.example.menu.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.menu.component.CategoryItem
import com.example.menu.component.AddCategoryDialog
import com.example.menu.component.DeleteCategoryDialog
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuEffect
import com.example.menu.viewmodel.MenuViewModel
import com.example.ui.theme.Spacing
import com.example.ui.theme.CornerRadius
import com.example.ui.theme.barrionColors

/**
 * 카테고리 관리 화면 - 색상 및 디자인 개선
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    viewModel: MenuViewModel,
    onNavigateBack: () -> Unit = {}
) {
    // State 구독
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 다이얼로그 상태들
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<com.example.domain.model.Category?>(null) }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MenuEffect.CategoryAddedSuccessfully -> {
                    showAddCategoryDialog = false
                }
                is MenuEffect.CategoryDeletedSuccessfully -> {
                    showDeleteCategoryDialog = false
                    categoryToDelete = null
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
                        text = "카테고리 관리",
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
                        onClick = { showAddCategoryDialog = true }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "카테고리 추가",
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
                windowInsets = WindowInsets.statusBars
            )
        }
    ) { paddingValues ->
        CategoryManagementContent(
            state = state,
            onIntent = viewModel::handleIntent,
            onNavigateToAddCategory = { showAddCategoryDialog = true },
            onDeleteCategory = { category ->
                categoryToDelete = category
                showDeleteCategoryDialog = true
            },
            modifier = Modifier.padding(paddingValues)
        )

        // 카테고리 추가 다이얼로그
        AddCategoryDialog(
            isVisible = showAddCategoryDialog,
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { categoryName ->
                viewModel.handleIntent(MenuIntent.AddCategory(categoryName))
            }
        )

        // 카테고리 삭제 확인 다이얼로그
        DeleteCategoryDialog(
            isVisible = showDeleteCategoryDialog,
            categoryName = categoryToDelete?.name ?: "",
            onDismiss = {
                showDeleteCategoryDialog = false
                categoryToDelete = null
            },
            onConfirm = {
                categoryToDelete?.let { category ->
                    viewModel.handleIntent(MenuIntent.DeleteCategory(category.id))
                }
            }
        )
    }
}

/**
 * 카테고리 관리 화면 내용 - 색상 개선
 */
@Composable
private fun CategoryManagementContent(
    state: com.example.menu.type.MenuState,
    onIntent: (MenuIntent) -> Unit,
    onNavigateToAddCategory: () -> Unit,
    onDeleteCategory: (com.example.domain.model.Category) -> Unit,
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
                modifier = modifier
                    .fillMaxSize()
                    .padding(Spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                // 카테고리 목록
                itemsIndexed(state.categories) { index, category ->
                    CategoryItem(
                        category = category,
                        menuCount = state.getMenusForCategory(category.id).size,
                        order = index + 1,
                        onDelete = {
                            if (!category.isDefault) {
                                onDeleteCategory(category)
                            }
                        }
                    )
                }

                // 하단 정보 및 추가 버튼
                item {
                    Spacer(modifier = Modifier.height(Spacing.Large))

                    Text(
                        text = "카테고리 순서를 변경하려면 드래그하여 위치를 조정하세요.\n카테고리 추가는 상단 + 버튼을 이용하세요.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.barrionColors.grayMedium,
                        modifier = Modifier.padding(vertical = Spacing.Medium)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "총 카테고리",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.barrionColors.grayBlack
                            )
                            Text(
                                text = "${state.categories.size}개",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.barrionColors.grayBlack
                            )
                        }

                        Button(
                            onClick = onNavigateToAddCategory,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.barrionColors.primaryBlue
                            ),
                            shape = RoundedCornerShape(CornerRadius.Medium)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.barrionColors.white
                            )
                            Spacer(modifier = Modifier.width(Spacing.Small))
                            Text(
                                text = "카테고리 추가",
                                color = MaterialTheme.barrionColors.white
                            )
                        }
                    }
                }
            }
        }
    }
}