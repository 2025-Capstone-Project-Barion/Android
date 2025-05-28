// :data/src/main/java/com/example/data/repository/StaffRepositoryImpl.kt
package com.example.data.repository

import android.util.Log
import com.example.data.api.StaffApi
import com.example.data.mapper.toCreateDto
import com.example.data.mapper.toDomain
import com.example.data.mapper.toDomainList
import com.example.domain.model.Staff
import com.example.domain.repository.StaffRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StaffRepositoryImpl @Inject constructor(
    private val staffApi: StaffApi
) : StaffRepository {

    companion object {
        private const val TAG = "StaffRepository"
    }

    // 로컬 캐시 (API 호출 후 결과 저장)
    private val _staffList = MutableStateFlow<List<Staff>>(emptyList())
    private val staffListFlow = _staffList.asStateFlow()

    override suspend fun getStaffList(): Result<List<Staff>> {
        Log.d(TAG, "🔍 직원 목록 조회 시작")
        return try {
            val response = staffApi.getAllEmployees()
            Log.d(TAG, "📡 API 응답: ${response.code()} - ${response.message()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d(TAG, "📊 응답 데이터: $responseBody")

                val staffList = responseBody?.toDomainList() ?: emptyList()
                Log.d(TAG, "✅ 변환된 직원 수: ${staffList.size}")
                staffList.forEach { staff ->
                    Log.d(TAG, "👤 직원: ${staff.name} (ID: ${staff.id})")
                }

                _staffList.value = staffList
                Result.success(staffList)
            } else {
                val errorMsg = "서버 오류: ${response.code()} - ${response.message()}"
                Log.e(TAG, "❌ $errorMsg")
                Log.e(TAG, "❌ 에러 바디: ${response.errorBody()?.string()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getStaffById(id: Long): Result<Staff> {
        Log.d(TAG, "🔍 직원 상세 조회 시작 - ID: $id")
        return try {
            val response = staffApi.getEmployeeById(id)
            Log.d(TAG, "📡 API 응답: ${response.code()} - ${response.message()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d(TAG, "📊 응답 데이터: $responseBody")

                val staff = responseBody?.toDomain()
                if (staff != null) {
                    Log.d(TAG, "✅ 직원 조회 성공: ${staff.name}")
                    Result.success(staff)
                } else {
                    Log.e(TAG, "❌ 응답 바디가 null")
                    Result.failure(Exception("직원을 찾을 수 없습니다."))
                }
            } else {
                val errorMsg = "서버 오류: ${response.code()} - ${response.message()}"
                Log.e(TAG, "❌ $errorMsg")
                Log.e(TAG, "❌ 에러 바디: ${response.errorBody()?.string()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun addStaff(staff: Staff): Result<Staff> {
        Log.d(TAG, "➕ 직원 추가 시작 - 이름: ${staff.name}")
        return try {
            val createDto = staff.toCreateDto()
            Log.d(TAG, "📤 전송할 데이터: $createDto")

            val response = staffApi.createEmployee(createDto)
            Log.d(TAG, "📡 API 응답: ${response.code()} - ${response.message()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d(TAG, "📊 응답 데이터: $responseBody")

                val newStaff = responseBody?.toDomain()
                if (newStaff != null) {
                    Log.d(TAG, "✅ 직원 추가 성공: ${newStaff.name} (ID: ${newStaff.id})")

                    // 로컬 캐시 업데이트
                    val updatedList = _staffList.value + newStaff
                    _staffList.value = updatedList
                    Log.d(TAG, "📝 로컬 캐시 업데이트 완료 - 총 ${updatedList.size}명")

                    Result.success(newStaff)
                } else {
                    Log.e(TAG, "❌ 응답 바디가 null")
                    Result.failure(Exception("직원 추가에 실패했습니다."))
                }
            } else {
                val errorMsg = "서버 오류: ${response.code()} - ${response.message()}"
                Log.e(TAG, "❌ $errorMsg")
                Log.e(TAG, "❌ 에러 바디: ${response.errorBody()?.string()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun updateStaff(staff: Staff): Result<Staff> {
        Log.d(TAG, "✏️ 직원 수정 시작 - 이름: ${staff.name} (ID: ${staff.id})")
        Log.w(TAG, "⚠️ 서버에 UPDATE API가 없어서 로컬 업데이트만 진행")

        return try {
            val updatedList = _staffList.value.map {
                if (it.id == staff.id) staff else it
            }
            _staffList.value = updatedList
            Log.d(TAG, "✅ 로컬 직원 정보 수정 완료")
            Result.success(staff)
        } catch (e: Exception) {
            Log.e(TAG, "💥 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteStaff(id: Long): Result<Unit> {
        Log.d(TAG, "🗑️ 직원 삭제 시작 - ID: $id")
        return try {
            val response = staffApi.deleteEmployee(id)
            Log.d(TAG, "📡 API 응답: ${response.code()} - ${response.message()}")

            if (response.isSuccessful) {
                Log.d(TAG, "✅ 서버에서 직원 삭제 성공")

                // 로컬 캐시에서도 제거
                val beforeSize = _staffList.value.size
                val updatedList = _staffList.value.filter { it.id != id }
                _staffList.value = updatedList

                Log.d(TAG, "📝 로컬 캐시 업데이트: ${beforeSize}명 → ${updatedList.size}명")
                Result.success(Unit)
            } else {
                val errorMsg = "서버 오류: ${response.code()} - ${response.message()}"
                Log.e(TAG, "❌ $errorMsg")
                Log.e(TAG, "❌ 에러 바디: ${response.errorBody()?.string()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun searchStaff(query: String): Result<List<Staff>> {
        Log.d(TAG, "🔍 직원 검색 시작 - 검색어: '$query'")
        return try {
            // 서버에 검색 API가 없으므로 전체 조회 후 로컬 필터링
            Log.d(TAG, "📡 서버에 검색 API가 없어서 전체 조회 후 필터링")
            val result = getStaffList()

            if (result.isSuccess) {
                val allStaff = result.getOrNull() ?: emptyList()
                Log.d(TAG, "📊 전체 직원 수: ${allStaff.size}")

                val searchResults = if (query.isBlank()) {
                    Log.d(TAG, "🔍 검색어 없음 - 전체 결과 반환")
                    allStaff
                } else {
                    val filtered = allStaff.filter { staff ->
                        staff.name.contains(query, ignoreCase = true) ||
                                staff.phoneNumber.contains(query)
                    }
                    Log.d(TAG, "🔍 검색 결과: ${filtered.size}명")
                    filtered.forEach { staff ->
                        Log.d(TAG, "👤 검색된 직원: ${staff.name}")
                    }
                    filtered
                }
                Result.success(searchResults)
            } else {
                Log.e(TAG, "❌ 전체 직원 조회 실패")
                result
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }

    override fun observeStaffList(): Flow<List<Staff>> {
        Log.d(TAG, "👀 직원 목록 관찰 시작")
        return staffListFlow
    }
}
//    // 임시 샘플 데이터 생성
//    private fun generateSampleStaffData(): List<Staff> {
//        return listOf(
//            Staff(
//                id = 1,
//                name = "임준식",
//                phoneNumber = "01012345678",
//                hourlyWage = 12500,
//                position = Position.MANAGER,
//                bank = Bank.WOORI,
//                accountNumber = "123-891-774411"
//            ),
//            Staff(
//                id = 2,
//                name = "김민지",
//                phoneNumber = "01098765432",
//                hourlyWage = 10000,
//                position = Position.BARISTA,
//                bank = Bank.KB,
//                accountNumber = "987-654-321012"
//            ),
//            Staff(
//                id = 3,
//                name = "박지현",
//                phoneNumber = "01045678912",
//                hourlyWage = 9800,
//                position = Position.CASHIER,
//                bank = Bank.SHINHAN,
//                accountNumber = "456-789-123456"
//            ),
//            Staff(
//                id = 4,
//                name = "이승우",
//                phoneNumber = "01033445566",
//                hourlyWage = 11000,
//                position = Position.KITCHEN,
//                bank = Bank.HANA,
//                accountNumber = "334-455-667788"
//            ),
//            Staff(
//                id = 5,
//                name = "정다은",
//                phoneNumber = "01077889900",
//                hourlyWage = 12500,
//                position = Position.KIOSK_MANAGER,
//                bank = Bank.NH,
//                accountNumber = "778-899-001122"
//            )
//        )
//    }
//}