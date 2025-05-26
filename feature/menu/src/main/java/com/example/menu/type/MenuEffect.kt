package com.example.menu.type

/**
 * 메뉴 화면에서 발생하는 일회성 이벤트들
 * - MVI 패턴에서 Side Effect를 나타내는 sealed class
 * - 네비게이션, 토스트 메시지, 다이얼로그 등 일회성 액션
 */
sealed interface MenuEffect {

    // 네비게이션 관련
    object NavigateToCategoryManagement : MenuEffect
    object NavigateToAddMenu : MenuEffect
    data class NavigateToCategoryDetail(val categoryId: Long, val categoryName: String) : MenuEffect
    data class NavigateToEditMenu(val menuId: Long) : MenuEffect

    // 메시지 표시
    data class ShowToast(val message: String) : MenuEffect
    data class ShowError(val error: String) : MenuEffect

    // 다이얼로그 관련
    data class ShowDeleteMenuDialog(val menuId: Long, val menuName: String) : MenuEffect
    data class ShowDeleteCategoryDialog(val categoryId: Long, val categoryName: String) : MenuEffect

    // 성공 메시지
    object MenuAddedSuccessfully : MenuEffect
    object MenuUpdatedSuccessfully : MenuEffect
    object MenuDeletedSuccessfully : MenuEffect
    object CategoryAddedSuccessfully : MenuEffect
    object CategoryDeletedSuccessfully : MenuEffect
}