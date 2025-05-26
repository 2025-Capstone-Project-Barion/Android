// feature/menu/src/main/java/com/barrion/feature/menu/type/MenuIntent.kt

package com.example.menu.type

import android.view.MenuItem

/**
 * MVI Pattern - Intent
 * 사용자의 모든 행동과 시스템 이벤트를 나타내는 Intent들
 */
sealed class MenuIntent {
    object LoadMenuData : MenuIntent()
    object RefreshMenuData : MenuIntent()
    object ClearError : MenuIntent()
}