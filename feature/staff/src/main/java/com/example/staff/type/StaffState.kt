package com.example.staff.type

data class StaffState(
    val staffList: List<Staff> = emptyList(),
    val selectedStaff: Staff? = null,
    val showAddStaffDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val adminStaff: List<Staff>
        get() = staffList.filter { it.role == StaffRole.ADMIN }

    val managerStaff: List<Staff>
        get() = staffList.filter { it.role == StaffRole.MANAGER }

    val employeeStaff: List<Staff>
        get() = staffList.filter { it.role == StaffRole.EMPLOYEE }
}

data class Staff(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: StaffRole,
    val isActive: Boolean = true
)

enum class StaffRole {
    ADMIN, MANAGER, EMPLOYEE
}