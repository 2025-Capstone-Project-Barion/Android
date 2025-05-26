package com.example.menu.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.menu.component.ImageUploadArea
import com.example.menu.component.CategoryDropdown
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuEffect
import com.example.menu.viewmodel.MenuViewModel

/**
 * 메뉴 수정 화면
 * - 기존 메뉴 정보로 폼 초기화
 * - 메뉴 정보 수정
 * - 입력 검증
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuScreen(
    menuId: Long,
    viewModel: MenuViewModel,
    onNavigateBack: () -> Unit = {}
) {
    // State 구독
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 수정할 메뉴 찾기
    val menuToEdit = remember(state.menusByCategory, menuId) {
        state.menusByCategory.values.flatten().find { it.id == menuId }
    }

    // 폼 상태들 (기존 메뉴 데이터로 초기화)
    var menuName by remember(menuToEdit) { mutableStateOf(menuToEdit?.name ?: "") }
    var price by remember(menuToEdit) { mutableStateOf(menuToEdit?.price?.toString() ?: "") }
    var description by remember(menuToEdit) { mutableStateOf(menuToEdit?.description ?: "") }
    var selectedCategory by remember(menuToEdit) { mutableStateOf(menuToEdit?.categoryId ?: 0L) }
    var imageUrl by remember(menuToEdit) { mutableStateOf(menuToEdit?.imageUrl ?: "") }

    // 에러 상태들
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MenuEffect.MenuUpdatedSuccessfully -> {
                    onNavigateBack()  // 성공 시 뒤로가기
                }
                is MenuEffect.ShowError -> {
                    // TODO: 토스트 메시지 또는 스낵바 표시
                }
                else -> {}
            }
        }
    }

    // 메뉴를 찾지 못한 경우
    if (menuToEdit == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "메뉴를 찾을 수 없습니다",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateBack) {
                    Text("돌아가기")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("메뉴 수정") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            // 입력 검증
                            var hasError = false

                            if (menuName.isBlank()) {
                                nameError = "메뉴 이름을 입력해주세요"
                                hasError = true
                            } else {
                                nameError = ""
                            }

                            val priceValue = price.toIntOrNull()
                            if (priceValue == null || priceValue < 0) {
                                priceError = "올바른 가격을 입력해주세요"
                                hasError = true
                            } else {
                                priceError = ""
                            }

                            if (selectedCategory == 0L) {
                                categoryError = "카테고리를 선택해주세요"
                                hasError = true
                            } else {
                                categoryError = ""
                            }

                            // 검증 통과 시 메뉴 수정
                            if (!hasError) {
                                val updatedMenu = menuToEdit.copy(
                                    name = menuName.trim(),
                                    price = priceValue!!,
                                    categoryId = selectedCategory,
                                    description = description.trim(),
                                    imageUrl = imageUrl
                                )
                                viewModel.handleIntent(MenuIntent.UpdateMenu(updatedMenu))
                            }
                        }
                    ) {
                        Text(
                            text = "수정",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        EditMenuContent(
            menuName = menuName,
            onMenuNameChange = {
                menuName = it
                nameError = ""
            },
            nameError = nameError,
            price = price,
            onPriceChange = {
                price = it
                priceError = ""
            },
            priceError = priceError,
            description = description,
            onDescriptionChange = { description = it },
            selectedCategory = selectedCategory,
            onCategoryChange = {
                selectedCategory = it
                categoryError = ""
            },
            categoryError = categoryError,
            categories = state.categories,
            imageUrl = imageUrl,
            onImageChange = { imageUrl = it },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

/**
 * 메뉴 수정 화면 내용
 */
@Composable
private fun EditMenuContent(
    menuName: String,
    onMenuNameChange: (String) -> Unit,
    nameError: String,
    price: String,
    onPriceChange: (String) -> Unit,
    priceError: String,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedCategory: Long,
    onCategoryChange: (Long) -> Unit,
    categoryError: String,
    categories: List<com.example.domain.model.Category>,
    imageUrl: String,
    onImageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 기존 메뉴 정보 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "기존 메뉴 정보를 수정하세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // 이미지 업로드 영역
        ImageUploadArea(
            imageUrl = imageUrl,
            onImageSelected = onImageChange,
            modifier = Modifier.fillMaxWidth()
        )

        // 메뉴 이름
        OutlinedTextField(
            value = menuName,
            onValueChange = onMenuNameChange,
            label = { Text("메뉴 이름") },
            placeholder = { Text("메뉴 이름 입력") },
            isError = nameError.isNotEmpty(),
            supportingText = if (nameError.isNotEmpty()) {
                { Text(nameError, color = MaterialTheme.colorScheme.error) }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 가격
        OutlinedTextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text("가격") },
            placeholder = { Text("0") },
            isError = priceError.isNotEmpty(),
            supportingText = if (priceError.isNotEmpty()) {
                { Text(priceError, color = MaterialTheme.colorScheme.error) }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 카테고리 선택
        CategoryDropdown(
            categories = categories,
            selectedCategoryId = selectedCategory,
            onCategorySelected = onCategoryChange,
            error = categoryError,
            modifier = Modifier.fillMaxWidth()
        )

        // 설명
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("설명") },
            placeholder = { Text("메뉴 설명 입력") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 4
        )
    }
}