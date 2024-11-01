package util

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeParserTest {
    @Test
    fun `Test date and time parsing`() {
        val dateStr = "Чт 14.11.2024"
        val timeStr = "20:15"
        val dateTime = DateTimeUtils.parseToLocalDateTime(dateStr, timeStr)

        assertEquals(LocalDateTime.of(2024, 11, 14, 20, 15, 0, 0), dateTime)
    }
}