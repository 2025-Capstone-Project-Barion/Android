package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// 메인 블루 색상
val BluePrimary = Color(0xFF1335C9)
val BlueSecondary = Color(0xFF2142FF)
val BlueTertiary = Color(0xFF4361FF)

// 블루 변형
val BlueDark = Color(0xFF0A1A5E)
val BlueMediumDark = Color(0xFF0E2894)
val BlueMedium = Color(0xFF1335C9)
val BlueBright = Color(0xFF2142FF)
val BlueLight = Color(0xFF4361FF)
val BlueLighter = Color(0xFF637BFF)
val BluePale = Color(0xFF8395FF)
val BlueVeryPale = Color(0xFFA3AFFF)
val BlueWhite = Color(0xFFE0E3FF)

// 그레이 색상
val GrayBlack = Color(0xFF1A1A1A)
val GrayVeryDark = Color(0xFF2E2E33)
val GrayDark = Color(0xFF474752)
val GrayMediumDark = Color(0xFF5F5F70)
val GrayMedium = Color(0xFF9696A3)
val GrayMediumLight = Color(0xFFB2B2BC)
val GrayLight = Color(0xFFCDCDD5)
val GrayVeryLight = Color(0xFFE8E8EC)
val GrayWhite = Color(0xFFF4F4F6)

// 데이터 클래스로 색상 정의 (CompositionLocal 사용을 위한)
internal val BarrionColors = BarrionColor(
    // 메인 블루 색상
    primaryBlue = BluePrimary,
    secondaryBlue = BlueSecondary,
    tertiaryBlue = BlueTertiary,

    // 블루 변형
    blueDark = BlueDark,
    blueMediumDark = BlueMediumDark,
    blueMedium = BlueMedium,
    blueBright = BlueBright,
    blueLight = BlueLight,
    blueLighter = BlueLighter,
    bluePale = BluePale,
    blueVeryPale = BlueVeryPale,
    blueWhite = BlueWhite,

    // 그레이 색상
    grayBlack = GrayBlack,
    grayVeryDark = GrayVeryDark,
    grayDark = GrayDark,
    grayMediumDark = GrayMediumDark,
    grayMedium = GrayMedium,
    grayMediumLight = GrayMediumLight,
    grayLight = GrayLight,
    grayVeryLight = GrayVeryLight,
    grayWhite = GrayWhite,

    // 기능 색상 (필요시 추가)
    error = Color(0xFFFF3434),
    success = Color(0xFF4CAF50),
    warning = Color(0xFFFFC107),
    info = Color(0xFF2196F3),

    // 기타 유틸리티 색상
    white = Color(0xFFFFFFFF),
    black = Color(0xFF000000),
    transparent = Color(0x00000000),

    // 투명도 변형
    blackOverlay30 = Color(0x4D000000),
    blackOverlay60 = Color(0x99000000),
    whiteOverlay30 = Color(0x4DFFFFFF)
)

@Immutable
data class BarrionColor(
    // 메인 블루 색상
    val primaryBlue: Color,
    val secondaryBlue: Color,
    val tertiaryBlue: Color,

    // 블루 변형
    val blueDark: Color,
    val blueMediumDark: Color,
    val blueMedium: Color,
    val blueBright: Color,
    val blueLight: Color,
    val blueLighter: Color,
    val bluePale: Color,
    val blueVeryPale: Color,
    val blueWhite: Color,

    // 그레이 색상
    val grayBlack: Color,
    val grayVeryDark: Color,
    val grayDark: Color,
    val grayMediumDark: Color,
    val grayMedium: Color,
    val grayMediumLight: Color,
    val grayLight: Color,
    val grayVeryLight: Color,
    val grayWhite: Color,

    // 기능 색상
    val error: Color,
    val success: Color,
    val warning: Color,
    val info: Color,

    // 기타 유틸리티 색상
    val white: Color,
    val black: Color,
    val transparent: Color,

    // 투명도 변형
    val blackOverlay30: Color,
    val blackOverlay60: Color,
    val whiteOverlay30: Color
)

// CompositionLocal을 통해 앱 전체에서 색상에 접근할 수 있도록 설정
internal val LocalBarrionColor = staticCompositionLocalOf {
    BarrionColors
}