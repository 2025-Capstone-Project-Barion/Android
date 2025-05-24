// feature/auth/screen/LoginViewModel.kt
package com.example.auth.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.AuthRepository
import com.example.auth.type.AuthResult
import com.example.auth.type.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그인 화면 뷰모델
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess.asSharedFlow()

    /**
     * 코드 업데이트
     */
    fun updateCode(code: String) {
        _uiState.update { currentState ->
            currentState.copy(
                code = code,
                isError = false, // 새로운 입력 시 에러 상태 초기화
                errorMessage = ""
            )
        }
    }

    /**
     * 로그인 시도
     */
    fun login() {
        val currentCode = _uiState.value.code
        if (currentCode.length != 4) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            when (val result = authRepository.login(currentCode)) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, isError = false)
                    }
                    _loginSuccess.emit(true)
                }
                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}