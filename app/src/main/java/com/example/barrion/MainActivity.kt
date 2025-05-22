// app/src/main/java/com/example/barrion/MainActivity.kt

package com.example.barrion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.presentation.splash.SplashViewModel
import androidx.navigation.compose.rememberNavController
import com.example.barrion.navigation.BarrionNavHost
import com.example.ui.theme.BarrionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 앱의 메인 액티비티
 * 앱의 시작점이며 Compose 기반 UI를 설정합니다.
 *
 * @AndroidEntryPoint 어노테이션은 Hilt의 의존성 주입을 이 액티비티에 적용합니다.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Hilt를 통해 SplashViewModel 의존성 주입
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 스플래시 스크린 설정 - Android 12+ 스플래시 화면 API 사용
        val splashScreen = installSplashScreen()

        // 뷰모델의 초기화 상태에 따라 스플래시 화면 표시 시간 조절
        // isInitialized.value가 true가 될 때까지 스플래시 화면 유지
        splashScreen.setKeepOnScreenCondition {
            !viewModel.isInitialized.value
        }

        super.onCreate(savedInstanceState)

        // 엣지투엣지 디스플레이 활성화 - 전체 화면 사용
        enableEdgeToEdge()

        // 앱 초기화 상태 관찰 및 처리
        lifecycleScope.launch {
            // 뷰모델의 초기화 상태를 관찰하여 필요한 작업 수행
            viewModel.isInitialized.collectLatest { isInitialized ->
                if (isInitialized) {
                    // 초기화가 완료된 후 수행할 작업
                    // 예: 로그인 상태 확인, 데이터 프리로드 등
                }
            }
        }

        // Compose UI 설정
        setContent {
            // 앱의 테마 적용
            BarrionTheme {
                // 스캐폴드를 사용하여 기본 레이아웃 구성
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // 네비게이션 컨트롤러 생성
                        val navController = rememberNavController()

                        // 앱의 메인 네비게이션 설정
                        BarrionNavHost(navController = navController)
                    }
                }
            }
        }
    }
}