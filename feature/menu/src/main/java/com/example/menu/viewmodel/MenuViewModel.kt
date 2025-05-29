package com.example.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.menu.AddCategoryUseCase
import com.example.domain.usecase.menu.GetMenusUseCase
import com.example.domain.usecase.menu.AddMenuUseCase
import com.example.domain.usecase.menu.DeleteCategoryUseCase
import com.example.domain.usecase.menu.DeleteMenuUseCase
import com.example.domain.usecase.menu.UpdateMenuUseCase
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
@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenusUseCase: GetMenusUseCase,
    private val addMenuUseCase: AddMenuUseCase,
    private val deleteMenuUseCase: DeleteMenuUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateMenuUseCase: UpdateMenuUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase  // 쉼표 추가
) : ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state: StateFlow<MenuState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MenuEffect>()
    val effect: SharedFlow<MenuEffect> = _effect.asSharedFlow()

    init {
        handleIntent(MenuIntent.LoadMenus)
    }

    fun handleIntent(intent: MenuIntent) {
        println("Intent 받음: $intent")
        when (intent) {
            is MenuIntent.LoadMenus -> loadMenus()
            is MenuIntent.RefreshData -> refreshData()
            is MenuIntent.NavigateToCategoryManagement -> {
                println("카테고리 관리로 이동 Intent 처리")
                navigateToCategoryManagement()
            }
            is MenuIntent.NavigateToAddMenu -> navigateToAddMenu()
            is MenuIntent.NavigateToCategoryDetail -> navigateToCategoryDetail(intent.categoryId)
            is MenuIntent.AddMenu -> addMenu(intent)
            is MenuIntent.UpdateMenu -> updateMenu(intent.menu)
            is MenuIntent.DeleteMenu -> deleteMenu(intent.menuId)
            is MenuIntent.AddCategory -> addCategory(intent.name)
            is MenuIntent.DeleteCategory -> deleteCategory(intent.categoryId)
            is MenuIntent.ClearError -> clearError()
            else -> { /* TODO: 나머지 구현 */ }
        }
    }

    /**
     * 카테고리 추가
     */
    private fun addCategory(name: String) {
        viewModelScope.launch {
            println("📁 ViewModel - 카테고리 추가 시작: $name")

            addCategoryUseCase.execute(name)
                .onSuccess { category ->
                    println("✅ ViewModel - 카테고리 추가 성공: $category")
                    _effect.emit(MenuEffect.CategoryAddedSuccessfully)
                    _effect.emit(MenuEffect.ShowToast("카테고리가 추가되었습니다"))
                    loadMenus()
                }
                .onFailure { exception ->
                    println("❌ ViewModel - 카테고리 추가 실패: ${exception.message}")
                    _effect.emit(MenuEffect.ShowError(
                        exception.message ?: "카테고리 추가 중 오류가 발생했습니다"
                    ))
                }
        }
    }

    /**
     * 카테고리 삭제
     */
    private fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            println("🗑️ ViewModel - 카테고리 삭제 시작: $categoryId")

            deleteCategoryUseCase.execute(categoryId)
                .onSuccess {
                    println("✅ ViewModel - 카테고리 삭제 성공")
                    _effect.emit(MenuEffect.CategoryDeletedSuccessfully)
                    _effect.emit(MenuEffect.ShowToast("카테고리가 삭제되었습니다"))
                    loadMenus() // 데이터 새로고침
                }
                .onFailure { exception ->
                    println("❌ ViewModel - 카테고리 삭제 실패: ${exception.message}")
                    _effect.emit(MenuEffect.ShowError(
                        exception.message ?: "카테고리 삭제 중 오류가 발생했습니다"
                    ))
                }
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
            println("📱 ViewModel - 메뉴 추가 요청")
            println("📱 메뉴 이름: ${intent.name}")
            println("📱 base64 이미지: ${intent.base64Image?.take(50) ?: "❌ NULL"}...")

            addMenuUseCase.execute(
                name = intent.name,
                price = intent.price,
                categoryId = intent.categoryId,
                description = intent.description,
                base64Image = intent.base64Image
            )
        }
    }

    /**
     * 메뉴 수정
     */
    private fun updateMenu(menu: com.example.domain.model.Menu) {
        viewModelScope.launch {
            updateMenuUseCase.execute(menu)
                .onSuccess { updatedMenu ->
                    _effect.emit(MenuEffect.MenuUpdatedSuccessfully)
                    _effect.emit(MenuEffect.ShowToast("메뉴가 수정되었습니다"))
                    loadMenus()
                }
                .onFailure { exception ->
                    _effect.emit(MenuEffect.ShowError(
                        exception.message ?: "메뉴 수정 중 오류가 발생했습니다"
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
            println("NavigateToCategoryManagement Effect 발생")
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