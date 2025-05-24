// feature/auth/screen/WelcomeScreen.kt
package com.example.auth.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.buttons.BarrionPrimaryButton
import com.example.ui.theme.barrionColors

/**
 * 온보딩 완료 후, 로그인 전에 표시되는 환영 화면
 */
@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        // barion 로고 텍스트
        Text(
            text = "Barion",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.barrionColors.primaryBlue,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // 로그인 버튼
        BarrionPrimaryButton(
            text = "로그인",
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}