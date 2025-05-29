package com.example.sales.type

// feature/sales/type/SalesEffect.kt

/**
 * Sales 화면의 부수 효과 (Effect)
 */
sealed class SalesEffect {
    /**
     * 에러 메시지 표시
     */
    data class ShowError(val message: String) : SalesEffect()

    /**
     * 성공 메시지 표시
     */
    data class ShowSuccess(val message: String) : SalesEffect()
}