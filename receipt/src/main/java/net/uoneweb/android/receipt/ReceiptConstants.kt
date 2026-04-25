package net.uoneweb.android.receipt

import java.util.Calendar

const val UNKNOWN_DATE_KEY = "__unknown__"

fun currentYearMonth(): String {
    val now = Calendar.getInstance()
    val year = now.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH) + 1
    return String.format("%04d-%02d", year, month)
}
