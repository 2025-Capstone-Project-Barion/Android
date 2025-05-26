package com.example.data.repository

import com.example.domain.model.Menu
import com.example.domain.model.Category
import com.example.domain.repository.MenuRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MenuRepository 구현체
 * - 실제 데이터 소스(API, 로컬DB)와 연동
 * - 현재는 임시 데이터로 구현, 추후 실제 API 연동
 */
@Singleton
class MenuRepositoryImpl @Inject constructor(
    // TODO: 추후 API 서비스 주입
    // private val menuApi: MenuApi,
    // private val menuDao: MenuDao
) : MenuRepository {

    // 임시 데이터 (추후 제거)
    private val tempCategories = listOf(
        Category(id = 1, name = "추천", order = 1, isDefault = true, menuCount = 2),
        Category(id = 2, name = "커피", order = 2, isDefault = false, menuCount = 4),
        Category(id = 3, name = "논커피", order = 3, isDefault = false, menuCount = 3),
        Category(id = 4, name = "디저트", order = 4, isDefault = false, menuCount = 2)
    )

    private val tempMenus = mutableListOf(
        Menu(id = 1, name = "시그니처 커피", price = 6000, categoryId = 1, description = "우리 카페만의 특별한 블렌드"),
        Menu(id = 2, name = "베스트 라떼", price = 5500, categoryId = 1, description = "가장 인기있는 시즌 라떼"),
        Menu(id = 3, name = "아메리카노", price = 4000, categoryId = 2, description = "진한 에스프레소"),
        Menu(id = 4, name = "카푸치노", price = 4500, categoryId = 2, description = "부드러운 거품"),
        Menu(id = 5, name = "카페라떼", price = 4500, categoryId = 2, description = "부드러운 우유"),
        Menu(id = 6, name = "바닐라라떼", price = 5000, categoryId = 2, description = "달콤한 바닐라")
    )

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            // TODO: 실제 API 호출
            // val response = menuApi.getCategories()
            Result.success(tempCategories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addCategory(name: String): Result<Category> {
        return try {
            // TODO: 실제 API 호출
            val newCategory = Category(
                id = tempCategories.size + 1L,
                name = name,
                order = tempCategories.size + 1,
                isDefault = false,
                menuCount = 0
            )
            Result.success(newCategory)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(categoryId: Long): Result<Unit> {
        return try {
            // TODO: 실제 API 호출
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCategoryOrder(categories: List<Category>): Result<Unit> {
        return try {
            // TODO: 실제 API 호출
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMenusByCategory(categoryId: Long): Result<List<Menu>> {
        return try {
            // TODO: 실제 API 호출
            val menus = tempMenus.filter { it.categoryId == categoryId }
            Result.success(menus)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllMenus(): Result<List<Menu>> {
        return try {
            // TODO: 실제 API 호출
            Result.success(tempMenus.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addMenu(menu: Menu): Result<Menu> {
        return try {
            // TODO: 실제 API 호출
            val newMenu = menu.copy(id = tempMenus.size + 1L)
            tempMenus.add(newMenu)
            Result.success(newMenu)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMenu(menu: Menu): Result<Menu> {
        return try {
            // TODO: 실제 API 호출
            val index = tempMenus.indexOfFirst { it.id == menu.id }
            if (index != -1) {
                tempMenus[index] = menu
                Result.success(menu)
            } else {
                Result.failure(IllegalArgumentException("메뉴를 찾을 수 없습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMenu(menuId: Long): Result<Unit> {
        return try {
            // TODO: 실제 API 호출
            val menuToRemove = tempMenus.find { it.id == menuId }
            if (menuToRemove != null) {
                tempMenus.remove(menuToRemove)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}