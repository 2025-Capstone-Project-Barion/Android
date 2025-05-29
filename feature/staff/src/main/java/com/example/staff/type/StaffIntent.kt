package com.example.staff.type

import com.example.domain.model.Staff


sealed interface StaffIntent {
    data object LoadStaffList : StaffIntent
    data class SearchStaff(val query: String) : StaffIntent
    data class SelectStaff(val id: Long) : StaffIntent
    data class AddStaff(val staff: Staff) : StaffIntent
    data class UpdateStaff(val staff: Staff) : StaffIntent
    data class DeleteStaff(val id: Long) : StaffIntent
    data object NavigateBack : StaffIntent
}

