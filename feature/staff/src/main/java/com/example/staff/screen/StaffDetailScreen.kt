// :feature:staff/src/main/java/com/example/staff/screen/StaffDetailScreen.kt
package com.example.staff.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.Staff
import com.example.staff.component.*
import com.example.staff.type.StaffEffect
import com.example.staff.type.StaffIntent
import com.example.staff.viewmodel.StaffViewModel
import com.example.ui.theme.barrionColors
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffDetailScreen(
    viewModel: StaffViewModel = hiltViewModel(),
    staffId: Long,
    onBack: () -> Unit  // ✅ onEditClick 파라미터 제거
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(staffId) {
        viewModel.onIntent(StaffIntent.SelectStaff(staffId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is StaffEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                StaffEffect.NavigateBack -> onBack()
                else -> {}
            }
        }
    }

    val staff = state.selectedStaff

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "직원 정보",
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
                    containerColor = MaterialTheme.barrionColors.white
                )
            )
        },
        containerColor = MaterialTheme.barrionColors.grayWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            staff?.let { staffInfo ->
                // 프로필 카드
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.barrionColors.white
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.barrionColors.blueVeryPale),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = staffInfo.name.firstOrNull()?.toString() ?: "?",
                                color = MaterialTheme.barrionColors.primaryBlue,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = staffInfo.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = staffInfo.position.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.barrionColors.grayMedium
                        )
                    }
                }

                // 정보 카드
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.barrionColors.white
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // 전화번호
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("전화번호", modifier = Modifier.width(80.dp))
                            Text(formatDetailPhoneNumber(staffInfo.phoneNumber))
                        }

                        Divider(color = MaterialTheme.barrionColors.grayVeryLight)

                        // 시급
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("시급", modifier = Modifier.width(80.dp))
                            Text(formatHourlyWage(staffInfo.hourlyWage))
                        }

                        Divider(color = MaterialTheme.barrionColors.grayVeryLight)

                        // 직무
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("직무", modifier = Modifier.width(80.dp))
                            Text(staffInfo.position.displayName)
                        }

                        Divider(color = MaterialTheme.barrionColors.grayVeryLight)

                        // 계좌
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("계좌", modifier = Modifier.width(80.dp))
                            Text("${staffInfo.bank.displayName} ${staffInfo.accountNumber}")
                        }
                    }
                }

                // ✅ 삭제하기 버튼만 있음 (수정하기 버튼 제거)
                Button(
                    onClick = {
                        viewModel.onIntent(StaffIntent.DeleteStaff(staffInfo.id))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        //containerColor = MaterialTheme.barrionColors.errorRed  // ✅ 삭제 색상으로 변경
                    )
                ) {
                    Text("삭제하기", color = MaterialTheme.barrionColors.white)
                }

                // 하단 여백
                Spacer(modifier = Modifier.height(150.dp))

            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.barrionColors.primaryBlue)
                    } else {
                        Text("직원 정보를 불러올 수 없습니다.")
                    }
                }
            }
        }
    }
}