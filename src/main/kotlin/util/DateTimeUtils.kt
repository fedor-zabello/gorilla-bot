package util

import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

object DateTimeUtils {
    fun parseToLocalDateTime(dateStr: String, timeStr: String): LocalDateTime {
        val datePattern = Regex("""\d{2}\.\d{2}\.\d{4}""")
        val dateMatch = datePattern.find(dateStr)
        val dateStrWithoutWeekDay = dateMatch?.value ?: throw IllegalArgumentException("Date format is incorrect")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val combinedDateTimeStr = "$dateStrWithoutWeekDay $timeStr"
        return LocalDateTime.parse(combinedDateTimeStr, dateTimeFormatter)
    }

    fun calculateDelayForNotification(matchDateTime: LocalDateTime): Long {
        var notificationTime = matchDateTime.minusDays(1).withHour(11)
        return Duration.between(LocalDateTime.now(), notificationTime).toMillis()
    }

    fun calculateDelayForScoreCheck(matchDateTime: LocalDateTime): Long {
        var checkTime = matchDateTime.plusHours(1).plusMinutes(30)
        return Duration.between(LocalDateTime.now(), checkTime).toMillis()
    }
}
