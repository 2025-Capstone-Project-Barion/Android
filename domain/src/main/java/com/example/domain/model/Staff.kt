// :domain/src/main/java/com/example/domain/model/Staff.kt
package com.example.domain.model

/**
 * 직원 도메인 모델
 */
data class Staff(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val hourlyWage: Int,
    val position: Position,
    val bank: Bank,
    val accountNumber: String,
    val createdAt: String? = null
)

/**
 * 직무/역할 enum
 */
enum class Position(val code: String, val displayName: String) {
    MANAGER("MANAGER", "매니저"),
    BARISTA("BARISTA", "바리스타"),
    CASHIER("CASHIER", "캐셔"),
    KITCHEN("KITCHEN", "주방보조"),
    KIOSK_MANAGER("KIOSK_MANAGER", "키오스크관리");

    companion object {
        fun fromCode(code: String): Position {
            return values().find { it.code == code } ?: BARISTA
        }

        fun getAllDisplayNames(): List<String> {
            return values().map { it.displayName }
        }
    }
}

/**
 * 은행 enum
 */
enum class Bank(val code: String, val displayName: String) {
    WOORI("WOORI", "우리"),
    KB("KB", "국민"),
    SHINHAN("SHINHAN", "신한"),
    HANA("HANA", "하나"),
    NH("NH", "농협"),
    IBK("IBK", "기업"),
    BUSAN("BUSAN", "부산"),
    DAEGU("DAEGU", "대구"),
    KWANGJU("KWANGJU", "광주"),
    JEONBUK("JEONBUK", "전북");

    companion object {
        fun fromCode(code: String): Bank {
            return values().find { it.code == code } ?: WOORI
        }

        fun getAllDisplayNames(): List<String> {
            return values().map { it.displayName }
        }
    }
}