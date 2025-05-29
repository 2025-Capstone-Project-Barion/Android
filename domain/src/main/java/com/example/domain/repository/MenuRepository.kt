package com.example.domain.repository

import com.example.domain.model.Category
import com.example.domain.model.Menu

/**
 * 메뉴 관련 데이터 접근을 위한 Repository 인터페이스
 * - Domain 계층에서 정의, Data 계층에서 구현
 * - 비즈니스 로직은 이 인터페이스에만 의존
 */
interface MenuRepository {

    // 카테고리 관련
    suspend fun getCategories(): Result<List<Category>>
    suspend fun addCategory(name: String): Result<Category>
    suspend fun deleteCategory(categoryId: Long): Result<Unit>
    suspend fun updateCategoryOrder(categories: List<Category>): Result<Unit>

    // 메뉴 관련
    suspend fun getMenusByCategory(categoryId: Long): Result<List<Menu>>
    suspend fun getAllMenus(): Result<List<Menu>>
    suspend fun addMenu(menu: Menu, base64Image: String? = null): Result<Menu>
    suspend fun updateMenu(menu: Menu): Result<Menu>
    suspend fun deleteMenu(menuId: Long): Result<Unit>
    suspend fun updateMenuWithImage(menu: Menu, base64Image: String): Result<Menu> // 추가
}