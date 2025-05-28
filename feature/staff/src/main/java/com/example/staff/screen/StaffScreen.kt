// :feature:staff/src/main/java/com/example/staff/screen/StaffScreen.kt
package com.example.staff.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.staff.component.StaffListItem
import com.example.staff.type.StaffEffect
import com.example.staff.type.StaffIntent
import com.example.staff.viewmodel.StaffViewModel
import com.example.ui.components.textfields.BarrionTextField
import com.example.ui.components.textfields.BarrionTextFieldState
import com.example.ui.theme.CornerRadius
import com.example.ui.theme.Spacing
import com.example.ui.theme.barrionColors
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(
    viewModel: StaffViewModel = hiltViewModel(),
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Long) -> Unit  // 개별 수정을 위한 새 파라미터
) {
    val state = viewModel.state
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.onIntent(StaffIntent.LoadStaffList)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is StaffEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                is StaffEffect.NavigateToDetail -> onNavigateToDetail(effect.id)
                StaffEffect.NavigateBack -> {}
            }
        }
    }

    // 검색어 변경 시 검색 실행
    LaunchedEffect(searchQuery) {
        viewModel.onIntent(StaffIntent.SearchStaff(searchQuery))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // 제목을 가운데 정렬 (메뉴 관리와 동일한 방식)
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "직원 목록",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.barrionColors.grayBlack
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.barrionColors.white,
                    titleContentColor = MaterialTheme.barrionColors.grayBlack
                ),
                actions = {
                    IconButton(onClick = onNavigateToAdd) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "직원 추가",
                            tint = MaterialTheme.barrionColors.primaryBlue
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.barrionColors.grayWhite  // 더 밝은 배경
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 검색바 (이미지 2처럼 더 심플하게)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("이름 또는 전화번호로 검색") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.Medium),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.barrionColors.white,
                    focusedContainerColor = MaterialTheme.barrionColors.white,
                    unfocusedBorderColor = MaterialTheme.barrionColors.grayLight,
                    focusedBorderColor = MaterialTheme.barrionColors.primaryBlue
                ),
                singleLine = true
            )

            // 직원 목록
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.Medium),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.barrionColors.primaryBlue
                        )
                    }
                }

                state.staffList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.Medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
                        ) {
                            Text(
                                text = if (searchQuery.isNotEmpty()) "검색 결과가 없습니다." else "등록된 직원이 없습니다.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.barrionColors.grayMedium
                            )
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "우상단 + 버튼을 눌러 직원을 추가해보세요.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.barrionColors.grayMedium
                                )
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(Spacing.Medium),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
                    ) {
                        items(items = state.staffList, key = { it.id }) { staff ->
                            StaffListItem(
                                staff = staff,
                                onClick = {
                                    // 개별 직원 클릭 시 바로 수정 화면으로 이동
                                    onNavigateToEdit(staff.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}