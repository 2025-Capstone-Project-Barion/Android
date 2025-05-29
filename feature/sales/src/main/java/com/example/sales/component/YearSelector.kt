package com.example.sales.component

import com.example.ui.theme.BarrionTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.barrionColors


/**
 * 연도 선택기 컴포넌트
 */
@Composable
fun YearSelector(
    selectedYear: Int,
    onYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minYear: Int = 2020,
    maxYear: Int = 2030
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이전 연도 버튼
        IconButton(
            onClick = {
                if (selectedYear > minYear) {
                    onYearChange(selectedYear - 1)
                }
            },
            enabled = selectedYear > minYear
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "이전 연도",
                tint = if (selectedYear > minYear) {
                    MaterialTheme.barrionColors.grayBlack
                } else {
                    MaterialTheme.barrionColors.grayMedium
                }
            )
        }

        // 연도 표시
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.barrionColors.primaryBlue)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${selectedYear}년",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.barrionColors.white
            )
        }

        // 다음 연도 버튼
        IconButton(
            onClick = {
                if (selectedYear < maxYear) {
                    onYearChange(selectedYear + 1)
                }
            },
            enabled = selectedYear < maxYear
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "다음 연도",
                tint = if (selectedYear < maxYear) {
                    MaterialTheme.barrionColors.grayBlack
                } else {
                    MaterialTheme.barrionColors.grayMedium
                }
            )
        }
    }
}



@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun YearSelectorPreview() {
    BarrionTheme {
        YearSelector(
            selectedYear = 2025,
            onYearChange = { }
        )
    }
}