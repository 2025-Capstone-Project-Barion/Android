package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 라이트 테마를 위한 색상 스키마 정의
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = BlueTertiary,

    // 배경 및 표면 색상
    background = GrayVeryLight,
    surface = GrayWhite,
    surfaceVariant = GrayLight,

    // 텍스트 및 아이콘 색상
    onPrimary = GrayWhite,  // 기본 색상 위 텍스트/아이콘
    onSecondary = GrayWhite,
    onTertiary = GrayWhite,
    onBackground = GrayBlack,  // 배경 위 텍스트/아이콘
    onSurface = GrayBlack,
    onSurfaceVariant = GrayDark,

    // 기타 색상
    error = Color(0xFFFF3434),
    onError = GrayWhite
)

// 다크 테마를 위한 색상 스키마 정의
private val DarkColorScheme = darkColorScheme(
    primary = BlueBright,  // 어두운 배경에서는 더 밝은 블루
    secondary = BlueLight,
    tertiary = BlueLighter,

    // 배경 및 표면 색상
    background = GrayBlack,
    surface = GrayVeryDark,
    surfaceVariant = GrayDark,

    // 텍스트 및 아이콘 색상
    onPrimary = GrayWhite,
    onSecondary = GrayWhite,
    onTertiary = GrayWhite,
    onBackground = GrayWhite,
    onSurface = GrayWhite,
    onSurfaceVariant = GrayMediumLight,

    // 기타 색상
    error = Color(0xFFFF5252),
    onError = GrayWhite
)

@Composable
fun BarrionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,  // 커스텀 색상을 우선하기 위해 기본값 false로 변경
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 상태 표시줄 색상 설정
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 상태 표시줄 배경색을 하얀색으로 설정
            window.statusBarColor = Color.White.toArgb()

            // 상태 표시줄 아이콘 색상 설정 (라이트 테마에서는 어두운 아이콘)
            // 다크 테마에서는 밝은 아이콘을, 라이트 테마에서는 어두운 아이콘을 사용
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // 커스텀 색상 시스템을 앱 전체에 제공
    CompositionLocalProvider(
        LocalBarrionColor provides BarrionColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}

// 커스텀 색상에 쉽게 접근하기 위한 확장 속성
val MaterialTheme.barrionColors: BarrionColor
    @Composable
    get() = LocalBarrionColor.current