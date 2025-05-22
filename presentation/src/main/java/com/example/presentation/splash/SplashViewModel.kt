package com.example.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 스플래시 화면 초기화를 관리하는 ViewModel
 */
class SplashViewModel @Inject constructor(
    // 필요한 레포지토리나 유스케이스 의존성 주입
) : ViewModel() {

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            // 1. 필요한 초기화 작업 수행
            // 예: 사용자 인증 상태 확인, 데이터 초기화 등

            // 2. 최소 스플래시 화면 표시 시간 보장
            delay(5000) // 1.5초 지연

            // 3. 초기화 완료
            _isInitialized.value = true
        }
    }
}