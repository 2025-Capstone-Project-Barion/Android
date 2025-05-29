// :feature:staff/src/main/java/com/example/staff/screen/StaffEditScreen.kt
package com.example.staff.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.Bank
import com.example.domain.model.Position
import com.example.domain.model.Staff
import com.example.staff.component.*
import com.example.staff.type.StaffEffect
import com.example.staff.type.StaffIntent
import com.example.staff.viewmodel.StaffViewModel
import com.example.ui.components.buttons.BarrionFullButton
import com.example.ui.theme.Spacing
import com.example.ui.theme.barrionColors
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffEditScreen(
    viewModel: StaffViewModel = hiltViewModel(),
    staffId: Long? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state
    val staff = state.selectedStaff
    val isEditMode = staffId != null

    // 최초 로딩 시 직원 정보 불러오기 (수정 모드일 때)
    LaunchedEffect(staffId) {
        staffId?.let {
            viewModel.onIntent(StaffIntent.SelectStaff(it))
        }
    }

    // 효과 처리 (토스트 및 뒤로가기)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is StaffEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                StaffEffect.NavigateBack -> onBack()
                else -> {}
            }
        }
    }

    // 입력 상태 (6개 필수 필드)
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var hourlyWage by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var selectedPosition by remember { mutableStateOf<Position?>(null) }
    var selectedBank by remember { mutableStateOf<Bank?>(null) }

    // 직원 정보 반영 (수정 모드일 때)
    LaunchedEffect(staff) {
        staff?.let {
            name = it.name
            phoneNumber = it.phoneNumber
            hourlyWage = it.hourlyWage.toString()
            accountNumber = it.accountNumber
            selectedPosition = it.position
            selectedBank = it.bank
        }
    }

    // 초기값 설정 (추가 모드일 때)
    LaunchedEffect(Unit) {
        if (!isEditMode && selectedPosition == null) {
            selectedPosition = Position.BARISTA // 기본값
            selectedBank = Bank.WOORI // 기본값
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // 제목을 뒤로가기 버튼 바로 옆에 붙임 (이미지 1처럼)
                    Text(
                        text = if (isEditMode) "직원 정보 수정" else "직원 등록",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.barrionColors.grayBlack
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(StaffIntent.NavigateBack) }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.barrionColors.grayBlack
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.barrionColors.white,
                    titleContentColor = MaterialTheme.barrionColors.grayBlack
                )
            )
        },
        containerColor = MaterialTheme.barrionColors.white  // 배경을 흰색으로 변경
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                // 6개 필수 필드 (이미지 3과 동일한 순서)

                // 1. 이름
                StaffTextField(
                    label = "이름",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "이름을 입력하세요"
                )

                // 2. 전화번호
                StaffTextField(
                    label = "전화번호",
                    value = phoneNumber,
                    onValueChange = { input ->
                        // 자동 하이픈 추가 포맷팅
                        val digitsOnly = input.replace("-", "")
                        phoneNumber = when {
                            digitsOnly.length <= 3 -> digitsOnly
                            digitsOnly.length <= 7 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3)}"
                            digitsOnly.length <= 11 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7)}"
                            else -> phoneNumber // 현재 값 유지
                        }
                    },
                    placeholder = "010-0000-0000",
                    keyboardType = KeyboardType.Phone
                )

                // 3. 시급
                StaffTextField(
                    label = "시급",
                    value = hourlyWage,
                    onValueChange = { input ->
                        // 숫자만 입력 허용
                        if (input.isEmpty() || input.all { it.isDigit() }) {
                            hourlyWage = input
                        }
                    },
                    placeholder = "시급을 입력하세요",
                    keyboardType = KeyboardType.Number
                )

                // 4. 직무 드롭다운 (이미지 3과 동일)
                selectedPosition?.let { position ->
                    StaffPositionSelector(
                        selectedPosition = position,
                        onPositionSelected = { selectedPosition = it },
                        label = "직무"
                    )
                }

                // 5. 은행 드롭다운 (이미지 3과 동일)
                selectedBank?.let { bank ->
                    StaffBankSelector(
                        selectedBank = bank,
                        onBankSelected = { selectedBank = it },
                        label = "은행"
                    )
                }

                // 6. 계좌번호
                StaffTextField(
                    label = "계좌번호",
                    value = accountNumber,
                    onValueChange = { input ->
                        // 숫자와 하이픈만 허용
                        if (input.isEmpty() || input.all { it.isDigit() || it == '-' }) {
                            accountNumber = input
                        }
                    },
                    placeholder = "계좌번호를 입력하세요",
                    keyboardType = KeyboardType.Number
                )

                // 하단 여백 추가
                Spacer(modifier = Modifier.height(100.dp))
            }

            // 저장 버튼을 하단에 고정 (이미지와 동일한 위치)
            BarrionFullButton(
                text = if (isEditMode) "변경사항 저장하기" else "직원 등록하기",
                onClick = {
                    val wage = hourlyWage.toIntOrNull() ?: 0
                    val newStaff = Staff(
                        id = staff?.id ?: 0L,
                        name = name.trim(),
                        phoneNumber = phoneNumber.replace("-", ""),
                        hourlyWage = wage,
                        accountNumber = accountNumber.trim(),
                        bank = selectedBank!!,
                        position = selectedPosition!!
                    )

                    if (isEditMode) {
                        viewModel.onIntent(StaffIntent.UpdateStaff(newStaff))
                    } else {
                        viewModel.onIntent(StaffIntent.AddStaff(newStaff))
                    }
                },
                enabled = selectedPosition != null &&
                        selectedBank != null &&
                        name.isNotBlank() &&
                        phoneNumber.isNotBlank() &&
                        hourlyWage.isNotBlank() &&
                        accountNumber.isNotBlank() &&
                        !state.isLoading,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(Spacing.Medium)
            )
        }
    }
}