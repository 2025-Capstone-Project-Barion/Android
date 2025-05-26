// feature/menu/src/main/java/com/barrion/feature/menu/type/MenuEffect.kt
package com.example.menu.type

import android.view.MenuItem


/**
 * MVI Pattern - Side Effect
 * 일회성 이벤트들 (Navigation, Toast, Dialog 등)
 */
sealed class MenuEffect {
    data class ShowToast(val message: String) : MenuEffect()
}