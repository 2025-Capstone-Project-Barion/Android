package com.example.menu.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
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
 * 메뉴 추가 화면
 * - 메뉴 정보 입력 폼
 * - 이미지 업로드
 * - 카테고리 선택
 * - 입력 검증
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuScreen(
    selectedCategoryId: Long? = null,
    viewModel: MenuViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // 폼 상태들
    var menuName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(selectedCategoryId ?: 0L) }
    var imageUrl by remember { mutableStateOf("") }
    var selectedBase64Image by remember { mutableStateOf<String?>(null) }

    // 에러 상태들
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    // Effect 처리 - 성공/에러 메시지 표시 후 화면 닫기
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MenuEffect.MenuAddedSuccessfully -> {
                    // 성공 메시지 표시
                    snackbarHostState.showSnackbar(
                        message = "메뉴가 추가되었습니다",
                        duration = SnackbarDuration.Short
                    )
                    // 즉시 화면 닫기 (delay 제거)
                    onNavigateBack()
                }
                is MenuEffect.ShowError -> {
                    // 에러 메시지 표시
                    snackbarHostState.showSnackbar(
                        message = effect.error,
                        duration = SnackbarDuration.Long
                    )
                }
                is MenuEffect.ShowToast -> {
                    // 토스트 메시지 표시
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("메뉴 추가") },
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

                            // 검증 통과 시 메뉴 추가
                            if (!hasError) {
                                println("🖼️ UI - 선택된 base64 이미지: ${selectedBase64Image?.take(50) ?: "없음"}...")

                                viewModel.handleIntent(
                                    MenuIntent.AddMenu(
                                        name = menuName.trim(),
                                        price = priceValue!!,
                                        categoryId = selectedCategory,
                                        description = description.trim(),
                                        imageUrl = imageUrl,
                                        base64Image = selectedBase64Image
                                    )
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "추가",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        AddMenuContent(
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
            // ✅ Base64 이미지를 우선적으로 표시
            imageUrl = if (selectedBase64Image != null) selectedBase64Image!! else imageUrl,
            onImageChange = { newImageData ->
                println("🖼️ ImageUploadArea - 이미지 받음: ${newImageData.take(50)}...")

                if (newImageData.startsWith("data:image")) {
                    // Base64 이미지인 경우
                    selectedBase64Image = newImageData
                    println("🖼️ Base64 데이터 저장됨")
                } else {
                    // 일반 URL인 경우
                    imageUrl = newImageData
                    selectedBase64Image = null // Base64 초기화
                    println("🖼️ URL 저장됨: $newImageData")
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

/**
 * 메뉴 추가 화면 내용
 */
@Composable
private fun AddMenuContent(
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
        // 이미지 업로드 영역 - Base64 이미지도 표시 가능
        ImageUploadArea(
            imageUrl = imageUrl, // Base64 또는 URL
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