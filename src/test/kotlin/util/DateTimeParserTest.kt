package util

import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeParserTest {
    @Test
    fun `Test date and time parsing`() {
        val dateStr = "Чт 14.11.2024"
        val timeStr = "20:15"
        val zonedDateTime = DateTimeParser.parseToZonedDateTime(dateStr, timeStr)

        assertEquals(ZonedDateTime.of(2024, 11, 14, 20, 15, 0, 0, ZoneId.of("GMT+3")), zonedDateTime)
    }
}