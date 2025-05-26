package com.example.staff.type

sealed class StaffEffect {
    data class ShowToast(val message: String) : StaffEffect()
    data class NavigateToStaffDetail(val staffId: String) : StaffEffect()
    object NavigateToStaffAdd : StaffEffect()
}