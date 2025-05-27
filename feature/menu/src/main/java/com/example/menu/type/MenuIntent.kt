package com.example.menu.type

/**
 * 메뉴 화면에서 발생하는 모든 사용자 액션들
 */
sealed interface MenuIntent {

    // 데이터 로드
    object LoadMenus : MenuIntent
    object RefreshData : MenuIntent

    // 네비게이션 관련
    object NavigateToCategoryManagement : MenuIntent
    object NavigateToAddMenu : MenuIntent
    data class NavigateToCategoryDetail(val categoryId: Long) : MenuIntent

    // 메뉴 관련 액션
    data class AddMenu(
        val name: String,
        val price: Int,
        val categoryId: Long,
        val description: String = "",
        val imageUrl: String = "",
        val base64Image: String? = null  // 추가
    ) : MenuIntent

    data class UpdateMenu(val menu: com.example.domain.model.Menu) : MenuIntent
    data class DeleteMenu(val menuId: Long) : MenuIntent

    // 카테고리 관련 액션
    data class AddCategory(val name: String) : MenuIntent
    data class DeleteCategory(val categoryId: Long) : MenuIntent

    // UI 상태 변경
    object ClearError : MenuIntent
}