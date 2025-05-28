package com.example.staff.viewmodel


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

    var state by mutableStateOf(StaffState())
        private set

    private val _effect = Channel<StaffEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: StaffIntent) {
        when (intent) {
            is StaffIntent.LoadStaffList -> loadStaffList()
            is StaffIntent.SearchStaff -> searchStaff(intent.query)
            is StaffIntent.SelectStaff -> selectStaff(intent.id)
            is StaffIntent.AddStaff -> addStaff(intent.staff)
            is StaffIntent.UpdateStaff -> updateStaff(intent.staff)
            is StaffIntent.DeleteStaff -> deleteStaff(intent.id)
            is StaffIntent.NavigateBack -> sendEffect(StaffEffect.NavigateBack)
        }
    }

    private fun loadStaffList() = launch {
        state = state.copy(isLoading = true)
        val result = getStaffListUseCase()
        result.onSuccess {
            state = state.copy(staffList = it, isLoading = false)
        }.onFailure {
            state = state.copy(errorMessage = it.message, isLoading = false)
        }
    }

    private fun searchStaff(query: String) = launch {
        val result = searchStaffUseCase(query)
        result.onSuccess {
            state = state.copy(staffList = it)
        }.onFailure {
            state = state.copy(errorMessage = it.message)
        }
    }

    private fun selectStaff(id: Long) = launch {
        val result = getStaffByIdUseCase(id)
        result.onSuccess {
            state = state.copy(selectedStaff = it)
            sendEffect(StaffEffect.NavigateToDetail(id))
        }.onFailure {
            state = state.copy(errorMessage = it.message)
        }
    }

    private fun addStaff(staff: Staff) = launch {
        val result = addStaffUseCase(staff)
        result.onSuccess {
            state = state.copy(successMessage = "직원 추가 완료")
            loadStaffList()
            sendEffect(StaffEffect.NavigateBack)
        }.onFailure {
            state = state.copy(errorMessage = it.message)
        }
    }

    private fun updateStaff(staff: Staff) = launch {
        val result = updateStaffUseCase(staff)
        result.onSuccess {
            state = state.copy(successMessage = "직원 수정 완료")
            loadStaffList()
            sendEffect(StaffEffect.NavigateBack)
        }.onFailure {
            state = state.copy(errorMessage = it.message)
        }
    }

    private fun deleteStaff(id: Long) = launch {
        val result = deleteStaffUseCase(id)
        result.onSuccess {
            state = state.copy(successMessage = "직원 삭제 완료")
            loadStaffList()
            sendEffect(StaffEffect.NavigateBack)
        }.onFailure {
            state = state.copy(errorMessage = it.message)
        }
    }

    private fun sendEffect(effect: StaffEffect) = launch {
        _effect.send(effect)
    }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }
}
