package com.example.staff.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Staff
import com.example.domain.usecase.staff.DeleteStaffUseCase
import com.example.domain.usecase.staff.GetStaffByIdUseCase
import com.example.domain.usecase.staff.GetStaffListUseCase
import com.example.domain.usecase.staff.SearchStaffUseCase
import com.example.domain.usecase.staff.UpdateStaffUseCase
import com.example.staff.type.StaffEffect
import com.example.staff.type.StaffIntent
import com.example.staff.type.StaffState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.setValue
import com.example.domain.usecase.staff.AddStaffUseCase

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val getStaffListUseCase: GetStaffListUseCase,
    private val searchStaffUseCase: SearchStaffUseCase,
    private val getStaffByIdUseCase: GetStaffByIdUseCase,
    private val addStaffUseCase: AddStaffUseCase,
    private val updateStaffUseCase: UpdateStaffUseCase,
    private val deleteStaffUseCase: DeleteStaffUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "StaffViewModel"
    }

    var state by mutableStateOf(StaffState())
        private set

    private val _effect = Channel<StaffEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: StaffIntent) {
        Log.d(TAG, "📩 Intent 수신: ${intent::class.simpleName}")
        when (intent) {
            is StaffIntent.LoadStaffList -> {
                Log.d(TAG, "🔄 직원 목록 로딩 Intent")
                loadStaffList()
            }
            is StaffIntent.SearchStaff -> {
                Log.d(TAG, "🔍 직원 검색 Intent - 검색어: '${intent.query}'")
                searchStaff(intent.query)
            }
            is StaffIntent.SelectStaff -> {
                Log.d(TAG, "👤 직원 선택 Intent - ID: ${intent.id}")
                selectStaff(intent.id)
            }
            is StaffIntent.AddStaff -> {
                Log.d(TAG, "➕ 직원 추가 Intent - 이름: ${intent.staff.name}")
                addStaff(intent.staff)
            }
            is StaffIntent.UpdateStaff -> {
                Log.d(TAG, "✏️ 직원 수정 Intent - 이름: ${intent.staff.name} (ID: ${intent.staff.id})")
                updateStaff(intent.staff)
            }
            is StaffIntent.DeleteStaff -> {
                Log.d(TAG, "🗑️ 직원 삭제 Intent - ID: ${intent.id}")
                deleteStaff(intent.id)
            }
            is StaffIntent.NavigateBack -> {
                Log.d(TAG, "🔙 뒤로가기 Intent")
                sendEffect(StaffEffect.NavigateBack)
            }
        }
    }

    private fun loadStaffList() = launch {
        Log.d(TAG, "🔄 직원 목록 로딩 시작")
        state = state.copy(isLoading = true)

        try {
            val result = getStaffListUseCase()
            Log.d(TAG, "📡 UseCase 호출 완료")

            result.onSuccess { staffList ->
                Log.d(TAG, "✅ 직원 목록 로딩 성공: ${staffList.size}명")
                staffList.forEachIndexed { index, staff ->
                    Log.d(TAG, "👤 [$index] ${staff.name} (ID: ${staff.id}, ${staff.position.displayName})")
                }

                state = state.copy(
                    staffList = staffList,
                    isLoading = false,
                    errorMessage = null
                )
                Log.d(TAG, "📱 UI 상태 업데이트 완료")

            }.onFailure { exception ->
                Log.e(TAG, "❌ 직원 목록 로딩 실패: ${exception.message}", exception)
                state = state.copy(
                    errorMessage = exception.message ?: "직원 목록을 불러오지 못했습니다.",
                    isLoading = false,
                    staffList = emptyList()
                )
                sendEffect(StaffEffect.ShowToast("직원 목록을 불러오지 못했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 직원 목록 로딩 중 예상치 못한 오류: ${e.message}", e)
            state = state.copy(
                errorMessage = e.message ?: "예상치 못한 오류가 발생했습니다.",
                isLoading = false
            )
            sendEffect(StaffEffect.ShowToast("예상치 못한 오류가 발생했습니다."))
        }
    }

    private fun searchStaff(query: String) = launch {
        Log.d(TAG, "🔍 직원 검색 시작 - 검색어: '$query'")

        try {
            val result = searchStaffUseCase(query)
            Log.d(TAG, "📡 검색 UseCase 호출 완료")

            result.onSuccess { searchResults ->
                Log.d(TAG, "✅ 검색 성공: ${searchResults.size}명 발견")
                searchResults.forEachIndexed { index, staff ->
                    Log.d(TAG, "🔍 [$index] ${staff.name} (${staff.phoneNumber})")
                }

                state = state.copy(
                    staffList = searchResults,
                    errorMessage = null
                )

            }.onFailure { exception ->
                Log.e(TAG, "❌ 검색 실패: ${exception.message}", exception)
                state = state.copy(
                    errorMessage = exception.message ?: "검색에 실패했습니다."
                )
                sendEffect(StaffEffect.ShowToast("검색에 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 검색 중 예상치 못한 오류: ${e.message}", e)
            state = state.copy(errorMessage = e.message ?: "검색 중 오류가 발생했습니다.")
            sendEffect(StaffEffect.ShowToast("검색 중 오류가 발생했습니다."))
        }
    }

    private fun selectStaff(id: Long) = launch {
        Log.d(TAG, "👤 직원 선택 시작 - ID: $id")

        try {
            val result = getStaffByIdUseCase(id)
            Log.d(TAG, "📡 직원 상세 UseCase 호출 완료")

            result.onSuccess { staff ->
                Log.d(TAG, "✅ 직원 조회 성공: ${staff.name}")
                state = state.copy(
                    selectedStaff = staff,
                    errorMessage = null
                )
                sendEffect(StaffEffect.NavigateToDetail(id))

            }.onFailure { exception ->
                Log.e(TAG, "❌ 직원 조회 실패: ${exception.message}", exception)
                state = state.copy(
                    errorMessage = exception.message ?: "직원 정보를 불러오지 못했습니다."
                )
                sendEffect(StaffEffect.ShowToast("직원 정보를 불러오지 못했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 직원 조회 중 예상치 못한 오류: ${e.message}", e)
            state = state.copy(errorMessage = e.message ?: "직원 조회 중 오류가 발생했습니다.")
            sendEffect(StaffEffect.ShowToast("직원 조회 중 오류가 발생했습니다."))
        }
    }

    private fun addStaff(staff: Staff) = launch {
        Log.d(TAG, "➕ 직원 추가 시작 - 이름: ${staff.name}")
        Log.d(TAG, "📋 추가할 직원 정보:")
        Log.d(TAG, "   - 이름: ${staff.name}")
        Log.d(TAG, "   - 전화번호: ${staff.phoneNumber}")
        Log.d(TAG, "   - 시급: ${staff.hourlyWage}원")
        Log.d(TAG, "   - 직무: ${staff.position.displayName}")
        Log.d(TAG, "   - 은행: ${staff.bank.displayName}")
        Log.d(TAG, "   - 계좌번호: ${staff.accountNumber}")

        state = state.copy(isLoading = true)

        try {
            val result = addStaffUseCase(staff)
            Log.d(TAG, "📡 직원 추가 UseCase 호출 완료")

            result.onSuccess { addedStaff ->
                Log.d(TAG, "✅ 직원 추가 성공: ${addedStaff.name} (ID: ${addedStaff.id})")
                state = state.copy(
                    successMessage = "직원 추가 완료",
                    isLoading = false,
                    errorMessage = null
                )
                sendEffect(StaffEffect.ShowToast("${addedStaff.name}님이 추가되었습니다."))

                Log.d(TAG, "🔄 직원 목록 새로고침 시작")
                loadStaffList()
                sendEffect(StaffEffect.NavigateBack)

            }.onFailure { exception ->
                Log.e(TAG, "❌ 직원 추가 실패: ${exception.message}", exception)
                state = state.copy(
                    errorMessage = exception.message ?: "직원 추가에 실패했습니다.",
                    isLoading = false
                )
                sendEffect(StaffEffect.ShowToast("직원 추가에 실패했습니다: ${exception.message}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 직원 추가 중 예상치 못한 오류: ${e.message}", e)
            state = state.copy(
                errorMessage = e.message ?: "직원 추가 중 오류가 발생했습니다.",
                isLoading = false
            )
            sendEffect(StaffEffect.ShowToast("직원 추가 중 오류가 발생했습니다."))
        }
    }

    private fun updateStaff(staff: Staff) = launch {
        Log.d(TAG, "✏️ 직원 수정 시작 - 이름: ${staff.name} (ID: ${staff.id})")
        Log.d(TAG, "📋 수정할 직원 정보:")
        Log.d(TAG, "   - ID: ${staff.id}")
        Log.d(TAG, "   - 이름: ${staff.name}")
        Log.d(TAG, "   - 전화번호: ${staff.phoneNumber}")
        Log.d(TAG, "   - 시급: ${staff.hourlyWage}원")
        Log.d(TAG, "   - 직무: ${staff.position.displayName}")
        Log.d(TAG, "   - 은행: ${staff.bank.displayName}")
        Log.d(TAG, "   - 계좌번호: ${staff.accountNumber}")

        state = state.copy(isLoading = true)

        try {
            val result = updateStaffUseCase(staff)
            Log.d(TAG, "📡 직원 수정 UseCase 호출 완료")

            result.onSuccess { updatedStaff ->
                Log.d(TAG, "✅ 직원 수정 성공: ${updatedStaff.name}")
                state = state.copy(
                    successMessage = "직원 수정 완료",
                    isLoading = false,
                    errorMessage = null
                )
                sendEffect(StaffEffect.ShowToast("${updatedStaff.name}님 정보가 수정되었습니다."))

                Log.d(TAG, "🔄 직원 목록 새로고침 시작")
                loadStaffList()
                sendEffect(StaffEffect.NavigateBack)

            }.onFailure { exception ->
                Log.e(TAG, "❌ 직원 수정 실패: ${exception.message}", exception)
                state = state.copy(
                    errorMessage = exception.message ?: "직원 수정에 실패했습니다.",
                    isLoading = false
                )
                sendEffect(StaffEffect.ShowToast("직원 수정에 실패했습니다: ${exception.message}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 직원 수정 중 예상치 못한 오류: ${e.message}", e)
            state = state.copy(
                errorMessage = e.message ?: "직원 수정 중 오류가 발생했습니다.",
                isLoading = false
            )
            sendEffect(StaffEffect.ShowToast("직원 수정 중 오류가 발생했습니다."))
        }
    }

    private fun deleteStaff(id: Long) = launch {
        Log.d(TAG, "🗑️ 직원 삭제 시작 - ID: $id")

        // 삭제할 직원 정보 로그 출력
        val staffToDelete = state.staffList.find { it.id == id }
        if (staffToDelete != null) {
            Log.d(TAG, "🗑️ 삭제할 직원: ${staffToDelete.name}")
        } else {
            Log.w(TAG, "⚠️ 삭제할 직원을 목록에서 찾을 수 없음")
        }

        state = state.copy(isLoading = true)

        try {
            val result = deleteStaffUseCase(id)
            Log.d(TAG, "📡 직원 삭제 UseCase 호출 완료")

            result.onSuccess {
                Log.d(TAG, "✅ 직원 삭제 성공")
                state = state.copy(
                    successMessage = "직원 삭제 완료",
                    isLoading = false,
                    errorMessage = null
                )
                sendEffect(StaffEffect.ShowToast("직원이 삭제되었습니다."))

                Log.d(TAG, "🔄 직원 목록 새로고침 시작")
                loadStaffList()
                sendEffect(StaffEffect.NavigateBack)

            }.onFailure { exception ->
                Log.e(TAG, "❌ 직원 삭제 실패: ${exception.message}", exception)
                state = state.copy(
                    errorMessage = exception.message ?: "직원 삭제에 실패했습니다.",
                    isLoading = false
                )
                sendEffect(StaffEffect.ShowToast("직원 삭제에 실패했습니다: ${exception.message}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 직원 삭제 중 예상치 못한 오류: ${e.message}", e)
            state = state.copy(
                errorMessage = e.message ?: "직원 삭제 중 오류가 발생했습니다.",
                isLoading = false
            )
            sendEffect(StaffEffect.ShowToast("직원 삭제 중 오류가 발생했습니다."))
        }
    }

    private fun sendEffect(effect: StaffEffect) = launch {
        Log.d(TAG, "🎭 Effect 전송: ${effect::class.simpleName}")
        _effect.send(effect)
    }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                Log.e(TAG, "💥 ViewModelScope에서 예상치 못한 오류: ${e.message}", e)
                state = state.copy(
                    errorMessage = e.message ?: "예상치 못한 오류가 발생했습니다.",
                    isLoading = false
                )
                sendEffect(StaffEffect.ShowToast("예상치 못한 오류가 발생했습니다."))
            }
        }
    }
}