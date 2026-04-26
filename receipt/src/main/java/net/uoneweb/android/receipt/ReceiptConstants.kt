package net.uoneweb.android.receipt

import java.util.Calendar

const val UNKNOWN_DATE_KEY = "__unknown__"

fun currentYearMonth(): String {
    val now = Calendar.getInstance()
    val year = now.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH) + 1
    return String.format("%04d-%02d", year, month)
}

fun currentDate(): String {
    val now = Calendar.getInstance()
    val year = now.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH) + 1
    val day = now.get(Calendar.DAY_OF_MONTH)
    return String.format("%04d-%02d-%02d", year, month, day)
}
