// core/ui/src/main/java/com/example/ui/components/buttons/BarrionActionButtons.kt
package com.example.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.barrionColors

/**
 * 장 버튼 - 전체 너비를 차지하는 기본 액션 버튼
 *
 * @param text 버튼에 표시할 텍스트
 * @param onClick 버튼 클릭 시 실행될 콜백
 * @param modifier 추가 Modifier (기본 크기: 너비 260dp, 높이 40dp)
 * @param enabled 버튼 활성화 여부
 */
@Composable
fun BarrionFullButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(260.dp)
            .height(40.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.barrionColors.primaryBlue,
            contentColor = MaterialTheme.barrionColors.white,
            disabledContainerColor = MaterialTheme.barrionColors.grayMedium,
            disabledContentColor = MaterialTheme.barrionColors.white
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 단 버튼 - 좌/우 버튼 쌍으로 구성된 네비게이션 버튼
 *
 * @param leftText 왼쪽 버튼에 표시할 텍스트
 * @param rightText 오른쪽 버튼에 표시할 텍스트
 * @param onLeftClick 왼쪽 버튼 클릭 시 실행될 콜백
 * @param onRightClick 오른쪽 버튼 클릭 시 실행될 콜백
 * @param rightEnabled 오른쪽 버튼 활성화 여부 (특정 조건 충족 여부)
 * @param modifier 추가 Modifier
 */
@Composable
fun BarrionNavigationButtons(
    leftText: String,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    rightEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        // 왼쪽 버튼 (아웃라인 스타일)
        OutlinedButton(
            onClick = onLeftClick,
            modifier = Modifier
                .weight(1f)
                .height(49.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, MaterialTheme.barrionColors.primaryBlue),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.barrionColors.white,
                contentColor = MaterialTheme.barrionColors.primaryBlue
            )
        ) {
            Text(
                text = leftText,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }

        // 오른쪽 버튼 (채워진 스타일)
        Button(
            onClick = onRightClick,
            modifier = Modifier
                .weight(1f)
                .height(49.dp),
            enabled = rightEnabled,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.barrionColors.primaryBlue,
                contentColor = MaterialTheme.barrionColors.white,
                disabledContainerColor = MaterialTheme.barrionColors.grayMedium,
                disabledContentColor = MaterialTheme.barrionColors.white
            )
        ) {
            Text(
                text = rightText,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 단일 아웃라인 버튼 - 네비게이션 버튼 쌍의 왼쪽 스타일과 동일한 단일 버튼
 *
 * @param text 버튼에 표시할 텍스트
 * @param onClick 버튼 클릭 시 실행될 콜백
 * @param modifier 추가 Modifier
 * @param enabled 버튼 활성화 여부
 */
@Composable
fun BarrionOutlineActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .width(157.dp)
            .height(49.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.barrionColors.primaryBlue),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.barrionColors.white,
            contentColor = MaterialTheme.barrionColors.primaryBlue,
            disabledContainerColor = MaterialTheme.barrionColors.white,
            disabledContentColor = MaterialTheme.barrionColors.primaryBlue.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 단일 필드 버튼 - 네비게이션 버튼 쌍의 오른쪽 스타일과 동일한 단일 버튼
 *
 * @param text 버튼에 표시할 텍스트
 * @param onClick 버튼 클릭 시 실행될 콜백
 * @param modifier 추가 Modifier
 * @param enabled 버튼 활성화 여부
 */
@Composable
fun BarrionFilledActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(157.dp)
            .height(49.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.barrionColors.primaryBlue,
            contentColor = MaterialTheme.barrionColors.white,
            disabledContainerColor = MaterialTheme.barrionColors.grayMedium,
            disabledContentColor = MaterialTheme.barrionColors.white
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 알림 다이얼로그 버튼 쌍 - 이미지 5와 같은 다이얼로그에서 사용되는 버튼 쌍
 *
 * @param leftText 왼쪽 버튼에 표시할 텍스트 (예: "아니오")
 * @param rightText 오른쪽 버튼에 표시할 텍스트 (예: "예")
 * @param onLeftClick 왼쪽 버튼 클릭 시 실행될 콜백
 * @param onRightClick 오른쪽 버튼 클릭 시 실행될 콜백
 * @param modifier 추가 Modifier
 */
@Composable
fun BarrionDialogButtons(
    leftText: String,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        // 왼쪽 버튼 (아웃라인 스타일)
        OutlinedButton(
            onClick = onLeftClick,
            modifier = Modifier
                .weight(1f)
                .height(49.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, MaterialTheme.barrionColors.primaryBlue),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.barrionColors.white,
                contentColor = MaterialTheme.barrionColors.primaryBlue
            )
        ) {
            Text(
                text = leftText,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }

        // 오른쪽 버튼 (채워진 스타일)
        Button(
            onClick = onRightClick,
            modifier = Modifier
                .weight(1f)
                .height(49.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.barrionColors.primaryBlue,
                contentColor = MaterialTheme.barrionColors.white
            )
        ) {
            Text(
                text = rightText,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}