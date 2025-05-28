// :feature:staff/src/main/java/com/example/staff/component/StaffDetailItem.kt
package com.example.staff.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.Spacing
import com.example.ui.theme.barrionColors
import java.text.NumberFormat
import java.util.*

/**
 * 직원 상세 정보 아이템
 * 이미지 디자인과 동일한 레이아웃: 아이콘 + 라벨 + 값
 */
@Composable
fun StaffDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        // 아이콘 (선택적)
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.barrionColors.grayMedium,
                modifier = Modifier.size(20.dp)
            )
        }

        // 라벨과 값을 세로로 배치
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // 라벨 (작은 회색 텍스트)
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.barrionColors.grayMedium,
                    fontWeight = FontWeight.Normal
                )
            )

            // 값 (큰 검은 텍스트)
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.barrionColors.grayBlack,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

/**
 * 아이콘이 포함된 직원 상세 정보 아이템들
 * 각 정보 타입에 맞는 아이콘을 자동으로 설정
 */
@Composable
fun StaffDetailItemWithIcon(
    label: String,
    value: String,
    type: StaffDetailType,
    modifier: Modifier = Modifier
) {
    val icon = when (type) {
        StaffDetailType.PHONE -> Icons.Outlined.Phone
        StaffDetailType.SALARY -> Icons.Outlined.Schedule
        StaffDetailType.POSITION -> Icons.Outlined.Work
        StaffDetailType.ACCOUNT -> Icons.Outlined.AccountBalance
        StaffDetailType.GENERAL -> null
    }

    StaffDetailItem(
        label = label,
        value = value,
        icon = icon,
        modifier = modifier
    )
}

/**
 * 직원 상세 정보 타입 정의
 */
enum class StaffDetailType {
    PHONE,      // 전화번호
    SALARY,     // 시급
    POSITION,   // 직무
    ACCOUNT,    // 계좌번호
    GENERAL     // 일반 정보
}

/**
 * 시급을 원화 형식으로 포맷팅 (12,500원)
 */
fun formatHourlyWage(wage: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${formatter.format(wage)}원"
}

/**
 * 전화번호를 010-xxxx-xxxx 형식으로 포맷팅
 * 함수명을 formatDetailPhoneNumber로 변경하여 중복 방지
 */
fun formatDetailPhoneNumber(phoneNumber: String): String {
    return when {
        phoneNumber.length == 11 && phoneNumber.startsWith("010") -> {
            "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3, 7)}-${phoneNumber.substring(7)}"
        }
        phoneNumber.contains("-") -> phoneNumber
        else -> phoneNumber
    }
}

/**
 * 편의 함수들 - 자주 사용되는 직원 정보 표시
 */
@Composable
fun StaffPhoneItem(phoneNumber: String, modifier: Modifier = Modifier) {
    StaffDetailItemWithIcon(
        label = "전화번호",
        value = formatDetailPhoneNumber(phoneNumber),
        type = StaffDetailType.PHONE,
        modifier = modifier
    )
}

@Composable
fun StaffSalaryItem(hourlyWage: Int, modifier: Modifier = Modifier) {
    StaffDetailItemWithIcon(
        label = "시급",
        value = formatHourlyWage(hourlyWage),
        type = StaffDetailType.SALARY,
        modifier = modifier
    )
}

@Composable
fun StaffPositionItem(position: String, modifier: Modifier = Modifier) {
    StaffDetailItemWithIcon(
        label = "직무",
        value = position,
        type = StaffDetailType.POSITION,
        modifier = modifier
    )
}

@Composable
fun StaffAccountItem(bankName: String, accountNumber: String, modifier: Modifier = Modifier) {
    StaffDetailItemWithIcon(
        label = "계좌번호",
        value = "$bankName $accountNumber",
        type = StaffDetailType.ACCOUNT,
        modifier = modifier
    )
}