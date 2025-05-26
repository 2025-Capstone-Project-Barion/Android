package com.example.domain.usecase.menu

import com.example.domain.model.Category
import com.example.domain.model.Menu
import com.example.domain.repository.MenuRepository
import javax.inject.Inject

/**
 * 메뉴 조회 UseCase
 * - 카테고리별로 메뉴를 조회하는 비즈니스 로직
 * - UI에서 필요한 형태로 데이터를 가공
 */

// 일반 생성자 주입. DI 프레임워크 (예: Hilt, Dagger 등) 없이 수동으로 객체를 생성해서 주입해야 합니다.
//class GetMenusUseCase(
//    private val menuRepository: MenuRepository
//) {

// @Inject 어노테이션으로 의존성 주입이 자동화됨.
// Hilt나 Dagger 등 DI 프레임워크에서 이 클래스를 주입 대상으로 인식하게 됩니다.

class GetMenusUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {

    /**
     * 모든 카테고리와 각 카테고리별 메뉴들을 조회
     * @return Pair<카테고리 리스트, 카테고리별 메뉴 맵>
     */
    suspend fun execute(): Result<Pair<List<Category>, Map<Long, List<Menu>>>> {
        return try {
            // 1. 모든 카테고리 조회
            val categoriesResult: Result<List<Category>> = menuRepository.getCategories()
            if (categoriesResult.isFailure) {
                return Result.failure(categoriesResult.exceptionOrNull()!!)
            }

            // 2. 모든 메뉴 조회
            val menusResult: Result<List<Menu>> = menuRepository.getAllMenus()
            if (menusResult.isFailure) {
                return Result.failure(menusResult.exceptionOrNull()!!)
            }

            val categories: List<Category> = categoriesResult.getOrThrow().sortedBy { it.order }
            val allMenus: List<Menu> = menusResult.getOrThrow()

            // 3. 카테고리별로 메뉴 그룹화
            val menusByCategory: Map<Long, List<Menu>> = allMenus.groupBy { it.categoryId }

            Result.success(Pair(categories, menusByCategory))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
