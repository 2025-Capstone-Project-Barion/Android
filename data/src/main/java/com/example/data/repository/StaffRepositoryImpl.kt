// :data/src/main/java/com/example/data/repository/StaffRepositoryImpl.kt
package com.example.data.repository

import com.example.data.api.StaffApi
import com.example.data.mapper.toCreateDto
import com.example.data.mapper.toDomain
import com.example.data.mapper.toDomainList
import com.example.data.mapper.toUpdateDto
import com.example.domain.model.Bank
import com.example.domain.model.Position
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

    // 임시 데이터 (나중에 실제 API 연결 시 제거)
    private val _staffList = MutableStateFlow<List<Staff>>(generateSampleStaffData())
    private val staffListFlow = _staffList.asStateFlow()

    override suspend fun getStaffList(): Result<List<Staff>> {
        return try {
            // TODO: 실제 API 호출
            // val response = staffApi.getStaffList()
            // if (response.isSuccessful) {
            //     val staffList = response.body()?.data?.toDomainList() ?: emptyList()
            //     _staffList.value = staffList
            //     Result.success(staffList)
            // } else {
            //     Result.failure(Exception("서버 오류: ${response.code()}"))
            // }

            // 임시 데이터 반환
            Result.success(_staffList.value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStaffById(id: Long): Result<Staff> {
        return try {
            // TODO: 실제 API 호출
            // val response = staffApi.getStaffById(id)
            // if (response.isSuccessful) {
            //     val staff = response.body()?.toDomain()
            //     if (staff != null) {
            //         Result.success(staff)
            //     } else {
            //         Result.failure(Exception("직원을 찾을 수 없습니다."))
            //     }
            // } else {
            //     Result.failure(Exception("서버 오류: ${response.code()}"))
            // }

            // 임시 데이터에서 검색
            val staff = _staffList.value.find { it.id == id }
            if (staff != null) {
                Result.success(staff)
            } else {
                Result.failure(Exception("직원을 찾을 수 없습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addStaff(staff: Staff): Result<Staff> {
        return try {
            // TODO: 실제 API 호출
            // val createDto = staff.toCreateDto()
            // val response = staffApi.createStaff(createDto)
            // if (response.isSuccessful) {
            //     val newStaff = response.body()?.toDomain()
            //     if (newStaff != null) {
            //         val updatedList = _staffList.value + newStaff
            //         _staffList.value = updatedList
            //         Result.success(newStaff)
            //     } else {
            //         Result.failure(Exception("직원 추가에 실패했습니다."))
            //     }
            // } else {
            //     Result.failure(Exception("서버 오류: ${response.code()}"))
            // }

            // 임시 데이터에 추가
            val newStaff = staff.copy(id = generateNewId())
            val updatedList = _staffList.value + newStaff
            _staffList.value = updatedList
            Result.success(newStaff)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStaff(staff: Staff): Result<Staff> {
        return try {
            // TODO: 실제 API 호출
            // val updateDto = staff.toUpdateDto()
            // val response = staffApi.updateStaff(staff.id, updateDto)
            // if (response.isSuccessful) {
            //     val updatedStaff = response.body()?.toDomain()
            //     if (updatedStaff != null) {
            //         val updatedList = _staffList.value.map {
            //             if (it.id == staff.id) updatedStaff else it
            //         }
            //         _staffList.value = updatedList
            //         Result.success(updatedStaff)
            //     } else {
            //         Result.failure(Exception("직원 정보 수정에 실패했습니다."))
            //     }
            // } else {
            //     Result.failure(Exception("서버 오류: ${response.code()}"))
            // }

            // 임시 데이터 수정
            val updatedList = _staffList.value.map {
                if (it.id == staff.id) staff else it
            }
            _staffList.value = updatedList
            Result.success(staff)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteStaff(id: Long): Result<Unit> {
        return try {
            // TODO: 실제 API 호출
            // val response = staffApi.deleteStaff(id)
            // if (response.isSuccessful) {
            //     val updatedList = _staffList.value.filter { it.id != id }
            //     _staffList.value = updatedList
            //     Result.success(Unit)
            // } else {
            //     Result.failure(Exception("서버 오류: ${response.code()}"))
            // }

            // 임시 데이터에서 삭제
            val updatedList = _staffList.value.filter { it.id != id }
            _staffList.value = updatedList
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchStaff(query: String): Result<List<Staff>> {
        return try {
            // TODO: 실제 API 호출
            // val response = staffApi.searchStaff(query)
            // if (response.isSuccessful) {
            //     val searchResults = response.body()?.data?.toDomainList() ?: emptyList()
            //     Result.success(searchResults)
            // } else {
            //     Result.failure(Exception("서버 오류: ${response.code()}"))
            // }

            // 임시 데이터에서 검색
            val searchResults = _staffList.value.filter { staff ->
                staff.name.contains(query, ignoreCase = true) ||
                        staff.phoneNumber.contains(query)
            }
            Result.success(searchResults)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeStaffList(): Flow<List<Staff>> {
        return staffListFlow
    }

    private fun generateNewId(): Long {
        return (_staffList.value.maxOfOrNull { it.id } ?: 0) + 1
    }

    // 임시 샘플 데이터 생성
    private fun generateSampleStaffData(): List<Staff> {
        return listOf(
            Staff(
                id = 1,
                name = "임준식",
                phoneNumber = "01012345678",
                hourlyWage = 12500,
                position = Position.MANAGER,
                bank = Bank.WOORI,
                accountNumber = "123-891-774411"
            ),
            Staff(
                id = 2,
                name = "김민지",
                phoneNumber = "01098765432",
                hourlyWage = 10000,
                position = Position.BARISTA,
                bank = Bank.KB,
                accountNumber = "987-654-321012"
            ),
            Staff(
                id = 3,
                name = "박지현",
                phoneNumber = "01045678912",
                hourlyWage = 9800,
                position = Position.CASHIER,
                bank = Bank.SHINHAN,
                accountNumber = "456-789-123456"
            ),
            Staff(
                id = 4,
                name = "이승우",
                phoneNumber = "01033445566",
                hourlyWage = 11000,
                position = Position.KITCHEN,
                bank = Bank.HANA,
                accountNumber = "334-455-667788"
            ),
            Staff(
                id = 5,
                name = "정다은",
                phoneNumber = "01077889900",
                hourlyWage = 12500,
                position = Position.KIOSK_MANAGER,
                bank = Bank.NH,
                accountNumber = "778-899-001122"
            )
        )
    }
}