package util

import model.SpbhlMatch
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageGeneratorTest {
    @Test
    fun `Message for victory`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 4, 20, 45, 0),
            "ОЗЦ",
            "Горилла - Самураи Старт",
            "4 : 1"
        )

        val message = MessageGenerator.getGorillaWonMessage(match)

        val expected = """
            *__Победа__*
            Горилла \- Самураи Старт
            счёт: 4 : 1
        """.trimIndent()
        assertEquals(expected, message)
    }

    @Test
    fun `Message for defeat`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 11, 20, 45, 0),
            "ОЗЦ",
            "Горилла - Кронверк",
            "2 : 4"
        )

        val message = MessageGenerator.getDefeatMessage(match)

        val expected = """
            *__Поражение__*
            Горилла \- Кронверк
            счёт: 2 : 4
        """.trimIndent()
        assertEquals(expected, message)
    }

    @Test
    fun `Get message for draw`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 11, 20, 45, 0),
            "ОЗЦ",
            "Горилла - Кронверк",
            "4 : 4"
        )

        val message = MessageGenerator.getDrawMessage(match)

        val expected = """
            *__Ничья__*
            Горилла \- Кронверк
            счёт: 4 : 4
        """.trimIndent()
        assertEquals(expected, message)
    }
}