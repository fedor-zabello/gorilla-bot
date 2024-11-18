package service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.SpbhlMatchDto
import util.MessageGenerator
import util.SpbhlMatchMapper
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MatchServiceTest {
    private val spbhlMatchMapper = SpbhlMatchMapper()
    private val messageGenerator = mockk<MessageGenerator>()
    private val spbhClient = mockk<SpbhClient>()
    private val matchService = MatchService(spbhlMatchMapper, messageGenerator, spbhClient)

    @BeforeTest
    fun init() {
        every { spbhClient.getAllMatches() }.returns(MatchFactoryForTest.getMatchDtoList())
        every { messageGenerator.getUpcomingMatchMessage(any()) }.returns("Upcoming match info")
    }

    @Test
    fun `Test get nearest matches`() {
        val nearestMatches = matchService.getNearestAsStrings()

        verify(exactly = 1) { spbhClient.getAllMatches() }
        assertEquals(3, nearestMatches.size)
    }

    @Test
    fun `Case when no upcoming matches found`() {
        every { spbhClient.getAllMatches() }.returns(emptyList())

        val nearestMatches = matchService.getNearestAsStrings()

        verify(exactly = 1) { spbhClient.getAllMatches() }
        assertTrue(nearestMatches.isEmpty())
    }

    @Test
    fun `Test get all upcoming matches`() {
        val upcomingMatches = matchService.getAllUpcoming()

        verify(exactly = 1) { spbhClient.getAllMatches() }
        assertEquals(4, upcomingMatches.size)
    }

    @Test
    fun `Test get all matches`() {
        val allMatches = matchService.getAll()

        verify(exactly = 1) { spbhClient.getAllMatches() }
        assertEquals(7, allMatches.size)
    }

    @Test
    fun `Test get last result`() {
        val message = "Match result info"

        every { messageGenerator.getMatchResultMessage(any()) }.returns(message)

        val lastResult = matchService.getLastWithResult()

        verify(exactly = 1) { spbhClient.getAllMatches() }
        assertTrue(lastResult.contains(message))
    }
}

object MatchFactoryForTest {
    fun getMatchDtoList(): List<SpbhlMatchDto> {
        return mutableListOf<SpbhlMatchDto>(
            SpbhlMatchDto(
                "TSP CUP - 2024 (Старт A) Групповой этап",
                "Вс 10.11.2024",
                "20:45",
                "СТД",
                "Ижорский батальон-2 - Горилла",
                "1 : 2"
            ),
            SpbhlMatchDto(
                "TSP CUP - 2024 (Старт A) Групповой этап",
                "Вс 15.11.2024",
                "20:45",
                "ОЗК",
                "Горилла - Мегавольт",
                "1 : 2"
            ),
            SpbhlMatchDto(
                "TSP CUP - 2024 (Старт A) Групповой этап",
                "Вс 21.11.2024",
                "20:45",
                "СТД",
                "Кабаны - Горилла",
                "1 : 2"
            ),
            SpbhlMatchDto(
                "TSP CUP - 2024 (Старт A) Групповой этап",
                "Вс 01.12.2024",
                "20:45",
                "ШАН",
                "Система - Горилла",
                ""
            ),
            SpbhlMatchDto(
                "TSP CUP - 2024 (Старт A) Групповой этап",
                "Вс 06.12.2024",
                "20:45",
                "ОЗК",
                "Горилла - SBS",
                ""
            ),
            SpbhlMatchDto(
                "TSP CUP - 2024 (Старт A) Групповой этап",
                "Вс 10.12.2024",
                "20:45",
                "СТД",
                "Sharks - Горилла",
                ""
            ),
            SpbhlMatchDto(
                "TSP CUP - 2024 (Старт A) Групповой этап",
                "Вс 13.12.2024",
                "20:45",
                "СТД",
                "Ледокол - Горилла",
                ""
            ),
        )
    }
}