// feature/menu/src/main/java/com/barrion/feature/menu/type/MenuState.kt
package com.example.menu.type

import android.view.MenuItem


/**
 * MVI Pattern - State
 * UI의 모든 상태를 나타내는 불변 데이터 클래스
 */
data class MenuState(
    val isLoading: Boolean = false,
    val error: String? = null
)