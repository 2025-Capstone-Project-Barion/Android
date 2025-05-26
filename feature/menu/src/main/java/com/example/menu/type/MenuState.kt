package com.example.menu.type

import com.example.domain.model.Menu
import com.example.domain.model.Category

/**
 * 메뉴 화면의 UI 상태를 나타내는 데이터 클래스
 * - MVI 패턴에서 화면에 표시될 모든 상태 정보 포함
 */
data class MenuState(
    val isLoading: Boolean = false,                           // 로딩 상태
    val categories: List<Category> = emptyList(),             // 카테고리 목록
    val menusByCategory: Map<Long, List<Menu>> = emptyMap(),  // 카테고리별 메뉴 맵
    val error: String? = null,                                // 에러 메시지
    val isRefreshing: Boolean = false                         // 새로고침 상태
) {

    /**
     * 특정 카테고리의 메뉴 목록 조회
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리의 메뉴 리스트 (없으면 빈 리스트)
     */
    fun getMenusForCategory(categoryId: Long): List<Menu> {
        return menusByCategory[categoryId] ?: emptyList()
    }

    /**
     * 메뉴가 있는 카테고리만 필터링
     * @return 메뉴가 1개 이상 있는 카테고리 리스트
     */
    fun getCategoriesWithMenus(): List<Category> {
        return categories.filter { category ->
            getMenusForCategory(category.id).isNotEmpty()
        }
    }
}