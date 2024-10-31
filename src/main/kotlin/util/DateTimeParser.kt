package util

import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

object DateTimeParser {
    fun parseToLocalDateTime(dateStr: String, timeStr: String): LocalDateTime {
        val datePattern = Regex("""\d{2}\.\d{2}\.\d{4}""")
        val dateMatch = datePattern.find(dateStr)
        val dateStrWithoutWeekDay = dateMatch?.value ?: throw IllegalArgumentException("Date format is incorrect")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val combinedDateTimeStr = "$dateStrWithoutWeekDay $timeStr"
        return LocalDateTime.parse(combinedDateTimeStr, dateTimeFormatter)
    }
}
