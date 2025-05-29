package com.example.staff.type

sealed interface StaffEffect {
    data class ShowToast(val message: String) : StaffEffect
    data class NavigateToDetail(val id: Long) : StaffEffect
    data object NavigateBack : StaffEffect
}

