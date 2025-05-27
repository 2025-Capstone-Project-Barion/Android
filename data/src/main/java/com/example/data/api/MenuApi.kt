// data/api/MenuApi.kt
package com.example.data.api

import com.example.data.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 메뉴 관련 API 엔드포인트 정의
 * 메뉴 CRUD 작업과 옵션 관리를 처리하는 Retrofit 인터페이스
 */
interface MenuApi {

    /**
     * 메뉴 목록 조회 (페이지네이션 지원)
     *
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (한 페이지당 항목 수, 기본값: 100)
     * @param category 카테고리 필터 (null이면 전체 메뉴 조회)
     * @return 페이지네이션된 메뉴 목록을 담은 Response
     * API: GET /api/menus?page=0&size=100&category=1
     */
    @GET("api/menus")
    suspend fun getMenus(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 100,
        @Query("category") category: Long? = null // Int에서 Long으로 변경
    ): Response<MenuPageResponse>

    /**
     * 특정 메뉴 상세 정보 조회
     *
     * @param menuId 조회할 메뉴의 ID
     * @return 메뉴 상세 정보를 담은 Response
     * API: GET /api/menus/{menuId}
     */
    @GET("api/menus/{menuId}")
    suspend fun getMenu(
        @Path("menuId") menuId: Long // Int에서 Long으로 변경
    ): Response<MenuDto>

    /**
     * 새로운 메뉴 생성
     *
     * @param request 생성할 메뉴 정보 (이름, 가격, 설명, 카테고리, base64 이미지 등)
     * @return 생성된 메뉴 정보를 담은 Response
     * API: POST /api/menus
     */
    @POST("api/menus")
    suspend fun createMenu(
        @Body request: MenuCreateRequest
    ): Response<MenuDto>

    /**
     * 기존 메뉴 정보 수정
     *
     * @param menuId 수정할 메뉴의 ID
     * @param request 수정할 메뉴 정보 (이름, 가격, 설명, 카테고리, base64 이미지 등)
     * @return 수정된 메뉴 정보를 담은 Response
     * API: PUT /api/menus/{menuId}
     */
    @PUT("api/menus/{menuId}")
    suspend fun updateMenu(
        @Path("menuId") menuId: Long, // Int에서 Long으로 변경
        @Body request: MenuUpdateRequest
    ): Response<MenuDto>

    /**
     * 특정 메뉴 삭제
     *
     * @param menuId 삭제할 메뉴의 ID
     * @return 삭제 결과를 담은 Response (성공 시 빈 응답)
     * API: DELETE /api/menus/{menuId}
     */
    @DELETE("api/menus/{menuId}")
    suspend fun deleteMenu(
        @Path("menuId") menuId: Long // Int에서 Long으로 변경
    ): Response<Unit>

    /**
     * 특정 메뉴의 옵션 목록 조회
     *
     * @param menuId 옵션을 조회할 메뉴의 ID
     * @return 메뉴 옵션 목록을 담은 Response
     * API: GET /api/menus/{menuId}/options
     *
     * 참고: 현재는 사용하지 않지만 향후 확장을 위해 주석 처리
     */
    /*
    @GET("api/menus/{menuId}/options")
    suspend fun getMenuOptions(
        @Path("menuId") menuId: Int
    ): Response<List<MenuOptionDto>>
    */

    /**
     * 메뉴에 새로운 옵션 추가
     *
     * @param menuId 옵션을 추가할 메뉴의 ID
     * @param request 추가할 옵션 정보
     * @return 추가된 옵션 정보를 담은 Response
     * API: POST /api/menus/{menuId}/options
     *
     * 참고: 현재는 사용하지 않지만 향후 확장을 위해 주석 처리
     */
    /*
    @POST("api/menus/{menuId}/options")
    suspend fun createMenuOption(
        @Path("menuId") menuId: Int,
        @Body request: MenuOptionCreateRequest
    ): Response<MenuOptionDto>
    */
}