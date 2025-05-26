package com.example.domain.usecase.menu

import com.example.domain.repository.MenuRepository
import javax.inject.Inject

/**
 * 메뉴 삭제 UseCase
 * - 메뉴 삭제 시 필요한 비즈니스 로직 처리
 * - 삭제 전 검증 로직 포함
 */

// 다른 UseCase 코드와 동일
class DeleteMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {

    /**
     * 메뉴를 삭제
     * @param menuId 삭제할 메뉴 ID
     * @return 삭제 결과
     */
    suspend fun execute(menuId: Long): Result<Unit> {

        // 1. 입력 데이터 검증
        if (menuId <= 0) {
            return Result.failure(IllegalArgumentException("올바른 메뉴 ID가 아닙니다"))
        }

        // 2. 메뉴 삭제 실행
        return try {
            menuRepository.deleteMenu(menuId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}