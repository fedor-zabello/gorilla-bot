package util

import SpbhlMatch
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue

class MessageGeneratorTest {
    @Test
    fun `Home victory`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 4, 20, 45, 0),
            "ОЗЦ",
            "Горилла - Самураи Старт",
            "4 : 1"
        )
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Победа"))
    }

    @Test
    fun `Away victory`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 10, 1, 21, 0, 0),
            "ЖДН",
            "Алмаз - Горилла",
            "2 : 3"
        )
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Победа"))
    }

    @Test
    fun `Away defeat`() {
        val match = SpbhlMatch(
            "Старт 2В Групповой этап",
            LocalDateTime.of(2024, 10, 31, 22, 30, 0),
            "БУТ",
            "Sharks Старт - Горилла-2",
            "3 : 1"
        )
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Поражение"))
    }

    @Test
    fun `Home defeat`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 11, 20, 45, 0),
            "ОЗЦ",
            "Горилла - Кронверк",
            "2 : 4"
        )
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Поражение"))
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
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Ничья"))
    }

    @Test
    fun `Get message for finished match after shootouts`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 26, 21, 30, 0),
            "ЛОК",
            "Ижорский батальон-2 - Горилла",
            "2 : 3ПБ"
        )
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Победа"))
    }

    @Test
    fun `Get message for technical win`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 26, 21, 30, 0),
            "ЛОК",
            "Ижорский батальон-2 - Горилла",
            "0 : 5ОТ"
        )
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Победа"))
    }

    @Test
    fun `Test big score`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 26, 21, 30, 0),
            "ЛОК",
            "Ижорский батальон-2 - Горилла",
            "9 : 27"
        )
        val message = MessageGenerator.getFinishedMatchMessage(match)

        assertTrue(message.contains("Победа"))
    }
}