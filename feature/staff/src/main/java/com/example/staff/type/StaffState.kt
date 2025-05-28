package com.example.staff.type

import com.example.domain.model.Staff

data class StaffState(
    val staffList: List<Staff> = emptyList(),
    val selectedStaff: Staff? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
