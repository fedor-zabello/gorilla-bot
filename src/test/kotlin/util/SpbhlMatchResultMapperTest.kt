package util

import model.SpbhlMatch
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpbhlMatchResultMapperTest {
    private val spbhlMatchMapper = SpbhlMatchMapper()
    @Test
    fun `Home victory`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 4, 20, 45, 0),
            "ОЗЦ",
            "Горилла - Самураи Старт",
            "4 : 1"
        )
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(4 to 1, matchResult.score)
        assertEquals("Горилла" to "Самураи Старт", matchResult.teams)
        assertTrue(matchResult.gorillaWon())
        assertFalse(matchResult.isDraw())
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
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(2 to 3, matchResult.score)
        assertEquals("Алмаз" to "Горилла", matchResult.teams)
        assertTrue(matchResult.gorillaWon())
        assertFalse(matchResult.isDraw())
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
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(3 to 1, matchResult.score)
        assertEquals("Sharks Старт" to "Горилла-2", matchResult.teams)
        assertFalse(matchResult.gorillaWon())
        assertFalse(matchResult.isDraw())
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
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(2 to 4, matchResult.score)
        assertEquals("Горилла" to "Кронверк", matchResult.teams)
        assertFalse(matchResult.gorillaWon())
        assertFalse(matchResult.isDraw())
    }

    @Test
    fun `Result is draw`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 11, 20, 45, 0),
            "ОЗЦ",
            "Горилла - Кронверк",
            "4 : 4"
        )
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(4 to 4, matchResult.score)
        assertEquals("Горилла" to "Кронверк", matchResult.teams)
        assertFalse(matchResult.gorillaWon())
        assertTrue(matchResult.isDraw())
    }

    @Test
    fun `Shootouts win`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 26, 21, 30, 0),
            "ЛОК",
            "Ижорский батальон-2 - Горилла",
            "2 : 3ПБ"
        )
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(2 to 3, matchResult.score)
        assertEquals("Ижорский батальон-2" to "Горилла", matchResult.teams)
        assertTrue(matchResult.gorillaWon())
        assertFalse(matchResult.isDraw())
    }

    @Test
    fun `Technical win`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.of(2024, 9, 26, 21, 30, 0),
            "ЛОК",
            "Ижорский батальон-2 - Горилла",
            "0 : 5ОТ"
        )
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(0 to 5, matchResult.score)
        assertEquals("Ижорский батальон-2" to "Горилла", matchResult.teams)
        assertTrue(matchResult.gorillaWon())
        assertFalse(matchResult.isDraw())
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
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        assertEquals(9 to 27, matchResult.score)
        assertEquals("Ижорский батальон-2" to "Горилла", matchResult.teams)
        assertTrue(matchResult.gorillaWon())
        assertFalse(matchResult.isDraw())
    }
}