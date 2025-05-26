package com.example.staff.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staff.type.Staff
import com.example.staff.type.StaffEffect
import com.example.staff.type.StaffIntent
import com.example.staff.type.StaffRole
import com.example.staff.type.StaffState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StaffViewModel : ViewModel() {

    private val _state = MutableStateFlow(StaffState())
    val state: StateFlow<StaffState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StaffEffect>()
    val effect: SharedFlow<StaffEffect> = _effect.asSharedFlow()

    init {
        handleIntent(StaffIntent.LoadStaff)
    }

    fun handleIntent(intent: StaffIntent) {
        when (intent) {
            is StaffIntent.LoadStaff -> loadStaff()
            is StaffIntent.SelectStaff -> selectStaff(intent.staffId)
            is StaffIntent.AddStaff -> addStaff(intent.staff)
            is StaffIntent.UpdateStaff -> updateStaff(intent.staff)
            is StaffIntent.DeleteStaff -> deleteStaff(intent.staffId)
            is StaffIntent.UpdateStaffRole -> updateStaffRole(intent.staffId, intent.role)
            is StaffIntent.ShowAddStaffDialog -> showAddStaffDialog()
            is StaffIntent.HideAddStaffDialog -> hideAddStaffDialog()
            is StaffIntent.ClearError -> clearError()
        }
    }

    private fun loadStaff() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // 임시 데이터
                val staffList = getSampleStaff()
                _state.value = _state.value.copy(
                    isLoading = false,
                    staffList = staffList
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "직원 데이터를 불러올 수 없습니다"
                )
            }
        }
    }

    private fun selectStaff(staffId: String) {
        val staff = _state.value.staffList.find { it.id == staffId }
        _state.value = _state.value.copy(selectedStaff = staff)

        viewModelScope.launch {
            _effect.emit(StaffEffect.NavigateToStaffDetail(staffId))
        }
    }

    private fun addStaff(staff: Staff) {
        _state.value = _state.value.copy(
            staffList = _state.value.staffList + staff,
            showAddStaffDialog = false
        )

        viewModelScope.launch {
            _effect.emit(StaffEffect.ShowToast("직원이 추가되었습니다"))
        }
    }

    private fun updateStaff(staff: Staff) {
        _state.value = _state.value.copy(
            staffList = _state.value.staffList.map {
                if (it.id == staff.id) staff else it
            }
        )
    }

    private fun deleteStaff(staffId: String) {
        _state.value = _state.value.copy(
            staffList = _state.value.staffList.filter { it.id != staffId }
        )

        viewModelScope.launch {
            _effect.emit(StaffEffect.ShowToast("직원이 삭제되었습니다"))
        }
    }

    private fun updateStaffRole(staffId: String, role: StaffRole) {
        _state.value = _state.value.copy(
            staffList = _state.value.staffList.map { staff ->
                if (staff.id == staffId) {
                    staff.copy(role = role)
                } else {
                    staff
                }
            }
        )
    }

    private fun showAddStaffDialog() {
        _state.value = _state.value.copy(showAddStaffDialog = true)
    }

    private fun hideAddStaffDialog() {
        _state.value = _state.value.copy(showAddStaffDialog = false)
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun getSampleStaff(): List<Staff> {
        return emptyList() // 일단 빈 리스트
    }
}