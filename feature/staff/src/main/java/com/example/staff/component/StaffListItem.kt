// :feature:staff/src/main/java/com/example/staff/component/StaffListItem.kt
package com.example.staff.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.Staff
import com.example.ui.theme.CornerRadius
import com.example.ui.theme.Spacing
import com.example.ui.theme.barrionColors

/**
 * 직원 목록 아이템 카드
 * 이미지 디자인과 동일한 레이아웃: 아바타 + 이름/전화번호 + 직무 태그
 */
@Composable
fun StaffListItem(
    staff: Staff,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(CornerRadius.Large),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.barrionColors.white
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            // 아바타 (이름 첫 글자)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.barrionColors.blueVeryPale),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = staff.name.firstOrNull()?.toString() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.barrionColors.primaryBlue
                )
            }

            // 직원 정보 (이름, 전화번호)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = staff.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.barrionColors.grayBlack
                )
                Text(
                    text = formatStaffPhoneNumber(staff.phoneNumber),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.barrionColors.grayMedium
                )
            }

            // 직무 태그 (이미지와 동일한 스타일)
            Surface(
                shape = RoundedCornerShape(CornerRadius.Medium),
                color = MaterialTheme.barrionColors.blueWhite,
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = staff.position.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.barrionColors.primaryBlue,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
                )
            }
        }
    }
}

/**
 * 전화번호를 010-xxxx-xxxx 형식으로 포맷팅
 * 함수명을 formatStaffPhoneNumber로 변경하여 중복 방지
 */
private fun formatStaffPhoneNumber(phoneNumber: String): String {
    return when {
        phoneNumber.length == 11 && phoneNumber.startsWith("010") -> {
            "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3, 7)}-${phoneNumber.substring(7)}"
        }
        phoneNumber.contains("-") -> phoneNumber // 이미 포맷된 경우
        else -> phoneNumber // 기타 경우 원본 반환
    }
}