// feature/auth/type/AuthTypes.kt
package com.example.auth.type

/**
 * 로그인 UI 상태
 */
data class LoginUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)

/**
 * 인증 결과
 */
sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

/**
 * 로그인 요청 데이터
 */
data class LoginRequest(
    val code: String
)

/**
 * 로그인 응답 데이터
 */
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userInfo: UserInfo
)

/**
 * 사용자 정보
 */
data class UserInfo(
    val id: String,
    val name: String,
    val role: String
)