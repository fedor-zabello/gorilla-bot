package util

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTimeUtilsTest {
    @Test
    fun `Test date and time parsing`() {
        val dateStr = "Чт 14.11.2024"
        val timeStr = "20:15"
        val dateTime = DateTimeUtils.parseToLocalDateTime(dateStr, timeStr)

        assertEquals(LocalDateTime.of(2024, 11, 14, 20, 15, 0, 0), dateTime)
    }

    @Test
    fun `Delay for checking the result is more than 0`() {
        val matchStartTime = LocalDateTime.now().plusHours(5)

        val startCheckResultDelay = DateTimeUtils.calculateDelayForScoreCheck(matchStartTime)

        assertTrue(startCheckResultDelay > 0)
    }
}