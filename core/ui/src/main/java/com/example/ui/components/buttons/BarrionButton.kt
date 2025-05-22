// core/ui/src/main/java/com/example/ui/components/buttons/BarrionButton.kt
package com.example.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BarrionTheme
import com.example.ui.theme.CornerRadius
import com.example.ui.theme.barrionColors

/**
 * 아웃라인 스타일 버튼 (테두리가 있는 흰색 배경)
 */
@Composable
fun BarrionOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = true
) {
    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth().height(56.dp)
    } else {
        modifier.height(56.dp)
    }

    OutlinedButton(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        shape = RoundedCornerShape(CornerRadius.XLarge),
        border = BorderStroke(1.dp, MaterialTheme.barrionColors.primaryBlue),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.barrionColors.white,
            contentColor = MaterialTheme.barrionColors.primaryBlue,
            disabledContentColor = MaterialTheme.barrionColors.primaryBlue.copy(alpha = 0.5f),
            disabledContainerColor = MaterialTheme.barrionColors.white
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
 * 주요 액션용 파란색 배경 버튼
 */
@Composable
fun BarrionPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = true
) {
    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth().height(56.dp)
    } else {
        modifier.height(56.dp)
    }

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        shape = RoundedCornerShape(CornerRadius.XLarge),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.barrionColors.primaryBlue,
            contentColor = MaterialTheme.barrionColors.white,
            disabledContainerColor = MaterialTheme.barrionColors.primaryBlue.copy(alpha = 0.5f),
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
 * 보조 액션용 회색 배경 버튼
 */
@Composable
fun BarrionSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = true
) {
    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth().height(56.dp)
    } else {
        modifier.height(56.dp)
    }

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        shape = RoundedCornerShape(CornerRadius.XLarge),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.barrionColors.grayMedium,
            contentColor = MaterialTheme.barrionColors.white,
            disabledContainerColor = MaterialTheme.barrionColors.grayMedium.copy(alpha = 0.5f),
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

// BarrionButton.kt 파일에 추가
@Preview(showBackground = true)
@Composable
fun BarrionButtonPreview() {
    BarrionTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BarrionOutlinedButton(
                text = "아웃라인 버튼",
                onClick = { }
            )

            BarrionPrimaryButton(
                text = "기본 버튼",
                onClick = { }
            )

            BarrionSecondaryButton(
                text = "보조 버튼",
                onClick = { }
            )
        }
    }
}