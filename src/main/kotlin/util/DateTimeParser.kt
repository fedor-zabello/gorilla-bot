package util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.ZoneId

object DateTimeParser {
    fun parseToZonedDateTime(dateStr: String, timeStr: String): ZonedDateTime {
        val datePattern = Regex("""\d{2}\.\d{2}\.\d{4}""")
        val dateMatch = datePattern.find(dateStr)
        val dateStrWithoutWeekDay = dateMatch?.value ?: throw IllegalArgumentException("Date format is incorrect")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val combinedDateTimeStr = "$dateStrWithoutWeekDay $timeStr"
        val localDateTime = LocalDateTime.parse(combinedDateTimeStr, dateTimeFormatter)
        val zoneId = ZoneId.of("GMT+3")

        return localDateTime.atZone(zoneId)
    }
}
