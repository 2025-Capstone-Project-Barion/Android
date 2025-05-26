package com.example.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.menu.GetMenusUseCase
import com.example.domain.usecase.menu.AddMenuUseCase
import com.example.domain.usecase.menu.DeleteMenuUseCase
import com.example.menu.type.MenuIntent
import com.example.menu.type.MenuState
import com.example.menu.type.MenuEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 메뉴 화면의 ViewModel - MVI 패턴 구현
 */
//class MenuViewModel(
//    private val getMenusUseCase: GetMenusUseCase,
//    private val addMenuUseCase: AddMenuUseCase,
//    private val deleteMenuUseCase: DeleteMenuUseCase
//) : ViewModel() {

@HiltViewModel //Hilt 추가
class MenuViewModel @Inject constructor(
    private val getMenusUseCase: GetMenusUseCase,
    private val addMenuUseCase: AddMenuUseCase,
    private val deleteMenuUseCase: DeleteMenuUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state: StateFlow<MenuState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MenuEffect>()
    val effect: SharedFlow<MenuEffect> = _effect.asSharedFlow()

    init {
        handleIntent(MenuIntent.LoadMenus)
    }

    fun handleIntent(intent: MenuIntent) {
        when (intent) {
            is MenuIntent.LoadMenus -> loadMenus()
            is MenuIntent.RefreshData -> refreshData()
            is MenuIntent.NavigateToCategoryManagement -> navigateToCategoryManagement()
            is MenuIntent.NavigateToAddMenu -> navigateToAddMenu()
            is MenuIntent.NavigateToCategoryDetail -> navigateToCategoryDetail(intent.categoryId)
            is MenuIntent.AddMenu -> addMenu(intent)
            is MenuIntent.DeleteMenu -> deleteMenu(intent.menuId)
            is MenuIntent.ClearError -> clearError()
            else -> { /* TODO: 나머지 구현 */ }
        }
    }

    private fun loadMenus() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            getMenusUseCase.execute()
                .onSuccess { (categories, menusByCategory) ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        categories = categories,
                        menusByCategory = menusByCategory
                    )
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "데이터 로드 중 오류가 발생했습니다"
                    )
                }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)

            getMenusUseCase.execute()
                .onSuccess { (categories, menusByCategory) ->
                    _state.value = _state.value.copy(
                        isRefreshing = false,
                        categories = categories,
                        menusByCategory = menusByCategory
                    )
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        isRefreshing = false,
                        error = exception.message ?: "새로고침 중 오류가 발생했습니다"
                    )
                }
        }
    }

    private fun addMenu(intent: MenuIntent.AddMenu) {
        viewModelScope.launch {
            addMenuUseCase.execute(
                name = intent.name,
                price = intent.price,
                categoryId = intent.categoryId,
                description = intent.description,
                imageUrl = intent.imageUrl
            )
                .onSuccess {
                    _effect.emit(MenuEffect.MenuAddedSuccessfully)
                    loadMenus()
                }
                .onFailure { exception ->
                    _effect.emit(MenuEffect.ShowError(
                        exception.message ?: "메뉴 추가 중 오류가 발생했습니다"
                    ))
                }
        }
    }

    private fun deleteMenu(menuId: Long) {
        viewModelScope.launch {
            deleteMenuUseCase.execute(menuId)
                .onSuccess {
                    _effect.emit(MenuEffect.MenuDeletedSuccessfully)
                    loadMenus()
                }
                .onFailure { exception ->
                    _effect.emit(MenuEffect.ShowError(
                        exception.message ?: "메뉴 삭제 중 오류가 발생했습니다"
                    ))
                }
        }
    }

    private fun navigateToCategoryManagement() {
        viewModelScope.launch {
            _effect.emit(MenuEffect.NavigateToCategoryManagement)
        }
    }

    private fun navigateToAddMenu() {
        viewModelScope.launch {
            _effect.emit(MenuEffect.NavigateToAddMenu)
        }
    }

    private fun navigateToCategoryDetail(categoryId: Long) {
        viewModelScope.launch {
            val categoryName = _state.value.categories
                .find { it.id == categoryId }?.name ?: ""

            _effect.emit(MenuEffect.NavigateToCategoryDetail(categoryId, categoryName))
        }
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
