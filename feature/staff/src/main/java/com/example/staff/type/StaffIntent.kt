package com.example.staff.type


sealed class StaffIntent {
    object LoadStaff : StaffIntent()
    data class SelectStaff(val staffId: String) : StaffIntent()
    data class AddStaff(val staff: Staff) : StaffIntent()
    data class UpdateStaff(val staff: Staff) : StaffIntent()
    data class DeleteStaff(val staffId: String) : StaffIntent()
    data class UpdateStaffRole(val staffId: String, val role: StaffRole) : StaffIntent()
    object ShowAddStaffDialog : StaffIntent()
    object HideAddStaffDialog : StaffIntent()
    object ClearError : StaffIntent()
}