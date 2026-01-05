package com.reader.app.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun parseIsoToLong(isoString: String): Long {
        return try {
            ZonedDateTime.parse(isoString).toInstant().toEpochMilli()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
