package com.example.data.repository

import android.util.Log
import com.example.data.api.CategoryApi
import com.example.data.api.MenuApi
import com.example.data.dto.CategoryCreateRequest
import com.example.data.dto.CategoryDto
import com.example.data.mapper.toDomain
import com.example.data.mapper.toCategoryDomainList
import com.example.data.mapper.toMenuDomainList
import com.example.data.mapper.toCreateRequest
import com.example.data.mapper.toUpdateRequest
import com.example.domain.model.Menu
import com.example.domain.model.Category
import com.example.domain.repository.MenuRepository
import javax.inject.Inject
import javax.inject.Singleton
import com.example.data.dto.MenuPageResponse
import com.example.data.dto.MenuDto
import kotlinx.serialization.json.Json
import retrofit2.Response
// 필요한 import도 추가
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
// 필요한 import 추가
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit
/**
 * MenuRepository 구현체
 * - 실제 API와 연동하여 메뉴/카테고리 데이터 관리
 * - 기존 임시 데이터에서 실제 API 호출로 변경
 */
@Singleton
class MenuRepositoryImpl @Inject constructor(
    // 실제 API 서비스 주입
    private val menuApi: MenuApi,
    private val categoryApi: CategoryApi
) : MenuRepository {

    companion object {
        private const val TAG = "MenuRepositoryImpl"
    }

    /**
     * 모든 카테고리 조회
     * 기존: 임시 데이터 반환 → 변경: 실제 API 호출
     */
    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            Log.d(TAG, "🔄 카테고리 목록 조회 시작")

            // 실제 API 호출
            val response = categoryApi.getCategories()
            Log.d(TAG, "📡 카테고리 API 응답 코드: ${response.code()}")
            Log.d(TAG, "📡 카테고리 API 응답 성공 여부: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "✅ 카테고리 응답 성공: ${body?.size}개 항목")
                Log.d(TAG, "📦 원본 카테고리 데이터: $body")

                val categories = body?.toCategoryDomainList() ?: emptyList()
                Log.d(TAG, "🔄 도메인 변환 완료: ${categories.size}개")
                Log.d(TAG, "🏆 최종 카테고리 데이터: $categories")

                Result.success(categories)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = "카테고리 조회 실패: ${response.code()} - ${response.message()}"
                Log.e(TAG, "❌ $error")
                Log.e(TAG, "❌ 에러 바디: $errorBody")
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            val error = "카테고리 조회 중 네트워크 오류: ${e.message}"
            Log.e(TAG, "💥 $error", e)
            Result.failure(Exception(error))
        }
    }

    /**
     * 새 카테고리 추가
     * 기존: 임시 카테고리 생성 → 변경: 실제 API 호출
     */
    /**
     * 새 카테고리 추가
     * 기존: 임시 카테고리 생성 → 변경: 실제 API 호출
     */
    override suspend fun addCategory(name: String): Result<Category> {
        return try {
            Log.d(TAG, "📁 카테고리 추가 (ID 포함): $name")

            // 먼저 기존 카테고리들을 조회해서 다음 ID 계산
            val existingCategories = getCategories().getOrElse { emptyList() }
            val nextId = if (existingCategories.isNotEmpty()) {
                existingCategories.maxOf { it.id } + 1
            } else {
                1L
            }

            val tempCategory = Category(
                id = nextId,  // 계산된 다음 ID 사용
                name = name,
                order = 999,
                isDefault = false,
                menuCount = 0
            )

            val request = tempCategory.toCreateRequest()
            Log.d(TAG, "📁 요청 데이터 (ID 포함): $request")

            val client = OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val json = """{"categoryId":${nextId},"categoryName":"$name"}"""
            Log.d(TAG, "📁 전송할 JSON: $json")

            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

            val httpRequest = okhttp3.Request.Builder()
                .url("http://13.209.99.95:8080/api/categories")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Barrion-Android-Manual")
                .build()

            val response = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                client.newCall(httpRequest).execute()
            }

            Log.d(TAG, "📁 응답 코드: ${response.code}")
            Log.d(TAG, "📁 응답 성공 여부: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                Log.d(TAG, "✅ 카테고리 생성 성공: $responseBody")

                val jsonParser = Json { ignoreUnknownKeys = true }
                val categoryDto = jsonParser.decodeFromString<CategoryDto>(responseBody)
                val category = categoryDto.toDomain()

                Result.success(category)
            } else {
                val errorBody = response.body?.string() ?: ""
                Log.e(TAG, "❌ 카테고리 생성 실패: ${response.code} - $errorBody")
                Result.failure(Exception("카테고리 생성 실패: ${response.code} - $errorBody"))
            }

        } catch (e: Exception) {
            Log.e(TAG, "💥 카테고리 생성 중 예외: ${e.message}", e)
            Result.failure(e)
        }
    }
    /**
     * 카테고리 삭제
     * 기존: 성공만 반환 → 변경: 실제 API 호출
     */

    // 3. MenuRepositoryImpl.kt의 deleteCategory 수정
    override suspend fun deleteCategory(categoryId: Long): Result<Unit> {
        return try {
            Log.d(TAG, "🗑️ 카테고리 삭제: $categoryId")

            val client = OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val request = okhttp3.Request.Builder()
                .url("http://13.209.99.95:8080/api/categories/$categoryId")
                .delete()
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Barrion-Android-Manual")
                .build()

            Log.d(TAG, "🗑️ 삭제 요청 URL: ${request.url}")
            Log.d(TAG, "🗑️ 삭제 요청 메서드: ${request.method}")

            val response = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                client.newCall(request).execute()
            }

            Log.d(TAG, "🗑️ 삭제 응답 코드: ${response.code}")
            Log.d(TAG, "🗑️ 삭제 응답 성공 여부: ${response.isSuccessful}")

            if (response.isSuccessful) {
                Log.d(TAG, "✅ 카테고리 삭제 성공")
                Result.success(Unit)
            } else {
                val errorBody = response.body?.string() ?: ""
                Log.e(TAG, "❌ 카테고리 삭제 실패: ${response.code} - $errorBody")
                Result.failure(Exception("카테고리 삭제 실패: ${response.code} - $errorBody"))
            }

        } catch (e: Exception) {
            Log.e(TAG, "💥 카테고리 삭제 중 예외: ${e.message}", e)
            Result.failure(Exception("카테고리 삭제 중 네트워크 오류: ${e.message}"))
        }
    }

    /**
     * 카테고리 순서 업데이트
     * 현재: API에 해당 엔드포인트가 없어서 주석 처리
     * TODO: 백엔드에 카테고리 순서 변경 API 추가 시 구현
     */
    override suspend fun updateCategoryOrder(categories: List<Category>): Result<Unit> {
        return try {
            // TODO: 실제 API 호출 (현재 API에 해당 엔드포인트 없음)
            // for (category in categories) {
            //     categoryApi.updateCategoryOrder(category.id, category.order)
            // }

            // 현재는 성공으로 처리 (임시)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 특정 카테고리의 메뉴들 조회
     * 기존: 임시 데이터 필터링 → 변경: 실제 API 호출
     */
    override suspend fun getMenusByCategory(categoryId: Long): Result<List<Menu>> {
        return try {
            val response = menuApi.getMenus(page = 0, size = 100, category = categoryId)

            if (response.isSuccessful) {
                val pageResponse: MenuPageResponse? = response.body()
                val content: List<MenuDto>? = pageResponse?.content
                val menus = content?.toMenuDomainList() ?: emptyList()
                Result.success(menus)
            } else {
                Result.failure(Exception("카테고리별 메뉴 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }

    /**
     * 모든 메뉴 조회
     * 기존: 임시 데이터 반환 → 변경: 실제 API 호출
     */
    override suspend fun getAllMenus(): Result<List<Menu>> {
        return try {
            val response = menuApi.getMenus(page = 0, size = 100, category = null)

            if (response.isSuccessful) {
                val pageResponse: MenuPageResponse? = response.body()
                val content: List<MenuDto>? = pageResponse?.content
                val menus = content?.toMenuDomainList() ?: emptyList()
                Result.success(menus)
            } else {
                Result.failure(Exception("메뉴 목록 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("메뉴 조회 중 네트워크 오류: ${e.message}"))
        }
    }

    /**
     * 새 메뉴 추가
     * 기존: 임시 ID 할당 → 변경: 실제 API 호출
     */
    override suspend fun addMenu(menu: Menu, base64Image: String?): Result<Menu> {  // = null 제거
        return try {
            Log.d(TAG, "📱 메뉴 추가 시작: ${menu.name}")
            Log.d(TAG, "🖼️ Base64 이미지: ${base64Image?.take(50) ?: "없음"}...")

            // base64Image를 실제로 전달
            val request = menu.toCreateRequest(base64Image = base64Image)
            val response = menuApi.createMenu(request)

            if (response.isSuccessful) {
                val createdMenu = response.body()?.toDomain()
                    ?: throw Exception("서버 응답이 비어있습니다")
                Log.d(TAG, "✅ 메뉴 생성 성공: ${createdMenu.name}")
                Result.success(createdMenu)
            } else {
                val error = "메뉴 생성 실패: ${response.code()} - ${response.message()}"
                Log.e(TAG, "❌ $error")
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            val error = "메뉴 생성 중 네트워크 오류: ${e.message}"
            Log.e(TAG, "💥 $error", e)
            Result.failure(Exception(error))
        }
    }

    /**
     * 메뉴 정보 수정
     * 기존: 임시 리스트 수정 → 변경: 실제 API 호출
     */
    override suspend fun updateMenu(menu: Menu): Result<Menu> {
        return try {
            // base64Image 없이 메뉴 수정 (임시)
            val request = menu.toUpdateRequest(base64Image = null)
            val response = menuApi.updateMenu(menu.id, request)

            if (response.isSuccessful) {
                val updatedMenu = response.body()?.toDomain()
                    ?: throw Exception("서버 응답이 비어있습니다")
                Result.success(updatedMenu)
            } else {
                Result.failure(Exception("메뉴 수정 실패: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("메뉴 수정 중 네트워크 오류: ${e.message}"))
        }
    }

    /**
     * 메뉴 삭제
     * 기존: 임시 리스트에서 제거 → 변경: 실제 API 호출
     */
    override suspend fun deleteMenu(menuId: Long): Result<Unit> {
        return try {
            // 실제 API 호출
            val response = menuApi.deleteMenu(menuId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("메뉴 삭제 실패: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("메뉴 삭제 중 네트워크 오류: ${e.message}"))
        }
    }

    // TODO: 이미지 업로드 기능을 위한 추가 메서드들
    // 향후 Repository 인터페이스에 추가 필요:
    // suspend fun addMenuWithImage(menu: Menu, base64Image: String): Result<Menu>
    // suspend fun updateMenuWithImage(menu: Menu, base64Image: String?): Result<Menu>
}