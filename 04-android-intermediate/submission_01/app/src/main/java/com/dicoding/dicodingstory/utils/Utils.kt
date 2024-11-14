package com.dicoding.dicodingstory.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatDate(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return ""

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set to UTC time zone since it has "Z" at the end

    val outputFormat = SimpleDateFormat("dd MMMM yyyy | hh:mm a", Locale.getDefault())


    return try {
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}