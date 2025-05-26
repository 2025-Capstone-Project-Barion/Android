// feature/menu/src/main/java/com/barrion/feature/menu/viewmodel/MenuViewModel.kt
package com.example.menu.viewmodel

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menu.type.MenuEffect
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * MVI Pattern ViewModel
 * Intent를 받아서 State를 변경하고 Effect를 발생시킴
 */

class MenuViewModel : ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state: StateFlow<MenuState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MenuEffect>()
    val effect: SharedFlow<MenuEffect> = _effect.asSharedFlow()

    init {
        handleIntent(MenuIntent.LoadMenuData)
    }

    fun handleIntent(intent: MenuIntent) {
        when (intent) {
            is MenuIntent.LoadMenuData -> loadMenuData()
            is MenuIntent.RefreshMenuData -> refreshMenuData()
            is MenuIntent.ClearError -> clearError()
        }
    }

    private fun loadMenuData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // 임시 로딩 시뮬레이션
                kotlinx.coroutines.delay(1000)
                _state.value = _state.value.copy(isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "메뉴 데이터를 불러올 수 없습니다"
                )
            }
        }
    }

    private fun refreshMenuData() = loadMenuData()

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}